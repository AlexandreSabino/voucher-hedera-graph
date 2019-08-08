package com.biscoito.voucher.usecases;

import com.biscoito.voucher.domains.Voucher;
import com.biscoito.voucher.gateways.VoucherGateway;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.account.AccountId;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class TransferToCustomer {

  private FindOrCreateCustomerAccount findOrCreateCustomerAccount;
  private VoucherGateway voucherGateway;

  public void execute(final String customerIdentifier, final BigDecimal amount) {

    try {
      //TODO get customer
      var nsClientAccountId = AccountId.fromString("");//TODO
      var clientAccountId = AccountId.fromString(""); //TODO
      var client = new Client(new HashMap<>());//TODO


      var recipientId = AccountId.fromString("0.0.3");

      var senderBalanceBefore = client.getAccountBalance(nsClientAccountId);
      var receiptBalanceBefore = client.getAccountBalance(clientAccountId);
    } catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
