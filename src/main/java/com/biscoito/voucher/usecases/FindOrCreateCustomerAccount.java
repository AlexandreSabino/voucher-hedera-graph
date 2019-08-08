package com.biscoito.voucher.usecases;

import com.biscoito.voucher.domains.Customer;
import com.biscoito.voucher.gateways.CustomerGateway;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.HederaException;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PrivateKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindOrCreateCustomerAccount {

    private final CustomerGateway customerGateway;

    private final Client client;

    public Customer execute(final String customerIdentifier) {
        final Optional<Customer> customerOptional = customerGateway.findOne(customerIdentifier);
        return customerOptional.orElseGet(() -> {
            try {
                var newKey = Ed25519PrivateKey.generate();
                var newAccountId = client.createAccount(newKey.getPublicKey(), 0);

                return Customer.builder()
                        .accountId(newAccountId.toString())
                        .customerIdentifier(customerIdentifier)
                        .build();

            } catch (HederaException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage());
            }
        });
    }
}
