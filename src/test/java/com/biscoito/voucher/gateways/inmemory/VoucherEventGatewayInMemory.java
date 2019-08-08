package com.biscoito.voucher.gateways.inmemory;

import com.biscoito.voucher.domains.VoucherEvent;
import com.biscoito.voucher.gateways.VoucherEventGateway;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class VoucherEventGatewayInMemory implements VoucherEventGateway {

  private Collection<VoucherEvent> events = new ArrayList<>();

  @Override
  public VoucherEvent save(final VoucherEvent voucherEvent) {
    final VoucherEvent _event = voucherEvent.toBuilder().id(UUID.randomUUID().toString()).build();
    events.add(_event);
    return _event;
  }
}