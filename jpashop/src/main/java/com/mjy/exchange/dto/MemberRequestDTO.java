package com.mjy.exchange.dto;

import com.mjy.exchange.entity.Member;
import com.mjy.exchange.status.MemberStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberRequestDTO {
    private String username;
    private String email;
    private String password;
    private String phone;

    @Builder
    public MemberRequestDTO(String username, String email, String password, String phone) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    // DTO -> Entity 변환
    public Member toEntity() {
        return Member.builder()
                .username(username)
                .email(email)
                .password(password)
                .phone(phone)
                .memberStatus(MemberStatus.ACTIVE)
                .build();
    }
}