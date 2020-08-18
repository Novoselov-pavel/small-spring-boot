package com.npn.spring.learning.spring.smallspringboot.model.mail;

import org.springframework.mail.SimpleMailMessage;

public interface SimpleMailInterface {

    /**
     * Возвращает простое текстовое письмо
     * @return SimpleMailMessage
     */
    SimpleMailMessage getMailMessage();
}
