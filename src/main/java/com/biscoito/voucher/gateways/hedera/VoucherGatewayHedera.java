package com.biscoito.voucher.gateways.hedera;

import com.biscoito.voucher.gateways.HederaHelper;
import com.biscoito.voucher.gateways.VoucherGateway;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.HederaException;
import com.hedera.hashgraph.sdk.TransactionRecord;
import com.hedera.hashgraph.sdk.account.AccountId;
import com.hedera.hashgraph.sdk.account.CryptoTransferTransaction;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class VoucherGatewayHedera implements VoucherGateway {

  private final HederaHelper hederaHelper;
  private Client client;

  @PostConstruct
  public void load() {
    this.client = hederaHelper.createHederaClient();
  }

  @Override
  public long getBalance(final String accountId) {
    try {
      return client.getAccountBalance(AccountId.fromString(accountId));
    } catch (final HederaException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public TransactionRecord debit(final String clientAccountId, final long amount) {
    var netshoesAccountId = hederaHelper.getNSAccountId();
    try {
      return  new CryptoTransferTransaction(client)
          .addSender(AccountId.fromString(clientAccountId), amount)
          .addRecipient(netshoesAccountId, amount)
          .setMemo(String.format("voucher debit of %s", amount))
          .executeForRecord();
    } catch (final HederaException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public TransactionRecord credit(final String clientAccountId, final long amount) {
    var netshoesAccountId = hederaHelper.getNSAccountId();
    try {
      return  new CryptoTransferTransaction(client)
          .addSender(netshoesAccountId, amount)
          .addRecipient(AccountId.fromString(clientAccountId), amount)
          .setMemo(String.format("voucher credit of %s", amount))
          .executeForRecord();
    } catch (final HederaException ex) {
      throw new RuntimeException(ex);
    }
  }
}
