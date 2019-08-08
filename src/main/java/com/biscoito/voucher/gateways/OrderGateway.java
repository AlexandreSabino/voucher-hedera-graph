package com.biscoito.voucher.gateways;

import com.biscoito.voucher.domains.Order;

import java.util.Optional;

public interface OrderGateway {

    Order save(Order order);

    Optional<Order> findById(String id);
}
