package cn.chiayhon.concurrent.completefuture;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * completeFuture
 */
@Slf4j
public class CompleteFutureTest1 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CompletableFuture<String> completableFuture1 = new CompletableFuture<>();

        ExecutorService cachePool = Executors.newFixedThreadPool(16);
        log.info("开始开启异步任务1");
        FutureTask<String> task = new FutureTask<>(() -> {
            log.info("异步任务1开始处理");
            Thread.sleep(3000);
            log.info("异步任务1启动回调");
            completableFuture1.complete("回调1！！！");
            log.info("异步任务1处理完毕");
            return "我是最终结果!!";
        });
        cachePool.submit(task);
        log.info("开启异步任务1完成，等待异步任务1执行结果");
        CompletableFuture<String> completableFuture2 = completableFuture1.whenComplete((s, throwable) -> log.info("异步任务1回调结果:{}", s));
        log.info("异步任务1执行结果:{}", task.get());
        log.info("根据上一次的异步任务结果, 继续开始一个新的异步任务!");
        CompletableFuture<String> completableFuture3 = new CompletableFuture<>();
        CompletableFuture<Integer> completableFuture4 = completableFuture2.thenApply(s -> {
            try {
                log.info("异步任务2开始处理");
                Thread.sleep(1000);
                log.info("异步任务2启动回调");
                completableFuture3.complete("回调2！！！");
                log.info("异步任务2处理完毕");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
            return 233333;
        });
        completableFuture3.whenComplete((i, throwable) -> log.info("异步任务2回调结果:{}", i));

        log.info("异步任务2执行结果:{}", completableFuture4.get());

        cachePool.shutdown();
    }
}
