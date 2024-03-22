package com.io.threegonew.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
class UpdatePasswordReq {
@NotBlank
    private String currentPassword;
@NotBlank
    @Pattern(regexp = "(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}",
        message = "비밀번호는 8~15자 영문 대, 소문자, 숫자, 특수문자를 사용해주세요.")
    private String newPassword;
//비밀번호 변경에 필요한 request 객체

}
