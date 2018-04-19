package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author：Jenny
 * Date:2017/1/19 18:28
 * E-mail：fishloveqin@gmail.com
 * 微信支付
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderPayParam {
    @JsonProperty("appid")
    public String appId;
    @JsonProperty("timestamp")
    public String timeStamp;
    @JsonProperty("noncestr")
    public String nonceStr;
    @JsonProperty("partnerid")
    public String partnerId;
    @JsonProperty("prepayid")
    public String prepayId;
    @JsonProperty("sign")
    public String paySign;
    @JsonProperty("package")
    public String wxPackage;

    @JsonProperty("order_sn")
    public String orderSN;
}
