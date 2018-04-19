package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author：Jenny
 * Date:2017/3/31 16:24
 * E-mail：fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SeriesDetailModel {

    @JsonProperty("head")
    public CarBean headerInfo;

    @JsonProperty("car_list")
    public  List<CarBean>  cars;
}
