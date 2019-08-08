package com.biscoito.voucher.domains;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Customer {

    private String customerIdentifier;

    private String accountId;

    private String shaPassword;
}
