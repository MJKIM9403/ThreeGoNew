package com.io.threegonew.controller;

import com.io.threegonew.dto.MailDTO;
import com.io.threegonew.service.SendEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class SendEmailController {
    private final SendEmailService SendEmailService;

//    @ResponseBody
//    @PostMapping("join/email")
//    public String SendEmailAuth(@RequestBody Map<String, String> request){
//        String email = request.get("email");
//        String userId = request.get("userId");
//
//        MailDTO dto = sendEmailService.createMailAndChangePassword(email, userId);
//        sendEmailService.mailSend(dto);
//    }
//}
}