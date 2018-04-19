package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author:Jenny
 * Date:2017/9/8
 * E-mail:fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketOrderModel {


    /**
     * amount_ori : 102.03
     * amount : 102.03
     * amount_discount : 0
     */

    @JsonProperty("amount_ori")
    public double amountOri;
    public double amount;
    @JsonProperty("amount_discount")
    public double amountDiscount;
}
