package cn.chiayhon.base.concurrent.executorservice;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


@Slf4j
public class ExecutorServiceTest1 {

    @SneakyThrows
    public static void main(String[] args) {
        new ExecutorServiceTest1().test();
    }

    public void test() throws ExecutionException, InterruptedException {
        // 创建异步执行任务:
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        log.info("创建异步任务");
        Thread mainTread = Thread.currentThread();
        Future<Double> cf = executorService.submit(() -> {
            try {
                log.info("异步任务开始处理");
                Thread.sleep(2000);
                log.info("主线程此时的状态:{}", mainTread.getState());
                log.info("异步任务处理完成");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }

            return 1.2;
        });
        log.info("创建异步任务完成");
        log.info("等待异步结果:{}", cf.get());
    }
}
