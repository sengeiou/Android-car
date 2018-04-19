package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author：Jenny
 * Date:2017/1/4 17:12
 * E-mail：fishloveqin@gmail.com
 */

public class ValueItem {

    @JsonProperty("car_id")
    public int    carId;

    @JsonProperty("value")
    public String content;

    @JsonProperty("car_name")
    public String carName;

}
