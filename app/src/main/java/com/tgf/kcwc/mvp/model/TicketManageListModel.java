package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/2/6 0006
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketManageListModel {
    @JsonProperty("count")
    public int       count;
    @JsonProperty("list")
    public ArrayList<TicketManage> mTicketManageList;

    public static class TicketManage {
        //分发id
        @JsonProperty("id")
        public int    id;
        //分发总量
        @JsonProperty("nums")
        public int    nums;
        //已发送给用户数量
        @JsonProperty("ht_user_nums")
        public int    htUserNums;
        //赠票id
        @JsonProperty("tf_id")
        public int    tfId;
        //展会id
        @JsonProperty("event_id")
        public int    event_id;
        @JsonProperty("ticketfree_info")
        public EXTicket ticketInfo;
        @JsonProperty("event_info")
        public Event  eventInfo;

        public int getTicketLeft() {
            return nums-htUserNums;
        }
    }
}
