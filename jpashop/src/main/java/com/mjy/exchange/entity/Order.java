package com.mjy.exchange.entity;

import com.mjy.exchange.status.OrderStatus;
import com.mjy.exchange.status.OrderType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ORDERS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity{
    private BigDecimal price;
    private BigDecimal quantity;
    @Enumerated(EnumType.STRING)
    private OrderType orderType;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MARKET_ID")
    private Market market;

    @Builder
    public Order(Member member, Market market, BigDecimal price, BigDecimal quantity, OrderType orderType, OrderStatus orderStatus) {
        this.member = member;
        this.market = market;
        this.price = price;
        this.quantity = quantity;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
    }
}
