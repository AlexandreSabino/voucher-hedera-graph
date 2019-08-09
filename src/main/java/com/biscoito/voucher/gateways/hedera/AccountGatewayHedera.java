package com.biscoito.voucher.gateways.hedera;

import com.biscoito.voucher.gateways.AccountGateway;
import com.biscoito.voucher.gateways.HederaHelper;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.HederaException;
import com.hedera.hashgraph.sdk.account.AccountId;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PrivateKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AccountGatewayHedera implements AccountGateway {

  private final HederaHelper hederaHelper;

  @Override
  public AccountId create(final Ed25519PrivateKey key) {
    final Client client = hederaHelper.buildClient(hederaHelper.getOperatorId(), hederaHelper.getOperatorKey());
    client.setMaxTransactionFee(hederaHelper.getMaxFee());
    try {
      return client.createAccount(key.getPublicKey(), 0);
    } catch (HederaException e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException(e);
    }
  }

}
