package cn.chiayhon.concurrent.countdownlatch;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class CountDownLatchTest3 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        int taskCount = 5;

        List<FutureTask<String>> futureTasks = new ArrayList<>(taskCount);

        /*
            子线程处理锁存器
         */
        CountDownLatch latch = new CountDownLatch(taskCount);


        // 依次创建并启动处于等待状态的5个MyRunnable线程

        log.info("主线程开始工作......");
        for (int i = 1; i <= taskCount; ++i) {
            FutureTask<String> futureTask = new FutureTask<>(new MyRunnable(latch));
            futureTasks.add(futureTask);
        }

        log.info("主线程创建完毕，唤醒所有子线程......");
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 10, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));

        futureTasks.forEach(threadPool::submit);

        latch.await();

        List<String> results = new ArrayList<>();
        for (FutureTask<String> task : futureTasks) {
            results.add(task.get());
        }

        log.info(String.valueOf(results));
        threadPool.shutdown();
    }


    public static class MyRunnable implements Callable<String> {


        private final CountDownLatch child;


        public MyRunnable(CountDownLatch child) {

            this.child = child;

        }

        @Override
        public String call() {
            String name = "";
            try {
                name = Thread.currentThread().getName();

                log.info(name + "开始执行");

                TimeUnit.SECONDS.sleep(1);

                synchronized (CountDownLatchTest3.class) {

                    child.countDown();

                    log.info(name + "执行完毕,通知主线程:还有" + child.getCount() + "未完成");
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();

            }

            return name + "成功";
        }
    }

}
