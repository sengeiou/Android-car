package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author:Jenny
 * Date:2017/10/19
 * E-mail:fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountBalanceModel {


    /**
     * money : 0.00
     * is_password : 1
     */

    public String money;
    @JsonProperty("is_password")
    public int isPwd;

    @JsonProperty("is_free_pay")
    public int isFreePay;

    @JsonProperty("free_pay_quota")
    public String freePayQuota;
}
