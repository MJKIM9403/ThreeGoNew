package com.io.threegonew.service;

import com.io.threegonew.Key;
import com.io.threegonew.dto.MailDTO;
import com.io.threegonew.repository.UserRepository;
import com.io.threegonew.util.JavaMailSenderImpl;
import com.io.threegonew.util.TempPassword;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SendEmailService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JavaMailSender javaMailSender;
    private final JavaMailSenderImpl javaMailSenderImpl;
    private static final String FROM_ADDRESS = Key.GMAIL;
    //    인증번호
    private static int number;
    public static void createNumber(){
        number = (int)(Math.random() * (90000)) + 100000;// (int) Math.random() * (최댓값-최소값+1) + 최소값
    }

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


//    인증번호
public MimeMessage CreateMail(String mail){
    createNumber();
    MimeMessage message = javaMailSender.createMimeMessage();
    System.out.println("이멜 전송 완료!");

//    try {
//        message.setFrom(FROM_ADDRESS);
//        message.setRecipients(MimeMessage.RecipientType.TO, mail);
//        message.setSubject("이메일 인증");
//        String body = "";
//        body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
//        body += "<h1>" + number + "</h1>";
//        body += "<h3>" + "감사합니다." + "</h3>";
//        message.setText(body,"UTF-8", "html");
//    } catch (MessagingException e) {
//        e.printStackTrace();
//    }

    return message;
}

    public int sendMail(String mail){
        MimeMessage message = CreateMail(mail);
        javaMailSender.send(message);

        return number;
    }
}
