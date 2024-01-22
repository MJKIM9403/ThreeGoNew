package com.io.threegonew.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ValidationController {
    @GetMapping("/checkDuplicateId")
    public ResponseEntity<Boolean> checkDuplicateId(@RequestParam String inputId) {
        // 실제로는 DB 등에서 중복 여부를 확인하는 로직이 들어가야 합니다.
        boolean isDuplicate = checkIfIdIsDuplicate(inputId);
        return ResponseEntity.ok(isDuplicate);
    }

    // 실제로는 DB 등에서 중복 여부를 확인하는 메서드
    private boolean checkIfIdIsDuplicate(String inputId) {
        // 여기에 실제 중복 확인 로직을 구현합니다.
        // 예: 임시로 inputId가 "admin"이면 중복으로 간주하도록 설정
        return "admin".equals(inputId);
    }
}
