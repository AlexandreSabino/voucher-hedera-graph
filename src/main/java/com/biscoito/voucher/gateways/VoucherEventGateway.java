package com.biscoito.voucher.gateways;

import com.biscoito.voucher.domains.VoucherEvent;

public interface VoucherEventGateway {

  VoucherEvent save(VoucherEvent voucherEvent);

}
