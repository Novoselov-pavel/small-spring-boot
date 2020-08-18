package com.npn.spring.learning.spring.smallspringboot.model.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    /**
     * Отправляет простое письмо
     *
     * @param message {@link SimpleMailMessage}
     * @throws MailParseException          in case of failure when parsing the message
     * @throws MailAuthenticationException in case of authentication failure
     * @throws MailSendException           in case of failure when sending the message
     */
    @Override
    public void sendSimpleMessage(SimpleMailMessage message) throws MailException {
        emailSender.send(message);
    }
}
