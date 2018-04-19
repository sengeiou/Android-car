package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author：Jenny
 * Date:2016/12/22 19:42
 * E-mail：fishloveqin@gmail.com
 *
 * 轮播图Entity
 */

public class Banner {

    public int    type;
    @JsonProperty("img_url")
    public String url;
    @JsonProperty("img_title")
    public String title;
}
