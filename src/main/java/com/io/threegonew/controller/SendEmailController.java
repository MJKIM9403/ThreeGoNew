package com.io.threegonew.controller;

import com.io.threegonew.dto.MailDTO;
import com.io.threegonew.service.SendEmailService;
import com.io.threegonew.util.TempPassword;
import com.io.threegonew.util.TempPasswordEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.Map;

@RestController
@RequestMapping("/api/join")
@RequiredArgsConstructor
public class SendEmailController {
    private final SendEmailService sendEmailService;

//등록된 이메일로 가입 번호를 발송, 발송된 번호 확인 후 가입 통과 시키기.
    @PostMapping("/sendEmail")
    public ResponseEntity sendIdAuth(@RequestBody Map<String, String> request){
        String email = request.get("email");
        try{
//            String tempPw = TempPassword.makeRandomPw(8);
            String tempEmail = TempPasswordEmail.makeRandomPw();
            MailDTO dto = sendEmailService.createVerificationCode(email, tempEmail);
            sendEmailService.sendVerificationCode(dto);
            return ResponseEntity.ok(tempEmail);
        }catch (MessagingException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}