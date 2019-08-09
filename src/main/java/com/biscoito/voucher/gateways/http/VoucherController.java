package com.biscoito.voucher.gateways.http;

import com.biscoito.voucher.domains.VoucherEvent;
import com.biscoito.voucher.gateways.http.json.CustomerBalanceJson;
import com.biscoito.voucher.gateways.http.json.CustomerInput;
import com.biscoito.voucher.gateways.http.json.VoucherEventOutput;
import com.biscoito.voucher.gateways.http.json.VoucherInput;
import com.biscoito.voucher.usecases.GetBalance;
import com.biscoito.voucher.usecases.TransferToCustomer;
import com.biscoito.voucher.usecases.TransferToNetshoes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/vouchers")
public class VoucherController {

    private final TransferToCustomer transferToCustomer;

    private final TransferToNetshoes transferToNetshoes;

    @PostMapping(path = "/me")
    @ResponseStatus(value = HttpStatus.CREATED)
    public VoucherEventOutput createVoucher(@RequestBody final VoucherInput voucherInput) {
        final VoucherEvent voucherEvent = transferToCustomer.execute(
                voucherInput.getCustomerIdentifier(),
                voucherInput.getHashPassword(),
                voucherInput.getAmount());

        return VoucherEventOutput.builder()
                .id(voucherEvent.getId())
                .customerIdentifier(voucherEvent.getCustomerIdentifier())
                .description(voucherEvent.getDescription())
                .when(voucherEvent.getWhen())
                .amount(voucherEvent.getAmountInCents())
                .build();
    }

    @PostMapping(path = "/use")
    @ResponseStatus(value = HttpStatus.CREATED)
    public VoucherEventOutput useVoucher(@RequestBody final VoucherInput voucherInput) {
        final VoucherEvent voucherEvent = transferToNetshoes.execute(
                voucherInput.getCustomerIdentifier(),
                voucherInput.getHashPassword(),
                voucherInput.getAmount());

        return VoucherEventOutput.builder()
                .id(voucherEvent.getId())
                .customerIdentifier(voucherEvent.getCustomerIdentifier())
                .description(voucherEvent.getDescription())
                .when(voucherEvent.getWhen())
                .amount(voucherEvent.getAmountInCents())
                .build();
    }
}
