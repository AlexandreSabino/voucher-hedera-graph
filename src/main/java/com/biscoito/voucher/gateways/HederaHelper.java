package com.biscoito.voucher.gateways;

import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.account.AccountId;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PrivateKey;
import java.util.Map;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HederaHelper {

    //100_000_000 (100 milhoes) tinybar => 1 hbar => 12 cents dolar

    @Value("${hedera.NODE_ADDRESS}")
    private String nodeAddress;

    @Value("${hedera.NODE_ID}")
    private String nodeId;

    @Getter
    @Value("${hedera.OPERATOR_ID}")
    private String operatorId;

    @Getter
    @Value("${hedera.OPERATOR_KEY}")
    private String operatorKey;

    @Getter
    @Value("${hedera.MAX_FEE}")
    private Long maxFee;

    public AccountId getNodeId() {
        return AccountId.fromString(nodeId);
    }

    public AccountId getNSAccountId() {
        return AccountId.fromString(operatorId);
    }

    public Client buildClient(final String accountId, final String key) {
        var client = new Client(Map.of(getNodeId(), nodeAddress));
        client.setOperator(AccountId.fromString(accountId), Ed25519PrivateKey.fromString(key));

        return client;
    }

}
