package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Auther: Scott
 * Date: 2017/1/5 0005
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponCategory {
    @JsonProperty("id")
    public int    id;
    @JsonProperty("name")
    public String name;
    @JsonProperty("icon")
    public String icon;
    @JsonProperty("pid")
    public String pid;
    @JsonProperty("address")
    public String desc;
    @JsonProperty("coupon_total")
    public String coupon_total;
    @JsonProperty("has_brand")
    public String has_brand;
}
