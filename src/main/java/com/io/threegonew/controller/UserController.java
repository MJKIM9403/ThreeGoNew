package com.io.threegonew.controller;

import com.io.threegonew.dto.AddUserRequest;
import com.io.threegonew.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.io.threegonew.service.UserDetailService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserDetailService userDetailService;

    @Autowired
    public UserController(UserService userService, UserDetailService userDetailService) {
        this.userService = userService;
        this.userDetailService = userDetailService;
    }



    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody AddUserRequest dto) {
        //이미 등록된 사용자 인지 확인
        if (userService.isIdDuplicate(dto.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 등록된 사용자입니다.");
        }

        // 등록된 사용자가 아니라면 가입 진행
        String userId = userService.save(dto);
        // 가입 성공 여부를 함께 반환하도록 수정
        return ResponseEntity.ok("가입을 환영합니다: " + userId);
    }

    @PostMapping("/login")
    public String login(@RequestParam String u_id, @RequestParam String u_pw, Model model) {
        // 로그인 처리 메서드
        UserDetails userDetails = userDetailService.loadUserByUsername(u_id);

        // 실제로는 비밀번호를 검증하는 등의 로직이 들어가야 합니다.
        // 여기서는 간단히 예제로만 작성하였습니다.
        if (userDetails.getPassword().equals(u_pw)) {
            return "redirect:/index"; // 로그인 성공 시 이동할 페이지
        } else {
            model.addAttribute("error", true);
            return "login"; // 로그인 실패 시 다시 로그인 페이지로
        }
    }
}


