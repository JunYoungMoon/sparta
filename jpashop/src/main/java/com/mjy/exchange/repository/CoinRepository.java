package com.mjy.exchange.repository;

import com.mjy.exchange.entity.Coin;
import com.mjy.exchange.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoinRepository extends JpaRepository<Coin, Long> {
    Optional<Coin> findBySymbol(String symbol);
}
