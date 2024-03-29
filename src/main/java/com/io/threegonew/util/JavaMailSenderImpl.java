package com.io.threegonew.util;

import com.io.threegonew.Key;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Properties;

public class JavaMailSenderImpl {
    private final String user = Key.GMAIL;
    private final String pwd = Key.GMAIL_PWD;
    private final Properties serverInfo;

    public JavaMailSenderImpl() {
        // 구글 SMTP 서버 접속 정보
        serverInfo = new Properties();
        serverInfo.put("mail.smtp.host", "smtp.gmail.com");
        serverInfo.put("mail.smtp.port", "587");
        serverInfo.put("mail.smtp.auth", "true");
        serverInfo.put("mail.smtp.starttls.enable", "true");
        serverInfo.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        serverInfo.put("mail.smtp.ssl.protocols","TLSv1.2");

    }

    public void emailSending(Map<String, String> mailInfo) throws MessagingException {
        Session session = Session.getInstance(serverInfo, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pwd);
            }
        });

        //메세지 작성
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(mailInfo.get("from")));
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(mailInfo.get("to")));
        msg.setSubject(mailInfo.get("subject"));

        // 메일 콘텐츠의 인코딩 설정
        String content = mailInfo.get("content");
        String format = mailInfo.get("format");
        if (format != null && format.equals("text/html")) {
            msg.setContent(content, "text/html; charset=UTF-8");
        } else {
            msg.setText(content, "UTF-8");
        }

        Transport.send(msg);
    }
}
