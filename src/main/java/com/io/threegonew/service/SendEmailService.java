package com.io.threegonew.service;

import com.io.threegonew.Key;
import com.io.threegonew.dto.MailDTO;
import com.io.threegonew.commons.JavaMailSenderImpl;
import com.io.threegonew.commons.VerificationCodeManager;
import com.io.threegonew.commons.TempPassword;
import com.io.threegonew.commons.TempPasswordEmail;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SendEmailService {
    private final VerificationCodeManager verificationCodeManager;
    private final UserService userService;
    private final JavaMailSenderImpl javaMailSenderImpl;
    private static final String FROM_ADDRESS = Key.GMAIL;
    // 유효시간 설정 (3분)
    private static final long EXPIRATION_TIME = 3 * 60 * 1000; // milliseconds

    // 인증번호와 유효시간을 저장하는 맵
    private static Map<String, Long> verificationCodes = new HashMap<>();

    // 인증번호와 유효시간을 저장
    public static void storeVerificationCode(String code) {
        verificationCodes.put(code, System.currentTimeMillis() + EXPIRATION_TIME);
    }

    // 인증번호가 유효한지 확인
    public static boolean isVerificationCodeValid(String code) {
        Long expirationTime = verificationCodes.get(code);
        if (expirationTime == null) {
            // 인증번호가 없음
            return false;
        }
        // 현재 시간과 유효시간을 비교하여 만료되었는지 확인
        return System.currentTimeMillis() < expirationTime;
    }


    @Transactional
    public MailDTO createMailAndChangePassword(String email, String id) {
        String tempPw = TempPassword.makeRandomPw(8);
        String tempEmail = TempPasswordEmail.makeRandomPw();
        storeVerificationCode(tempPw); // 인증번호와 유효시간을 저장
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

//    인증번호 발송
    @Transactional
    public MailDTO createVerificationCode(String email, String tempEmail){
        storeVerificationCode(tempEmail); // 인증번호와 유효시간을 저장
        MailDTO dto = new MailDTO();
        dto.setAddress(email);
        dto.setTitle("[둘레둘레] 가입 인증 안내 이메일 입니다.");
        dto.setMessage("안녕하세요. 둘레둘레 입니다.\r\n가입 인증 번호 : "  + tempEmail);

        return dto;
    }


    // 이메일로 인증번호 전송
    public void sendVerificationCode(MailDTO mailDTO) throws MessagingException{
        //    createNumber();
        System.out.println("가입인증 이메일 전송");
        Map<String, String> mailInfo = new HashMap<>();
        mailInfo.put("from", FROM_ADDRESS);
        mailInfo.put("to", mailDTO.getAddress());
        mailInfo.put("subject", mailDTO.getTitle());
        mailInfo.put("content", mailDTO.getMessage());
        mailInfo.put("format", "text/plain");

        // 이메일 전송
        javaMailSenderImpl.emailSending(mailInfo);
    }

}
