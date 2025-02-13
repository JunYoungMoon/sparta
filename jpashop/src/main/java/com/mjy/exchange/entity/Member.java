package com.mjy.exchange.entity;


import com.mjy.exchange.status.MemberStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    private String username;
//    @Column(name = "email", unique = true)
    private String email;
    private String password;
    private String phone;
    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    @Builder
    public Member(String username, String email, String password, String phone, MemberStatus memberStatus) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.memberStatus = memberStatus;
    }

    @Embedded
    private Period usePeriod;
    @Embedded
    private Address homeAddress;

    public void updateFiled(String email, String phone) {
        this.email = email;
        this.phone = phone;
    }

    public void updateUsePeriod(LocalDateTime startDate, LocalDateTime endDate) {
        this.usePeriod = Period.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    public void updateHomeAddress(String city, String street, String zipCode) {
        this.homeAddress = Address.builder()
                .city(city)
                .street(street)
                .zipCode(zipCode)
                .build();
    }
}
