package com.zju.ysoretarted.juc.threadLocal;

public class ThreadLocalTest {
    public static ThreadLocal<Integer> threadLocal = new MyThreadLocal();
    public static void main(String[] args) throws InterruptedException {
        testOne();

    }


    /**
     * 这里看到 主线程改了值，  但 A 线程 还是没有反应
     * @throws InterruptedException
     */
    public static void testOne() throws InterruptedException {
        System.out.println(threadLocal.get());
        threadLocal.set(5);
        System.out.println(threadLocal.get());

        Thread thread = new Thread(()->{
            System.out.println(Thread.currentThread().getId());
            System.out.println(threadLocal.get());
            threadLocal.set(100);
            System.out.println(threadLocal.get());
        },"A");
        thread.start();
        Thread.sleep(5000);
        System.out.println("=============");
        System.out.println(threadLocal.get());
        threadLocal.remove();
        System.out.println(threadLocal.get());
    }


    public static class MyThreadLocal extends  ThreadLocal<Integer>{
        /*@Override
        protected  Object initialValue() {
            return Integer.valueOf(10086);
        }*/

        @Override
        protected Integer initialValue() {
            return Integer.valueOf(10086);
        }

    }
}
