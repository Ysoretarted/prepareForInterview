package com.zju.ysoretarted.api.comparatorAndCompareTo;

import lombok.Data;

/**
 * @author zcz
 * @CreateTime 2020/7/3 13:36
 */
@Data
public class Person implements Comparable<Person>{
    private String name;

    private Long id;
    public Person(String name, Long id){
        this.id = id;
        this.name = name;
    }
    @Override
    public int compareTo(Person o) {
        if(null == o){
            return 1;
        }
        if(this.name.equals(o.name)){
            return (id - o.id) > 0 ? 0 : 1;
        }
        return o.name.compareTo(this.name);
    }
}
