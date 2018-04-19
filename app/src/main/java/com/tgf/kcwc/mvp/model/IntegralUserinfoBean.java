package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/4/21.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntegralUserinfoBean {
    public int code;

    public String msg;

    public Data data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        public int id;

        public String tel;

        public String nickname;

        public int level;
        @JsonProperty("moto_level")
        public int motoLevel;
        @JsonProperty("business_level")
        public String businessLevel;
        @JsonProperty("moto_business_level")
        public String motoBusinessLevel;
        @JsonProperty("org_id")
        public int orgId;
        @JsonProperty("real_name")
        public String realName;

        public String avatar;

        public int sex;
        @JsonProperty("kcwc_freeze_points")
        public int kcwcFreezePoints;
        @JsonProperty("moto_freeze_points")
        public int motoFreezePoints;
        @JsonProperty("kcwc_business_freeze_points")
        public int kcwcBusinessFreezePoints;
        @JsonProperty("moto_business_freeze_points")
        public int motoBusinessFreezePoints;
        @JsonProperty("current_points")
        public int currentPoints;
        @JsonProperty("current_business_points")
        public int currentBusinessPoints;
        @JsonProperty("current_exp")
        public int currentExp;
        @JsonProperty("current_business_exp")
        public int currentBusinessExp;
        @JsonProperty("used_points")
        public int usedPoints;
        @JsonProperty("used_business_points")
        public int usedBusinessPoints;
        @JsonProperty("get_points")
        public int getPoints;
        @JsonProperty("get_business_points")
        public int getBusinessPoints;
        @JsonProperty("vehicle_type")
        public String vehicleType;
        @JsonProperty("next_level")
        public int nextLevel;
        @JsonProperty("next_business_level")
        public int nextBusinessLevel;
        @JsonProperty("user_money")
        public String userMoney;

        @JsonProperty("is_doyen")
        public int              isDoyen;
        @JsonProperty("is_model")
        public int              isModel;
        @JsonProperty("is_master")
        public int              isMaster;
        @JsonProperty("is_vip")
        public int              isVip;

        public String              logo;

    }


}
