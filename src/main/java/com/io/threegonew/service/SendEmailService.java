package com.io.threegonew.service;

import com.io.threegonew.dto.MailDTO;
import com.io.threegonew.repository.UserRepository;
import com.io.threegonew.util.TempPassword;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SendEmailService {

    @Autowired
    UserRepository userRepository;
    UserService userService;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "본인의 이메일 주소를 입력하세요";

    public MailDTO createMailAndChangePassword(String email, String id) {
        String str = getTempPassword();
        MailDTO dto = new MailDTO();
        dto.setAddress(email);
        dto.setTitle(id + "님의 임시 비밀번호 안내 이메일 입니다.");
        dto.setMessage("안녕하세요. 임시 비밀번호 안내 관련 이메일입니다." + "[" + id + "}" + "님의 " + str + "입니다.");
        updatePassword(str, email);
        return dto;
    }

    public void updatePassword(String str, String email) {
        String pw = bCryptPasswordEncoder.encode(str);
        userService.updateUserPassword(email, pw); // updateUserPassword 메서드 사용하여 비밀번호 업데이트
    }

    public String getTempPassword(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";

        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;

    }

//    STMP mailSend
    public void mailSend(MailDTO mailDto){
        System.out.println("이멜 전송 완료!");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getAddress());
        message.setFrom(SendEmailService.FROM_ADDRESS);
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getMessage());

        mailSender.send(message);
    }
}
