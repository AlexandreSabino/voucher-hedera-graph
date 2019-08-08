package com.biscoito.voucher.gateways.http.json;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerInput {

    private String customerIdentifier;

    private String shaPassword;
}
