package com.zju.ysoretarted.threadpool;

import java.util.Queue;
import java.util.concurrent.*;

/**
 * @author zhongcz
 * @CreateTime 2020/7/13 8:52
 */
public class ThreadPoolCase {

    public static void main(String[] args) {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(5,10,5000, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(10), Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());

        /*Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("這是 調試 runnable");
            }
        };*/
        for(int i = 0; i < 20; i++){
            final int tmp = i;
            Runnable runnable = ()->{
                System.out.println("這是 第 " + tmp+ "任務");
            };
            executor.execute(runnable);
        }
       /*Thread thread = new Thread(()->{
           Thread t = Thread.currentThread();
           System.out.println(t.getName());
       });

       thread.start();*/
    }
}
