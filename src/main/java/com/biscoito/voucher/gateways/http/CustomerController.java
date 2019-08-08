package com.biscoito.voucher.gateways.http;

import com.biscoito.voucher.domains.Customer;
import com.biscoito.voucher.gateways.http.json.CustomerInput;
import com.biscoito.voucher.gateways.http.json.CustomerOutput;
import com.biscoito.voucher.usecases.CreateCustomer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CreateCustomer createCustomer;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public CustomerOutput create(@RequestBody final CustomerInput customerJson) {
        final Customer customer = createCustomer.execute(customerJson.getCustomerIdentifier(), customerJson.getShaPassword());
        return new CustomerOutput(customer.getCustomerIdentifier(), customer.getAccountId());
    }
}
