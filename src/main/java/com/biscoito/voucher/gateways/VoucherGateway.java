package com.biscoito.voucher.gateways;

import com.hedera.hashgraph.sdk.TransactionRecord;

public interface VoucherGateway {

  long getBalance(String accountId);

  TransactionRecord debit(String from, long amount);

  TransactionRecord credit(String to, long amount);

}
