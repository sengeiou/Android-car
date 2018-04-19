package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Auther: Scott
 * Date: 2017/2/7 0007
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketManageDetailModel {
    @JsonProperty("details")
    public Detail mDetail;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Detail {
        @JsonProperty("event_cover")
        public String eventCover;
        @JsonProperty("event_name")
        public String eventName;
        @JsonProperty("ticket_name")
        public String ticketName;
        @JsonProperty("price")
        public String price;
        @JsonProperty("color")
        public String color;
        @JsonProperty("can_get_nums")
        public int    canGetNums;
        @JsonProperty("use_time_start")
        public String useTimeStart;
        @JsonProperty("use_time_end")
        public String useTimeEnd;
        @JsonProperty("tf_id")
        public int    tfid;
        @JsonProperty("id")
        public int    id;
        @JsonProperty("nums")
        public int    nums;
        @JsonProperty("ht_user_nums")
        public int    htUserNums;
        @JsonProperty("receive_nums")
        public int    receiveNums;
        @JsonProperty("use_nums")
        public int    useNums;
        @JsonProperty("event_id")
        public int    eventId;
        @JsonProperty("receive_nums_person")
        public int    receiveNumsPerson;
        @JsonProperty("use_nums_person")
        public int    useNumsPerson;
        @JsonProperty("status")
        public int    status;
        @JsonProperty("remarks")
        public String    remarks;
        @JsonProperty("sign")
        public String    sign;
    }
}
