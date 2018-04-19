package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Author：Jenny
 * Date:2017/3/16 11:10
 * E-mail：fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Hall {

    public int    id;
    public String name;
}
