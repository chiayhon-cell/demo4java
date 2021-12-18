package cn.chiayhon.concurrent.ThreadPool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Slf4j
abstract class Class_1 implements Runnable {

    @Override
    public void run() {
        try {
            log.info("开始执行");
            doRun();
        } catch (Exception e) {
            log.info("发生异常：" + e);
        } finally {
            log.info("class1执行结束");
        }
    }

    protected abstract void doRun();
}

@Slf4j
class Class_2 extends Class_1 {

    @Override
    protected void doRun() {
        try {
            System.out.println("class2开始执行");
            for (int i = 0; i < 4; i++) {
                if (i == 1) {
                    System.out.println("i=1，执行结束");
                    return;
                }
                System.out.println(i);
            }
        } finally {
            log.info("class2执行结束");
        }
    }
}


@Slf4j
public class ThreadPool_Test1 {

    public static void main(String[] args) throws InterruptedException {
        log.info("子线程开启");
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.submit(new Class_2());
    }
}


