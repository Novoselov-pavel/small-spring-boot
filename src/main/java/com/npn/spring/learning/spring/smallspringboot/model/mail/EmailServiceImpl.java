package com.npn.spring.learning.spring.smallspringboot.model.mail;

import com.npn.spring.learning.spring.smallspringboot.model.dbservices.AuthorisationMailDataInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private AuthorisationMailDataInterface dataService;

    /**
     * Отправляет авторизационное письмо в HTML формате письмо
     *
     * @param message {@link AuthorisationMailInterface}
     * @throws MailParseException          in case of failure when parsing the message
     * @throws MailAuthenticationException in case of authentication failure
     * @throws MailSendException           in case of failure when sending the message
     */
    @Override
    public void sendAuthorisationMail(AuthorisationMailInterface message) throws MailException, MessagingException {
        emailSender.send(message.getMimeMessage());
        dataService.save(message.getMailData());
    }
}
