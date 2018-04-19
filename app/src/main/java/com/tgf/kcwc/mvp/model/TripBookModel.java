package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tgf.kcwc.R;
import com.tgf.kcwc.util.TextUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/5/2 0002
 * E-mail:hekescott@qq.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class TripBookModel {

    public ArrayList<TripBookModelItem> list;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class  TripBookModelItem{
        public int    id;
        public int    thread_id;
        public String content;
        public int    user_id;
        public int    ride_id;
        public String lng;
        public String lat;
        public String start_adds;
        public String start_time;
        public String start_lng;
        public String start_lat;
        public String end_adds;
        public String end_time;
        public String end_lng;
        public String end_lat;
        public String post_time;
        public int    is_concise;
        public int    is_recommend;
        public int    is_plus;
        public int    status;
        public int    updateby;
        public String update_time;
        public int    downloadcount;
        public String title;
        public String cover;
        public int    reply_count;
        public int    view_count;
        public int    digg_count;
        public int    like_count;
        public int    share_count;
        public String avatar;
        public String nickname;
        public String speed_max;
        public String speed_average;
        public int    altitude_average;
        public String mileage;
        public String actual_time;
        public double    quotient;
        public double    distance;
        @JsonProperty("ride_status")
        public int rideStatus;
        public String getSpeedMax() {
            DecimalFormat df = new DecimalFormat("#.0");
//            helper.setText(R.id.tripbook_mileage,);
        if(TextUtil.isEmpty(speed_max)){
            return "0.0";
        }
        String str =df.format(Double.parseDouble(speed_max));
            if(".0".equals(str.trim())){
                return "0.0";
            }
            return str;
        }
        public String getSpeedAverage() {
            DecimalFormat df = new DecimalFormat("#.0");
//            helper.setText(R.id.tripbook_mileage,);
            if(TextUtil.isEmpty(speed_average)){
                return "0.0";
            }
            String str =df.format(Double.parseDouble(speed_average));
            if(".0".equals(str.trim())){
                return "0.0";
            }
            return str;
        }
    }
}
