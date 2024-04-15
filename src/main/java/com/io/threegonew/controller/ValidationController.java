package com.io.threegonew.controller;

import com.io.threegonew.repository.UserRepository;
import com.io.threegonew.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ValidationController {

    private final UserService userService;

    @GetMapping("/checkDuplicateId")
    public ResponseEntity<Boolean> checkDuplicateId(@RequestParam(name = "userId") String userId) {
        boolean isDuplicate = userService.isIdDuplicate(userId);

        if (isDuplicate) {
            System.out.println("중복된 아이디가 감지되었습니다: " + userId); // 콘솔 출력 추가
            return ResponseEntity.ok(true);
        }
        else {
            System.out.println("사용 가능한 아이디입니다: " + userId); // 콘솔 출력 추가
            return ResponseEntity.ok(false);
        }
    }


    @GetMapping("/checkDuplicateEmail")
    public ResponseEntity<Boolean> checkDuplicateEmail(@RequestParam(name = "email") String email){
        System.out.println("email에 대한 요청이 수신되었습니다:" + email);
        boolean isDuplicate = userService.isEmailDuplicate(email);

        if(isDuplicate) {
            System.out.println("중복된 이메일이 감지되었습니다." +  email);
            return ResponseEntity.ok(true);
        }
        else{
            System.out.println("사용가능이메일" + email);
            return ResponseEntity.ok(false);
        }
    }


}
