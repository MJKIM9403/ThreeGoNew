package com.io.threegonew.controller;

import com.io.threegonew.domain.User;
import com.io.threegonew.dto.AddUserRequest;
import com.io.threegonew.dto.LoginRequest;
import com.io.threegonew.repository.UserRepository;
import com.io.threegonew.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.io.threegonew.service.UserDetailService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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


//
@PostMapping("/login")
public String login(@RequestBody LoginRequest loginDto,
                    HttpServletRequest request) {

    System.out.println("usercontroller : " + loginDto.getId());

    // 인증 로직을 사용하여 사용자를 인증하려고 시도합니다.
    User loginUser = userService.authenticateUser(loginDto.getId(), loginDto.getPw());

    if (loginUser == null) {
        // 인증 실패 시 리다이렉트 및 메시지 처리 (선택사항)
        return "redirect:/login";
    }

    // 세션에 인증된 사용자 설정
    HttpSession session = request.getSession();
    session.setAttribute("loginUser", loginUser);

    // 리다이렉트할 때는 return 구문을 여기에 작성합니다.
    return "redirect:/index";
}



    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler()
                .logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }



}




