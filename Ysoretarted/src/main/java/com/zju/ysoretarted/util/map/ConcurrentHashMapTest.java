package com.zju.ysoretarted.util.map;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapTest {
    public static void main(String[] args) {
        ConcurrentHashMap<String, String> mp = new ConcurrentHashMap<>();
        mp.put(null, "AAA");
        System.out.println(mp.get(null));
        int a = 1;
        System.out.println(a++ == 2);
    }
}
