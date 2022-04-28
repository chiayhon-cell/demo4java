package cn.chiayhon.base.concurrent.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class ThreadExceptionCaughtDemo {

    public static void main(String[] args) {
        Thread.UncaughtExceptionHandler handler = (thread, e) -> log.info("未处理的异常: " + e);

        final Thread thread = new Thread(() -> {
            log.info("开始睡眠..");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                log.info("中断");
            }
            log.info("抛出异常");
            throw new RuntimeException("运行时异常");
        });

        thread.setUncaughtExceptionHandler(handler);

        thread.start();
        log.info("主线程结束");

    }
}
