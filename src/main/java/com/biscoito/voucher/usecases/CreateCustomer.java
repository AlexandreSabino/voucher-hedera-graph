package com.biscoito.voucher.usecases;

import com.biscoito.voucher.domains.Customer;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.HederaException;
import com.hedera.hashgraph.sdk.account.AccountId;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PrivateKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateCustomer {

    private final Client client;

    public Customer execute(String customerIdentifier) {
        try {
            var newKey = Ed25519PrivateKey.generate();
            final AccountId account = client.createAccount(newKey.getPublicKey(), 0);
            return Customer.builder()
                    .accountId(account.toString())
                    .customerIdentifier(customerIdentifier)
                    .build();
        } catch (HederaException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
