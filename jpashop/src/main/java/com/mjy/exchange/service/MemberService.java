package com.mjy.exchange.service;

import com.mjy.exchange.entity.Member;
import com.mjy.exchange.repository.MemberRepository;
import com.mjy.exchange.status.MemberStatus;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member registerMember(String username, String email, String password, String phone) {
        Member member = Member.builder()
                .username(username)
                .email(email)
                .password(password)
                .phone(phone)
                .memberStatus(MemberStatus.ACTIVE)
                .build();

        try {
            return memberRepository.save(member);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }
    }

    public Member getMember(Long memberId) {
        //즉시 예외를 던지는 방식
        return memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
    }

    public Member updateMember(Long memberId, String newEmail, String newPhone) {
        Member member = getMember(memberId);

        //isPresent(), ifPresent()와 같이 나중에 처리 하는방식
//        Optional<Member> member = memberRepository.findById(memberId);
//
//        if (member.isPresent()) {
//            Member updatedMember = Member.builder()
//                    .id(member.get().getId())
//                    .email(newEmail)
//                    .phone(newPhone)
//                    .build();
//        } else {
//            new IllegalArgumentException("회원이 없음");
//        }

        member.updateFiled(newEmail, newPhone);

        return memberRepository.save(member);
    }

    public void deleteMember(Long memberId) {
        Member member = getMember(memberId);

        memberRepository.delete(member);
    }
}
