package com.biscoito.voucher.gateways.impl;

import com.biscoito.voucher.domains.Order;
import com.biscoito.voucher.gateways.OrderGateway;
import com.biscoito.voucher.gateways.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderGatewayImpl implements OrderGateway {

    private final OrderRepository orderRepository;

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Optional<Order> findById(String id) {
        return orderRepository.findById(id);
    }
}
