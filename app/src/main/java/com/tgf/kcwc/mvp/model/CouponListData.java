package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author：Jenny
 * Date:2016/12/22 16:31
 * E-mail：fishloveqin@gmail.com
 * 代金券列表
 */

public class CouponListData {

    @JsonProperty("coupon_list")
    public List<Coupon> lists;
}
