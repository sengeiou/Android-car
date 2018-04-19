package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

/**
 * Author:Jenny
 * Date:2017/4/28
 * E-mail:fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class RideData implements Parcelable {

    /**
     * dipangle : -4.3016872
     * latitude : 29.524232911329257
     * longitude : 106.49163526090507
     * ridelinedataid : 20
     * ridelineid : A7DFCB6C072748D78BCD151D905277A0
     * speed : 7.3499994
     * time : 1487598453363
     */

    @SerializedName("dipangle")
    public double dipAngle;
    public double latitude;
    public double longitude;
    @SerializedName("ridelinedataid")
    public int    rideLineDataId;
    @SerializedName("ridelineid")
    public String rideLineId;
    public double speed;
    public long   time;

    public double maxSpeed;
    public double totalMile;

    @JsonProperty("ride_node_id")
    public int    rideNodeId;
    @JsonProperty("ride_id")
    public int    rideId;

    public String address;

    public double alt;           //海拔

    @SerializedName("city_name")
    public String cityName;

    public int    pointType = 0;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.dipAngle);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeInt(this.rideLineDataId);
        dest.writeString(this.rideLineId);
        dest.writeDouble(this.speed);
        dest.writeLong(this.time);
        dest.writeDouble(this.maxSpeed);
        dest.writeDouble(this.totalMile);
        dest.writeInt(this.rideNodeId);
        dest.writeInt(this.rideId);
        dest.writeString(this.address);
        dest.writeDouble(this.alt);
        dest.writeString(this.cityName);
        dest.writeInt(this.pointType);
    }

    public RideData() {
    }

    protected RideData(Parcel in) {
        this.dipAngle = in.readDouble();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.rideLineDataId = in.readInt();
        this.rideLineId = in.readString();
        this.speed = in.readDouble();
        this.time = in.readLong();
        this.maxSpeed = in.readDouble();
        this.totalMile = in.readDouble();
        this.rideNodeId = in.readInt();
        this.rideId = in.readInt();
        this.address = in.readString();
        this.alt = in.readDouble();
        this.cityName = in.readString();
        this.pointType = in.readInt();
    }

    public static final Parcelable.Creator<RideData> CREATOR = new Parcelable.Creator<RideData>() {
        @Override
        public RideData createFromParcel(Parcel source) {
            return new RideData(source);
        }

        @Override
        public RideData[] newArray(int size) {
            return new RideData[size];
        }
    };

    @Override
    public String toString() {
        return "RideData{" +
                "dipAngle=" + dipAngle +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", rideLineDataId=" + rideLineDataId +
                ", rideLineId='" + rideLineId + '\'' +
                ", speed=" + speed +
                ", time=" + time +
                ", maxSpeed=" + maxSpeed +
                ", totalMile=" + totalMile +
                ", rideNodeId=" + rideNodeId +
                ", rideId=" + rideId +
                ", address='" + address + '\'' +
                ", alt=" + alt +
                ", cityName='" + cityName + '\'' +
                ", pointType=" + pointType +
                '}';
    }
}
