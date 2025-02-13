package com.mjy.exchange.entity;

import com.mjy.exchange.status.WithdrawalRequestStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class WithdrawalRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal amount;
    private BigDecimal fee;
    private String address;
    private LocalDateTime requestAt;
    private LocalDateTime processedAt;
    @Enumerated(EnumType.STRING)
    private WithdrawalRequestStatus withdrawalRequestStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WALLET_ID")
    private Wallet wallet;
}

