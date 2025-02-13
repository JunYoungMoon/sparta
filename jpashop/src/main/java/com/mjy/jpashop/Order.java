package com.mjy.jpashop;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "ORDERS")
public class Order {

    @Id @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;
    private LocalDateTime orderData;

    @ManyToOne
    @JoinColumn(name ="MEMBER_ID")
    private Member member;

    private OrderStatus status;

    @OneToOne
    @JoinColumn(name="DELIVERY_ID")
    private Delivery delivery;
}
