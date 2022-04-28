package cn.chiayhon.base.concurrent.twotermination;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SomeService {
  private final BlockingQueue<String> queue = new ArrayBlockingQueue<>(5);

  private final Producer producer = new Producer();
  private final Consumer consumer = new Consumer();

  public static void main(String[] args) throws InterruptedException {
    SomeService ss = new SomeService();
    ss.init();
    TimeUnit.SECONDS.sleep(30);
    ss.shutdown();
  }

  // 停止生产者和消费者的执行
  public void shutdown() {
    producer.terminate(true);  // 先停止生产者，只有在生产者完全停止之后才会停止消费者
    consumer.terminate();  // 停止消费者
  }

  // 启动生产者和消费者
  public void init() {
    producer.start();
    consumer.start();
  }

  // 生产者
  private class Producer extends TerminateSupport {
    private int i = 0;

    @Override
    protected void doRun() throws InterruptedException {
      log.info("Processing product: " + i++);
      queue.put(String.valueOf(i));  // 将任务添加到任务队列中
      TimeUnit.SECONDS.sleep(new Random().nextInt(3));
      consumer.terminationToken.reservations.incrementAndGet();  // 更新需要执行的任务数量
    }

    @Override
    protected void doTerminate() {
      try {
        TimeUnit.SECONDS.sleep(new Random().nextInt(3));
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        e.printStackTrace();
      }
      log.info("正在销毁生产者");
    }
  }

  // 消费者
  private class Consumer extends TerminateSupport {
    @Override
    protected void doRun() throws InterruptedException {
      String product = queue.take();  // 获取任务
      log.info("consuming product: " + product);
      try {
        TimeUnit.SECONDS.sleep(new Random().nextInt(3));  // 模拟消费者对任务的执行
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        // ignore
      } finally {
        terminationToken.reservations.decrementAndGet();  // 更新需要执行的任务数量
      }
    }

    @Override
    protected void doTerminate() {
      try {
        TimeUnit.SECONDS.sleep(new Random().nextInt(3));
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        e.printStackTrace();
      }
      log.info("正在销毁消费者");
    }
  }
}
