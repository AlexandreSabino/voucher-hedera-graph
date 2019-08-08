package com.biscoito.voucher.domains;

import lombok.*;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @NotBlank
    private String customerIdentifier;

    @NotBlank
    private String accountId;

    @NotBlank
    private String shaPassword;

    @NotBlank
    private String privateKey;
}
