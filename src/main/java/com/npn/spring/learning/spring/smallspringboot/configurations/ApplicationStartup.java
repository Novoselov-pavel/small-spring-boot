package com.npn.spring.learning.spring.smallspringboot.configurations;

import com.npn.spring.learning.spring.smallspringboot.model.mail.EmailSendingTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    EmailSendingTask sendingTask;

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        sendingTask.startEmailSending();
    }
}
