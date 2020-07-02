package com.zju.ysoretarted.designPattern.decoratePattern.equip;

import com.zju.ysoretarted.designPattern.decoratePattern.Iequipment;

import java.util.Vector;

/**  吸血书
 *   装备是来修饰英雄的
 * @author zcz
 * @CreateTime 2020/7/2 15:01
 */
public class VampireBook implements IEquipmentDecorator {
    private Iequipment iequipment;

    public VampireBook(Iequipment iequipment){
        this.iequipment = iequipment;
    }
    @Override
    public int getAttack() {
        return 180 +iequipment.getAttack();
    }

    @Override
    public String getDescription() {
        return iequipment.getDescription() + "噬神之书 ";
    }
}
