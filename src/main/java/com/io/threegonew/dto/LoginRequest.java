package com.io.threegonew.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    @NotEmpty(message = "사용자ID 또는 비밀번호를 확인해 주세요.")
    private  String id;

    @NotEmpty(message = "사용자ID 또는 비밀번호를 확인해 주세요.")
    private String pw;
}
