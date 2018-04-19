package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Auther: Scott
 * Date: 2017/7/12 0012
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RideDataNodeItem implements Parcelable {

    public int id;
    @JsonProperty("ride_id")
    public int rideId;
    public String lng;
    public String lat;
    @JsonProperty("add_time")
    public String addTime;
    public double altitude;
    public double speed;
    @JsonProperty("bending_angle")
    public double bendingAngle;
    public double mileage;
    public String address;
    @JsonProperty("city_name")
    public String cityName;
    @JsonProperty("ride_time")
    public int rideTime;
    @JsonProperty("speed_max")
    public double speedMax;

    public RideDataNodeItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.rideId);
        dest.writeString(this.lng);
        dest.writeString(this.lat);
        dest.writeString(this.addTime);
        dest.writeDouble(this.altitude);
        dest.writeDouble(this.speed);
        dest.writeDouble(this.bendingAngle);
        dest.writeDouble(this.mileage);
        dest.writeString(this.address);
        dest.writeString(this.cityName);
        dest.writeInt(this.rideTime);
        dest.writeDouble(this.speedMax);
    }

    protected RideDataNodeItem(Parcel in) {
        this.id = in.readInt();
        this.rideId = in.readInt();
        this.lng = in.readString();
        this.lat = in.readString();
        this.addTime = in.readString();
        this.altitude = in.readDouble();
        this.speed = in.readDouble();
        this.bendingAngle = in.readDouble();
        this.mileage = in.readDouble();
        this.address = in.readString();
        this.cityName = in.readString();
        this.rideTime = in.readInt();
        this.speedMax = in.readDouble();
    }

    public static final Creator<RideDataNodeItem> CREATOR = new Creator<RideDataNodeItem>() {
        @Override
        public RideDataNodeItem createFromParcel(Parcel source) {
            return new RideDataNodeItem(source);
        }

        @Override
        public RideDataNodeItem[] newArray(int size) {
            return new RideDataNodeItem[size];
        }
    };
}
