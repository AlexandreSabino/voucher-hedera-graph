package com.biscoito.voucher.gateways.inmemory;

import com.biscoito.voucher.domains.Customer;
import com.biscoito.voucher.gateways.CustomerGateway;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class CustomerGatewayInMemory implements CustomerGateway {

  private Collection<Customer> customers = new ArrayList<>();

  @Override
  public Optional<Customer> findOne(final String customerIdentifier) {
    return customers.stream()
        .filter(customer -> customer.getCustomerIdentifier().equals(customerIdentifier))
        .findFirst();
  }

  @Override
  public Customer save(final Customer customer) {
    customers.add(customer);
    return customer;
  }

  public void clean() {
    this.customers.clear();
  }

}
