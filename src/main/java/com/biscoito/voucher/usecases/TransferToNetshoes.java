package com.biscoito.voucher.usecases;

import com.biscoito.voucher.domains.Customer;
import com.biscoito.voucher.domains.VoucherEvent;
import com.biscoito.voucher.domains.VoucherEvent.EventType;
import com.biscoito.voucher.exceptions.InsuficientFundsException;
import com.biscoito.voucher.gateways.HederaHelper;
import com.biscoito.voucher.gateways.VoucherEventGateway;
import com.biscoito.voucher.gateways.VoucherGateway;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class TransferToNetshoes {

    private final HederaHelper hederaHelper;
    private final FindOrCreateCustomerAccount findOrCreateCustomerAccount;
    private final VoucherEventGateway voucherEventGateway;
    private final VoucherGateway voucherGateway;
    private final TinybarsCalculator tinybarsCalculator;

    public VoucherEvent execute(final String customerIdentifier, final String hashPassword, final long amountInCents) {
        log.info("Transfer amount {} from customer {} to NS", amountInCents, customerIdentifier);
        final Customer customer = findOrCreateCustomerAccount.execute(customerIdentifier, hashPassword);

        var nsBalanceBefore = voucherGateway.getBalance(hederaHelper.getOperatorId());
        var customerBalanceBefore = voucherGateway.getBalance(customer.getAccountId());

        log.debug("Netshoes balance before: {}", nsBalanceBefore);
        log.debug("Customer balance before: {}", customerBalanceBefore);

        if (tinybarsCalculator.toRealInCents(customerBalanceBefore) <= 0L
            || tinybarsCalculator.toRealInCents(customerBalanceBefore) < amountInCents) {
            throw new InsuficientFundsException("No funds from Customer");
        }

        final long tinybars = tinybarsCalculator.toTinybars(amountInCents);
        var record = voucherGateway.transfer(customer.getAccountId(), customer.getPrivateKey(),
            hederaHelper.getOperatorId(), tinybars);

        var nsBalanceAfter = voucherGateway.getBalance(hederaHelper.getOperatorId());
        var customerBalanceAfter = voucherGateway.getBalance(customer.getAccountId());

        log.debug("Netshoes balance after: {}", nsBalanceAfter);
        log.debug("Customer balance after: {}", customerBalanceAfter);

        final VoucherEvent event = VoucherEvent.builder()
            .customerIdentifier(customerIdentifier)
            .transactionHash(record.getTransactionHash())
            .transactionFee(record.getTransactionFee())
            .description(record.getMemo())
            .when(LocalDateTime.now())
            .amountInCents(tinybarsCalculator.toRealInCents(tinybars))
            .amountInCentsTinybar(tinybars)
            .type(EventType.DEBIT)
            .build();

        return voucherEventGateway.save(event);
    }

}
