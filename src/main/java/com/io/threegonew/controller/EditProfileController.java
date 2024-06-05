package com.io.threegonew.controller;

import com.io.threegonew.commons.SecurityUtils;
import com.io.threegonew.dto.UpdateUserProfileRequest;
import com.io.threegonew.dto.UserInfoResponse;
import com.io.threegonew.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class EditProfileController {
    private final UserService userService;

    @GetMapping("editprofile")
    public String showEditProfilePage(Model model) {
//        // 현재 인증된 사용자의 아이디와 이메일 주소 가져오기
        String userId = SecurityUtils.getCurrentUsername();
        UserInfoResponse loginUser = userService.findUserInfo(userId);

        model.addAttribute("loginUser", loginUser);
        return "editProfile";
    }

    @PostMapping("editprofile")
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


    @GetMapping("/delete")
    public String delete(){
        return "/delete";
    }

//삭제
@DeleteMapping("/delete")
public ResponseEntity deleteUser(@RequestParam("currentPassword") String currentPassword) {
    String userId = userService.getCurrentUserId();
    if (userService.isSamePassword(userService.getUser(userId), currentPassword)) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok().build(); // 삭제 성공 시 200 OK 응답 반환
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 삭제 중 오류 발생 시 500 에러 응답 반환
        }
    } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 일치하지 않습니다."); // 비밀번호 불일치 시 400 에러 응답 반환
    }
}


}
