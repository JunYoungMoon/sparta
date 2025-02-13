package com.mjy.exchange.service;

import com.mjy.exchange.entity.Coin;
import com.mjy.exchange.repository.CoinRepository;
import com.mjy.exchange.status.CoinStatus;
import org.springframework.stereotype.Service;

@Service
public class CoinService {

    private final CoinRepository coinRepository;

    public CoinService(CoinRepository coinRepository) {
        this.coinRepository = coinRepository;
    }

    public Coin createCoin(String name, String symbol, Integer decimal, String network, CoinStatus status) {
        Coin order = Coin.builder()
                .name(name)
                .symbol(symbol)
                .decimalValue(decimal)
                .network(network)
                .coinStatus(status)
                .build();

        return coinRepository.save(order);
    }

    public Coin getCoin(String symbol) {
        return coinRepository.findBySymbol(symbol).orElseThrow(() -> new IllegalArgumentException("코인이 존재 하지 않습니다."));
    }
}
