package com.tgf.kcwc.mvp.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tgf.kcwc.util.URLUtil;

/**
 * Author：Jenny
 * Date:2016/12/8 20:38
 * E-mail：fishloveqin@gmail.com
 * 用户个人资料实体类
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreBrand {

    public int         auth;
    @JsonProperty("factory_list")
    public List<Brand> dataList;

}
