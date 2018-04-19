package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Administrator on 2017/11/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangeExhibitionModel {
    @JsonProperty("data")
    public Data data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        public String addtime;
        public String name;
        public String deposit;
        public String detail;
        @JsonProperty("service_charge")
        public String serviceCharge;
        @JsonProperty("ticket_type")
        public String ticketType;
        public int delete;
        public int id;
        @JsonProperty("event_id")
        public int eventId;
        @JsonProperty("is_give_ticket")
        public int isGiveTicket;
        @JsonProperty("ticket_number")
        public int ticketNumber;
    }
}
