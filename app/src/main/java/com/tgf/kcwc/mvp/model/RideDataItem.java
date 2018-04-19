package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Author:Jenny
 * Date:2017/5/4
 * E-mail:fishloveqin@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RideDataItem implements Serializable, Parcelable {

    public transient int    progress;

    public String           title   = "";
    public transient String content = "";

    public transient int    max     = 100;

    public transient int    progressColorId;

    public String           address;
    public String           lat;
    public String           lng;
    public String           orders;
    public int              id;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.address);
        dest.writeString(this.lat);
        dest.writeString(this.lng);
        dest.writeString(this.orders);
        dest.writeInt(this.id);
    }

    public RideDataItem() {
    }

    protected RideDataItem(Parcel in) {
        this.title = in.readString();
        this.address = in.readString();
        this.lat = in.readString();
        this.lng = in.readString();
        this.orders = in.readString();
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<RideDataItem> CREATOR = new Parcelable.Creator<RideDataItem>() {
        @Override
        public RideDataItem createFromParcel(Parcel source) {
            return new RideDataItem(source);
        }

        @Override
        public RideDataItem[] newArray(int size) {
            return new RideDataItem[size];
        }
    };
}
