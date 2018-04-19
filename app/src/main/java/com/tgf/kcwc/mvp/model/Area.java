package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Auther: Scott
 * Date: 2016/12/29 0029
 * E-mail:hekescott@qq.com
 */

public class Area {
    @JsonProperty("area_id")
    public int         areaId;
    @JsonProperty("area_name")
    public String      areaName;
    @JsonProperty("brand_list")
    public List<Brand> brands;
}
