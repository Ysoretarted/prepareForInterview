package com.zju.ysoretarted.designPattern.decoratePattern.heros;

import com.zju.ysoretarted.designPattern.decoratePattern.Iequipment;

/**
 *
 *    英雄其实也可以看做是一个装备
 *
 *    一个英雄可以买多个装备， 之后要计算穿上装备后的 攻击力
 * @author zcz
 * @CreateTime 2020/7/2 15:07
 */
public class FireDance implements Iequipment {



    @Override
    public int getAttack() {
        return 1000;
    }

    @Override
    public String getDescription() {
        return "火舞装备了 ";
    }
}
