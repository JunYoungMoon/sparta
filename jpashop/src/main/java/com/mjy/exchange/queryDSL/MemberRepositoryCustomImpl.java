package com.mjy.exchange.queryDSL;

import com.mjy.exchange.entity.Member;
import com.mjy.exchange.entity.QMember;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public List<Member> findMembers(String username, Long minId, Long maxId) {
        QMember m = QMember.member;
        BooleanBuilder builder = new BooleanBuilder();

        // 동적 조건 추가
        if (username != null && !username.isEmpty()) {
            builder.and(m.username.like("%" + username + "%"));
        }
        if (minId != null) {
            builder.and(m.id.goe(minId));  // minId보다 크거나 같은
        }
        if (maxId != null) {
            builder.and(m.id.loe(maxId));  // maxId보다 작거나 같은
        }

        return queryFactory.selectFrom(m)
                .where(builder)
                .orderBy(m.id.desc())
                .fetch();
    }
}
