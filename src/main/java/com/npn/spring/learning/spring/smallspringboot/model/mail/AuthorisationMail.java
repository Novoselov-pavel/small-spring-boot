package com.npn.spring.learning.spring.smallspringboot.model.mail;

import com.npn.spring.learning.spring.smallspringboot.model.security.AuthorisationMailData;
import com.npn.spring.learning.spring.smallspringboot.model.security.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class AuthorisationMail implements AuthorisationMailInterface {
    private final int DAY_OF_MAIL_EXPIRED = 15;

    private final String EMAIL_FROM;

    private final User user;

    private final AuthorisationMailData mailData;

    private final JavaMailSender emailSender;


    public AuthorisationMail(User user, String fromAddress, JavaMailSender emailSender) {
        this.user = user;
        this.mailData =getMailDataFromUser(user);
        this.EMAIL_FROM = fromAddress;
        this.emailSender = emailSender;
    }

    /**
     * Возвращает простое текстовое письмо
     *
     * @return SimpleMailMessage
     */
    @Override
    public SimpleMailMessage getMailMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getUsername());
        message.setFrom(EMAIL_FROM);
        message.setSubject("Confirm email address");
        message.setText(getSimpleMailString(mailData));
        return message;
    }

    /**
     * Возвращает данные авторизационного письма
     *
     * @return AuthorisationMailData
     */
    @Override
    public AuthorisationMailData getMailData() {
        return mailData;
    }

    /**
     * Возвращает HTML письмо
     *
     * @return MimeMessage
     */
    @Override
    public MimeMessage getMimeMessage() throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage,false,"UTF-8");
        message.setTo(user.getUsername());
        message.setFrom(EMAIL_FROM);
        message.setSubject("Confirm email address");
        message.setText(getHTMLMessageString(mailData),true);
        return mimeMessage;
    }

    private String getSimpleMailString(AuthorisationMailData data) {
        return "Thank you for registration at ....\n"+
                "Please confirm your email by following this link: \n"+
                mailData.getLink();
    }

    private String getHTMLMessageString(AuthorisationMailData data) {
        String retval = "<!DOCTYPE html><html lang=\"en\"><head>" +
                "<meta charset='UTF-8'><title>Authorisation Mail</title>"+
                "</head><body>" +
                "<h4>Thank you for registration at ....</h4>" +
                "<h5>Please confirm your email by following this <a href='"+mailData.getLink()+"'>link</a></h5>"+
                "</body></html>";
        return retval;
    }


    private AuthorisationMailData getMailDataFromUser(User user) {
        AuthorisationMailData mailData = new AuthorisationMailData();
        String userNameToken = user.getName() + RandomStringUtils.randomAlphabetic(15);
        String authorisationToken = RandomStringUtils.randomAlphabetic(45);

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.add(Calendar.DATE,DAY_OF_MAIL_EXPIRED);
        calendar.set(Calendar.MILLISECOND,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.HOUR_OF_DAY,24);

        mailData.setUserId(user.getId());
        mailData.setUserNameToken(userNameToken);
        mailData.setAuthorisationToken(authorisationToken);
        mailData.setExpiredDate(calendar.getTimeInMillis());
        return mailData;
    }
}
