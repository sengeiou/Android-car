package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tgf.kcwc.util.NumFormatUtil;
import com.tgf.kcwc.util.TextUtil;

import java.util.List;

/**
 * Author：Jenny
 * Date:2016/12/22 16:26
 * E-mail：fishloveqin@gmail.com
 * 代金券
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Coupon {

    public int count;
    @JsonProperty("coupon_logo")
    public String logo;
    @JsonProperty("expire_date")
    public String expire;
    @JsonProperty("coupon_price")
    public double couponPrice;
    @JsonProperty("coupon_name")
    public String couponName;
    @JsonProperty("coupon_origin_price")
    public String originalPrice;

    @JsonProperty("get_limit")
    public int getLimit;

    @JsonProperty("nums")
    public String nums;
    @JsonProperty("codes")
    public String code;
    @JsonProperty("is_expiration")
    public int isExpiration;
    @JsonProperty("name")
    public String name;
    /**
     * 代金券id
     */
    @JsonProperty("id")
    public int id;
    /**
     * 面额
     */
    @JsonProperty("denomination")
    public String denomination;
    /**
     * 价格
     */
    @JsonProperty("price")
    public String price;
    /**
     * 标题
     */
    @JsonProperty("title")
    public String title;
    /**
     * 地址
     */
    @JsonProperty("address")
    public String desc;
    /**
     * 描述
     */
    @JsonProperty("desc")
    public String descripiton;
    /**
     * 封面
     */
    @JsonProperty("cover")
    public String cover;
    /**
     * 销量
     */
    @JsonProperty("sales")
    public int sales;
    /**
     * 抢光
     */
    @JsonProperty("is_finished")
    public int is_finished;
    /**
     * 已领
     */
    @JsonProperty("total")
    public int total;

    @JsonProperty("distance")
    public Double distance;

    @JsonProperty("use_limit_type")
    public String useLimitType;
    @JsonProperty("check_type")
    public int checkType;
    @JsonProperty("discount")
    public String discount;
    @JsonProperty("begin_time")
    public String beginTime;

    @JsonProperty("coupon_id")
    public int couponId;

    @JsonProperty("end_time")
    public String endTime;
    @JsonProperty("code_list")
    public List<Code> codes;
    @JsonProperty("dealers")
    public List<CouponDetailModel.Dealers> dealers;

    public String getDistance() {
        if(distance<1){
            return ((int)(distance*1000))+"m";
        }else {
            String tmp =distance+"";
            if(!TextUtil.isEmpty(tmp)){
                if(tmp.length()>1){
                    return NumFormatUtil.getFmtOneNum(distance)+"km";
                }
            }
        }
        return 0+"m";
    }
    public static class Code {
        @JsonProperty("code_id")
        public int codeId;
        @JsonProperty("check_time")
        public String checkTime;
        @JsonProperty("check_status")
        public String checkStatus;
        @JsonProperty("code")
        public String code;
        @JsonProperty("is_expiration")
        public int isExpiration;
    }

}
