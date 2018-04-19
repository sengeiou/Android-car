package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/5/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageListBean {
    public int code;

    public String msg;

    public Data data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        @JsonProperty("pagination")
        public Pagination pagination;
        public List<DataList> lists;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataList {
        public int number;

        public String title;

        public int type;

        public String time;

        public String name;

        public String sysname;

        public int id;

        public String content;

        public String createtime;
        @JsonProperty("group_id")
        public int groupId;
        @JsonProperty("sender_uid")
        public int senderUid;
        @JsonProperty("user_id")
        public int userId;
        @JsonProperty("event_id")
        public int eventId;

        public int count;

        public int letteruser;

        public String avatar;

        public String uname;

        public String icon;

        public int selectype;

        public int imagenum;

    }

}
