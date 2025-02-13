package com.mjy.exchange.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Fee extends BaseEntity {
    private BigDecimal percentage;
    private BigDecimal fixedAmount;
    private BigDecimal minFee;
    private BigDecimal maxFee;

    @Builder
    public Fee(BigDecimal percentage, BigDecimal fixedAmount, BigDecimal minFee, BigDecimal maxFee) {
        this.percentage = percentage;
        this.fixedAmount = fixedAmount;
        this.minFee = minFee;
        this.maxFee = maxFee;
    }
}
