package cn.chiayhon.concurrent.ThreadPool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: Gentle
 * @Date: 2019/4/10 17:50
 * @Description:
 */
@Slf4j
public class SynchronousQueue_Test1 {
    public static void main(String[] args) throws Exception {
        test();
    }

    public static void test() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                2,
                4,
                10,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(true),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        CountDownLatch latch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            int temp = i;
            threadPoolExecutor.submit(() -> {
                log.info("任务{}开始执行。。。" + temp);
                try {
                    TimeUnit.SECONDS.sleep(2);
                    latch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        try {
            latch.await();
            System.out.println("任务全部执行完成");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}