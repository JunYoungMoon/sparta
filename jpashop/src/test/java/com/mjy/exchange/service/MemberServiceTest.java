package com.mjy.exchange.service;

import com.mjy.exchange.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    @Transactional
    @Rollback(false)
    public void testRegisterMember() throws Exception {
        //given
        Member member = memberService.registerMember("john_doe", "john.doe@example.com", "password123", "010-1234-5678");

        //when & then
        assertNotNull(member.getId());
        assertEquals("john_doe", member.getUsername());
        assertEquals("john.doe@example.com", member.getEmail());
    }

    @Test
    public void testRegisterMember_DuplicateEmail() throws Exception {
        //given
        String email = "john.doe@example.com";

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberService.registerMember("john_doe", email, "password123", "010-1234-5678");
        });

        // then
        assertEquals("이미 등록된 이메일입니다.", exception.getMessage());
    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void testUpdateMember() throws Exception {
        //when
        Member updatedMember = memberService.updateMember(1L, "new_email@example.com", "010-0000-0000");

        //then
        assertEquals("new_email@example.com", updatedMember.getEmail());
        assertEquals("010-0000-0000", updatedMember.getPhone());
    }

    @Test
    public void testGetMember() throws Exception {
        //given
        Member member = memberService.registerMember("john_doe", "john.doe@example.com", "password123", "010-1234-5678");

        //when
        Member findMember = memberService.getMember(member.getId());

        //then
        assertNotNull(findMember.getId());
        assertEquals("john_doe", findMember.getUsername());
        assertEquals("010-1234-5678", findMember.getPhone());
    }

    @Test
    public void testGetMember_NotExist() throws Exception {
        //when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> memberService.getMember(9999L));

        assertEquals("회원이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void testDeleteMember() throws Exception {
        //given
        Member member = memberService.registerMember("john_doe", "john.doe@example.com", "password123", "010-1234-5678");

        //when
        memberService.deleteMember(member.getId());

        //then
        assertThrows(IllegalArgumentException.class, () -> memberService.getMember(member.getId()));
    }
}