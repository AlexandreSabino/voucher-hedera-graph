package com.biscoito.voucher.gateways;

import com.biscoito.voucher.domains.Voucher;
import java.util.Optional;

public interface VoucherGateway {

  Optional<Voucher> findbyCustomerIdentifier(String customerIdentifier);

}
