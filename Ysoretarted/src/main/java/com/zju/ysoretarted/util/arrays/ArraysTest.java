package com.zju.ysoretarted.util.arrays;

import com.zju.ysoretarted.api.comparatorAndCompareTo.Person;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.IntUnaryOperator;

/**
 * @author zcz
 * @CreateTime 2020/7/3 9:07
 */
public class ArraysTest {
    public static void main(String[] args) {
        List<Integer> integers = Arrays.asList(1, 2, 5, 6);
        integers.add(5);
        Integer[] a = new Integer[]{1,3,100,5};
        Arrays.setAll(a, x-> a[x] * 2);
        for(int x : a){
            System.out.println(x);
        }
        System.out.println("=============");
        Person[] peopleArray = new Person[5];
        Person person1 = new Person("111",1L);
        Person person3 = new Person("333",3L);
        Person person2 = new Person("222",2L);
        Person person5 = new Person("555",5L);
        Person person4 = new Person("444",4L);
        peopleArray[0] = person1;
        peopleArray[2] = person2;
        peopleArray[1] = person3;
        peopleArray[4] = person4;
        peopleArray[3] = person5;
//        Arrays.sort(peopleArray);
     Arrays.sort(peopleArray,Person::compareTo);
      /*Arrays.sort(peopleArray, new Comparator<Person>() {
          @Override
          public int compare(Person o1, Person o2) {
              return 0;
          }
      });*/
        /*Arrays.sort(peopleArray,(o1,o2)->{
            return o1.getId() - o2.getId() > 0 ? -1 : 1;
        });*/
        for(Person x : peopleArray){
            System.out.println(x.getName());
        }
        int[] binaryArray = { 1,3,3,4,5,6,7,8,9,10,11};
        int i = Arrays.binarySearch(binaryArray, 3);
        System.out.println(i);
    }




}
