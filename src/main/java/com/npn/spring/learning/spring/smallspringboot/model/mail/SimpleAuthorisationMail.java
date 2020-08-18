package com.npn.spring.learning.spring.smallspringboot.model.mail;

import com.npn.spring.learning.spring.smallspringboot.model.dbservices.AuthorisationMailDataInterface;
import com.npn.spring.learning.spring.smallspringboot.model.security.AuthorisationMailData;
import com.npn.spring.learning.spring.smallspringboot.model.security.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class SimpleAuthorisationMail implements SimpleMailInterface {
    private final int DAY_OF_MAIL_EXPIRED = 15;
    private final String EMAIL_FROM = "noreply@ya.com";


    @Autowired
    AuthorisationMailDataInterface service;


    private final User user;

    private final AuthorisationMailData mailData = new AuthorisationMailData();


    public SimpleAuthorisationMail(User user) {
        this.user = user;

        String userNameToken = user.getName() + RandomStringUtils.randomAlphabetic(15);
        String authorisationToken = RandomStringUtils.randomAscii(65);

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
    }

    /**
     * Возвращает простое текстовое письмо
     *
     * @return SimpleMailMessage
     */
    @Override
    public SimpleMailMessage getMailMessage() {
        service.save(mailData);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getUsername());
        message.setFrom(EMAIL_FROM);
        message.setSubject("Confirm email address");
        message.setText(getMessageString(mailData));
        return message;
    }

    private String getMessageString(AuthorisationMailData data) {
        String retval = "<!DOCTYPE html><html lang=\"en\"><head>" +
                "<meta charset='UTF-8'><title>Authorisation Mail</title>"+
                "</head><body>" +
                "<h4>Thank you for registration at ....</h4>" +
                "<h5>Please confirm your email by following this <a href='"+mailData.getAuthorisationToken()+"'>link</a></h5>"+
                "</body></html>";
        return retval;
    }
}
