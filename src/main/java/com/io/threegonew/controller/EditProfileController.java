package com.io.threegonew.controller;

import com.io.threegonew.domain.Message;
import com.io.threegonew.domain.User;
import com.io.threegonew.dto.PasswordResetForm;
import com.io.threegonew.dto.UserInfoResponse;
import com.io.threegonew.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/editprofile") // editprofile 경로로 매핑
@RequiredArgsConstructor
public class EditProfileController {
    private final UserService userService;

    @GetMapping("")
    public String showEditProfilePage(Model model) {
//        // 현재 인증된 사용자의 아이디와 이메일 주소 가져오기
        String userId = userService.getCurrentUserId();
        UserInfoResponse loginUser = userService.findUserInfo(userId);
        System.out.println(loginUser.getId());
        System.out.println(loginUser.getName());
        System.out.println(loginUser.getEmail());
        System.out.println(loginUser.getAbout());
        model.addAttribute("loginUser", loginUser);
//        String email = userService.getCurrentUserEmail();
//        String name = userService.getCurrentUserName();
//        String about = userService.getCurrentUserAbout();
//
//        // 모델에 사용자 정보 추가
//        model.addAttribute("userId", userId);
//        model.addAttribute("email", email);
//        model.addAttribute("name", name);
//        model.addAttribute("about", about);
        return "editprofile";
    }

    @PostMapping("")
    @ResponseBody
    public ResponseEntity updateProfile(@RequestParam(value = "id") String userId,
                                                @RequestParam(value = "name") String name,
                                                @RequestParam(value = "about") String about)
    {
        System.out.println("id: " + userId);
        System.out.println("name: " + name);
        System.out.println("about: " + about);
        try{
            userService.modifyUserProfile(userId, name, about);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
