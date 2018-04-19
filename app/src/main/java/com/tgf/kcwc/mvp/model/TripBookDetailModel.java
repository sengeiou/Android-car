package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.view.richeditor.SEditorData;

/**
 * Auther: Scott
 * Date: 2017/5/11 0011
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TripBookDetailModel {
    public String                         title;
    public int                         thread_id;
    public int                         ride_id;
    public int                         is_collect;
    public String                         user_id;
    public String                         start_adds;
    public String                         end_adds;
    public String                         content;
    public String                         address;
    public String                         lng;
    public String                         lat;
    public String                         cover;
    public int                         is_praise;
    @JsonProperty("start_time")
    public String                         startTime;
    @JsonProperty("end_time")
    public String                         endTime;
    @JsonProperty("post_time")
    public String                         postTime;
    @JsonProperty("ride_report")
    public RideReportDetailModel.RideBean rideReport;
    @JsonProperty("user_info")
    public User                           userInfo;
    @JsonProperty("tag_list")
    public ArrayList<Topic> topiclist;
    @JsonProperty("honor_list")
    public List<Honor> honorList;
    @JsonProperty("line_list")
    public List<NodeDesc> lineList;
    @JsonProperty("recommend_info")
    public Recomment recommendInfo;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User {
        public int        id;
        public String     nickname;
        public int        sex;
        public String     avatar;
        @JsonProperty("is_doyen")
        public int        isDaren;
        @JsonProperty("is_model")
        public int        isModel;
        public Auth auth;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Auth{
        @JsonProperty("brand_logo")
        public String brandLogo;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NodeDesc{
        @JsonProperty("ride_add_time")
        public String arriveTime;
        @JsonProperty("ride_node_id")
        public int rideNodeId;
        public String adds;
        public ArrayList<SEditorData> desc;
        @JsonProperty("content_list")
        public ArrayList<AttrationEntity> contentList;  //
        @JsonProperty("coupon_list")
        public ArrayList<Coupon> couponList;
        @JsonProperty("current_previous_time")
        public String rideTime;
        @JsonProperty("current_previous_mileage")
        public String rideDistance;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Coupon implements Parcelable {
        @JsonProperty("coupon_denomination")
        public String denomination;
        @JsonProperty("coupon_price")
        public String price;
        @JsonProperty("org_name")
        public String orgName;
        @JsonProperty("coupon_id")
        public int couponId;
        @JsonProperty("coupon_title")
        public String couponTitle;
        @JsonProperty("coupon_cover")
        public String couponCover;
        @JsonProperty("coupon_discount")
        public String couponDiscount;
        public double distance;
        @JsonProperty("coupon_used")
        public int couponused;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.denomination);
            dest.writeString(this.price);
            dest.writeString(this.orgName);
            dest.writeInt(this.couponId);
            dest.writeString(this.couponTitle);
            dest.writeString(this.couponCover);
            dest.writeString(this.couponDiscount);
            dest.writeDouble(this.distance);
            dest.writeInt(this.couponused);
        }

        public Coupon() {
        }

        protected Coupon(Parcel in) {
            this.denomination = in.readString();
            this.price = in.readString();
            this.orgName = in.readString();
            this.couponId = in.readInt();
            this.couponTitle = in.readString();
            this.couponCover = in.readString();
            this.couponDiscount = in.readString();
            this.distance = in.readDouble();
            this.couponused = in.readInt();
        }

        public static final Parcelable.Creator<Coupon> CREATOR = new Parcelable.Creator<Coupon>() {
            @Override
            public Coupon createFromParcel(Parcel source) {
                return new Coupon(source);
            }

            @Override
            public Coupon[] newArray(int size) {
                return new Coupon[size];
            }
        };
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Honor{

        public String icon;
        public String text;
        public String substr;
        public String tag;
        public int integral;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Recomment{
        @JsonProperty("recommend_content")
        public String content;
        public float quotient;
    }
    public String getDistance(KPlayCarApp kPlayCarApp){
        LatLng latLng1 = new LatLng(Double.parseDouble(kPlayCarApp.getLattitude()),Double.parseDouble(kPlayCarApp.getLongitude()));
        LatLng latLng2 = new LatLng(Double.parseDouble(this.lat),Double.parseDouble(this.lng));
        float meter= AMapUtils.calculateLineDistance(latLng1,latLng2);
       meter =  new BigDecimal(meter).divide(new BigDecimal(1000),1,BigDecimal.ROUND_HALF_UP).floatValue();
        return  meter+"km";
    }
}
