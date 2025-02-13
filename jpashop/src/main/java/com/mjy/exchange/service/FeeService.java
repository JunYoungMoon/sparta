package com.mjy.exchange.service;

import com.mjy.exchange.entity.Coin;
import com.mjy.exchange.entity.Fee;
import com.mjy.exchange.repository.FeeRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FeeService {

    private final FeeRepository feeRepository;

    public FeeService(FeeRepository feeRepository) {
        this.feeRepository = feeRepository;
    }

    public Fee createFee(BigDecimal percentage, BigDecimal fixedAmount, BigDecimal minFee, BigDecimal maxFee) {
        Fee fee = Fee.builder()
                .percentage(percentage)
                .fixedAmount(fixedAmount)
                .minFee(minFee)
                .maxFee(maxFee)
                .build();

        return feeRepository.save(fee);
    }

    public Fee getFee(Long feeId) {
        return feeRepository.findById(feeId).orElseThrow(() -> new IllegalArgumentException("수수료 정보가 없습니다."));
    }

}
