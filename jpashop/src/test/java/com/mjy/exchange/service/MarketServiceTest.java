package com.mjy.exchange.service;

import com.mjy.exchange.entity.Coin;
import com.mjy.exchange.entity.Fee;
import com.mjy.exchange.entity.Market;
import com.mjy.exchange.status.CoinStatus;
import com.mjy.exchange.status.MarketStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MarketServiceTest {

    @Autowired
    private MarketService marketService;
    @Autowired
    private CoinService coinService;
    @Autowired
    private FeeService feeService;

    @Test
    @Transactional
    @Rollback(value = false)
    public void testCreateMarket() throws Exception {
        //given
        Fee fee = feeService.getFee(1L);

        //when
        Market market = marketService.createMarket("KRW", 0.1, 10000.0, MarketStatus.ACTIVE, fee);

        assertEquals(fee, market.getFee());
    }

}