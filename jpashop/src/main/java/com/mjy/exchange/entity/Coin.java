package com.mjy.exchange.entity;

import com.mjy.exchange.status.CoinStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coin extends BaseEntity {
    private String name;
    private String symbol;
    private Integer decimalValue;
    private String network;
    @Enumerated(EnumType.STRING)
    private CoinStatus coinStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MARKET_ID")
    private Market market;

    @Builder
    public Coin(String name, String symbol, Integer decimalValue, String network, CoinStatus coinStatus, Market market) {
        this.name = name;
        this.symbol = symbol;
        this.decimalValue = decimalValue;
        this.network = network;
        this.coinStatus = coinStatus;
        this.market = market;
    }
}
