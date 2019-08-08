package com.biscoito.voucher.usecases;

import com.biscoito.voucher.domains.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindOrCreateCustomerAccount {

    private final FindCustomer findCustomer;

    private final CreateCustomer createCustomer;

    public Customer execute(final String customerIdentifier, final String hashPass) {
        return findCustomer.execute(customerIdentifier, hashPass)
                .orElseGet(() -> createCustomer.execute(customerIdentifier, hashPass));
    }
}
