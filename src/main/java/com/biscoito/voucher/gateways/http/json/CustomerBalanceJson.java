package com.biscoito.voucher.gateways.http.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomerBalanceJson {

    private long balance;

    private String customerIdentifier;
}
