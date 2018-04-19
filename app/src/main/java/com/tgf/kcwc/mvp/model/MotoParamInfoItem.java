package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author：Jenny
 * Date:2016/12/12 17:50
 * E-mail：fishloveqin@gmail.com
 */
public class MotoParamInfoItem {
   @JsonProperty("name")
    public String key;
    @JsonProperty("value")
    public String value;

}