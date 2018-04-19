package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author:Jenny
 * Date:2017/9/8
 * E-mail:fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketCoupon extends Coupon {


    /**
     * ticket_id : 216
     * coupon_title : 门票代金券-全国级
     * ticket_type : 通票
     */
    @JsonProperty("ticket_id")
    public int ticketId;
    @JsonProperty("coupon_title")
    public String couponTitle;

    @JsonProperty("ticket_type")
    public String ticketType;

    @JsonProperty("can_get")
    public int canGet;
}
