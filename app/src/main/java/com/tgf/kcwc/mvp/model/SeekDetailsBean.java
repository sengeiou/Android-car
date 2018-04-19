package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/5/22.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SeekDetailsBean {
    public int code;

    public String msg;

    public Data data;

    public static class Data {
        public List<DataList> list;

        public Pagination pagination;

    }

    public static class DataList {
        /*------------公用----------------------------*/
        public int id;

        public String index;

        public String type;


        /*----------------用户-----------------------*/
        public String avatar;
        @JsonProperty("fans_num")
        public int fansNum;
        @JsonProperty("follow_num")
        public int followNum;
        @JsonProperty("nickname")
        public String nickname;
        @JsonProperty("thread_num")
        public int threadNum;

        /*----------------群组-----------------------*/

        @JsonProperty("user_num")
        public int userNum;

        /*------------------资讯------------------------*/

        public int clicks;

        /*----------------车主--------------------*/
        @JsonProperty("buy_year")
        public String buyYear;
        @JsonProperty("road_haul")
        public String roadHaul;

        /*----------------找车---------------*/
        public String name;
        @JsonProperty("cover_out")
        public String coverOut;
        @JsonProperty("reference_price_max")
        public String referencePriceMax;
        @JsonProperty("reference_price_min")
        public String referencePriceMin;
        @JsonProperty("thread_goods_total")
        public int threadGoodsTotal;
        @JsonProperty("organization_car_total")
        public int organizationCarTotal;
        @JsonProperty("car_total")
        public int carTotal;
        @JsonProperty("car_level")
        public String carLevel;

        /*----------------车友、开车去、请你玩、路书、测评---------------*/
        public String model;

        public String title;

        public String cover;
        @JsonProperty("create_time")
        public String createTime;
        @JsonProperty("view_count")
        public int viewCount;
        @JsonProperty("like_count")
        public int likeCount;
        @JsonProperty("reply_count")
        public int replyCount;

        public String area;

        /*----------------代金券---------------*/

        public int price;

        public int denomination;

        public String desc;

        public int sales;
        @JsonProperty("org_names")
        public String orgNames;

        /*----------------优惠--------------------*/
        @JsonProperty("privilege_type")
        public int privilegeType;

        public int special;


    }

}
