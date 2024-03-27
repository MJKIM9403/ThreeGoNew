package com.io.threegonew.util;

import java.security.SecureRandom;
import java.util.regex.Pattern;

public class TempPassword {
    private static final char[] randomCharArr = new char[]{
            //number
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            //uppercase
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            //lowercase
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            //special symbols
            '!', '@', '#', '$', '%', '^', '*', '+', '=', '-'
    };
    public static String makeRandomPw(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();

        int randomCharArrLength = randomCharArr.length;
        for (int i = 0; i < length; i++) {
            stringBuilder.append(randomCharArr[random.nextInt(randomCharArrLength)]);
        }

        String randomPw = stringBuilder.toString();

        // 최소 8자리에 대문자, 소문자, 숫자, 특수문자 각 1개 이상 포함
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8}$";
        if (!Pattern.matches(pattern, randomPw)) {
            return makeRandomPw(length);    //비밀번호 조건(패턴)에 맞지 않는 경우 메서드 재실행
        }
        return randomPw;
    }
}
