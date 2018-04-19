package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author：Jenny
 * Date:2017/1/17 14:12
 * E-mail：fishloveqin@gmail.com
 */

public class SalespersonModel {

    @JsonProperty("org_user")
    public List<Account.UserInfo> users;
}
