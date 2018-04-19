package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Auther: Scott
 * Date: 2017/8/1 0001
 * E-mail:hekescott@qq.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class EaseLoginModel {

    @JsonProperty("user_name")
    public String mUserName;
    @JsonProperty("attached")
    public String mAttached;
}
