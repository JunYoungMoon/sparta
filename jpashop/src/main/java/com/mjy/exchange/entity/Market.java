package com.mjy.exchange.entity;

import com.mjy.exchange.status.MarketStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Market extends BaseEntity{
    private String symbol;
    private Double minOrderAmount;
    private Double maxOrderAmount;
    @Enumerated(EnumType.STRING)
    private MarketStatus marketStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FEE_ID")
    private Fee fee;

    @Builder
    public Market(String symbol, Double minOrderAmount, Double maxOrderAmount, MarketStatus marketStatus, Fee fee) {
        this.symbol = symbol;
        this.minOrderAmount = minOrderAmount;
        this.maxOrderAmount = maxOrderAmount;
        this.marketStatus = marketStatus;
        this.fee = fee;
    }
}

