package seoul.bulletin.domain;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

public class EmailSend {
    public static void main(String[] args) {

        final String username = "jesustark0235@gmail.com";
        final String password = "***";

        String fromEmail = "jesustark0235@naver.com";
        String fromUsername = "mtak";

        // 메일에 출력할 텍스트
        StringBuffer sb = new StringBuffer();
        sb.append("<h3>안녕하세요</h3>\n");
        sb.append("<h4>개발하는 도치입니다.</h4>\n");
        String html = sb.toString();


        final String bodyEncoding = "UTF-8"; //콘텐츠 인코딩


        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
//        prop.put("mail.smtp.host", "smtp.naver.com");
        prop.put("mail.smtp.port", "465");
//        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        prop.put("mail.smtp.socketFactory.fallback", "false");
        prop.put("mail.smtp.debug", "true");

        prop.put("mail.transport.protocol", "smtp");

        prop.put("mail.smtp.quitwait", "false");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        try {
        Authenticator auth = new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };
                Session session = Session.getDefaultInstance(prop, auth);
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(fromEmail, fromUsername));
                message.setRecipients(
                        Message.RecipientType.TO,
                        InternetAddress.parse("jesustark0235@gmail.com, jesustark0235@naver.com")
    //                    new InternetAddress("to_send@gmial.com")
                );
                message.setSubject("tiiiiiiiitle");
                message.setSentDate(new Date());
                message.setText("this is content"
                        + "\n\n Maybe body");
            // 메일 콘텐츠 설정
            Multipart mParts = new MimeMultipart();
            MimeBodyPart mTextPart = new MimeBodyPart();
            MimeBodyPart mFilePart = null;

            // 메일 콘텐츠 - 내용
            mTextPart.setText(html, bodyEncoding, "html");
            mParts.addBodyPart(mTextPart);

            // 메일 콘텐츠 설정
            message.setContent(mParts);

            // MIME 타입 설정
            MailcapCommandMap MailcapCmdMap = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            MailcapCmdMap.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            MailcapCmdMap.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            MailcapCmdMap.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            MailcapCmdMap.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            MailcapCmdMap.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
            CommandMap.setDefaultCommandMap(MailcapCmdMap);
                Transport.send(message);
                System.out.println("Done");
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
