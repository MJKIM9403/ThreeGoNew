package com.io.threegonew.controller;

import com.io.threegonew.dto.MailDTO;
import com.io.threegonew.service.SendEmailService;
import com.io.threegonew.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class FindPwController {
    private final SendEmailService sendEmailService;

    @GetMapping("/findPw")
    public String getFindPw() {
        return "findPw";
    }


    //등록된 이메일로 임시비밀번호를 발송하고 발송된 임시비밀번호로 사용자의 pw를 변경하는 컨트롤러
    @PostMapping("/findPw/sendEmail")
    @ResponseBody
    public void sendEmail(@RequestBody Map<String, String> request){
        String email = request.get("email");
        String userId = request.get("userId");
        MailDTO dto = sendEmailService.createMailAndChangePassword(email, userId);
        sendEmailService.mailSend(dto);
    }

}

