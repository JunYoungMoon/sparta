package com.mjy.jpashop2;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void testMember() throws Exception {
        //given
        Member memberA = new Member();
        memberA.setUsername("memberA");

        Member memberB = new Member();
        memberB.setUsername("memberB");

        //when
        Long saveId = memberRepository.save(memberA);
        Member findMember = memberRepository.find(saveId);

        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(memberA.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(memberA.getUsername());
        Assertions.assertThat(findMember).isEqualTo(memberA);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void testMember2() throws Exception {
        //given
        Member memberA = new Member();
        memberA.setUsername("memberA");
        Member memberB = new Member();
        memberB.setUsername("memberB");

        Team teamA = new Team();
        teamA.setName("teamA");

        //단방향 양방향 상관없이 외래키가 있는쪽이 주인이고 여기서 OneToMany나 ManyToOne을 해야한다.
        memberA.setTeam(teamA); // 연관관계 주인에게 입력해야 됨 OK
        teamA.getMembers().add(memberA);    //주인이 아닌곳에 가짜입력

        //양방향 관계에서는 순수 객체 상태를 고려해서 항상 양쪽에 값을 설정해야한다.
        //1차 캐시에서 주인이 아닌곳에 값을 넣지 않으면 값을 못가져온다.
        //teamA.getMembers().add(memberA);
        //member의 set안에 teamA.getMembers().add(this); 이렇게 넣기도 한다.
        //양방향 연결을 역방향으로 탐색할때 사용한다.
        //중요한건 단방향 연결만 잘하면 양방향 연결은 필요할때 추가해도 된다.

        teamRepository.save(teamA);
        memberRepository.save(memberA);

        //when
        Member findMember = memberRepository.find(memberA.getId());

        List<Member> members = findMember.getTeam().getMembers();

        //then
        Assertions.assertThat(members.size()).isEqualTo(1);
    }
}