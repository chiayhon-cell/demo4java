package cn.chiayhon.concurrent.completefuture;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * completeFuture
 */
@Slf4j
public class CompleteFutureTest2 {

    @SneakyThrows
    public static void main(String[] args) {

        final FutureTask<Boolean> futureTask = new FutureTask<>(() -> {
            try {
                test();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, true);

        Executors.newFixedThreadPool(3).submit(futureTask);
        TimeUnit.SECONDS.sleep(2);
        futureTask.get();
    }

    private static void test() {
        final CompletableFuture<Object> future = CompletableFuture.runAsync(
                () -> {
                    log.info("异步任务开始执行");
                    int count = 1000000;
                    for (int i = 0; i <= count; i++) {
                        if (i == count) {
                            i = 0;
                            count--;
                        }
                    }
                    log.info("异步任务结束");
                }
        ).handle((r, e) -> {
                    if (e != null) {
                        log.error("异常", e);
                    }
                    return null;
                }
        );

        future.join();
    }
}
