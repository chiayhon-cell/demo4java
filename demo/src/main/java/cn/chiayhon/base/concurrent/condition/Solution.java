package cn.chiayhon.base.concurrent.condition;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class Solution {
    int count;
    ReentrantLock lock = new ReentrantLock();
    private static final int CAPACITY = 10;
    Condition notFull = lock.newCondition();
    Condition notEmpty = lock.newCondition();

    class Producer implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(300);
                    lock.lockInterruptibly();
                    while (count == CAPACITY)
                        notFull.await();
                    count++;
                    log.info(Thread.currentThread().getName() + "生产者生产, 当前还有" + count);
                    notEmpty.signal();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    class Consumer implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {

                try {
                    Thread.sleep(300);
                    lock.lockInterruptibly();
                    while (count == 0) {
                        notEmpty.await();
                    }
                    count--;
                    log.info(Thread.currentThread().getName() + "消费者消费, 当前还有" + count);
                    notFull.signal();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static void main(String[] args) {
        Solution test1 = new Solution();
        new Thread(test1.new Producer()).start();
        new Thread(test1.new Consumer()).start();
        new Thread(test1.new Producer()).start();
        new Thread(test1.new Consumer()).start();
        new Thread(test1.new Producer()).start();
        new Thread(test1.new Consumer()).start();
    }
}