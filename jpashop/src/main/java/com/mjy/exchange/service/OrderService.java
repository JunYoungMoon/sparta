package com.mjy.exchange.service;

import com.mjy.exchange.entity.Market;
import com.mjy.exchange.entity.Member;
import com.mjy.exchange.entity.Order;
import com.mjy.exchange.repository.MarketRepository;
import com.mjy.exchange.repository.MemberRepository;
import com.mjy.exchange.repository.OrderRepository;
import com.mjy.exchange.status.OrderStatus;
import com.mjy.exchange.status.OrderType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final MarketRepository marketRepository;

    public OrderService(OrderRepository orderRepository, MemberRepository memberRepository, MarketRepository marketRepository) {
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.marketRepository = marketRepository;
    }

    public Order createOrder(Long memberId, Long marketId, BigDecimal price, BigDecimal quantity, OrderType type) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));
        Market market = marketRepository.findById(marketId).orElseThrow(() -> new IllegalArgumentException("마켓 정보가 없습니다."));

        Order order = Order.builder()
                .member(member)
                .market(market)
                .price(price)
                .quantity(quantity)
                .orderStatus(OrderStatus.PENDING)
                .orderType(type)
                .build();

        return orderRepository.save(order);
    }
}
