package com.biscoito.voucher.gateways.http;

import com.biscoito.voucher.domains.Order;
import com.biscoito.voucher.gateways.http.json.OrderInput;
import com.biscoito.voucher.usecases.PlaceOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/place-order")
public class PlaceOrderController {

    private final PlaceOrder placeOrder;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public void placeOrder(@RequestBody OrderInput orderJson) {
        log.info("Placing new order with id: {}", orderJson.getId());
        placeOrder.place(convertOrder(orderJson));
    }

    private Order convertOrder(OrderInput orderJson) {
        return Order.builder()
                .accountId(orderJson.getAccountId())
                .amount(orderJson.getAmount())
                .shaPassword(orderJson.getShaPassword())
                .customerIdentifier(orderJson.getCustomerIdentifier())
                .build();
    }

}
