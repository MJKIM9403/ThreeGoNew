package com.io.threegonew.controller;

import com.io.threegonew.dto.MailDTO;
import com.io.threegonew.service.SendEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SendEmailController {

    private final SendEmailService sendEmailService;

    @Autowired
    public SendEmailController(SendEmailService sendEmailService) {
        this.sendEmailService = sendEmailService;
    }

    @PostMapping("/join")
    public ResponseEntity<String> sendVerificationCode(@RequestBody String email) {
        // 이메일로 인증번호 전송
        sendEmailService.sendVerificationCode(email);

        // 클라이언트에게 응답
        return new ResponseEntity<>("이메일로 인증번호가 전송되었습니다.", HttpStatus.OK);
    }
}