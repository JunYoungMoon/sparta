package com.mjy.exchange.service;

import com.mjy.exchange.entity.Coin;
import com.mjy.exchange.entity.Fee;
import com.mjy.exchange.entity.Market;
import com.mjy.exchange.repository.MarketRepository;
import com.mjy.exchange.status.MarketStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MarketService {

    private final MarketRepository marketRepository;

    public MarketService(MarketRepository marketRepository) {
        this.marketRepository = marketRepository;
    }

    public Market createMarket(String symbol, Double minOrderAmount, Double maxOrderAmount, MarketStatus status, Fee fee) {
        Market market = Market.builder()
                .symbol(symbol)
                .minOrderAmount(minOrderAmount)
                .maxOrderAmount(maxOrderAmount)
                .marketStatus(status)
                .fee(fee)
                .build();

        return marketRepository.save(market);
    }

//    public Market getMarket(){
//
//    }
}
