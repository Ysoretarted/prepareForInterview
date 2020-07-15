package com.zju.ysoretarted.designPattern.singleton;

/**
 * @author zhongcz
 * @CreateTime 2020/7/14 9:53
 */
public class SingletonTest {

    public static void main(String[] args) {

        for(int i = 0; i < 10000; ++i){

            new Thread(()->{
                System.out.println(Thread.currentThread().getName() + "   "+ DCL.getInstance());
            }).start();
        }
        System.out.println(Singleton02.TEST.hashCode());
        System.out.println(Singleton02.INSTANCE.hashCode());
    }
}
