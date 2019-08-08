package com.biscoito.voucher.domains;

import lombok.*;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    private String id;

    @NotBlank
    private String customerIdentifier;

    @NotBlank
    private String shaPassword;

    @NotBlank
    private String accountId;

    @NotBlank
    private Long amount;

}
