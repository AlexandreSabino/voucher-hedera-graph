package com.biscoito.voucher.usecases;

import com.biscoito.voucher.domains.Customer;
import com.biscoito.voucher.domains.VoucherEvent;
import com.biscoito.voucher.domains.VoucherEvent.VoucherType;
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
public class TransferToCustomer {

  private final HederaHelper hederaHelper;
  private final FindOrCreateCustomerAccount findOrCreateCustomerAccount;
  private final VoucherEventGateway voucherEventGateway;
  private final VoucherGateway voucherGateway;

  public VoucherEvent execute(final String customerIdentifier, final String hashPassword, final long amount) {
    log.info("Transfer amount {} from NS to customer {}", amount, customerIdentifier);
    final Customer customer = findOrCreateCustomerAccount.execute(customerIdentifier, hashPassword);

    var nsBalanceBefore = voucherGateway.getBalance(hederaHelper.getOperatorId());
    var customerBalanceBefore = voucherGateway.getBalance(customer.getAccountId());

    if (nsBalanceBefore <= 0l) {
      throw new InsuficientFundsException("no money...");
    }

    log.debug("Netshoes balance before: {}", nsBalanceBefore);
    log.debug("Customer balance before: {}", customerBalanceBefore);

    var record = voucherGateway.transfer(hederaHelper.getOperatorId(),
        hederaHelper.getOperatorKey(), customer.getAccountId(), amount);

    var nsBalanceAfter = voucherGateway.getBalance(hederaHelper.getOperatorId());
    var customerBalanceAfter = voucherGateway.getBalance(customer.getAccountId());

    log.debug("Netshoes balance before: {}", nsBalanceAfter);
    log.debug("Customer balance before: {}", customerBalanceAfter);

    final VoucherEvent event = VoucherEvent.builder()
        .customerIdentifier(customerIdentifier)
        .transactionHash(record.getTransactionHash())
        .transactionFee(record.getTransactionFee())
        .description(record.getMemo())
        .when(LocalDateTime.now())
        .type(VoucherType.CREDIT)
        .amount(amount)
        .build();

    return voucherEventGateway.save(event);
  }

}
