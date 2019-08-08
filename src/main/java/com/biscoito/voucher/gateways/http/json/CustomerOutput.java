package com.biscoito.voucher.gateways.http.json;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomerOutput {

    private String customerIdentifier;

    private String accountId;
}
