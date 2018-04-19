package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/5/2.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplyListBean {
    public int    code;

    public String msg;

    public Data   data;

    public static class Data {
        @JsonProperty("create_by")
        public String            createBy;
        @JsonProperty("user_id")
        public int            userId;

        public Pagination     pagination;

        public List<DataList> list;
    }

    public static class DataList {
        public int     id;
        @JsonProperty("applicant_id")
        public int     applicantId;

        public String  avatar;

        public String  nickname;

        public int     num;
        @JsonProperty("check_status")
        public int     checkStatus;

        public int     sex;

        public String  tel;
        @JsonProperty("create_time")
        public String  createTime;

        public String  reason;
        @JsonProperty("check_time")
        public String  checkTime;

        public String  relation;

        public String  remark;

        public boolean isUnfold=false;
    }

}
