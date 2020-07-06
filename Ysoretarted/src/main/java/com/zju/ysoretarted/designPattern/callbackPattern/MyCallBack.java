package com.zju.ysoretarted.designPattern.callbackPattern;

/**
 * @author zcz
 * @CreateTime 2020/7/6 8:54
 */
@FunctionalInterface
public interface MyCallBack {

    void process();

    boolean equals(Object obj);
}
