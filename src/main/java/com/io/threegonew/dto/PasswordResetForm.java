package com.io.threegonew.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetForm {

    @NotEmpty(message = "현재 비밀번호 필수항목입니다.")
    private String currentPw;

    @NotEmpty(message = "변경할 비밀번호는 필수항목입니다.")
    private String newPw;

    @NotEmpty(message = "변경할 비밀번호 확인은 필수항목입니다.")
    private String newPwConfirm;


}
