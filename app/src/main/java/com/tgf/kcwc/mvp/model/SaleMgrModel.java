package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：Jenny
 * Date:2017/2/27 19:12
 * E-mail：fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SaleMgrModel {

    /**
     * pagination : {"count":9,"page":1}
     * list : [{"id":373,"title":"6399","cover":"cover.png","reply_count":0,"is_pass":1,"view_count":0,"price":"39955555.00","is_grounding":0,"create_time":"2017-02-24 15:31:30","is_failure":1},{"id":374,"title":"6399","cover":"cover.png","reply_count":0,"is_pass":1,"view_count":0,"price":"39955555.00","is_grounding":0,"create_time":"2017-02-24 15:31:39","is_failure":1},{"id":375,"title":"bmw-x4","cover":"cover.png","reply_count":0,"is_pass":1,"view_count":0,"price":"13008455.00","is_grounding":0,"create_time":"2017-02-27 12:00:02","is_failure":1},{"id":376,"title":"good","cover":"cover.png","reply_count":0,"is_pass":1,"view_count":0,"price":"321475.00","is_grounding":0,"create_time":"2017-02-27 13:40:58","is_failure":1},{"id":377,"title":"good","cover":"cover.png","reply_count":0,"is_pass":1,"view_count":0,"price":"321475.00","is_grounding":0,"create_time":"2017-02-27 13:41:07","is_failure":1},{"id":378,"title":"good","cover":"cover.png","reply_count":0,"is_pass":1,"view_count":0,"price":"321475.00","is_grounding":0,"create_time":"2017-02-27 13:41:18","is_failure":1},{"id":379,"title":"好","cover":"cover.png","reply_count":0,"is_pass":1,"view_count":0,"price":"325885.00","is_grounding":0,"create_time":"2017-02-27 13:43:24","is_failure":1},{"id":380,"title":"好","cover":"cover.png","reply_count":0,"is_pass":1,"view_count":0,"price":"325885.00","is_grounding":0,"create_time":"2017-02-27 13:44:17","is_failure":1},{"id":381,"title":"好","cover":"cover.png","reply_count":0,"is_pass":1,"view_count":0,"price":"123.00","is_grounding":0,"create_time":"2017-02-27 15:08:20","is_failure":1}]
     */
    public Pagination pagination;
    @JsonProperty("list")
    public List<ListBean> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListBean {
        /**
         * id : 373
         * title : 6399
         * cover : cover.png
         * reply_count : 0
         * is_pass : 1
         * view_count : 0
         * price : 39955555.00
         * is_grounding : 0
         * create_time : 2017-02-24 15:31:30
         * is_failure : 1
         */

        public int id;
        public String title;
        public String cover;
        @JsonProperty("reply_count")
        public int replyCount;
        @JsonProperty("thread_status")
        public int threadStatus;
        @JsonProperty("is_pass")
        public int isPass;
        @JsonProperty("view_count")
        public int viewCount;
        @JsonProperty("like_count")
        public int likeCount;
        public String price;
        @JsonProperty("is_grounding")
        public int isGrounding;
        @JsonProperty("create_time")
        public String createTime;
        @JsonProperty("is_failure")
        public int isFailure;
//        @JsonProperty("cache")
//        public Cache cache;

        @JsonProperty("apply")
        public Apply apply;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Apply {
            public int id;
            public int status;
            @JsonProperty("brand_name")
            public String brandName;
            @JsonProperty("apply_name")
            public String applyName;
            @JsonProperty("plate_number")
            public String plateNumber;
            @JsonProperty("park_time_id")
            public int parkTimeId;
            @JsonProperty("park_id")
            public int parkId;
            @JsonProperty("hall_id")
            public int hallId;
            @JsonProperty("event_id")
            public int eventId;
            @JsonProperty("user_id")
            public int userId;
            @JsonProperty("brand_logo")
            public String brandLogo;
            @JsonProperty("car_series_name")
            public String carSeriesName;
            @JsonProperty("car_name")
            public String carName;
            @JsonProperty("buy_year")
            public int buyYear;
            @JsonProperty("road_haul")
            public String roadHaul;
            public String price;
            @JsonProperty("event_name")
            public String eventName;
            @JsonProperty("hall_name")
            public String hallName;
            @JsonProperty("park_name")
            public String parkName;
            @JsonProperty("start_time")
            public String startTime;
            @JsonProperty("end_time")
            public String endTime;
            @JsonProperty("car_image_in")
            public String carImageIn;
            @JsonProperty("car_image_out")
            public String carImageOut;
            @JsonProperty("in_place_time")
            public String inPlaceTime;
            @JsonProperty("out_place_time")
            public String outPlaceTime;
            @JsonProperty("in_park_time")
            public String inParkTime;
            @JsonProperty("out_park_time")
            public String outParkTime;
            @JsonProperty("apply_phone")
            public String applyPhone;
            public String nickname;
            public String avatar;
            @JsonProperty("event_start_time")
            public String eventStartTime;
            @JsonProperty("event_end_time")
            public String eventEndTime;
            @JsonProperty("check_apply_note")
            public String checkApplyNote;
            @JsonProperty("order_sn")
            public String orderSn;
            @JsonProperty("price_string")
            public int priceString;
            @JsonProperty("need_pay")
            public int needPay;
            public String statusName;

            @JsonProperty("event_status")
            public EventStatus eventStatus;

            @JsonProperty("event_time")
            public EventTime eventTime;

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class EventStatus {
                public int status;
                public String statusName;
                public String statusTime;
                @JsonProperty("start_status")
                public int startStatus;

            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class EventTime {
                public String date;
                public String start;
                public String end;
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Cache {
            public String token;
            public String step;
            public String carNumber;
            public String curRegion;
            @JsonProperty("idcard_back")
            public String idcardBack;
            @JsonProperty("idcard_front")
            public String idcardFront;
            @JsonProperty("driving_license")
            public String drivingLicense;
            @JsonProperty("car_image_in")
            public String carImageIn;
            @JsonProperty("car_image_out")
            public String carImageOut;
            public String eventId;
            public String eventName;
            @JsonProperty("thread_id")
            public int threadId;
            @JsonProperty("apply_id")
            public String applyId;
            @JsonProperty("apply_name")
            public String applyName;
        }
    }
}
