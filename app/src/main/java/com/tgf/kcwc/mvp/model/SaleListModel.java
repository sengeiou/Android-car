package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author：Jenny
 * Date:2017/2/10 16:25
 * E-mail：fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SaleListModel {

    /**
     * list : [{"create_user":{"tel":"15123082913","id":2,"nickname":"建平"},"digg_count":0,"create_time":"2017-01-10 16:05:07","like_count":0,"status":1,"reply_count":0,"title":"我要卖车","model":"goods","model_id":3,"id":231,"view_count":0,"vehicle_type":"car","is_digest":1},{"create_user":{"tel":"15123082913","id":2,"nickname":"建平"},"digg_count":0,"create_time":"2017-01-10 14:32:00","like_count":0,"status":0,"reply_count":0,"title":"我要卖车","model":"goods","model_id":2,"id":230,"view_count":0,"vehicle_type":"car","is_digest":0}]
     * pagination : {"count":2,"page":1}
     */

    public Pagination pagination;
    public List<SaleItemBean> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SaleItemBean {
        /**
         * create_user : {"tel":"15123082913","id":2,"nickname":"建平"}
         * digg_count : 0
         * create_time : 2017-01-10 16:05:07
         * like_count : 0
         * status : 1
         * reply_count : 0
         * title : 我要卖车
         * model : goods
         * model_id : 3
         * id : 231
         * view_count : 0
         * vehicle_type : car
         * is_digest : 1
         */

        @JsonProperty("create_user")
        public CreateUserBean createUser;
        @JsonProperty("event")
        public Event event;
        @JsonProperty("create_time")
        public String createTime;
        public String title;
        public int id;
        public int status;
        public String cover;
        @JsonProperty("vehicle_type")
        public String vehicleType;
        @JsonProperty("is_digest")
        public int isDigest;
        @JsonProperty("buy_year")
        public int buyYear;
        @JsonProperty("road_haul")
        public String roadHaul;
        public double price;
        public String area;
        @JsonProperty("event_name")
        public String eventName;
        @JsonProperty("has_event")
        public int hasEvent;
        @JsonProperty("is_car")
        public int isCar;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Event {
            public int id;
            public String name;
            public String address;
            @JsonProperty("start_time")
            public String startTime;
            @JsonProperty("end_time")
            public String endTime;
        }
    }
}
