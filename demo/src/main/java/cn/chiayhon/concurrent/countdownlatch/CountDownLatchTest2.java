package cn.chiayhon.concurrent.countdownlatch;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CountDownLatchTest2 {


    public static void main(String[] args) throws InterruptedException {

        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 10, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<>(3));

        int count = 10;

        final CountDownLatch latch = new CountDownLatch(count);


        for (int i = 0; i < count; i++) {

            threadPool.execute(new MyRunnable1(latch, i));

        }

        // 等待线程池处理任务
        latch.await();

        log.info("等待线程被唤醒！");

        threadPool.shutdown();

    }



    static class MyRunnable1 implements Runnable {


        CountDownLatch latch;

        int i;


        public MyRunnable1(CountDownLatch latch, int i) {

            this.latch = latch;

            this.i = i;

        }


        @Override

        public void run() {

            log.info("线程" + i + "完成了操作...");

            try {
                Thread.sleep(4000);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();

            }

            latch.countDown();

        }


    }
}

