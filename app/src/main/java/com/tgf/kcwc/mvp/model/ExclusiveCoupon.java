package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author：Jenny
 * Date:2017/1/23 16:10
 * E-mail：fishloveqin@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class  ExclusiveCoupon {

    public Coupon                          coupon;
    @JsonProperty("expiration_time")
    public String                          expirationTime;
    @JsonProperty("get_num")
    public int                             getNum;

    public int                             num;

    public int                             id;

    @JsonProperty("dealers")
    public List<CouponDetailModel.Dealers> dealers;
    @JsonProperty("issue_org")
    public CouponDetailModel.Dealers issueOrg;

    @JsonProperty("distribute_user")
    public ExclusiveUser              user;

    @JsonProperty("current_time")
    public String                          currentTime;

    public static class ExclusiveUser{
        public int id;
        public String nickname;
        public String tel;
    }
}
