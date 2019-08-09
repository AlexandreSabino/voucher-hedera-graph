package com.biscoito.voucher.gateways.impl;

import com.biscoito.voucher.domains.VoucherEvent;
import com.biscoito.voucher.domains.VoucherEvent.EventType;
import com.biscoito.voucher.gateways.VoucherEventGateway;
import com.biscoito.voucher.gateways.repositories.VoucherEventRepository;
import java.util.Collection;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class VoucherEventGatewayImpl implements VoucherEventGateway {

  private final VoucherEventRepository repository;

  @Override
  public VoucherEvent save(final VoucherEvent voucherEvent) {
    return repository.save(voucherEvent);
  }

  @Override
  public Collection<VoucherEvent> findAllByTypeSortable(final EventType type) {
    return repository.findAllByTypeOrderByWhenDesc(type);
  }


  @Override
  public Collection<VoucherEvent> findAllByCustomerSortable(final String customerIdentifier) {
    return repository.findAllByCustomerIdentifierOrderByWhen(customerIdentifier);
  }
}
