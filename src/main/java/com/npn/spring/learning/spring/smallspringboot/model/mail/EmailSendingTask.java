package com.npn.spring.learning.spring.smallspringboot.model.mail;

import com.npn.spring.learning.spring.smallspringboot.model.backgroundtread.BackgroundThreadPool;
import com.npn.spring.learning.spring.smallspringboot.model.dbservices.AuthorisationMailDataInterface;
import com.npn.spring.learning.spring.smallspringboot.model.dbservices.UserServiceInterface;
import com.npn.spring.learning.spring.smallspringboot.model.mail.exceptions.MessageException;
import com.npn.spring.learning.spring.smallspringboot.model.security.AuthorisationMailData;
import com.npn.spring.learning.spring.smallspringboot.model.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Отправляет письма авторизационные письма пользователям. Работает переодически.
 */
@Service
public class EmailSendingTask {
    private final int SENDING_PERIOD_SEC = 20;

    private static final ReentrantLock lock = new ReentrantLock();

    @Value("${mail.from.address}")
    private String EMAIL_FROM;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthorisationMailDataInterface mailDataService;

    @Autowired
    private UserServiceInterface userService;

    @Autowired
    BackgroundThreadPool backgroundThreadPool;

    private List<User> userList = new ArrayList<>();

    public EmailSendingTask() {
    }

    /**
     * Запускает фоновый процесс отправки авторизационных писем
     */
    public void startEmailSending(){
        backgroundThreadPool.addTask(()->{
            while (true){
                try {
                    Thread.sleep(SENDING_PERIOD_SEC*1000);
                    lock.lock();
                    List<AuthorisationMailData> dataList = mailDataService.getAllAuthorisationMailData();
                    List<AuthorisationMailInterface> mails = userService
                            .getAllNonConfirmedUser(dataList)
                            .stream()
                            .map(x->new AuthorisationMail(x,EMAIL_FROM, emailSender))
                            .collect(Collectors.toList());
                    List<CompletableFuture<AuthorisationMailData>> future = backgroundThreadPool.addTasks(mails
                            .stream()
                            .map(this::getSupplier)
                            .collect(Collectors.toList()));

                    future.forEach(x->{
                        try {
                            x.get();
                            int index = future.indexOf(x);
                            if (index > -1 && index < mails.size()) {
                                AuthorisationMailInterface mail = mails.get(index);
                                AuthorisationMailData mailData = mail.getMailData();
                                mailData.setSended(true);
                                mailDataService.save(mailData);
                            }
                        } catch (Exception | MessageException e) {
                            // TODO когда будут идеи по логгированию
                        }
                    });
                }catch (InterruptedException e) {
                    return;
                } finally {
                    lock.unlock();
                }
            }
        });
    }

    private Supplier<AuthorisationMailData> getSupplier(AuthorisationMailInterface mail) {
        return () -> {
            try {
                emailService.sendAuthorisationMail(mail);
            } catch (MessagingException e) {
                throw new MessageException(e);
            }
            return mail.getMailData();
        };
    }

}
