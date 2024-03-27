package com.io.threegonew.controller;

import com.io.threegonew.dto.MailDTO;
import com.io.threegonew.repository.UserRepository;
import com.io.threegonew.service.SendEmailService;
import com.io.threegonew.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class FindPwcontroller {
    private final UserService userService;
    @Autowired
    private final SendEmailService sendEmailService;

    @GetMapping("/findPw")
    public String getFindPw() {
        return "findPw";
    }
@PostMapping("/findPw")
public @ResponseBody Map<String, String> pwFind(@RequestParam(value = "email") String email,
                                                @RequestParam(value = "userId") String userId) {
    Map<String, String> response = new HashMap<>();

    // 이메일과 아이디가 일치하는지 확인
    if (userService.userEmailCheck(email, userId)) {
        // 임시 비밀번호 생성
        String tempPassword = sendEmailService.getTempPassword();
        // 비밀번호 업데이트
        userService.updateUserPassword(userId, tempPassword);
        // 이메일 전송
        MailDTO mailDTO = sendEmailService.createMailAndChangePassword(email, userId);
        sendEmailService.mailSend(mailDTO);
        response.put("message", "이메일로 임시 비밀번호가 전송되었습니다.");
    } else {
        response.put("message", "이메일과 아이디가 일치하지 않습니다.");
    }

    return response;
}


//    이메일과 아이디 일치여부 check
//    @GetMapping("/findPw")
//    public @ResponseBody Map<String, Boolean> pw_find(@RequestParam(value = "email") String email,
//                                                      @RequestParam(value = "userId") String userId){
//        Map<String, Boolean> json = new HashMap<>();
//        boolean pwFindCheck = userService.userEmailCheck(email, userId);
//        json.put("check", pwFindCheck);
//        return json;
//    }



    //등록된 이메일로 임시비밀번호를 발송하고 발송된 임시비밀번호로 사용자의 pw를 변경하는 컨트롤러
    @PostMapping("/findPw/sendEmail")
    public @ResponseBody void sendEmail(String email, String userId){
        MailDTO dto = sendEmailService.createMailAndChangePassword(email, userId);
        sendEmailService.mailSend(dto);
        // 비밀번호 업데이트
        userService.updateUserPassword(email, "새로운비밀번호");
    }

}
