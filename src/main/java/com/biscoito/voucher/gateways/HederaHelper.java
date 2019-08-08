package com.biscoito.voucher.gateways;

import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.account.AccountId;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PrivateKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HederaHelper {

    @Value("${hedera.NODE_ADDRESS}")
    private String nodeAddress;

    @Value("${hedera.NODE_ID}")
    private String nodeId;

    @Value("${hedera.OPERATOR_ID}")
    private String operatorId;

    @Value("${hedera.OPERATOR_KEY}")
    private String operatorKey;

    public AccountId getNodeId() {
        return AccountId.fromString(nodeId);
    }

    public AccountId getOperatorId() {
        return AccountId.fromString(operatorId);
    }

    public Ed25519PrivateKey getOperatorKey() {
        return Ed25519PrivateKey.fromString(operatorKey);
    }

    public Client createHederaClient() {
        // To connect to a network with more nodes, add additional entries to the network map
        var client = new Client(Map.of(getNodeId(), nodeAddress));

        // Defaults the operator account ID and key such that all generated transactions will be paid for
        // by this account and be signed by this key
        client.setOperator(getOperatorId(), getOperatorKey());

        return client;
    }
}
