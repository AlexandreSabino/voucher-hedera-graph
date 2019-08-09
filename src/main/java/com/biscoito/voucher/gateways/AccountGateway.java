package com.biscoito.voucher.gateways;

import com.hedera.hashgraph.sdk.account.AccountId;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PrivateKey;

public interface AccountGateway {

  AccountId create(Ed25519PrivateKey key);

}
