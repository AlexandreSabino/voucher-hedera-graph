package com.biscoito.voucher.gateways.repositories;

import com.biscoito.voucher.domains.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {

}
