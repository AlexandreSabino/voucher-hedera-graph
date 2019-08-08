package com.biscoito.voucher.usecases;

import com.biscoito.voucher.domains.Customer;
import com.biscoito.voucher.gateways.HederaHelper;
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

    private final HederaHelper hederaHelper;

    private final PasswordManager passwordManager;

    public Customer execute(final String customerIdentifier, final String pass) {
        try {
            var newKey = Ed25519PrivateKey.generate();
            final Client hederaClient = hederaHelper.createHederaClient();
            hederaClient.setMaxTransactionFee(100_000_000);
            final AccountId account = hederaClient.createAccount(newKey.getPublicKey(), 0);
            return Customer.builder()
                    .accountId(account.toString())
                    .customerIdentifier(customerIdentifier)
                    .shaPassword(pass)
                    .build();
        } catch (HederaException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
