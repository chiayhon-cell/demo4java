package cn.chiayhon.concurrent.blockingqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ProducerConsumerPattern {

    public static void main(String args[]) {
        //Creating shared object
        BlockingQueue sharedQueue = new LinkedBlockingQueue();
        //Creating Producer and Consumer Thread
        Thread prodThread = new Thread(new Producer(sharedQueue));
        Thread consThread = new Thread(new Consumer(sharedQueue));
        //Starting producer and Consumer thread
        prodThread.start();
        consThread.start();
    }

    static class Producer implements Runnable {
        private final BlockingQueue sharedQueue;

        public Producer(BlockingQueue sharedQueue) {
            this.sharedQueue = sharedQueue;
        }


        @Override
        public void run() {
            for (int i = 1; i <= 10; i++) {
                try {
                    TimeUnit.SECONDS.sleep(Math.round(1));
                    System.out.print("\r" + "生产: " + i);
                    sharedQueue.put(i);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    static class Consumer implements Runnable {
        private final BlockingQueue sharedQueue;

        public Consumer(BlockingQueue sharedQueue) {
            this.sharedQueue = sharedQueue;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(Math.round(2));
                    System.out.println("\t\t" + "消费: " + sharedQueue.take());
                } catch (InterruptedException ex) {
                    Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}