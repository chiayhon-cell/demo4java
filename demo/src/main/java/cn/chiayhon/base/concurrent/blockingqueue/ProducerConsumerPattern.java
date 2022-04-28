package cn.chiayhon.base.concurrent.blockingqueue;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
public class ProducerConsumerPattern {

    public static void main(String[] args) {
        //Creating shared object
        BlockingQueue<Integer> sharedQueue = new LinkedBlockingQueue<>();
        //Creating Producer and Consumer Thread
        Thread prodThread = new Thread(new Producer(sharedQueue));
        Thread consThread = new Thread(new Consumer(sharedQueue));
        //Starting producer and Consumer thread
        prodThread.start();
        consThread.start();
    }

    static class Producer implements Runnable {
        private final BlockingQueue<Integer> sharedQueue;

        public Producer(BlockingQueue<Integer> sharedQueue) {
            this.sharedQueue = sharedQueue;
        }


        @Override
        public void run() {
            for (int i = 1; i <= 10; i++) {
                try {
                    TimeUnit.SECONDS.sleep(new Random().nextInt(2));
                    log.info("\r" + "生产: " + i);
                    sharedQueue.put(i);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    static class Consumer implements Runnable {
        private final BlockingQueue<Integer> sharedQueue;

        public Consumer(BlockingQueue<Integer> sharedQueue) {
            this.sharedQueue = sharedQueue;
        }

        @Override
        public void run() {
            while (Thread.currentThread().isInterrupted()) {
                try {
                    TimeUnit.SECONDS.sleep(new Random().nextInt(2));
                    log.info("\t\t" + "消费: " + sharedQueue.take());
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}