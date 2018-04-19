package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Author:Jenny
 * Date:2017/8/2
 * E-mail:fishloveqin@gmail.com
 *
 * 热门标签（与车相关，如价格，车级等）
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class HotTag implements Parcelable {


    /**
     * text : 0-10万
     * type : price
     * value : 0-10
     */

    public String text;
    public String type;
    public String value;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeString(this.type);
        dest.writeString(this.value);
    }

    public HotTag() {
    }

    protected HotTag(Parcel in) {
        this.text = in.readString();
        this.type = in.readString();
        this.value = in.readString();
    }

    public static final Parcelable.Creator<HotTag> CREATOR = new Parcelable.Creator<HotTag>() {
        @Override
        public HotTag createFromParcel(Parcel source) {
            return new HotTag(source);
        }

        @Override
        public HotTag[] newArray(int size) {
            return new HotTag[size];
        }
    };
}
