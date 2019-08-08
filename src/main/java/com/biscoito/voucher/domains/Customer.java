package com.biscoito.voucher.domains;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class Customer {

    @Id
    private String customerIdentifier;

    private String accountId;

    private String shaPassword;
}
