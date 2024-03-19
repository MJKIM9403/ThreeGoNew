package com.io.threegonew.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserRequest {
    @Size(min = 3, max = 25)
    @NotEmpty(message = "사용자ID는 필수항목입니다.")
    private String id;

    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String pw;

    @NotEmpty(message = "이름은 필수항목입니다.")
    private String name;

    @NotEmpty(message = "이메일은 필수항목입니다.")
    @Email
    private String email;
    private String u_ofile;
    private String u_sfile;
    private String about;
}
