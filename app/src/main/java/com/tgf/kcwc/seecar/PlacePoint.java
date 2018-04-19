package com.tgf.kcwc.seecar;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.maps.model.LatLng;

/**
 * Auther: Scott
 * Date: 2017/2/24 0024
 * E-mail:hekescott@qq.com
 */

public class PlacePoint implements Parcelable {
    public String address;
    public String city;
    public String cityCode;
    public LatLng myLalng;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.address);
        dest.writeString(this.city);
        dest.writeString(this.cityCode);
        dest.writeParcelable(this.myLalng, flags);
    }

    public PlacePoint() {
    }

    protected PlacePoint(Parcel in) {
        this.address = in.readString();
        this.city = in.readString();
        this.cityCode = in.readString();
        this.myLalng = in.readParcelable(LatLng.class.getClassLoader());
    }

    public static final Parcelable.Creator<PlacePoint> CREATOR = new Parcelable.Creator<PlacePoint>() {
        @Override
        public PlacePoint createFromParcel(Parcel source) {
            return new PlacePoint(source);
        }

        @Override
        public PlacePoint[] newArray(int size) {
            return new PlacePoint[size];
        }
    };
}
