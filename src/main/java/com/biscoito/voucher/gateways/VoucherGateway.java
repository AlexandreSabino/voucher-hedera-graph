package com.biscoito.voucher.gateways;

import com.hedera.hashgraph.sdk.TransactionRecord;

public interface VoucherGateway {

  long getBalance(String accountId);

  TransactionRecord transfer(String accountFrom, String keyFrom, String accountTo, long amount);
}
