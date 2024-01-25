package com.io.threegonew.controller;

import com.io.threegonew.dto.AddUserRequest;
import com.io.threegonew.dto.LoginRequest;
import com.io.threegonew.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
    public String login(Model model,
                        @RequestParam(value="error", required = false) String error,
                        @RequestParam(value="exception", required = false) String exception,
                        @RequestParam(value="id", required = false) String id) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        // id 값 로깅
        System.out.println("Received id: " + id);

        return "/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler()
                .logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

}




