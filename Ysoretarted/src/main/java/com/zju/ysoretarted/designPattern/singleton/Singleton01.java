package com.zju.ysoretarted.designPattern.singleton;

/**
 * @author zhongcz
 * @CreateTime 2020/7/14 9:56
 */

/**
 * 利用静态代码块 来实现 单例模式
 */
public class Singleton01 {

    // 这个不用volatitle修饰
    private static /*volatile*/ Singleton01 instance;

    private Singleton01(){

    }
    static{
        instance = new Singleton01();
    }

    public static Singleton01 getInstance(){
        return instance;
    }
}
