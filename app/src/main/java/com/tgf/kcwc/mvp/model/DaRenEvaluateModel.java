package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Auther: Scott
 * Date: 2017/3/29 0029
 * E-mail:hekescott@qq.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class DaRenEvaluateModel {
    public int id;
    public String title;
    public String cover;
    public int digg_count;
    public int view_count;
    public int reply_count;
    public CreateUserBean create_user;
}
