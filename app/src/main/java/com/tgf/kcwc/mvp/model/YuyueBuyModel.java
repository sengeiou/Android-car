package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Auther: Scott
 * Date: 2017/2/27 0027
 * E-mail:hekescott@qq.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class YuyueBuyModel {

    @JsonProperty("brand_name")
    public String brand_name;
    @JsonProperty("car_series_name")
    public String seriesName;
    @JsonProperty("car_name")
    public String car_name;
    @JsonProperty("car_cover")
    public String car_cover;
    @JsonProperty("user_id")
    public int user_id;
    @JsonProperty("out_color")
    public String outColor;
    @JsonProperty("in_color")
    public String inColor;
    @JsonProperty("id")
    public int id;
    @JsonProperty("order_status")
    public int orderStatus;


    @JsonProperty("offer_list")
    public ArrayList<OrgItem> offer_list;
    @JsonProperty("offer_count")
    public int offer_count;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrgItem implements Parcelable {
        @JsonProperty("status")
        public int status;
        @JsonProperty("avatar")
        public String avatar;
        @JsonProperty("msg_num")
        public int    msg_num;
        @JsonProperty("nickname")
        public String nickname;
        @JsonProperty("integral")//评分
        public String integral;
        @JsonProperty("tel")
        public String tel;
        @JsonProperty("username")
        public String username;
        @JsonProperty("offer")
        public double offer;
        @JsonProperty("org_name")
        public String org_name;
        @JsonProperty("org_address")
        public String org_address;
        @JsonProperty("id")
        public int    id;
        @JsonProperty("offer_user_id")
        public int    offer_user_id;
        @JsonProperty("comment_star")
        public float    commentStar;
        @JsonProperty("star")
        public String    star;
        @JsonProperty("longitude")
        public String    lng;
        @JsonProperty("latitude")
        public String    lat;

        public OrgItem() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.status);
            dest.writeString(this.avatar);
            dest.writeInt(this.msg_num);
            dest.writeString(this.nickname);
            dest.writeString(this.integral);
            dest.writeString(this.tel);
            dest.writeString(this.username);
            dest.writeDouble(this.offer);
            dest.writeString(this.org_name);
            dest.writeString(this.org_address);
            dest.writeInt(this.id);
            dest.writeInt(this.offer_user_id);
            dest.writeFloat(this.commentStar);
            dest.writeString(this.star);
            dest.writeString(this.lng);
            dest.writeString(this.lat);
        }

        protected OrgItem(Parcel in) {
            this.status = in.readInt();
            this.avatar = in.readString();
            this.msg_num = in.readInt();
            this.nickname = in.readString();
            this.integral = in.readString();
            this.tel = in.readString();
            this.username = in.readString();
            this.offer = in.readDouble();
            this.org_name = in.readString();
            this.org_address = in.readString();
            this.id = in.readInt();
            this.offer_user_id = in.readInt();
            this.commentStar = in.readFloat();
            this.star = in.readString();
            this.lng = in.readString();
            this.lat = in.readString();
        }

        public static final Creator<OrgItem> CREATOR = new Creator<OrgItem>() {
            @Override
            public OrgItem createFromParcel(Parcel source) {
                return new OrgItem(source);
            }

            @Override
            public OrgItem[] newArray(int size) {
                return new OrgItem[size];
            }
        };
    }
}
