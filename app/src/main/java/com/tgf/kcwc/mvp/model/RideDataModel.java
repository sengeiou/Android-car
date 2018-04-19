package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Jenny
 * Date:2017/5/3
 * E-mail:fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class RideDataModel {

    /**
     * list : [{"id":1,"user_id":1,"book_id":0,"title":"","cover":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg","status":0,"start_adds":"高庙村富洲新城","start_lng":"106.592146","start_lat":"29.568784","end_adds":"红旗河沟汽车站","end_lng":"0.000000","end_lat":"0.000000","start_time":"2017-02-20 21:05:37","end_time":"0000-00-00 00:00:00","speed_max":"0.00","speed_average":"0.00","altitude_average":0,"altitude_max":0,"mileage":"0.00","bending_angle_max":0,"isplan":0,"hundred_time":0,"four_hundred_time":0,"actual_time":0,"cardrive_name":"","user_car_id":0,"drivetype":1,"mileage_drived":"0.00","nav_ride_id":0},{"id":4,"user_id":1,"book_id":0,"title":"","cover":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg","status":1,"start_adds":"沙坪坝","start_lng":"106.592146","start_lat":"29.568784","end_adds":"解放碑","end_lng":"106.493540","end_lat":"29.525829","start_time":"2017-02-20 21:10:33","end_time":"2017-02-20 21:19:56","speed_max":"0.00","speed_average":"0.00","altitude_average":0,"altitude_max":0,"mileage":"0.00","bending_angle_max":0,"isplan":0,"hundred_time":0,"four_hundred_time":0,"actual_time":0,"cardrive_name":"","user_car_id":0,"drivetype":1,"mileage_drived":"0.00","nav_ride_id":0},{"id":5,"user_id":1,"book_id":51,"title":"昨天晚上的骑行","cover":"/car/1701/10/402cf48cf87ac65199a966292957cac4.jpg","status":1,"start_adds":"南平","start_lng":"106.592146","start_lat":"29.568784","end_adds":"观音桥","end_lng":"106.493540","end_lat":"29.525829","start_time":"2017-02-21 08:20:22","end_time":"2017-02-21 14:53:23","speed_max":"10.00","speed_average":"10.00","altitude_average":800,"altitude_max":2000,"mileage":"600.00","bending_angle_max":0,"isplan":0,"hundred_time":0,"four_hundred_time":0,"actual_time":22222,"cardrive_name":"","user_car_id":0,"drivetype":1,"mileage_drived":"0.00","nav_ride_id":0},{"id":6,"user_id":1,"book_id":0,"title":"测试周边的人","cover":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg","status":0,"start_adds":"天堂宝","start_lng":"106.472268","start_lat":"29.466585","end_adds":"大坪","end_lng":"106.516461","end_lat":"29.539411","start_time":"2017-04-26 12:08:15","end_time":"2017-04-26 12:47:37","speed_max":"0.00","speed_average":"0.00","altitude_average":0,"altitude_max":0,"mileage":"0.00","bending_angle_max":0,"isplan":0,"hundred_time":0,"four_hundred_time":0,"actual_time":0,"cardrive_name":"","user_car_id":0,"drivetype":1,"mileage_drived":"0.00","nav_ride_id":0},{"id":8,"user_id":1,"book_id":59,"title":"测试2","cover":"","status":1,"start_adds":"科园四路口","start_lng":"106.492149","start_lat":"29.524883","end_adds":"科园四路end","end_lng":"106.492160","end_lat":"29.530138","start_time":"2017-04-28 09:42:33","end_time":"2017-05-02 10:32:41","speed_max":"23.00","speed_average":"21.00","altitude_average":500,"altitude_max":503,"mileage":"10.00","bending_angle_max":0,"isplan":0,"hundred_time":0,"four_hundred_time":60,"actual_time":480,"cardrive_name":"","user_car_id":0,"drivetype":1,"mileage_drived":"0.00","nav_ride_id":0}]
     * pagination : {"count":5,"page":1}
     */

    public Pagination     pagination;
    public List<RideData> list;
    @JsonProperty("node_list")
    public ArrayList<RideDataNodeItem> nodeList;
    public int ride_id;
    public int status;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RideData {
        /**
         * id : 1
         * user_id : 1
         * book_id : 0
         * title :
         * cover : /car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg
         * status : 0
         * start_adds : 高庙村富洲新城
         * start_lng : 106.592146
         * start_lat : 29.568784
         * end_adds : 红旗河沟汽车站
         * end_lng : 0.000000
         * end_lat : 0.000000
         * start_time : 2017-02-20 21:05:37
         * end_time : 0000-00-00 00:00:00
         * speed_max : 0.00
         * speed_average : 0.00
         * altitude_average : 0
         * altitude_max : 0
         * mileage : 0.00
         * bending_angle_max : 0
         * isplan : 0
         * hundred_time : 0
         * four_hundred_time : 0
         * actual_time : 0
         * cardrive_name :
         * user_car_id : 0
         * drivetype : 1
         * mileage_drived : 0.00
         * nav_ride_id : 0
         */

        public int    id;
        @JsonProperty("user_id")
        public int    userId;
        @JsonProperty("book_id")
        public int    bookId;
        public String title;
        public String cover;
        public int    status;
        @JsonProperty("start_adds")
        public String startAdds;
        @JsonProperty("start_lng")
        public String startLng;
        @JsonProperty("start_lat")
        public String startLat;
        @JsonProperty("end_adds")
        public String endAdds;
        @JsonProperty("end_lng")
        public String endLng;
        @JsonProperty("end_lat")
        public String endLat;
        @JsonProperty("start_time")
        public String startTime;
        @JsonProperty("end_time")
        public String endTime;
        @JsonProperty("speed_max")
        public String speedMax;
        @JsonProperty("speed_average")
        public String speedAverage;
        @JsonProperty("altitude_average")
        public int    altitudeAverage;
        @JsonProperty("altitude_max")
        public int    altitudeMax;
        public String mileage;
        @JsonProperty("bending_angle_max")
        public int    bendingAngleMax;
        @JsonProperty("isplan")
        public int    isPlan;
        @JsonProperty("hundred_time")
        public int    hundredTime;
        @JsonProperty("four_hundred_time")
        public int    fourHundredTime;
        @JsonProperty("actual_time")
        public int    actualTime;
        @JsonProperty("cardrive_name")
        public String cardriveName;
        @JsonProperty("user_car_id")
        public int    userCarId;
        @JsonProperty("drivetype")
        public int    driveType;
        @JsonProperty("mileage_drived")
        public String mileageDrived;
        @JsonProperty("nav_ride_id")
        public int    navRideId;

        @JsonProperty("via_count")
        public int    viaCount;
        @JsonProperty("book_status")
        public int    bookStatus;
    }
}
