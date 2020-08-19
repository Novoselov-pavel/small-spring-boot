package com.npn.spring.learning.spring.smallspringboot.model.mail;

import com.npn.spring.learning.spring.smallspringboot.model.backgroundtread.BackgroundThreadPool;
import com.npn.spring.learning.spring.smallspringboot.model.dbservices.AuthorisationMailDataInterface;
import com.npn.spring.learning.spring.smallspringboot.model.dbservices.UserServiceInterface;
import com.npn.spring.learning.spring.smallspringboot.model.security.AuthorisationMailData;
import com.npn.spring.learning.spring.smallspringboot.model.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        backgroundThreadPool.addTask(()->{
           while (true){
               try {
                   Thread.sleep(SENDING_PERIOD_SEC*1000);
                   lock.lock();
                   List<AuthorisationMailData> dataList = mailDataService.getAllAuthorisationMailData();
                   List<User> nonConfirmedUserList = userService.getAllNonConfirmedUser(dataList);
                   List <Supplier<Void>> tasks = nonConfirmedUserList
                           .stream()
                           .map(SimpleAuthorisationMail::new)
                           .map(this::getSupplier).collect(Collectors.toList());

                   List<CompletableFuture<Void>> future = backgroundThreadPool.addTasks(tasks);
                   future.forEach(CompletableFuture::join);//TODO бработку исключений - удаление данных из списка
               }catch (InterruptedException e) {
                   return;
               } finally {
                   lock.unlock();
               }
           }
        });
    }

    private Supplier<Void> getSupplier(SimpleMailInterface mail) {
        return () -> {
            emailService.sendSimpleMessage(mail.getMailMessage());
            return null;
        };
    }

}
