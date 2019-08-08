package com.biscoito.voucher.usecases;

import com.biscoito.voucher.domains.Customer;
import com.biscoito.voucher.gateways.CustomerGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FindCustomer {

    private final CustomerGateway customerGateway;

    public Optional<Customer> execute(final String customerIdentifier, final String hashPass) {
        final Optional<Customer> customerOptional = customerGateway.findOne(customerIdentifier);
        if (customerOptional.isPresent()) {
            final Customer customer = customerOptional.get();
            if (customer.getShaPassword().equals(hashPass)) {
                throw new RuntimeException("Invalid password");
            }
            return customerOptional;
        }
        return Optional.empty();
    }
}
