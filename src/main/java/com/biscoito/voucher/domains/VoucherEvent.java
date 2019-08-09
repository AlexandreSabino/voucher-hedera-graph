package com.biscoito.voucher.domains;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder(toBuilder = true)
@ToString
public class VoucherEvent {

  public enum EventType {
    CREDIT,
    DEBIT
  }

  private String id;
  private String customerIdentifier;
  private byte[] transactionHash;
  private String description;
  private LocalDateTime when;
  private long amountInCents;
  private long amountInCentsTinybar;
  private long transactionFee;
  private EventType type;

}
