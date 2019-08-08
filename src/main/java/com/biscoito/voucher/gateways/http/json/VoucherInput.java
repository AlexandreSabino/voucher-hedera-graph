package com.biscoito.voucher.gateways.http.json;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoucherInput {
    private String customerIdentifier;
    private String hashPassword;
    private long amount;
}
