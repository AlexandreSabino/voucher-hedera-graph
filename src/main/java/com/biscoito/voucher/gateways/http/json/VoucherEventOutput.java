package com.biscoito.voucher.gateways.http.json;

import com.biscoito.voucher.domains.VoucherEvent.EventType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class VoucherEventOutput {
    private String id;
    private String customerIdentifier;
    private String description;
    private EventType type;
    private LocalDateTime when;
    private long amount;
}
