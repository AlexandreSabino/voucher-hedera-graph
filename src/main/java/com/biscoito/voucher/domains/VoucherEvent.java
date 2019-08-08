package com.biscoito.voucher.domains;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class VoucherEvent {

  public enum VoucherType {
    CREDIT,
    DEBIT
  }

  private String id;
  private String customerIdentifier;
  private byte[] transactionHash;
  private String description;
  private LocalDateTime when;
  private long amount;
  private long transactionFee;
  private VoucherType type;

}
