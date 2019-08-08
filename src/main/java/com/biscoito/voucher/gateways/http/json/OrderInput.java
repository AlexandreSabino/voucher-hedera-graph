package com.biscoito.voucher.gateways.http.json;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderInput {

    private String id;

    private String customerIdentifier;

    private String shaPassword;

    private String accountId;

    private Long amount;
}
