package com.biscoito.voucher.usecases;

import com.biscoito.voucher.domains.Customer;
import com.biscoito.voucher.domains.VoucherEvent;
import com.biscoito.voucher.domains.VoucherEvent.VoucherType;
import com.biscoito.voucher.exceptions.InsuficientFundsException;
import com.biscoito.voucher.gateways.HederaHelper;
import com.biscoito.voucher.gateways.VoucherEventGateway;
import com.hedera.hashgraph.sdk.HederaException;
import com.hedera.hashgraph.sdk.account.AccountId;
import com.hedera.hashgraph.sdk.account.CryptoTransferTransaction;
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

  public VoucherEvent execute(final String customerIdentifier, final String hashPassword, final long amount) {
    log.info("Transfer amount {} from NS to customer {}", amount, customerIdentifier);
    final Customer customer = findOrCreateCustomerAccount.execute(customerIdentifier, hashPassword);

    var nsClientAccountId = hederaHelper.getOperatorId();
    var clientAccountId = AccountId.fromString(customer.getAccountId());
    var client = hederaHelper.createHederaClient();

    try {
      var nsBalanceBefore = client.getAccountBalance(nsClientAccountId);
      var customerBalanceBefore = client.getAccountBalance(clientAccountId);

      if (nsBalanceBefore <= 0l) {
        throw new InsuficientFundsException("no money...");
      }

      log.debug("NS balance before: {}", nsBalanceBefore);
      log.debug("Customer balance before: {}", customerBalanceBefore);

      var record = new CryptoTransferTransaction(client)
          .addSender(nsClientAccountId, amount)
          .addRecipient(clientAccountId, amount)
          .setMemo(String.format("voucher credit of %s", amount))
          .executeForRecord();

      var nsBalanceAfter = client.getAccountBalance(nsClientAccountId);
      var customerBalanceAfter = client.getAccountBalance(clientAccountId);

      log.debug("NS balance before: {}", nsBalanceAfter);
      log.debug("Customer balance before: {}", customerBalanceAfter);

      final VoucherEvent event = VoucherEvent.builder()
          .customerIdentifier(customerIdentifier)
          .transactionHash(record.getTransactionHash())
          .transactionFee(record.getTransactionFee())
          .description(record.getMemo())
          .when(LocalDateTime.now())
          .type(VoucherType.CREDIT)
          .build();

      return voucherEventGateway.save(event);
    } catch (final HederaException ex) {
      throw new RuntimeException(ex);
    }
  }

}
