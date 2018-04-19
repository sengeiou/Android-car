package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/4/20.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PleasePlayModel {
    public int    code;

    public String msg;

    public Data   data;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        public Pagination     pagination;

        public List<DataList> list;

    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataList {
        public int         id;

        public String      cover;

        public String      title;
        @JsonProperty("view_count")
        public int         viewCount;
        @JsonProperty("digg_count")
        public int         diggCount;
        @JsonProperty("reply_count")
        public int         replyCount;
        @JsonProperty("is_digest")
        public int         isDigest;

        public String      type;

        public String      create_time;
        @JsonProperty("deadline_time")
        public String      deadlineTime;

        public String      rendezvous;
        @JsonProperty("rend_city")
        public String      rendCity;
        @JsonProperty("org_id")
        public int         orgId;
        @JsonProperty("limit_max")
        public int         limitMax;
        @JsonProperty("org_type_id")
        public int         orgType_id;
        @JsonProperty("create_user")
        public Create_user createUser;

        public int         surplus;
        @JsonProperty("begin_time")
        public String      beginTime;
        @JsonProperty("activity_status")
        public String      activityStatus;

        public Org         org;

        @JsonProperty("end_time")
        public String                          endTime;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Create_user {
        public int    id;

        public String avatar;

        public String username;
        @JsonProperty("real_name")
        public String realName;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Org {
        public int         id;

        public String      name;

        public String      logo;

        public List<Brand> brand;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Brand {
        public int    id;

        public String name;

        public String logo;

        public String vehicle_type;
    }

}
