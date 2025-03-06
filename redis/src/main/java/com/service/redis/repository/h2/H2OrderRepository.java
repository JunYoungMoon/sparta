package com.service.redis.repository.h2;

import com.service.redis.entity.h2.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface H2OrderRepository extends JpaRepository<Order, Long> {
}
