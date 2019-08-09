package com.biscoito.voucher.usecases;

import com.biscoito.voucher.gateways.HederaHelper;
import com.biscoito.voucher.gateways.VoucherGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetBalanceNetshoes {

    private final VoucherGateway voucherGateway;

    private final HederaHelper hederaHelper;

    public long getTinybars() {
        return voucherGateway.getBalance(hederaHelper.getOperatorId());
    }
}