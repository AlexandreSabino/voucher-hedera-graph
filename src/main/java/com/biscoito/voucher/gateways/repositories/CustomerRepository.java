package com.biscoito.voucher.gateways.repositories;

import com.biscoito.voucher.domains.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, String> {
}
