package com.io.threegonew.commons;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class VerificationCodeManager {
    // 인증번호와 유효시간을 저장하는 맵
    private static Map<String, Long> verificationCodes = new HashMap<>();

    // 인증번호와 유효시간을 저장
    public static void storeVerificationCode(String code, long expirationTime) {
        verificationCodes.put(code, System.currentTimeMillis() + expirationTime);
    }

    // 인증번호가 유효한지 확인
    public static boolean isVerificationCodeValid(String code) {
        Long expirationTime = verificationCodes.get(code);
        if (expirationTime == null) {
            // 인증번호가 없음
            return false;
        }
        // 현재 시간과 유효시간을 비교하여 만료되었는지 확인
        return System.currentTimeMillis() < expirationTime;
    }

    // 만료된 인증번호 제거 (매 3분마다 호출)
    @Scheduled(fixedRate = 180000) // 3분마다 실행
    public static void removeExpiredVerificationCodes() {
        long currentTime = System.currentTimeMillis();
        verificationCodes.entrySet().removeIf(entry -> entry.getValue() < currentTime);
    }

}
