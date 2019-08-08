package com.biscoito.voucher.usecases;

import com.biscoito.voucher.domains.Customer;
import com.biscoito.voucher.gateways.HederaHelper;
import com.hedera.hashgraph.sdk.HederaException;
import com.hedera.hashgraph.sdk.account.AccountId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class GetBalance {

    private final HederaHelper hederaHelper;

    private final FindCustomer findCustomer;

    public long execute(final String customerIdentifier, final String hashPass) {
        try {
            final Customer customer = findCustomer.execute(customerIdentifier, hashPass)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            return hederaHelper.createHederaClient()
                    .getAccountBalance(AccountId.fromString(customer.getAccountId()));
            
        } catch (HederaException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
