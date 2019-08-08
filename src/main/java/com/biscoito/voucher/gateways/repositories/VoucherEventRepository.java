package com.biscoito.voucher.gateways.repositories;

import com.biscoito.voucher.domains.VoucherEvent;
import org.springframework.data.repository.CrudRepository;

public interface VoucherEventRepository extends CrudRepository<VoucherEvent, String>  {

}
