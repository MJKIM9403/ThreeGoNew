package com.io.threegonew.controller;

import com.io.threegonew.dto.AddUserRequest;
import com.io.threegonew.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserApiController {
    private final UserService userService;

    @PostMapping("/user")
    public String signup(AddUserRequest request){
        userService.save(request); //회원가입 메소드 호출
        return"redirect:/login"; //회원가입완료 후 로그인 페이지로 이동
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler()
                .logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

//    @GetMapping("/id/check")
//    public ResponseEntity<?> checkIdDuplication(@RequestParam(value = "userId") String userId ) throws BadRequestException{
//        System.out.println(userId);
//
//        if(userService.existsById(userId) == true){
//            throw new BadRequestException("이미 사용 중인 아이디입니다.");
//        }else {
//            return ResponseEntity.ok("사용 가능한 아이디입니다.");
//        }
//
//    }
}
