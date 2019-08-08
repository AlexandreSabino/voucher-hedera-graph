package com.biscoito.voucher.gateways.impl;

import com.biscoito.voucher.domains.Customer;
import com.biscoito.voucher.gateways.CustomerGateway;
import com.biscoito.voucher.gateways.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerGatewayImpl implements CustomerGateway {

    private final CustomerRepository customerRepository;

    @Override
    public Optional<Customer> findOne(final String customerIdentifier) {
        return customerRepository.findById(customerIdentifier);
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }
}
