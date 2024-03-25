package com.io.threegonew.controller;

import com.io.threegonew.domain.User;
import com.io.threegonew.dto.AddUserRequest;
import com.io.threegonew.dto.LoginRequest;
import com.io.threegonew.dto.UserInfoResponse;
import com.io.threegonew.repository.UserRepository;
import com.io.threegonew.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.io.threegonew.service.UserDetailService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserDetailService userDetailService;
    private final UserRepository userRepository;


    // 회원 조회
    // /api/users/search?userId=
    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<UserInfoResponse> getUserForShare(@RequestParam String userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserInfoResponse userInfo = UserInfoResponse.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .build();

            return ResponseEntity.ok(userInfo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @PostMapping("/register")
    @ResponseBody
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

}




