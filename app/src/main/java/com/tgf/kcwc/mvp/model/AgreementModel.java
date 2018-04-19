package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @anthor noti
 * @time 2017/9/30
 * @describle
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgreementModel {
    public int id;

    public String name;

    public String content;

    public String remark;

    public String place;
}
