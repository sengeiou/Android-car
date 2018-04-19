package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tgf.kcwc.util.DataUtil;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.NumFormatUtil;
import com.tgf.kcwc.util.TextUtil;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/1/12 0012
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponDetailModel {
    /** 代金券id*/
    @JsonProperty("id")
    public String id;
    /** 标题*/
    @JsonProperty("title")
    public String title;
    /** 封面*/
    @JsonProperty("cover")
    public String cover;
    /** 封面*/
    @JsonProperty("get_limit")
    public int getLimit;
    /** 价格*/
    @JsonProperty("price")
    public double price;
    /** 随时退1，过期退0*/
    @JsonProperty("refund_type")
    public int refundType;

    /** 面额*/
    @JsonProperty("denomination")
    public String denomination;
    /** 开始时间*/
    @JsonProperty("begin_time")
    public String begintime;
    /** 结束时间*/
    @JsonProperty("end_time")
    public String endTime;
    /** 当前时间*/
    @JsonProperty("current_time")
    public String currentTime;
    /** 描述*/
    @JsonProperty("address")
    public String desc;
    /** 详细*/
    @JsonProperty("detail")
    public String detail;
    /** 销量*/
    @JsonProperty("send_total")
    public String sendTotal;
    /** 店铺*/
    @JsonProperty("dealers")
    public  ArrayList<Dealers> dealers;
    /** 需知*/
    @JsonProperty("rule")
    public  ArrayList<Rrule> rules;
    /** 在线*/
    @JsonProperty("online")
    public  ArrayList<OnlineItem> onlineList;

    /** 销量*/
    @JsonProperty("distance")
    public int distance;
    public  Statistics statistics;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public  static class Statistics{
        /** 销量*/
        @JsonProperty("total")
        public String total;
        public String send;
        @JsonProperty("user_is_collect")
        public int isCollect;
        /** 抢光了*/
        @JsonProperty("is_finished")
        public int  isFinished;
        /** 剩余*/
        @JsonProperty("surplus")
        public String  surplus;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public  static class Dealers implements Serializable{
        /** 销量*/
        @JsonProperty("id")
        public int id;
        @JsonProperty("name")
        public String name;
        @JsonProperty("full_name")
        public String fullName;
        @JsonProperty("address")
        public String address;
        @JsonProperty("tel")
        public String tel;
        @JsonProperty("longitude")
        public String longitude;
        @JsonProperty("latitude")
        public String latitude;
        @JsonProperty("logo")
        public String logo;
        @JsonProperty("distance")
        public Double distance;
        @JsonProperty("service_score")
        public int service_score;

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
    }
    public static class Rrule{
        @JsonProperty("title")
        public String title;
        @JsonProperty("content")
        public ArrayList<String> contentList;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OnlineItem{

        public int id;
        public String nickname;
        public String avatar;
        public String star;
    }
}
