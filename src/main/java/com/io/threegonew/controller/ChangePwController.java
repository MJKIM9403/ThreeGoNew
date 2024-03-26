package com.io.threegonew.controller;

import com.io.threegonew.domain.User;
import com.io.threegonew.dto.PasswordResetForm;
import com.io.threegonew.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class ChangePwController {
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage/changePw")
    public String resetPassword(Model model) {
        model.addAttribute("passwordResetForm", new PasswordResetForm());
        return "mypage/changePw"; // 비밀번호 변경 페이지를 랜더링
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/mypage/changePw")

    public ResponseEntity<String> changePassword(@RequestParam("currentPw") String currentPassword,
                                                 @RequestParam("newPw") String newPassword,
                                                 @RequestParam("newPwConfirm") String newPasswordConfirm,
                                                 Principal principal) {
        User user = this.userService.getUser(principal.getName());

        // 비밀번호 일치 여부 확인
        if (!this.userService.isSamePassword(user, currentPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호 일치 여부 확인
        if (!newPassword.equals(newPasswordConfirm)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("새 비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 변경
        try {
            userService.resetPassword(user, newPassword);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 변경 중 오류가 발생했습니다.");
        }

        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }


}