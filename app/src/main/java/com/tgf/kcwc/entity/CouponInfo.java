package com.tgf.kcwc.entity;

import java.io.Serializable;

/**
 * Auther: Scott
 * Date: 2017/4/6 0006
 * E-mail:hekescott@qq.com
 */

public class CouponInfo implements Serializable{

    private static final long serialVersionUID = 3L;
    /** 代金券id*/
    public String id;
    /** 标题*/
    public String title;
    /** 封面*/
    public String cover;
    /** 价格*/
    public double price;
    /**限领*/
    public int limit;
    /**面额*/
    public String denomination;
    /**店铺*/
    public String dealerName;
}
