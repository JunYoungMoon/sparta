package com.mjy.exchange.repository;


import com.mjy.exchange.entity.Member;
import com.mjy.exchange.queryDSL.MemberRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
}
