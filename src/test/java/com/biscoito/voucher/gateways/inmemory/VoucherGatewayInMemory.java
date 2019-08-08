package com.biscoito.voucher.gateways.inmemory;

import com.biscoito.voucher.gateways.VoucherGateway;
import com.hedera.hashgraph.sdk.TransactionRecord;
import java.util.HashMap;
import java.util.Map;

public class VoucherGatewayInMemory implements VoucherGateway {

  private Map<String, Long> balances = new HashMap<>();

  @Override
  public long getBalance(final String accountId) {
    return balances.getOrDefault(accountId, 0l);
  }

  @Override
  public TransactionRecord debit(String accountId, long amount) {
    return null;
  }

  @Override
  public TransactionRecord credit(String accountId, long amount) {
    return null;
  }
}
