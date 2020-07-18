package com.zju.ysoretarted.util.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HashMapTest {
    /**
     * jdk 1.7   抛出ConcurrentModificationException
     * 一遍修改，一遍遍历
     */
   /* private static HashMap<String, String> mp = new HashMap<>();
    public static void main(String[] args) {

        mp.put("AAA","VBBB");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 10000; ++i){
                    mp.put(String.valueOf(i), String.valueOf(i));
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(Map.Entry<String, String> node : mp.entrySet()){
                    System.out.println(node.getKey() + "   " + node.getValue());
                }
            }
        });

        thread.start();
        thread2.start();
    }*/
    public static void main(String[] args) {
        /*Map<String, String> mp = new ConcurrentHashMap<>();
        mp.put("aaa000", null);
        System.out.println(mp.get("aaa000"));*/

        HashMap<List<String>, Object> changeMap = new HashMap<>();
        List<String> list = new ArrayList<>();
        System.out.println(list.hashCode());
        list.add("hello");
        Object objectValue = new Object();
        changeMap.put(list, objectValue);
        System.out.println(changeMap.get(list));
        list.add("hello world");   // hashcode发生了改变
        System.out.println(list.hashCode());
        System.out.println(changeMap.get(list));
    }
}
