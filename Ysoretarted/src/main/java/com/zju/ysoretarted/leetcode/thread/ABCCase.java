package com.zju.ysoretarted.leetcoe.thread;


import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 三个线程循环打印abc
 */
public class ABCCase {
    private static int state = 0;
    private static AtomicInteger ZZZ = new AtomicInteger(0);

    private static String lock = "lock";

    public static void main(String[] args) throws InterruptedException {
       // caseOne();
        caseTwo();
    }

    public static void caseOne(){
        Thread threadA = new Thread(()->{
            synchronized (lock){
                while(true){

                    while(state != 0){
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    state = 1;
                    System.out.println("AAAA");
                    lock.notifyAll();
                }
            }
        });


        Thread threadB = new Thread(()->{
            synchronized (lock){
                while(true){

                    while(state != 1){
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    state = 2;
                    System.out.println("BBB");
                    lock.notifyAll();
                }
            }
        });

        Thread threadC = new Thread(()->{
            synchronized (lock){
                while(true){
                    while(state != 2){
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    state = 0;
                    System.out.println("CCC");
                    lock.notifyAll();
                }

            }
        });

        threadA.start();
        threadB.start();
        threadC.start();
    }

    public static void caseTwo() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();
        Condition condition3 = lock.newCondition();
        Thread threadA = new Thread(()->{
            try{
                lock.lock();
                while(true){
                    if(state != 0)
                        condition1.await();
                    System.out.println("AAA");  //要先唤醒 其他的人  再阻塞自己
                    state = 1;
                    condition2.signal();
                    condition1.await();

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        });

        Thread threadB = new Thread(()->{
            try{
                lock.lock();
                while(true){
                    if(state != 1)
                        condition2.await();
                    state = 2;
                    System.out.println("BBB");
                    condition3.signal();
                    condition2.await();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        });

        Thread threadC = new Thread(()->{
            try{
                lock.lock();
                while(true){
                    if(state != 2)
                        condition3.await();
                    state = 0;
                    System.out.println("CCC");
                    condition1.signal();
                    condition3.await();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        });
        threadC.start();
        threadB.start();
        threadA.start();
        //Thread.sleep(500);

        //Thread.sleep(500);

}
}
