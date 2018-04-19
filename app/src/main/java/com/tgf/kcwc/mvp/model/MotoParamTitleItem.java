package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author：Jenny
 * Date:2016/12/12 17:50
 * E-mail：fishloveqin@gmail.com
 */
public class MotoParamTitleItem {
    @JsonProperty("name")
    public String title;
    @JsonProperty("param_items")
    public List<MotoParamInfoItem> paramInfoItems;
}