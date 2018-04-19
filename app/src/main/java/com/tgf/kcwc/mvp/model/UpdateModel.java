package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Auther: Scott
 * Date: 2017/7/11 0011
 * E-mail:hekescott@qq.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateModel {

    public String download_url;
    public int type;
    public int version_code;
    public String version_name;
}
