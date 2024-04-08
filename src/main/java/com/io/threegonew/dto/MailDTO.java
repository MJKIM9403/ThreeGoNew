package com.io.threegonew.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MailDTO {
    private String address;
    private String title;
    private String message;
    private int number; // 인증번호 변수 추가

    private String format; // 새로운 필드 추가


}
