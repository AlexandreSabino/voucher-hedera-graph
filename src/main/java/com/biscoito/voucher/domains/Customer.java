package com.biscoito.voucher.domains;

import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    private String customerIdentifier;

    private String accountId;

    private String shaPassword;
}
