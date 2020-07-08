package com.zju.ysoretarted.designPattern.decorate.equip;

import com.zju.ysoretarted.designPattern.decorate.Iequipment;

/**   法杖
 *    装备是来修饰 英雄的
 * @author zcz
 * @CreateTime 2020/7/2 15:05
 */
public class Staves implements IEquipmentDecorator {
    private Iequipment iequipment;

    public Staves(Iequipment iequipment) {
        this.iequipment = iequipment;
    }

    @Override
    public int getAttack() {
        return 240 + iequipment.getAttack();
    }

    @Override
    public String getDescription() {
        return iequipment.getDescription() + "回响法杖";
    }
}
