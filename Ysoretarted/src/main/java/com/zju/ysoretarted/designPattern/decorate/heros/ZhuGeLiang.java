package com.zju.ysoretarted.designPattern.decorate.heros;

import com.zju.ysoretarted.designPattern.decorate.Iequipment;

/**
 * 英雄其实也可以看做是一个装备
 *
 *  一个英雄可以买多个装备， 之后要计算穿上装备后的 攻击力
 * @author zcz
 * @CreateTime 2020/7/2 15:09
 */
public class ZhuGeLiang implements Iequipment {

    @Override
    public int getAttack() {
        return 100;
    }

    @Override
    public String getDescription() {
        return "诸葛亮装备了 ";
    }
}
