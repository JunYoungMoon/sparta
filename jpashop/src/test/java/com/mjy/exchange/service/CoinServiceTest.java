package com.mjy.exchange.service;

import com.mjy.exchange.entity.Coin;
import com.mjy.exchange.status.CoinStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CoinServiceTest {

    @Autowired
    private CoinService coinService;

    @Test
    @Transactional
    @Rollback(value = false)
    public void testCreateCoin() throws Exception {
        //given
        Coin BTC = coinService.createCoin("Bitcoin", "BTC", 8, "Bitcoin Network", CoinStatus.ACTIVE);
        Coin ETH = coinService.createCoin("Ethereum", "ETH", 18, "Ethereum Network", CoinStatus.ACTIVE);
        Coin KRW = coinService.createCoin("Korean Won", "KRW", 2, "KRW Network", CoinStatus.ACTIVE);

        //when & then
        assertEquals("Bitcoin", BTC.getName());
        assertEquals("Ethereum", ETH.getName());
        assertEquals("Korean Won", KRW.getName());
    }

    @Test
    public void testGetCoin() throws Exception {
        //when
        Coin coin = coinService.getCoin("BTC");
        //then
        assertEquals("Bitcoin", coin.getName());
    }

    @Test
    public void testGetCoin_NotExist() throws Exception {
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> coinService.getCoin("GLM"));
        //then
        assertEquals("코인이 존재 하지 않습니다.", exception.getMessage());
    }

}