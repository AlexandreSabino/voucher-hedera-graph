package com.biscoito.voucher.gateways;

import com.biscoito.voucher.domains.VoucherEvent;
import com.biscoito.voucher.domains.VoucherEvent.EventType;
import java.util.Collection;

public interface VoucherEventGateway {

  VoucherEvent save(VoucherEvent voucherEvent);

  Collection<VoucherEvent> findAllByTypeSortable(EventType type);

  Collection<VoucherEvent> findAllByCustomerSortable(String customerIdentifier);

}
