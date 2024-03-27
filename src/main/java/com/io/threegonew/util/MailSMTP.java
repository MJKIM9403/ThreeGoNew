//package com.io.threegonew.util;
//
//import com.io.threegonew.ApiKey;
//
//import javax.mail.*;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import java.util.Map;
//import java.util.Properties;
//
//public class MailSMTP {
//    private final String user = ApiKey.GMAIL;
//    private final String pwd = ApiKey.GMAIL_PWD;
//    private final Properties serverInfo;
//
//    public MailSMTP() {
//        // 구글 SMTP 서버 접속 정보
//        serverInfo = new Properties();
//        serverInfo.put("mail.smtp.host", "smtp.gmail.com");
//        serverInfo.put("mail.smtp.port", "587");
//        serverInfo.put("mail.smtp.auth", "true");
//        serverInfo.put("mail.smtp.ssl.enable", "true");
//        serverInfo.put("mail.smtp.ssl.trust", "smtp.gmail.com");
//        serverInfo.put("mail.smtp.ssl.protocols","TLSv1.2");
//    }
//
//    public void emailSending(Map<String, String> mailInfo) throws MessagingException {
//        Session session = Session.getDefaultInstance(serverInfo, new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(user, pwd);
//            }
//        });
//
//        //메세지 작성
//        MimeMessage msg = new MimeMessage(session);
//        msg.setFrom(new InternetAddress(mailInfo.get("from")));
//        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(mailInfo.get("to")));
//        msg.setSubject(mailInfo.get("subject"));
//        msg.setContent(mailInfo.get("content"), mailInfo.get("format"));
//
//        Transport.send(msg);
//    }
//}
