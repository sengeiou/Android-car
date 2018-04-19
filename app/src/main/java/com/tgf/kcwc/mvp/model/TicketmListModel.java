package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/2/10 0010
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketmListModel {
    @JsonProperty("event_info")
    public TicketmExhibitModel eventInfo;
    public Num nums;
    @JsonProperty("list")
    public ArrayList<HandleTicket> handleList;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Num {
        public int all;
        public int have;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HandleTicket {
        public int id;
        public String name;
        public String color;
        public String remarks;
        public String price;
        @JsonProperty("ticket_list")
        public ArrayList<HandleTicketOrg> ticketList;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HandleTicketOrg {
        public int id;
        @JsonProperty("org_name")
        public String orgName;
        public int nums;
        @JsonProperty("have_nums")
        public int haveNums;
    }
}
