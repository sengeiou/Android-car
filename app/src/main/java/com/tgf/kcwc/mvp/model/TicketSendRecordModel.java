package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/2/8 0008
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketSendRecordModel {
    @JsonProperty("list")
    public ArrayList<RecordItem> recordItems;

    public static class RecordItem {
        @JsonProperty("send_time")
        public String            sendTime;
        @JsonProperty("receive_time_limit")
        public String            timeLimit;
        @JsonProperty("list")
        public ArrayList<Person> userList;
    }

    public static class Person {
        @JsonProperty("mobile")
        public String mobile;
        @JsonProperty("user_type")
        public int    userType;
        @JsonProperty("nums")
        public int    nums;
        @JsonProperty("user_name")
        public String userName;
    }
}
