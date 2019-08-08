package com.biscoito.voucher.usecases;

import com.biscoito.voucher.domains.Customer;
import com.biscoito.voucher.gateways.CustomerGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindOrCreateCustomerAccount {

    private final CustomerGateway customerGateway;

    private final CreateCustomer createCustomer;

    public Customer execute(final String customerIdentifier, final String pass) {
        final Optional<Customer> customerOptional = customerGateway.findOne(customerIdentifier);
        return customerOptional.orElseGet(() -> createCustomer.execute(customerIdentifier, pass));
    }
}
