package com.biscoito.voucher.gateways.http;

import com.biscoito.voucher.gateways.http.json.NetshoesBalanceJson;
import com.biscoito.voucher.usecases.GetBalanceNetshoes;
import com.biscoito.voucher.usecases.TinybarsCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/netshoes")
public class NetshoesController {

    private final GetBalanceNetshoes getBalanceNetshoes;

    private final TinybarsCalculator tinybarsCalculator;

    @GetMapping(path = "/balance")
    public NetshoesBalanceJson getBalance() {
        final long tinybars = getBalanceNetshoes.getTinybars();
        return new NetshoesBalanceJson(tinybars, tinybarsCalculator.toRealInCents(tinybars));
    }
}
