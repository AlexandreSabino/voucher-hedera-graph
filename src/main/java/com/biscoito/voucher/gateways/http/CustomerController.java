package com.biscoito.voucher.gateways.http;

import com.biscoito.voucher.domains.Customer;
import com.biscoito.voucher.gateways.http.json.CustomerInput;
import com.biscoito.voucher.gateways.http.json.CustomerOutput;
import com.biscoito.voucher.usecases.CreateCustomer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CreateCustomer createCustomer;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public CustomerOutput create(@RequestBody @Valid final CustomerInput customerJson) {
        log.info("Creating new customer with id: {}", customerJson.getCustomerIdentifier());
        final Customer customer = createCustomer.execute(customerJson.getCustomerIdentifier(), customerJson.getShaPassword());
        return new CustomerOutput(customer.getCustomerIdentifier(), customer.getAccountId());
    }
}
