package com.mjy.exchange.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Wallet extends BaseEntity {
    private BigDecimal balance;
    private BigDecimal availableBalance;
    private BigDecimal frozenBalance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COIN_ID")
    private Coin coin;

}