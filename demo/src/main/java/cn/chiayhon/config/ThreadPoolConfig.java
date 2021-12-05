package cn.chiayhon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig {

    @Bean("excelBatchThreadPool")
    public ThreadPoolExecutor excelBatchThreadPool(){
        return  new ThreadPoolExecutor(
                2,
                20,
                300,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(true),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
