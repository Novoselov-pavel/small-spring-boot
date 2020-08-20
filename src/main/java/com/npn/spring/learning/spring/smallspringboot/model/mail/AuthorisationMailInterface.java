package com.npn.spring.learning.spring.smallspringboot.model.mail;

import com.npn.spring.learning.spring.smallspringboot.model.security.AuthorisationMailData;
import org.springframework.mail.SimpleMailMessage;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public interface AuthorisationMailInterface {

    /**
     * Возвращает простое текстовое письмо
     * @return SimpleMailMessage
     */
    SimpleMailMessage getMailMessage();

    /**
     * Возвращает данные авторизационного письма
     *
     * @return AuthorisationMailData
     */
    AuthorisationMailData getMailData();

    /**
     * Возвращает HTML письмо
     * @return MimeMessage
     */
    MimeMessage getMimeMessage() throws MessagingException;
}
