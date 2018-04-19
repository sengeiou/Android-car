package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author:Jenny
 * Date:2017/8/10
 * E-mail:fishloveqin@gmail.com
 * 用户驾途数据Model
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRideDataModel {


    /**
     * ride_data : {"time":42,"mileage":3240.03,"duration":"17.5","max":345,"max_duration":"03:09:59"}
     * list : [{"id":1166,"cover":"/avatar/1708/01/0719ac87aa54d0903d72c27250f23827.png","title":"","start_time":"2017-08-01 07:29:32","end_time":"2017-08-01 07:38:50","mileage":"7.68","start_adds":"重庆市沙坪坝区青凤路44-附49-1号靠近中共青木关镇委员会","end_adds":"重庆市沙坪坝区靠近村公所","isplan":0,"speed_max":"104.40"},{"id":1164,"cover":"/avatar/1707/31/bc03e5c9a33874c87af8d9ab7320605f.png","title":"","start_time":"2017-07-28 16:48:34","end_time":"2017-07-31 14:03:52","mileage":"1.05","start_adds":"重庆市九龙坡区科园四街40号靠近南方君临酒店","end_adds":"重庆市九龙坡区科园四街40号靠近南方君临酒店","isplan":0,"speed_max":"6.30"},{"id":1120,"cover":"/avatar/1707/26/783437ea0aa036083b1bdeda37009462.png","title":"","start_time":"2017-07-26 22:07:51","end_time":"2017-07-26 22:45:12","mileage":"33.91","start_adds":"重庆市九龙坡区科园四路269号靠近重庆展览中心","end_adds":"重庆市沙坪坝区青东路靠近中共青木关镇委员会","isplan":0,"speed_max":"113.40"},{"id":1086,"cover":"/avatar/1707/26/079970829b3582b530f696a1439cbaf6.png","title":"","start_time":"2017-07-26 16:01:47","end_time":"2017-07-26 16:10:36","mileage":"1.54","start_adds":"重庆市九龙坡区科园四路269号靠近重庆展览中心","end_adds":"重庆市九龙坡区渝州路靠近后工星座","isplan":0,"speed_max":"31.50"},{"id":697,"cover":"","title":"","start_time":"2017-07-12 17:50:41","end_time":"0000-00-00 00:00:00","mileage":"0.00","start_adds":"重庆市九龙坡区科园四街40号靠近重庆展览中心","end_adds":"","isplan":0,"speed_max":"0.00"},{"id":696,"cover":"","title":"","start_time":"2017-07-12 17:45:14","end_time":"0000-00-00 00:00:00","mileage":"0.00","start_adds":"重庆市九龙坡区科园四街40号靠近南方君临酒店","end_adds":"","isplan":0,"speed_max":"0.00"},{"id":694,"cover":"","title":"","start_time":"2017-07-12 17:38:05","end_time":"0000-00-00 00:00:00","mileage":"0.00","start_adds":"重庆市九龙坡区科园四街40号靠近南方君临酒店","end_adds":"","isplan":0,"speed_max":"0.00"},{"id":692,"cover":"/avatar/1707/12/29af89955f1c7fc0edd35200c4b9656d.png","title":"","start_time":"2017-07-12 16:42:39","end_time":"2017-07-12 16:45:58","mileage":"1.20","start_adds":"重庆市九龙坡区渝州路靠近重庆协和医院(渝州路)","end_adds":"重庆市九龙坡区科园四路靠近南方君临酒店","isplan":0,"speed_max":"31.50"},{"id":689,"cover":"/avatar/1707/12/29676bfe0fe8913590226ad00a86387d.png","title":"","start_time":"2017-07-12 16:38:20","end_time":"2017-07-12 16:42:22","mileage":"1.28","start_adds":"重庆市九龙坡区石小路靠近上方·新人居","end_adds":"重庆市九龙坡区渝州路靠近华宇名都29号商务楼","isplan":0,"speed_max":"41.40"},{"id":687,"cover":"/avatar/1707/12/c1f40b591a073ec5f05f55905e5f2a40.png","title":"","start_time":"2017-07-12 16:33:49","end_time":"2017-07-12 16:37:33","mileage":"1.61","start_adds":"重庆市沙坪坝区石小路靠近斌鑫胜景雅苑","end_adds":"重庆市九龙坡区石小路靠近怡锦苑","isplan":0,"speed_max":"38.70"}]
     */

    @JsonProperty("ride_data")
    public RideBaseBean data;
    public List<RideDataBean> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RideBaseBean {
        /**
         * time : 42
         * mileage : 3240.03
         * duration : 17.5
         * max : 345
         * max_duration : 03:09:59
         */

        public int time;
        public double mileage;
        public String duration;
        public int max;
        public String max_duration;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RideDataBean {
        /**
         * id : 1166
         * cover : /avatar/1708/01/0719ac87aa54d0903d72c27250f23827.png
         * title :
         * start_time : 2017-08-01 07:29:32
         * end_time : 2017-08-01 07:38:50
         * mileage : 7.68
         * start_adds : 重庆市沙坪坝区青凤路44-附49-1号靠近中共青木关镇委员会
         * end_adds : 重庆市沙坪坝区靠近村公所
         * isplan : 0
         * speed_max : 104.40
         */

        public int id;
        public String cover;
        public String title;
        @JsonProperty("start_time")
        public String start;
        @JsonProperty("end_time")
        public String end;
        public String mileage;
        @JsonProperty("start_adds")
        public String startAdds;

        @JsonProperty("end_adds")
        public String endAdds;
        public int isplan;
        @JsonProperty("speed_max")
        public String speedMax;
    }
}
