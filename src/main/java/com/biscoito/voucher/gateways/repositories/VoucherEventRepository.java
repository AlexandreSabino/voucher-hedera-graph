package com.biscoito.voucher.gateways.repositories;

import com.biscoito.voucher.domains.VoucherEvent;
import com.biscoito.voucher.domains.VoucherEvent.EventType;
import java.util.Collection;
import org.springframework.data.repository.CrudRepository;

public interface VoucherEventRepository extends CrudRepository<VoucherEvent, String>  {

  Collection<VoucherEvent> findAllByTypeOrderByWhenDesc(EventType type);

  Collection<VoucherEvent> findAllByCustomerIdentifierOrderByWhen(String customerIdentifier);

}
