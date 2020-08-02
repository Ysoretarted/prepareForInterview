package com.zju.ysoretarted.leetcode.offer;

import com.sun.deploy.util.StringUtils;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class Issue59_II {
    private Queue<Integer> queue;
    private Deque<Integer> window;
    public Issue59_II() {
        queue = new LinkedList<>();
    }

    public int max_value() {
        if(queue.isEmpty())
            return -1;
        return window.getFirst();
    }

    public void push_back(int value) {
        if(window.isEmpty()){
            window.addLast(value);
        }else{
            while(!window.isEmpty() && value > window.getLast())
                window.removeLast();
            window.addLast(value);
        }
        queue.add(value);
    }

    public int pop_front() {
        if(queue.isEmpty())
            return -1;
        int x = queue.remove();
        if(x == window.getFirst()){
            window.removeFirst();
        }
        return x;
    }


    public static void main(String[] args) {
        Person person = new Person("zcz",5);
       /* person.setStatus(0);
        person.setName("AAA");*/
        change(person);
        System.out.println(person.getStatus());
        System.out.println(person.getName());


    }

    static class Person{
        private int status;
        private String name;

        Person(String name, int status){
            this.name = name;
            this.status = status;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

   static void change(Person person){
        person.setName("AAA");
        person.setStatus(0);
    }
}
