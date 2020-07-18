package com.zju.ysoretarted.util.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
//import java.util.stream.Collectors;

/**
 * @author zcz
 * @CreateTime 2020/7/4 19:00
 */
public class CollectionsTest {
    public static void main(String[] args) {
        List<Integer> array = Arrays.asList(1,2,3,4);
        int i = Collections.binarySearch(array, 4);
        System.out.println(i);

        List c = new ArrayList();
        c.add("l");
        c.add("o");
        c.add("v");
        c.add("e");
        System.out.println(c);
        Collections.shuffle(c);
        System.out.println(c);


        List m = Arrays.asList("one two".split(" "));
        System.out.println(m);
        List n = Arrays.asList("我 是 复制过来的哈".split(" "));
        System.out.println(n);
        Collections.copy(m,n);
        System.out.println(m);
    }
}
