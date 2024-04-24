package com.io.threegonew.commons;

import java.security.SecureRandom;

public class TempPasswordEmail {
    public static String makeRandomPw() {
        SecureRandom random = new SecureRandom();
        int randomNum = random.nextInt(100000); // 0부터 99999 사이의 랜덤 숫자 생성
        return String.format("%05d", randomNum); // 숫자를 5자리로 만들고, 앞을 0으로 채움
    }
}
