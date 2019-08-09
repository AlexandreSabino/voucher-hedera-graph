package com.biscoito.voucher.gateways.http.json;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NetshoesBalanceJson {

    private long tinybars;

    private long amountInCents;
}
