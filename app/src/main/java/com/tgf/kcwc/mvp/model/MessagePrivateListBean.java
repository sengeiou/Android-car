package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/5/19.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessagePrivateListBean {
    public int code;

    public String msg;

    public Data data;

    public static class Data {
        @JsonProperty("pagination")
        public Pagination pagination;
        public List<DataList> lists;
    }

    public static class DataList {
        public int id;

        public String content;

        public String createtime;
        @JsonProperty("group_id")
        public int groupId;
        @JsonProperty("sender_uid")
        public int senderUid;
        @JsonProperty("user_id")
        public int userId;

        public int letteruser;

        public String avatar;

        public String uname;
    }


}
