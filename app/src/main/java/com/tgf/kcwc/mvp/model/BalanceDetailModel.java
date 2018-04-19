package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author:Jenny
 * Date:2017/10/19
 * E-mail:fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class BalanceDetailModel {


    /**
     * id : 47
     * money : 125.00
     * flag : 2
     * source_type : 3
     * act_type : 提现
     * create_time : 2017-10-17 13:56:19
     * remarks :
     * balance : 0.00
     * order_sn :
     */

    public int id;
    public String money;
    public int flag;
    @JsonProperty("source_type")
    public int sourceType;
    @JsonProperty("act_type")
    public String actType;
    @JsonProperty("create_time")
    public String createTime;
    public String remarks;
    public String balance;
    @JsonProperty("order_sn")
    public String orderSN;
}
