package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author：Jenny
 * Date:2017/1/12 13:44
 * E-mail：fishloveqin@gmail.com
 * 页码
 */

public class Pagination {

    @JsonProperty("count")
    public int count;
    @JsonProperty("page")
    public int page;
}
