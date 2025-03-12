package sakhno.springframework.sfgjms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableAsync
public class TaskConfig {

    /**
     * Этот метод создает бин, который предоставляет TaskExecutor для асинхронного выполнения задач.
     * SimpleAsyncTaskExecutor — это простая реализация TaskExecutor, которая создает новый поток для
     * каждой задачи.
     * @return - бин для выполнения задач в отдельном потоке
     */
    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }
}
