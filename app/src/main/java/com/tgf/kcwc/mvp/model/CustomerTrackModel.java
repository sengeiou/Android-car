package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/8/1 14:30
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerTrackModel {
    public Pagination pagination;
    @JsonProperty("list")
    public ArrayList<CustomerTrackItem> list;

    /**
     * "id":172,
     * "org_id":3,
     * "from_type":1,
     * "from_id":111,
     * "create_time":"2017-08-01 16:27:28",
     * "is_rob":0,
     * "action":{
     * "action":"发起了我要看",
     * "object":"一汽奥迪奥迪A6L"
     * },
     * "user":{
     * "id":206,
     * "avatar":"\/event\/1705\/19\/99deccab2bf741149af23ea5800252af.jpg",
     * "nickname":"帅呆呆",
     * "tel":"18521358579"
     * },
     * "rob_user":{
     * }
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CustomerTrackItem {
        public int id;
        //机构id
        @JsonProperty("org_id")
        public int orgId;
        //来源类型
        @JsonProperty("from_type")
        public int fromType;
        @JsonProperty("from_id")
        public int fromId;
        @JsonProperty("friend_id")
        public int friendId;
        @JsonProperty("create_time")
        public String createTime;
        //是否被抢
        @JsonProperty("is_rob")
        public int isRob;

        @JsonProperty("action")
        public ActionItem actionItem;
        @JsonProperty("user")
        public UserItem userItem;
        @JsonProperty("rob_user")
        public RobUserItem robUserItem;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ActionItem {
            public String action;
            public String object;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class UserItem {
            public int id;
            public String avatar;
            public String nickname;
            public String tel;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class RobUserItem {
            public int id;
            public String avatar;
            public String nickname;
            public String tel;
        }
    }
}
