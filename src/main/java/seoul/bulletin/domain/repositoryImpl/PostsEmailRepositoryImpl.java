package seoul.bulletin.domain.repositoryImpl;

import seoul.bulletin.domain.PostsEmailRepository;
import seoul.bulletin.dto.PostOnEmailDto;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;

public class PostsEmailRepositoryImpl implements PostsEmailRepository {

    String fromEmail = "jesustark0235@naver.com";
    String fromUsername = "Bulletin Manager";
    String bodyEncoding = "UTF-8";
    String toEmail = "jesustark0235@kakao.com, jesustark0235@naver.com";

    @Override
    public boolean updateOnEmail(PostOnEmailDto post) {
        String msg2Send = getUpdateForm(post);
        sendMail("게시물이 업데이트 되었습니다",msg2Send);
        return  true;
    }

    @Override
    public boolean deleteOnEmail(Long id) {
        String msg2Send = getDeleteForm(id);
        sendMail("게시물이 삭제되었습니다.", msg2Send);
        return true;
    }

    @Override
    public Long saveOnEmail(PostOnEmailDto post) {
        String msg2Send = getSaveForm(post);
        sendMail("게시물이 등록되었습니다.", msg2Send);
        return post.getId();
    }

    private String getSaveForm(PostOnEmailDto post) {
        StringBuffer sb = new StringBuffer();
        sb.append("<h1> 게시글이 생성되었습니다</h1>\n");
        sb.append( "[" + post.getId() + "] ");
        sb.append(post.getTitle() + "\n");
        sb.append(post.getContent() + "\n");
        sb.append("by." + post.getAuthor());
        return sb.toString();
    }

    private String getUpdateForm(PostOnEmailDto post) {
        StringBuffer sb = new StringBuffer();
        sb.append("<h1> 게시글이 수정되었습니다</h1>\n");
        sb.append( "[" + post.getId() + "]");
        sb.append(post.getTitle() + "\n");
        sb.append(post.getContent() + "\n");
        sb.append("by." + post.getAuthor());
        return sb.toString();
    }

    private String getDeleteForm(Long id) {
        StringBuffer sb = new StringBuffer();
        sb.append("<h1> 게시글이 삭제되었습니다</h1>\n");
        sb.append( "[" + id + "]");
        return sb.toString();
    }

    private void sendMail(String title, String content) {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
//        prop.put("mail.smtp.host", "smtp.naver.com");
        prop.put("mail.smtp.port", "465");
//        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        prop.put("mail.smtp.debug", "true");
        prop.put("mail.transport.protocol", "smtp");
        prop.put("mail.smtp.quitwait", "false");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.socketFactory.fallback", "false");

        try {
            Authenticator auth = new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(authUserName, authPassword);
                }
            };
            Session session = Session.getDefaultInstance(prop, auth);
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromEmail, fromUsername));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            msg.setSubject(title);
            msg.setSentDate(new Date());
            Multipart mParts = new MimeMultipart();
            MimeBodyPart mTextPart = new MimeBodyPart();
            MimeBodyPart mFilePart = null;
            mTextPart.setText(content, bodyEncoding, "html");
            mParts.addBodyPart(mTextPart);
            msg.setContent(mParts);

            MailcapCommandMap MailcapCmdMap = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            MailcapCmdMap.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            MailcapCmdMap.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            MailcapCmdMap.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            MailcapCmdMap.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            MailcapCmdMap.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
            CommandMap.setDefaultCommandMap(MailcapCmdMap);

            Transport.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
