package com.zju.ysoretarted.designPattern.singleton;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @author zhongcz
 * @CreateTime 2020/7/14 9:49
 */
public class DCL {

    private static /*volatile */ DCL instance; //也可以不用volatile修饰

    private DCL() {

    }

    public static DCL getInstance() {
        if (instance == null) {
            synchronized (DCL.class) {
                //准备工作
                if (instance == null) {
                    instance = new DCL();
                }
            }
        }
        return instance;
    }

}
