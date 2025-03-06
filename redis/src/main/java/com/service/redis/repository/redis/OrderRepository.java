package com.service.redis.repository.redis;

import com.service.redis.entity.redis.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, String> {}
