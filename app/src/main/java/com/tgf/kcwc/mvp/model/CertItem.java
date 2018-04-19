package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author：Jenny
 * Date:2017/2/7 21:07
 * E-mail：fishloveqin@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CertItem {
    @JsonProperty("times_all")
    public int       timesAll;
    @JsonProperty("serial_sn")
    public String    serialSN;
    public String    company;
    @JsonProperty("is_active")
    public int       isActive;
    @JsonProperty("is_print")
    public int       isPrint;
    @JsonProperty("times_today")
    public int       timesToday;
    public int       type;
    @JsonProperty("is_receive")
    public int       isReceive;
    @JsonProperty("check_status")
    public int       checkStatus;
    public int       status;
    @JsonProperty("event_id")
    public int       eventId;
    public int       cid;
    @JsonProperty("cert_sn")
    public String    certSN;
    public int       id;
    public String    name;
    @JsonProperty("is_on")
    public int       isON;
    public String    brand;
    @JsonProperty("cert_info")
    public CertInfo  certInfo;

    @JsonProperty("event_info")
    public EventInfo eventInfo;

    public static class CertInfo {
        @JsonProperty("end_time")
        public String endTime;
        public String name;
        @JsonProperty("start_time")
        public String startTime;

        public int    id;
        @JsonProperty("times_type")
        public int    timesType;

        public int    times;
    }

    public static class EventInfo {
        public String name;
        public int    id;
    }
}
