package com.ana29.deliverymanagement.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UpdateRequestDto {
    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])[a-z0-9^\\s]{4,10}$", message = "닉네임은 4자 이상 10자 이하로 입력해주세요.")
    private String nickname;

    @Email(message = "유효한 이메일 주소를 입력하세요.")
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,25}$", message = "유효한 이메일 주소를 입력하세요.")
    private String email;

    @NotBlank(message = "전화번호는 필수 입력값입니다.")
    @Pattern(regexp = "^010-?\\d{4}-?\\d{4}$", message = "유효한 전화번호를 입력하세요.")
    private String phone;

}
