package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author：Jenny
 * Date:2017/1/10 15:58
 * E-mail：fishloveqin@gmail.com
 * 购票
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class BuyTicketModel {

    @JsonProperty("event_name")
    public String senseName;
    @JsonProperty("have_content")
    public int haveContent;
    @JsonProperty("list")
    public List<TicketDetail> tickets;

    @JsonProperty("event_cover")
    public String cover;

    @JsonProperty("coupon")
    public List<BaseCoupon> coupons;

    @JsonProperty("content_activity")
    public String ticketDescContent;


    @JsonProperty("haveCoupon")
    public  int haveCoupon;
}
