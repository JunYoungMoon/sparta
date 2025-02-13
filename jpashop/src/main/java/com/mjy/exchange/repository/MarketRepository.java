package com.mjy.exchange.repository;

import com.mjy.exchange.entity.Market;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketRepository extends JpaRepository<Market, Long> {
}
