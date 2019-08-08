package com.biscoito.voucher.gateways.http;

import com.biscoito.voucher.domains.Customer;
import com.biscoito.voucher.gateways.http.json.CustomerBalanceJson;
import com.biscoito.voucher.gateways.http.json.CustomerInput;
import com.biscoito.voucher.gateways.http.json.CustomerOutput;
import com.biscoito.voucher.usecases.CreateCustomer;
import com.biscoito.voucher.usecases.FindCustomer;
import com.biscoito.voucher.usecases.GetBalance;
import com.biscoito.voucher.usecases.exceptions.InvalidPasswordException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CreateCustomer createCustomer;

    private final FindCustomer findCustomer;

    private final GetBalance getBalance;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public CustomerOutput create(@RequestBody @Valid final CustomerInput customerJson) {
        log.info("Creating new customer with id: {}", customerJson.getCustomerIdentifier());
        final Customer customer = createCustomer.execute(customerJson.getCustomerIdentifier(), customerJson.getShaPassword());
        return new CustomerOutput(customer.getCustomerIdentifier(), customer.getAccountId());
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerOutput> login(@RequestBody final CustomerInput customerJson) {
        log.info("Logging in customer with id: {}", customerJson.getCustomerIdentifier());
        try {
            final Optional<Customer> customerOptional =
                    findCustomer.execute(customerJson.getCustomerIdentifier(), customerJson.getShaPassword());
            if (customerOptional.isPresent()) {
                final Customer customer = customerOptional.get();
                return ResponseEntity.ok(new CustomerOutput(customer.getCustomerIdentifier(), customer.getAccountId()));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping(path = "/me/balance")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CustomerBalanceJson getBalance(@RequestBody final CustomerInput customerInput) {
        final long balance =
                getBalance.execute(customerInput.getCustomerIdentifier(), customerInput.getShaPassword());

        return new CustomerBalanceJson(balance, customerInput.getCustomerIdentifier());
    }
}
