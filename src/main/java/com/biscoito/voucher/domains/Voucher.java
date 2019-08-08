package com.biscoito.voucher.domains;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class Voucher {

    private String id;

    private String clientId;

    private Long amount;

    private Collection<String> events;
}