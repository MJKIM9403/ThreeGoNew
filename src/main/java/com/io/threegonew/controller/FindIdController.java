package com.io.threegonew.controller;

import com.io.threegonew.domain.User;
import com.io.threegonew.dto.UserInfoResponse;
import com.io.threegonew.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class FindIdController {

    private final UserService userService;

    @GetMapping("/findId")
    public String getFindId(){
        return "findId";
    }

    @PostMapping("/findId")
    public String findId(Model model, @RequestParam String email) {
        try {
            // 이메일로 사용자 정보를 검색합니다.
            User foundUser = userService.findUserByEmail(email);

            if (foundUser != null) {
                // 검색된 사용자 정보를 모델에 추가합니다.
                model.addAttribute("findId", foundUser.getId());
                return "findIdResult"; // 아이디를 찾은 경우 결과 페이지 반환
            } else {
                model.addAttribute("msg", "해당 이메일로 등록된 사용자가 없습니다.");
                return "findIdResult"; // 사용자를 찾지 못한 경우 다시 아이디 찾기 폼으로 이동
            }
        } catch (Exception e){
            model.addAttribute("msg", "오류가 발생하였습니다.");
            e.printStackTrace();
            System.out.println("Exception occurred: " + e.getMessage()); // 에러 발생 시 메시지 출력
            return "findId"; // 에러 발생 시 아이디 찾기 폼으로 이동
        }
    }
}
