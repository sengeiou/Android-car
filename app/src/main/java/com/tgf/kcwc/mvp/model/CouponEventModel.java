package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Auther: Scott
 * Date: 2017/2/15 0015
 * E-mail:hekescott@qq.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponEventModel {
    @JsonProperty("id")
    public int id;
    @JsonProperty("title")
    public String title;
    @JsonProperty("cover")
    public String cover;
    @JsonProperty("inventory")
    public int inventory;
    @JsonProperty("get_limit")
    public int limitNums;
}
