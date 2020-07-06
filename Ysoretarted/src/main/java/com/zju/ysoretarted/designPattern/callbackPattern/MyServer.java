package com.zju.ysoretarted.designPattern.callbackPattern;

/**
 * @author zcz
 * @CreateTime 2020/7/6 8:54
 */
public class MyServer {

    public void response(MyCallBack callBack) throws InterruptedException {
        Thread.sleep(5000);
        System.out.println("服务端对客户端的请求进行相应");
        callBack.process();
    }
}
