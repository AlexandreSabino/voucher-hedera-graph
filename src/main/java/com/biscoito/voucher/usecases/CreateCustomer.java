package com.biscoito.voucher.usecases;

import com.biscoito.voucher.domains.Customer;
import com.biscoito.voucher.gateways.CustomerGateway;
import com.biscoito.voucher.gateways.HederaHelper;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.HederaException;
import com.hedera.hashgraph.sdk.account.AccountId;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PrivateKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateCustomer {

    private final CustomerGateway customerGateway;

    private final HederaHelper hederaHelper;

    @Value("${hedera.MAX_FEE}")
    private Long maxFee;

    public Customer execute(final String customerIdentifier, final String pass) {
        try {
            var newKey = Ed25519PrivateKey.generate();
            final Client hederaClient = hederaHelper.buildClient(hederaHelper.getOperatorId(), hederaHelper.getOperatorKey());
            hederaClient.setMaxTransactionFee(maxFee);
            final AccountId account = hederaClient.createAccount(newKey.getPublicKey(), 0);
            final Customer customer = Customer.builder()
                    .accountId(account.toString())
                    .customerIdentifier(customerIdentifier)
                    .shaPassword(pass)
                    .privateKey(newKey.toString())
                    .build();

            return customerGateway.save(customer);
        } catch (HederaException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
