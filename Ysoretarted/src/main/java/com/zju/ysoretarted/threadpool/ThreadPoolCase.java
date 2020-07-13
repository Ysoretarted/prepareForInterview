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

        for(int i = 0; i < 20; i++){
            final int tmp = i;
            Runnable runnable = ()->{
                System.out.println("第 " + tmp + "个任务");
            };
            executor.execute(runnable);
        }
    }
}
