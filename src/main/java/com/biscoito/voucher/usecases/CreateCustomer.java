package com.biscoito.voucher.usecases;

import com.biscoito.voucher.domains.Customer;
import com.biscoito.voucher.gateways.AccountGateway;
import com.biscoito.voucher.gateways.CustomerGateway;
import com.hedera.hashgraph.sdk.account.AccountId;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PrivateKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateCustomer {

    private final CustomerGateway customerGateway;
    private final AccountGateway accountGateway;

    public Customer execute(final String customerIdentifier, final String pass) {
        log.info("Creating customer {}", customerIdentifier);

        var newKey = Ed25519PrivateKey.generate();
        final AccountId accountId = accountGateway.create(newKey);
        final Customer customer = Customer.builder()
            .accountId(accountId.toString())
            .customerIdentifier(customerIdentifier)
            .shaPassword(pass)
            .privateKey(newKey.toString())
            .build();

        return customerGateway.save(customer);
    }
}
