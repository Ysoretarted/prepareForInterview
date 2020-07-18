package com.zju.ysoretarted.juc.lock;

/**
 * @author zhongcz
 * @CreateTime 2020/7/16 8:54
 */

import org.omg.PortableServer.THREAD_POLICY_ID;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程之间的通信
 */
public class Communication {


    public static void testSleep() throws InterruptedException {
//        synchronized (Communication.class) {
//            System.out.println("sleep前");
//            System.out.println(Thread.holdsLock(Communication.class));
//            Thread.sleep(1000);
//            System.out.println("sleep后");
//        }
//        System.out.println(Thread.holdsLock(Communication.class));
//    }
//
//    public static void testJoin() {
//        Thread thread1 = new Thread(() -> {
//            System.out.println("我开始了");
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("我结束了");
//        });
//
//        Thread thread2 = new Thread(() -> {
//            System.out.println("线程二开始了");
//        });
//
//        thread1.start();
//        try {
//            thread1.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        thread2.start();
//    }
//
//    public static void testYield() {
//        Thread thread1 = new Thread(() -> {
//            synchronized (Communication.class) {
//                System.out.println("我开始了");
//                Thread.yield();
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println("我结束了");
//            }
//        });
//
//        Thread thread2 = new Thread(() -> {
//            synchronized (Communication.class) {
//                System.out.println("线程二开始了");
//            }
//        });
//
//        thread1.start();
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        thread2.start();
//    }
//
//
//    public static void testWaitAndNotify() {
//        String str = "zhongchangze";
//        Thread[] threads = new Thread[10];
//        for (int i = 0; i < 10; ++i) {
//            threads[i] = new Thread(() -> {
//                synchronized (str) {
//                    System.out.println(Thread.currentThread().getName() + "我开始了");
//                    try {
//                        str.wait();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println(Thread.currentThread().getName() + "我被唤醒了 我结束了");
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, "wait 线程" + i);
//            threads[i].start();
//        }
//
//        Thread thread2 = new Thread(() -> {
//            synchronized (str) {
//                System.out.println("我成功抢到锁了");
//                str.notify();
//                System.out.println("我 唤醒其他 线程");
//            }
//        }, "唤醒线程");
//        thread2.start();
//    }
//
//    public static void testLockAndUnlock() {
//        ReentrantLock lock = new ReentrantLock();
//        Condition condition = lock.newCondition();
//        //Condition condition1 = lock.newCondition();
//
//        Thread thread2 = new Thread(() -> {
//            lock.lock();
//            try {
//                System.out.println(Thread.currentThread().getName() + "线程2 抢到锁啦");
//            } finally {
//                condition.signalAll();
//                System.out.println(Thread.currentThread().getName() + "我要释放锁了");
//                lock.unlock();
//            }
//
//        });
//        for (int i = 0; i < 10; ++i) {
//            Thread thread1 = new Thread(() -> {
//                lock.lock();
//                try {
//                    System.out.println(Thread.currentThread().getName() + "我上锁了AA" + "然后执行await");
//                    condition.await();
//                    System.out.println(Thread.currentThread().getName() + "我重新获得锁了");
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } finally {
//                    System.out.println(Thread.currentThread().getName() + "我要释放锁了");
//                    lock.unlock();
//                }
//            }, "await 线程" + i);
//            thread1.start();
//
//            if (i == 5)
//                thread2.start();
//        }
//
//        //thread1.start();
//        //thread2.start();
//    }
//
//    public static void testParkAndUnpark() throws InterruptedException {
//        Thread thread = new Thread(() -> {
//            System.out.println("随便做点什么");
//            LockSupport.park();
//            System.out.println("我看看自己有没有被park");
//        }, "被park线程");
//
//        thread.start();
//        Thread.sleep(5000);
//        System.out.println(Thread.currentThread().getName());
//        LockSupport.unpark(thread);
//    }
//
//    public static void testInterrupt() {
//        Thread thread = new Thread(() -> {
//            for (int i = 0; i < 10000; ++i)
//                System.out.println("AAA");
//        }, "interrupt线程");
//        thread.start();
//        thread.interrupt();
//        System.out.println(thread.isInterrupted());
//        System.out.println(thread.isInterrupted());
//        System.out.println(thread.interrupted());
//        System.out.println(thread.interrupted());
//        System.out.println(thread.isAlive());
//    }
//
//
//    public static void main(String[] args) throws InterruptedException {
//        // testSleep();
//        //testJoin();
//        //testYield();
//        //testWaitAndNotify();
//        //testLockAndUnlock();
//        // testParkAndUnpark();
//        /*testInterrupt();
//        Thread.sleep(1000);
//        System.out.println("调试用的");*/
//
//
//        Thread.currentThread().interrupt();
//        System.out.println("第一次调用Thread.currentThread().interrupt()："
//                + Thread.currentThread().isInterrupted());
//        System.out.println("第一次调用thread.interrupted()："
//                + Thread.currentThread().interrupted());
//        System.out.println("第二次调用thread.interrupted()："
//                + Thread.currentThread().interrupted());
    }

}

