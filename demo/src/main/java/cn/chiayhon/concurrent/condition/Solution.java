package cn.chiayhon.concurrent.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Solution {
    int count;
    ReentrantLock lock = new ReentrantLock();
    private int CAPACITY = 10;
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
                    System.out.println(Thread.currentThread().getName() + "生产者生产, 当前还有" + count);
                    notEmpty.signal();
                } catch (InterruptedException e) {
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
                    System.out.println(Thread.currentThread().getName() + "消费者消费, 当前还有" + count);
                    notFull.signal();
                } catch (InterruptedException e) {
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