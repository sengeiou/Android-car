package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Auther: Scott
 * Date: 2017/5/24 0024
 * E-mail:hekescott@qq.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponScanOffResultModel implements Parcelable {


    public String title;
    @JsonProperty("begin_time")
    public String beginTime;
    @JsonProperty("end_time")
    public String endTime;
    public String code;
    @JsonProperty("check_time")
    public String checkTime;
    public String denomination;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.beginTime);
        dest.writeString(this.endTime);
        dest.writeString(this.code);
        dest.writeString(this.checkTime);
        dest.writeString(this.denomination);
    }

    public CouponScanOffResultModel() {
    }

    protected CouponScanOffResultModel(Parcel in) {
        this.title = in.readString();
        this.beginTime = in.readString();
        this.endTime = in.readString();
        this.code = in.readString();
        this.checkTime = in.readString();
        this.denomination = in.readString();
    }

    public static final Creator<CouponScanOffResultModel> CREATOR = new Creator<CouponScanOffResultModel>() {
        @Override
        public CouponScanOffResultModel createFromParcel(Parcel source) {
            return new CouponScanOffResultModel(source);
        }

        @Override
        public CouponScanOffResultModel[] newArray(int size) {
            return new CouponScanOffResultModel[size];
        }
    };
}
