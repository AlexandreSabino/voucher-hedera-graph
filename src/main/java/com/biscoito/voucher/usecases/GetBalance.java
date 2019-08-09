package com.biscoito.voucher.usecases;

import com.biscoito.voucher.domains.Customer;
import com.biscoito.voucher.gateways.VoucherGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class GetBalance {

    private final VoucherGateway voucherGateway;

    private final FindCustomer findCustomer;

    private final TinybarsCalculator tinybarsCalculator;

    public long execute(final String customerIdentifier, final String hashPass) {
        final Customer customer = findCustomer.execute(customerIdentifier, hashPass)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

        return tinybarsCalculator.toRealInCents(voucherGateway.getBalance(customer.getAccountId()));
    }
}
