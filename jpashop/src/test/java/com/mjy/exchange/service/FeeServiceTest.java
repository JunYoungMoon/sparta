package com.mjy.exchange.service;

import com.mjy.exchange.entity.Coin;
import com.mjy.exchange.entity.Fee;
import com.mjy.exchange.status.CoinStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class FeeServiceTest {

    @Autowired
    private FeeService feeService;

    @Test
    @Transactional
    @Rollback(value = false)
    public void testCreateFee() throws Exception {
        //given
        Fee fee = feeService.createFee(new BigDecimal("0.15"), new BigDecimal("1000"), new BigDecimal("500"), new BigDecimal("10000"));

        //when & then
        assertEquals(new BigDecimal("0.15"),fee.getPercentage());
        assertEquals(new BigDecimal("1000"),fee.getFixedAmount());
        assertEquals(new BigDecimal("500"),fee.getMinFee());
        assertEquals(new BigDecimal("10000"),fee.getMaxFee());
    }

    @Test
    public void testGetFee() throws Exception {
        //given
        Fee fee = feeService.getFee(1L);

        //when & then
        assertEquals(new BigDecimal("0.15"),fee.getPercentage());
        assertEquals(new BigDecimal("1000"),fee.getFixedAmount());
        assertEquals(new BigDecimal("500"),fee.getMinFee());
        assertEquals(new BigDecimal("10000"),fee.getMaxFee());
    }
}