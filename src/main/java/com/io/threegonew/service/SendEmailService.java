package com.io.threegonew.service;

import com.io.threegonew.ApiKey;
import com.io.threegonew.dto.MailDTO;
import com.io.threegonew.repository.UserRepository;
import com.io.threegonew.util.JavaMailSenderImpl;
import com.io.threegonew.util.TempPassword;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SendEmailService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JavaMailSenderImpl javaMailSenderImpl;
    private static final String FROM_ADDRESS = ApiKey.GMAIL;

    @Transactional
    public MailDTO createMailAndChangePassword(String email, String id) {
        String tempPw = TempPassword.makeRandomPw(8);
        MailDTO dto = new MailDTO();
        dto.setAddress(email);
        dto.setTitle(id + "님의 임시 비밀번호 안내 이메일 입니다.");
        dto.setMessage("안녕하세요. 임시 비밀번호 안내 관련 이메일입니다." + "[" + id + "]" + "님의 " + tempPw + "입니다.");
        userService.updateUserPassword(id, tempPw);
        return dto;
    }

//    public String getTempPassword(){
//        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
//                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
//
//        String str = "";
//
//        int idx = 0;
//        for (int i = 0; i < 10; i++) {
//            idx = (int) (charSet.length * Math.random());
//            str += charSet[idx];
//        }
//        return str;
//
//    }

//    STMP mailSend
    public void mailSend(MailDTO mailDto){
        System.out.println("이멜 전송 완료!");
        Map<String, String> mailInfo = new HashMap<>();
        mailInfo.put("from", FROM_ADDRESS);
        mailInfo.put("to", mailDto.getAddress());
        mailInfo.put("subject", mailDto.getTitle());
        mailInfo.put("content", mailDto.getMessage());
        mailInfo.put("format", "text/plain");
        try {
            javaMailSenderImpl.emailSending(mailInfo); // JavaMailSenderImpl 인스턴스를 통해 이메일 전송
        } catch (MessagingException e) {
            e.printStackTrace(); // 에러 발생 시 에러 메시지 출력
        }
    }
}
