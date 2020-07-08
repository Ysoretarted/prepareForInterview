package com.zju.ysoretarted.designPattern.callback;

/**
 * @author zcz
 * @CreateTime 2020/7/6 9:01
 */
public class CallBackTest {
    public static void main(String[] args) throws InterruptedException {
        MyServer server = new MyServer();
        MyClient client = new MyClient(server);

        client.request(client);
    }
}
