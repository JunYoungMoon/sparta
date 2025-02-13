package com.mjy.exchange.service;

import com.mjy.exchange.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private MemberService memberService;

    @Test
    public void testCreateOrder() throws Exception {
        //given
        Member member = memberService.registerMember("john_doe", "john.doe@example.com", "password123", "010-1234-5678");
        
        //when
        
        //then
    }

}