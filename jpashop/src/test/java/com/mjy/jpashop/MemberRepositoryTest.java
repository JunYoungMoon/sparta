package com.mjy.jpashop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void testMember() throws Exception {
        //given
        Member memberA = new Member();
        memberA.setName("memberA");

        Member memberB = new Member();
        memberB.setName("memberB");

        //when
        Long saveId = memberRepository.save(memberA);
        Member findMember = memberRepository.find(saveId);

        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(memberA.getId());
        Assertions.assertThat(findMember.getName()).isEqualTo(memberA.getName());
        Assertions.assertThat(findMember).isEqualTo(memberA);
    }

}