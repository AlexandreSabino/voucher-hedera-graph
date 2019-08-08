package com.biscoito.voucher.usecases;

import com.biscoito.voucher.domains.Order;
import com.biscoito.voucher.exceptions.OrderNotFoundException;
import com.biscoito.voucher.gateways.OrderGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlaceOrder {

    private final OrderGateway orderGateway;

    public void place(Order order) {
        log.debug("Placing new order");
        orderGateway.save(order);
    }

    public Order findById(final String id) {
        log.debug("Finding order by id: {}", id);
        return orderGateway.findById(id).orElseThrow(() -> {
            throw new OrderNotFoundException(String.format("order not found with id %s", id));
        });
    }
}
