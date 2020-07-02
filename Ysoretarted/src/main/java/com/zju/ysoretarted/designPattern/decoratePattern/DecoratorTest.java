package com.zju.ysoretarted.designPattern.decoratePattern;

import com.zju.ysoretarted.designPattern.decoratePattern.equip.Staves;
import com.zju.ysoretarted.designPattern.decoratePattern.equip.VampireBook;
import com.zju.ysoretarted.designPattern.decoratePattern.heros.FireDance;
import com.zju.ysoretarted.designPattern.decoratePattern.heros.ZhuGeLiang;

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
