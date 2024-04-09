package com.io.threegonew.controller;

import com.io.threegonew.dto.MailDTO;
import com.io.threegonew.service.SendEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class SendEmailController {
    private final SendEmailService sendEmailService;

//등록된 이메일로 가입 번호를 발송, 발송된 번호 확인 후 가입 통과 시키기.
    @PostMapping("/join/sendEmail")
    @ResponseBody
    public void sendIdAuth(@RequestBody Map<String, String> request){
        String email = request.get("email");
        String userId = request.get("userId");
        MailDTO dto = sendEmailService.createVerificationCode(email, userId);
        sendEmailService.sendVerificationCode(dto);
    }


}