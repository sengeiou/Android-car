package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Auther: Scott
 * Date: 2017/2/13 0013
 * E-mail:hekescott@qq.com
 */

public class CheckSendSeeModel {
    @JsonProperty("mobile")
    public String mobile;
    @JsonProperty("nums")
    public int num;
}
