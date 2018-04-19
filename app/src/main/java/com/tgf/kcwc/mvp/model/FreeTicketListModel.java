package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author：Jenny
 * Date:2017/1/9 19:32
 * E-mail：fishloveqin@gmail.com
 * 赠票
 */

public class FreeTicketListModel {

    @JsonProperty("count")
    public int count;

    @JsonProperty("list")
    public List<FreeTicketItem> list;

    public static class FreeTicketItem {

        @JsonProperty("status")
        public int status;
        @JsonProperty("send_time")
        public String sendTime;

        @JsonProperty("event_info")
        public Sense sense;

        @JsonProperty("ticket_info")
        public Ticket ticket;
        @JsonProperty("org_info")
        public OrgModel orgInfo;
        @JsonProperty("id")
        public int id;
        @JsonProperty("receive_time_limit")
        public int receiveTimeLimit;
        @JsonProperty("nums")
        public int nums;

        @JsonProperty("ids")
        public List<Integer> ids;
        @JsonProperty("org_worker_info")
        public OrgWorkerInfo workerInfo;
        @JsonProperty("lose_time")
        public String loseTime;
        @JsonProperty("now_time")
        public String nowTime;

        public static class OrgWorkerInfo {

            public int id;
            public String tel;
            public String nickname;
        }
    }
}
