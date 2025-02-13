package com.mjy.exchange.dto;

import com.mjy.exchange.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberResponseDTO {
    private String username;
    private String email;
    private String phone;

    @Builder
    public MemberResponseDTO(String username, String email, String phone) {
        this.username = username;
        this.email = email;
        this.phone = phone;
    }

    // Entity -> ResponseDTO 변환
    public static MemberResponseDTO fromEntity(Member member) {
        return MemberResponseDTO.builder()
                .username(member.getUsername())
                .email(member.getEmail())
                .phone(member.getPhone())
                .build();
    }
}

