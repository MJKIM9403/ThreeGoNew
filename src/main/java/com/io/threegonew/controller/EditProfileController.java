package com.io.threegonew.controller;

import com.io.threegonew.dto.UpdateUserProfileRequest;
import com.io.threegonew.dto.UserInfoResponse;
import com.io.threegonew.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public ResponseEntity updateProfile(@ModelAttribute UpdateUserProfileRequest request) {
        System.out.println(request.getName());
        System.out.println(request.getNewProfileImg());
        try{
            userService.modifyUserProfile(request);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
