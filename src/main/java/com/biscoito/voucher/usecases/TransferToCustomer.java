package com.biscoito.voucher.usecases;

import com.biscoito.voucher.domains.Customer;
import com.biscoito.voucher.domains.VoucherEvent;
import com.biscoito.voucher.domains.VoucherEvent.EventType;
import com.biscoito.voucher.exceptions.InsuficientFundsException;
import com.biscoito.voucher.gateways.HederaHelper;
import com.biscoito.voucher.gateways.VoucherEventGateway;
import com.biscoito.voucher.gateways.VoucherGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class TransferToCustomer {

    private final HederaHelper hederaHelper;
    private final FindOrCreateCustomerAccount findOrCreateCustomerAccount;
    private final VoucherEventGateway voucherEventGateway;
    private final VoucherGateway voucherGateway;
    private final TinybarsCalculator tinybarsCalculator;

    @Value("${hedera.COST_TRANSACTION}")
    private Long costTransaction;

    public VoucherEvent execute(final String customerIdentifier, final String hashPassword, final long amountInCents) {
        log.info("Transfer amount {} from NS to customer {}", amountInCents, customerIdentifier);
        final Customer customer = findOrCreateCustomerAccount.execute(customerIdentifier, hashPassword);

        var nsBalanceBefore = voucherGateway.getBalance(hederaHelper.getNetshoesAccountId());
        var customerBalanceBefore = voucherGateway.getBalance(customer.getAccountId());

        if (tinybarsCalculator.toRealInCents(nsBalanceBefore) <= 0L
            || tinybarsCalculator.toRealInCents(nsBalanceBefore) < amountInCents) {
            throw new InsuficientFundsException("No funds from Netshoes");
        }

        log.debug("Netshoes balance before: {}", nsBalanceBefore);
        log.debug("Customer balance before: {}", customerBalanceBefore);

        final long tinybars = tinybarsCalculator.toTinybars(amountInCents + costTransaction);
        var record = voucherGateway.transfer(hederaHelper.getNetshoesAccountId(),
                hederaHelper.getNetshoesKey(), customer.getAccountId(), tinybars);

        var nsBalanceAfter = voucherGateway.getBalance(hederaHelper.getNetshoesAccountId());
        var customerBalanceAfter = voucherGateway.getBalance(customer.getAccountId());

        log.debug("Netshoes balance after: {}", nsBalanceAfter);
        log.debug("Customer balance after: {}", customerBalanceAfter);

        final VoucherEvent event = VoucherEvent.builder()
            .customerIdentifier(customerIdentifier)
            .transactionHash(record.getTransactionHash())
            .transactionFee(record.getTransactionFee())
            .description(record.getMemo())
            .when(LocalDateTime.now())
            .type(EventType.CREDIT)
            .amountInCents(tinybarsCalculator.toRealInCents(tinybars))//+ fee
            .amountInCentsTinybar(tinybars)
            .build();

        return voucherEventGateway.save(event);
    }

}
