package com.npn.spring.learning.spring.smallspringboot.model.backgroundtread;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Служба работы с фоновыми процессами сервера
 */
@Service
public class BackgroundThreadPool {
    private static final int MAX_THREAD_NUMBER = 5;
    private BackgroundThreadFactory factory = new BackgroundThreadFactory();
    private ExecutorService executorService = Executors.newFixedThreadPool(5, factory);

    public void addTask(Runnable task){
        executorService.submit(task);
    }

    public <V> Future<V> addTask(Callable<V> task) {
        return executorService.submit(task);
    }

    public <V> CompletableFuture<V> addTask(Supplier<V> task) {
        return CompletableFuture.supplyAsync(task,executorService);
    }

    public <V> List<CompletableFuture<V>> addTasks (List<Supplier<V>> task) {
        return task
                .stream()
                .map(this::addTask)
                .collect(Collectors.toList());
    }


    private class BackgroundThreadFactory implements ThreadFactory{
        private final String THREAD_PREFIX_NAME = "ServerBackgroundProcess";
        private final String THREAD_GROUP_NAME = "ServerBackgroundThreadGroup";

        private long count = 0;
        private ThreadGroup backgroundGroup = new ThreadGroup(THREAD_GROUP_NAME);

        public BackgroundThreadFactory() {
            backgroundGroup.setDaemon(true);
        }

        /**
         * Constructs a new {@code Thread}.  Implementations may also initialize
         * priority, name, daemon status, {@code ThreadGroup}, etc.
         *
         * @param r a runnable to be executed by new thread instance
         * @return constructed thread, or {@code null} if the request to
         * create a thread is rejected
         */
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(backgroundGroup,r,THREAD_PREFIX_NAME+"-"+ count++);
        }

        public ThreadGroup getBackgroundGroup() {
            return backgroundGroup;
        }
    }

}
