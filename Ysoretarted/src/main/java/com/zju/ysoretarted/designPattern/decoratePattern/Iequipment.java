package com.zju.ysoretarted.designPattern.decoratePattern;

/**
 * @author zcz
 * @CreateTime 2020/7/2 14:58
 */

/**
 *  装备的接口
 */
public interface Iequipment {

    /**
     * 这件装备加多少点攻击力
     */
    int getAttack();

    /**
     * 返回这件装备的名称
     * @return
     */
    String getDescription();
}
