package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/5/4.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrgIntegralDetailsBean {
    @JsonProperty("code")
    public int code;
    @JsonProperty("msg")
    public String msg;

    @JsonProperty("data")
    public Data data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {

        public int id;

        public int uid;

        public String crease;

        public String exps;

        public String points;

        public String mark;
        @JsonProperty("remain_value")
        public String remainValue;

        public String sn;
        @JsonProperty("product_id")
        public int productId;

        public int nums;

        public String remarks;
        @JsonProperty("create_time")
        public String createTime;
        @JsonProperty("vehicle_type")
        public String vehicleType;
        @JsonProperty("rules_id")
        public int rulesId;
        @JsonProperty("create_by")
        public int createBy;

        public int type;
        @JsonProperty("org_id")
        public int orgId;
        @JsonProperty("rule_name")
        public String ruleName;
        @JsonProperty("record_type")
        public int recordType;

    }
}
