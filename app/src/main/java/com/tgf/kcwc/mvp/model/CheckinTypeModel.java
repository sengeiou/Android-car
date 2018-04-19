package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author：Jenny
 * Date:2017/2/7 15:07
 * E-mail：fishloveqin@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckinTypeModel {

    public int count;

    @JsonProperty("list")
    public List<CheckinType> types;

    public static class CheckinType {

        public int id;

        public String name;
        @JsonProperty("apply_items")
        public List<Form> forms;

        public String remarks;
    }

    public Event event;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Event {


        /**
         * id : 161
         * name : 九龙坡大型展会YY
         * start_time : 2017-08-01 00:00:00
         * end_time : 2017-10-31 23:59:59
         */

        public int id;
        public String name;
        @JsonProperty("start_time")
        public String startTime;
        @JsonProperty("end_time")
        public String endTime;
    }
}
