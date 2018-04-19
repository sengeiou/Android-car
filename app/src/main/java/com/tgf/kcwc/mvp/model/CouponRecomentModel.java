package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/1/10 0010
 * E-mail:hekescott@qq.com
 *
 * 代金券推荐模型
 */

public class CouponRecomentModel {
    @JsonProperty("posid")
    public Coupon headRecomend;
    @JsonProperty("like")
    public ArrayList<Coupon> likeCoupon;

}
