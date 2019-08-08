package com.biscoito.voucher.gateways;

import com.biscoito.voucher.domains.Customer;

import java.util.Optional;

public interface CustomerGateway {

    Optional<Customer> findOne(String customerIdentifier);

    Customer save(Customer customer);
}
