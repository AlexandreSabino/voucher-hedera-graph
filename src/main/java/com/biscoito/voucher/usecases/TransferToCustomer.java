package com.biscoito.voucher.usecases;

import com.biscoito.voucher.domains.Customer;
import com.biscoito.voucher.domains.Voucher;
import com.biscoito.voucher.domains.VoucherEvent;
import com.biscoito.voucher.gateways.HederaHelper;
import com.biscoito.voucher.gateways.VoucherGateway;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.HederaException;
import com.hedera.hashgraph.sdk.account.AccountId;
import com.hedera.hashgraph.sdk.account.CryptoTransferTransaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
//@AllArgsConstructor
@Component
public class TransferToCustomer {

  private HederaHelper hederaHelper;
  private FindOrCreateCustomerAccount findOrCreateCustomerAccount;
  private VoucherGateway voucherGateway;

  public VoucherEvent execute(final String customerIdentifier, final String hashPassword, final long amount) {
    log.info("Transfer amount {} to {}", amount, customerIdentifier);
    final Customer customer = findOrCreateCustomerAccount.execute(customerIdentifier, hashPassword);

    var nsClientAccountId = hederaHelper.getOperatorId();
    var clientAccountId = AccountId.fromString(customer.getAccountId());
    var client = hederaHelper.createHederaClient();

    try {
      var nsBalanceBefore = client.getAccountBalance(nsClientAccountId);
      var customerBalanceBefore = client.getAccountBalance(clientAccountId);

      if (nsBalanceBefore <= 0l) {
        throw new RuntimeException("no money...");
      }

      log.debug("Ns balance before: {}", nsBalanceBefore);
      log.debug("Customer balance before: {}", customerBalanceBefore);

      //TODO retry

      var record = new CryptoTransferTransaction(client)
          // .addSender and .addRecipient can be called as many times as you want as long as the total sum from
          // both sides is equivalent
          .addSender(nsClientAccountId, amount)
          .addRecipient(clientAccountId, amount)
          .setMemo("voucher credit")
          // As we are sending from the operator we do not need to explicitly sign the transaction
          .executeForRecord();

      var nsBalanceAfter = client.getAccountBalance(nsClientAccountId);
      var customerBalanceAfter = client.getAccountBalance(clientAccountId);

      log.debug("Ns balance before: {}", nsBalanceAfter);
      log.debug("Customer balance before: {}", customerBalanceAfter);

      return VoucherEvent.builder()
          .transactionHash(record.getTransactionHash())
          .transactionFee(record.getTransactionFee())
          .description(record.getMemo())
          .when(LocalDateTime.now())
          .build();

    } catch (final HederaException ex) {
      throw new RuntimeException(ex);
    }
  }

}
