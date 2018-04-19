package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author:Jenny
 * Date:2017/5/5
 * E-mail:fishloveqin@gmail.com
 *
 * 骑行报告
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class RideReportDetailModel {


    /**
     * ride : {"id":8,"speed_max":"23.00","speed_average":"21.00","altitude_average":500,"altitude_max":503,"bending_angle_max":0,"hundred_time":0,"four_hundred_time":60,"actual_time":"00:08:00","mileage":"10.00","start_time":"2017-04-28 09:42:33","end_time":"2017-04-28 10:42:33","cover":"","avatar":"/avatar/1704/21/67ca7221ea1312d91e78cd8279ed810b.jpg","nickname":"重庆展览中心总账号","all_time":"01:00:00"}
     * rideNodeKey : [{"id":10,"ride_id":8,"type":2,"lng":"106.492149","lat":"29.524883","add_time":"2017-04-28 09:42:33","altitude":495,"speed":"0.00","bending_angle":0,"isinlinepoint":1,"operation_type":2},{"id":13,"ride_id":8,"type":1,"lng":"106.492122","lat":"29.527516","add_time":"2017-04-28 15:17:36","altitude":503,"speed":"0.00","bending_angle":0,"isinlinepoint":0,"operation_type":3},{"id":18,"ride_id":8,"type":3,"lng":"106.492160","lat":"29.530138","add_time":"2017-05-02 10:32:41","altitude":500,"speed":"0.00","bending_angle":0,"isinlinepoint":0,"operation_type":4}]
     * rideNodeList : [{"id":10,"ride_id":8,"type":2,"lng":"106.492149","lat":"29.524883","add_time":"2017-04-28 09:42:33","altitude":495,"speed":"0.00","bending_angle":0,"isinlinepoint":1,"operation_type":2},{"id":11,"ride_id":8,"type":1,"lng":"106.492170","lat":"29.525807","add_time":"2017-04-28 13:57:42","altitude":500,"speed":"20.00","bending_angle":0,"isinlinepoint":0,"operation_type":0},{"id":12,"ride_id":8,"type":1,"lng":"106.492128","lat":"29.526909","add_time":"2017-04-28 14:23:41","altitude":500,"speed":"20.00","bending_angle":0,"isinlinepoint":0,"operation_type":0},{"id":13,"ride_id":8,"type":1,"lng":"106.492122","lat":"29.527516","add_time":"2017-04-28 15:17:36","altitude":503,"speed":"0.00","bending_angle":0,"isinlinepoint":0,"operation_type":3},{"id":14,"ride_id":8,"type":1,"lng":"106.492122","lat":"29.527516","add_time":"2017-04-28 15:45:02","altitude":503,"speed":"0.00","bending_angle":0,"isinlinepoint":0,"operation_type":1},{"id":15,"ride_id":8,"type":1,"lng":"106.492219","lat":"29.528412","add_time":"2017-04-28 15:57:28","altitude":502,"speed":"23.00","bending_angle":0,"isinlinepoint":0,"operation_type":0},{"id":18,"ride_id":8,"type":3,"lng":"106.492160","lat":"29.530138","add_time":"2017-05-02 10:32:41","altitude":500,"speed":"0.00","bending_angle":0,"isinlinepoint":0,"operation_type":4}]
     */

    public RideBean ride;
    public List<RideNodeKeyBean> rideNodeKey;
    public List<RideNodeListBean> rideNodeList;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RideBean {
        /**
         * id : 8
         * speed_max : 23.00
         * speed_average : 21.00
         * altitude_average : 500
         * altitude_max : 503
         * bending_angle_max : 0
         * hundred_time : 0
         * four_hundred_time : 60
         * actual_time : 00:08:00
         * mileage : 10.00
         * start_time : 2017-04-28 09:42:33
         * end_time : 2017-04-28 10:42:33
         * cover :
         * avatar : /avatar/1704/21/67ca7221ea1312d91e78cd8279ed810b.jpg
         * nickname : 重庆展览中心总账号
         * all_time : 01:00:00
         */

        public int id;
        @JsonProperty("speed_max")
        public String speedMax;
        @JsonProperty("speed_average")
        public String speedAverage;
        @JsonProperty("altitude_average")
        public int altitudeAverage;
        @JsonProperty("altitude_max")
        public int altitudeMax;
        @JsonProperty("bending_angle_max")
        public int bendingAngleMax;
        @JsonProperty("hundred_time")
        public int hundredTime;
        @JsonProperty("four_hundred_time")
        public int fourHundredTime;
        @JsonProperty("start_adds")
        public String startAdds;
        @JsonProperty("end_adds")
        public String endAdds;
        @JsonProperty("actual_time")
        public String actualTime;
        public String mileage;
        @JsonProperty("start_time")
        public String startTime;
        @JsonProperty("end_time")
        public String endTime;
        public String cover;
        public String avatar;
        public String nickname;
        @JsonProperty("all_time")
        public String allTime;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RideNodeKeyBean {
        /**
         * id : 10
         * ride_id : 8
         * type : 2
         * lng : 106.492149
         * lat : 29.524883
         * add_time : 2017-04-28 09:42:33
         * altitude : 495
         * speed : 0.00
         * bending_angle : 0
         * isinlinepoint : 1
         * operation_type : 2
         */

        public int id;
        @JsonProperty("ride_id")
        public int rideId;
        public int type;
        public String lng;
        public String lat;
        @JsonProperty("add_time")
        public String addTime;
        public int altitude;
        public String speed;
        @JsonProperty("bending_angle")
        public int bendingAngle;
        public int isinlinepoint;
        @JsonProperty("operation_type")
        public int operationType;
        public  String title;
        public  String address;
        @JsonProperty("city_name")
        public  String cityName;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RideNodeListBean {
        /**
         * id : 10
         * ride_id : 8
         * type : 2
         * lng : 106.492149
         * lat : 29.524883
         * add_time : 2017-04-28 09:42:33
         * altitude : 495
         * speed : 0.00
         * bending_angle : 0
         * isinlinepoint : 1
         * operation_type : 2
         */

        public int id;
        @JsonProperty("ride_id")
        public int rideId;
        public int type;
        public String lng;
        public String lat;
        @JsonProperty("add_time")
        public String addTime;
        public int altitude;
        public String speed;
        @JsonProperty("bending_angle")
        public int bendingAngle;
        public int isinlinepoint;
        @JsonProperty("operation_type")
        public int operationType;
    }
}
