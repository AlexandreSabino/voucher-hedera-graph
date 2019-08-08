package com.biscoito.voucher.gateways.hedera;

import com.biscoito.voucher.gateways.HederaHelper;
import com.biscoito.voucher.gateways.VoucherGateway;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.HederaException;
import com.hedera.hashgraph.sdk.TransactionRecord;
import com.hedera.hashgraph.sdk.account.AccountId;
import com.hedera.hashgraph.sdk.account.CryptoTransferTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class VoucherGatewayHedera implements VoucherGateway {

  private final HederaHelper hederaHelper;

  @Override
  public long getBalance(final String accountId) {
    try {
      final Client client = hederaHelper.buildClient(hederaHelper.getOperatorId(), hederaHelper.getOperatorKey());
      return client.getAccountBalance(AccountId.fromString(accountId));
    } catch (final HederaException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public TransactionRecord transfer(String accountFrom, String keyFrom, String accountTo, long amount) {
    final Client client = hederaHelper.buildClient(accountFrom, keyFrom);
    try {
      return  new CryptoTransferTransaction(client)
          .addSender(AccountId.fromString(accountFrom), amount)
          .addRecipient(AccountId.fromString(accountTo), amount)
          .setMemo(String.format("voucher transfer of %s", amount))
          .executeForRecord();
    } catch (final HederaException ex) {
      throw new RuntimeException(ex);
    }
  }
}
