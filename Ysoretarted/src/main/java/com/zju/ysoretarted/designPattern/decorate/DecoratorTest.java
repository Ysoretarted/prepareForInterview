package com.zju.ysoretarted.designPattern.decorate;

import com.zju.ysoretarted.designPattern.decorate.equip.Staves;
import com.zju.ysoretarted.designPattern.decorate.equip.VampireBook;
import com.zju.ysoretarted.designPattern.decorate.heros.FireDance;
import com.zju.ysoretarted.designPattern.decorate.heros.ZhuGeLiang;

/**
 * @author zcz
 * @CreateTime 2020/7/2 15:11
 */
public class DecoratorTest {
    public static void main(String[] args) {
        Iequipment iequipment = new Staves(new VampireBook(new ZhuGeLiang()));
        System.out.println(iequipment.getAttack());
        System.out.println(iequipment.getDescription());

        Iequipment fireDance = new Staves(new VampireBook(new FireDance()));
        System.out.println(fireDance.getAttack());
        System.out.println(fireDance.getDescription());
    }
}
