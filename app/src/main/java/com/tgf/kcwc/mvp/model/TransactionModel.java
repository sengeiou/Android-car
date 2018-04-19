package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author：Jenny
 * Date:2017/1/23 11:51
 * E-mail：fishloveqin@gmail.com
 * 交易/支付模型
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionModel {
    @JsonProperty("message")
    public String           message;

    @JsonProperty("need_pay")
    public int              needPay;
    @JsonProperty("order_id")
    public int              orderId;

    @JsonProperty("unified_order")
    public OrderPayParam transactionParam;
}
