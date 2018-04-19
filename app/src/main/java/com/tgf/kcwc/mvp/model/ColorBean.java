package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author：Jenny
 * Date:2017/4/17 18:55
 * E-mail：fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ColorBean implements Parcelable {

    /**
     * id : 9
     * appearance_color_name : 白色
     * appearance_color_value : #FCFCFC
     * interior_color_name : 黑色
     * interior_color_value : #0F0F0F
     * series_name : 朗逸
     * car_name : 2017款 380THP 豪华版
     * is_current : 1
     */

    public int    id;
    @JsonProperty("appearance_color_name")
    public String appearanceColorName;
    @JsonProperty("appearance_color_value")
    public String appearanceColorValue;
    @JsonProperty("interior_color_name")
    public String interiorColorName;
    @JsonProperty("interior_color_value")
    public String interiorColorValue;
    @JsonProperty("series_name")
    public String seriesName;
    @JsonProperty("car_name")
    public String carName;
    @JsonProperty("is_current")
    public int    isCurrent;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.appearanceColorName);
        dest.writeString(this.appearanceColorValue);
        dest.writeString(this.interiorColorName);
        dest.writeString(this.interiorColorValue);
        dest.writeString(this.seriesName);
        dest.writeString(this.carName);
        dest.writeInt(this.isCurrent);
    }

    public ColorBean() {
    }

    protected ColorBean(Parcel in) {
        this.id = in.readInt();
        this.appearanceColorName = in.readString();
        this.appearanceColorValue = in.readString();
        this.interiorColorName = in.readString();
        this.interiorColorValue = in.readString();
        this.seriesName = in.readString();
        this.carName = in.readString();
        this.isCurrent = in.readInt();
    }

    public static final Parcelable.Creator<ColorBean> CREATOR = new Parcelable.Creator<ColorBean>() {
        @Override
        public ColorBean createFromParcel(Parcel source) {
            return new ColorBean(source);
        }

        @Override
        public ColorBean[] newArray(int size) {
            return new ColorBean[size];
        }
    };
}
