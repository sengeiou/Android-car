package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author：Jenny
 * Date:2016/12/27 10:50
 * E-mail：fishloveqin@gmail.com
 *
 * 新车发布Model
 */

public class CarLaunchModel {

    @JsonProperty("pagination")
    public Pagination    pagination;

    @JsonProperty("list")
    public List<NewCarBean> lists;
}
