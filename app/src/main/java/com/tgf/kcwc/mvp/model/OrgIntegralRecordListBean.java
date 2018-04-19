package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/4/21.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrgIntegralRecordListBean {
    public int code;

    public String msg;

    public Data data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        public Pagination pagination;

        public List<DataList> list;
        @JsonProperty("current_points")
        public int currentPoints;
        @JsonProperty("current_exps")
        public int currentExps;
        @JsonProperty("used_points")
        public int usedPoints;
        @JsonProperty("get_points")
        public int getPoints;

        public String name;
        @JsonProperty("full_name")
        public String fullName;

        public int exp;

        public int points;

        public int level;
        @JsonProperty("admin_tel")
        public String adminTel;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataList {

        public int id;

        public String sn;
        @JsonProperty("point_type")
        public int pointType;

        public int points;

        public int price;
        @JsonProperty("order_time")
        public String orderTime;
        @JsonProperty("pay_time")
        public String payTime;
        @JsonProperty("pay_status")
        public int payStatus;
        @JsonProperty("sale_uid")
        public int saleUid;
        @JsonProperty("sale_org_id")
        public int saleOrgId;
        @JsonProperty("buy_uid")
        public int buyUid;
        @JsonProperty("buy_org_id")
        public int buyOrgId;
        @JsonProperty("vehicle_type")
        public String vehicleType;
        @JsonProperty("buy_name")
        public String buyName;
        @JsonProperty("buy_tel")
        public String buyTel;
        @JsonProperty("sale_name")
        public String saleName;
        @JsonProperty("sale_tel")
        public String saleTel;
        @JsonProperty("buy_org_name")
        public String buyOrgName;
        @JsonProperty("sale_org_name")
        public String saleOrgName;

    }

}
