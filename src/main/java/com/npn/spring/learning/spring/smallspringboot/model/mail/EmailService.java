package com.npn.spring.learning.spring.smallspringboot.model.mail;

import org.springframework.mail.*;

import javax.mail.MessagingException;


public interface EmailService {
    /**
     * Отправляет авторизационное письмо и записывает в базу данных
     * @param message {@link AuthorisationMailInterface}
     * @throws MailParseException in case of failure when parsing the message
     * @throws MailAuthenticationException in case of authentication failure
     * @throws MailSendException in case of failure when sending the message
     */
    void sendAuthorisationMail(AuthorisationMailInterface message) throws MailException, MessagingException;

}
