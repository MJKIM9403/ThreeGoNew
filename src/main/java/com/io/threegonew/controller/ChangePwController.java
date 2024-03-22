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

    //    @PreAuthorize("isAuthenticated()")
//    @PostMapping("/mypage/changePw")
//    public String resetPassword(@Valid @ModelAttribute("passwordResetForm")
//                                PasswordResetForm passwordResetForm, BindingResult bindingResult, Principal principal, Model model){
//        User user = this.userService.getUser(principal.getName());
//
//        if (bindingResult.hasErrors()) {
//            return "changePw"; // 비밀번호 변경 페이지를 다시 렌더링하고 오류 표시
//        }
//
//        if (!this.userService.isSamePassword(user, passwordResetForm.getCurrentPw())) {
//            bindingResult.rejectValue("currentPw", "notCurrentPassword", "현재 비밀번호와 일치하지 않습니다.");
//            return "mypage/changePw";
//        }
//
//        if (passwordResetForm.getNewPw().equals(passwordResetForm.getCurrentPw())) {
//            bindingResult.rejectValue("newPw", "sameAsCurrentPassword", "현재 비밀번호와 동일한 비밀번호입니다.");
//            return "mypage/changePw";
//        }
//
//        if (!passwordResetForm.getNewPw().equals(passwordResetForm.getNewPwConfirm())) {
//            bindingResult.rejectValue("newPwConfirm", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
//            return "mypage/changePw";
//        }
//
//        try {
//            userService.resetPassword(user, passwordResetForm.getNewPw());
//        } catch (Exception e) {
//            e.printStackTrace();
//            bindingResult.reject("modifyPasswordFailed", e.getMessage());
//            return "mypage/changePw";
//        }
//
//        model.addAttribute("data", new Message("비밀번호 변경 되었습니다.", "/"));
//        return "mypage/changePw"; // 변경 성공
//    }
//
//
//}
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