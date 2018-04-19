package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Author:Jenny
 * Date:2017/11/15
 * E-mail:fishloveqin@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrePaidModel {

    public String tn;
    public String id;
}
