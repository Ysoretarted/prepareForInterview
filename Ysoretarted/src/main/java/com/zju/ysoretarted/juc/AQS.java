package com.zju.ysoretarted.juc;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhongcz
 * @CreateTime 2020/7/15 21:50
 */
public class AQS {

    public static void main(String[] args) throws InterruptedException {
  /*      ReentrantLock lock = new ReentrantLock(true);
        Thread thread1 = new Thread(()->{
            lock.lock();
            System.out.println("AAA");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.unlock();
        },"钟昌泽的第一个线程");
        thread1.start();

        Thread thread2 = new Thread(()->{
            lock.lock();
            System.out.println("BBB");
            System.out.println("CCC");
            lock.unlock();
        },"第二个线程");
        thread2.start();*/


    }
}
