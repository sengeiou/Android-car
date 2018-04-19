package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author：Jenny
 * Date:2017/2/9 15:17
 * E-mail：fishloveqin@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntegralOrderDetailBean {

    public String sn;
    @JsonProperty("point_type")
    public int pointType;

    public int points;

    public int price;
    @JsonProperty("pay_time")
    public String payTime;
    @JsonProperty("order_time")
    public String orderTime;
    @JsonProperty("pay_status")
    public int payStatus;

    public String payment;
    @JsonProperty("buy_uid")
    public int buyUid;
    @JsonProperty("sale_off")
    public String saleOff;
    @JsonProperty("user_money")
    public String userMoney;
}
