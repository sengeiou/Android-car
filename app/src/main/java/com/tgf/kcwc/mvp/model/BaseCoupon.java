package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Author:Jenny
 * Date:2017/8/23
 * E-mail:fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseCoupon implements Parcelable {


    /**
     * cid : 324
     * tid : 189
     * name : 票类代金券3
     * price : 60.00
     * codes : H80T8EIS
     * nums : 1
     */

    public int cid;
    public String tid;
    public String name;
    public String price;
    public String codes;
    public int nums;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.cid);
        dest.writeString(this.tid);
        dest.writeString(this.name);
        dest.writeString(this.price);
        dest.writeString(this.codes);
        dest.writeInt(this.nums);
    }

    public BaseCoupon() {
    }

    protected BaseCoupon(Parcel in) {
        this.cid = in.readInt();
        this.tid = in.readString();
        this.name = in.readString();
        this.price = in.readString();
        this.codes = in.readString();
        this.nums = in.readInt();
    }

    public static final Parcelable.Creator<BaseCoupon> CREATOR = new Parcelable.Creator<BaseCoupon>() {
        @Override
        public BaseCoupon createFromParcel(Parcel source) {
            return new BaseCoupon(source);
        }

        @Override
        public BaseCoupon[] newArray(int size) {
            return new BaseCoupon[size];
        }
    };
}
