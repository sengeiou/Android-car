package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author：Jenny
 * Date:2017/2/14 14:53
 * E-mail：fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateUserBean {

    /**
     * id : 2
     * nickname : 建平
     * tel : 15123082913
     * sex : 1
     * is_doyen : 0
     * is_model : 0
     * count : 3
     */

    public int    id;
    public String nickname;
    public String tel;
    public int    sex;
    @JsonProperty("is_doyen")
    public int    isDoyen;
    @JsonProperty("is_model")
    public int    isModel;
    public int    count;
    public String avatar;
}
