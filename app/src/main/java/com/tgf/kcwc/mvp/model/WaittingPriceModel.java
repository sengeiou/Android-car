package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.maps.model.LatLng;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tgf.kcwc.util.NumFormatUtil;
import com.tgf.kcwc.util.TextUtil;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/2/22 0022
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WaittingPriceModel {
    @JsonProperty("brand_name")
    public String         brandName;
    @JsonProperty("car_name")
    public String         carName;
    @JsonProperty("id")
    public int            id;
    @JsonProperty("lat")
    public String         lat;
    @JsonProperty("lng")
    public String         lng;
    @JsonProperty("out_color")
    public String         outColor;
    @JsonProperty("in_color")
    public String         inColor;
    @JsonProperty("org_count")
    public int            orgCount;
    @JsonProperty("create_time")
    public String            createTime;
    @JsonProperty("org_list")
    public ArrayList<Org> org_list;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Org implements Parcelable {
        @JsonProperty("address")
        public String address;
        @JsonProperty("distance")
        public double distance;


        @JsonProperty("full_name")
        public String fullName;
        @JsonProperty("id")
        public int id;
        @JsonProperty("latitude")
        public String latitude;
        @JsonProperty("longitude")
        public String longitude;
        @JsonProperty("icon_logo")
        public String logo;
        @JsonProperty("name")
        public String name;
        @JsonProperty("tel")
        public String tel;

        private LatLng mLatLng;

        public LatLng getmLatLng() {
            if(mLatLng ==null)
                mLatLng =   new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
            return mLatLng;
        }

        public String getDistance() {
            if(distance<1){
                return ((int)(distance*1000))+"m";
            }else {
                String tmp =distance+"";
                if(!TextUtil.isEmpty(tmp)){
                    if(tmp.length()>1){
                        return NumFormatUtil.getFmtOneNum(distance)+"km";
                    }
                }
            }
            return 0+"m";
        }
        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.address);
            dest.writeDouble(this.distance);
            dest.writeString(this.fullName);
            dest.writeInt(this.id);
            dest.writeString(this.latitude);
            dest.writeString(this.longitude);
            dest.writeString(this.logo);
            dest.writeString(this.name);
            dest.writeString(this.tel);
            dest.writeParcelable(this.mLatLng, flags);
        }

        public Org() {
        }

        protected Org(Parcel in) {
            this.address = in.readString();
            this.distance = in.readDouble();
            this.fullName = in.readString();
            this.id = in.readInt();
            this.latitude = in.readString();
            this.longitude = in.readString();
            this.logo = in.readString();
            this.name = in.readString();
            this.tel = in.readString();
            this.mLatLng = in.readParcelable(LatLng.class.getClassLoader());
        }

        public static final Creator<Org> CREATOR = new Creator<Org>() {
            @Override
            public Org createFromParcel(Parcel source) {
                return new Org(source);
            }

            @Override
            public Org[] newArray(int size) {
                return new Org[size];
            }
        };
    }
}
