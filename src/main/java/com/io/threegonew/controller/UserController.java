package com.io.threegonew.controller;

import com.io.threegonew.dto.AddUserRequest;
import com.io.threegonew.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody AddUserRequest dto) {
        String userId = userService.save(dto);
        // 가입 성공 여부를 함께 반환하도록 수정
        return ResponseEntity.ok("가입을 환영합니다: " + userId);
    }
}

