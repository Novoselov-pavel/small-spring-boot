package com.npn.spring.learning.spring.smallspringboot.model.mail;

import org.springframework.mail.*;


public interface EmailService {
    /**
     * Отправляет простое письмо
     * @param message {@link SimpleMailMessage}
     * @throws MailParseException in case of failure when parsing the message
     * @throws MailAuthenticationException in case of authentication failure
     * @throws MailSendException in case of failure when sending the message
     */
    void sendSimpleMessage(SimpleMailMessage message) throws MailException;
}
