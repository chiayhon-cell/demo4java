package cn.chiayhon.jdk.concurrent.countdownlatch;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 场景：
 * 1. 要求主线程创建5个子线程，并且子线程要等待主线程创建完所有主线程后才能开始执行任务。
 * 2. 主线程需等待所有子线程，待所有子线程都完成任务后，主线程才能结束
 */
@Slf4j
public class CountDownLatchTest1 {

    public static void main(String[] args) throws InterruptedException {

        /*
            主线程处理锁存器
         */
        CountDownLatch main = new CountDownLatch(1);


        /*
            子线程处理锁存器
         */
        CountDownLatch child = new CountDownLatch(5);



        // 依次创建并启动处于等待状态的5个MyRunnable线程

        log.info("主线程开始工作......");
        int threadSize = 5;
        for (int i = 1; i <= threadSize; ++i) {
            log.info("主线程创建子线程中......");
            TimeUnit.SECONDS.sleep(1);
            new Thread(
                    new MyRunnable(main, child), "子线程" + i
            ).start();

        }
        log.info("主线程创建完毕，唤醒所有子线程......");

        main.countDown();

        log.info("唤醒完毕,主线程等待子线程完成......");

        child.await();

        log.info("Bingo!");

    }



    public static class MyRunnable implements Runnable {



        private final CountDownLatch main;

        private final CountDownLatch child;



        public MyRunnable(CountDownLatch main, CountDownLatch child) {

            this.main = main;

            this.child = child;

        }



        public void run() {

            try {
                String name = Thread.currentThread().getName();

                log.info(name + "等待主线程创建完毕，陷入休眠");

                main.await();//等待主线程执行完毕，获得开始执行信号...

                log.info(name + "开始执行");

                TimeUnit.SECONDS.sleep(1);

                synchronized (CountDownLatchTest1.class) {

                    child.countDown();

                    log.info(name + "执行完毕,通知主线程,还有" + child.getCount() + "未完成");
                }


            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();

            }

        }

    }

}
