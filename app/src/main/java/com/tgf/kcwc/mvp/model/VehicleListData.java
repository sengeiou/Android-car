package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author：Jenny
 * Date:2016/12/13 08:47
 * E-mail：fishloveqin@gmail.com
 * 车型列表
 */

public class VehicleListData {

    @JsonProperty("total_num")
    public int         modelNum;
    @JsonProperty("brand_list")
    public List<Brand> brands;

}
