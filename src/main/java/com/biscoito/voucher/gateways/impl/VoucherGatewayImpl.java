package com.biscoito.voucher.gateways.impl;

import com.biscoito.voucher.domains.VoucherEvent;
import com.biscoito.voucher.gateways.VoucherEventGateway;
import com.biscoito.voucher.gateways.repositories.VoucherEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class VoucherGatewayImpl implements VoucherEventGateway {

  private final VoucherEventRepository repository;

  @Override
  public VoucherEvent save(final VoucherEvent voucherEvent) {
    return repository.save(voucherEvent);
  }
}
