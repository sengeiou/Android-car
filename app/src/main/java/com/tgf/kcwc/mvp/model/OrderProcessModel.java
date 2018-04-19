package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/1.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderProcessModel {
    @JsonProperty("pagination")
    public Pagination pagination;
    @JsonProperty("list")
    public ArrayList<OrderProcessItem> list;
    @JsonProperty("user")
    public UserItem userItem;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrderProcessItem {
        /**
         * "list": [
         * {
         * "action": "发起了我要看",
         * "object": "一汽奥迪奥迪A6L",
         * "time": "2017-07-27 16:19:22"
         * }
         * ],
         */
        public String action;
        public String object;
        public String time;
    }

    /**
     * "id": 2,
     * "avatar": "\/avatar\/1703\/24\/ccab54caaf2b16ea94cc4405ff884c1c.jpg",
     * "nickname": "拉风的男人",
     * "sale_count": 2
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserItem {
        public int id;
        public String avatar;
        public String nickname;
        @JsonProperty("sale_count")
        public int saleCount;
    }
}
