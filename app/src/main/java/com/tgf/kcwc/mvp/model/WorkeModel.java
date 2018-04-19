package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Auther: Scott
 * Date: 2017/10/20 0020
 * E-mail:hekescott@qq.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkeModel {

    public int user_id;
    public String nickname;
    public String avatar;
    public String tel;
    public String real_name;
}
