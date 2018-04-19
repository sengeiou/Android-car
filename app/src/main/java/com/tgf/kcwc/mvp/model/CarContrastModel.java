package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author：Jenny
 * Date:2017/1/4 17:06
 * E-mail：fishloveqin@gmail.com
 */

public class CarContrastModel {

    @JsonProperty("param")
    public List<ParamContrast> params;

    @JsonProperty("base")
    public List<ValueItem>     baseInfos;

}
