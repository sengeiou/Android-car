package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Auther: Scott
 * Date: 2017/8/17 0017
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImgItem implements Parcelable {
    @JsonProperty("thread_id")
    public int    threadId;
    @JsonProperty("name")
    public String name;
    @JsonProperty("link_url")
    public String linkUrl;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.threadId);
        dest.writeString(this.name);
        dest.writeString(this.linkUrl);
    }

    public ImgItem() {
    }

    protected ImgItem(Parcel in) {
        this.threadId = in.readInt();
        this.name = in.readString();
        this.linkUrl = in.readString();
    }

    public static final Parcelable.Creator<ImgItem> CREATOR = new Parcelable.Creator<ImgItem>() {
        @Override
        public ImgItem createFromParcel(Parcel source) {
            return new ImgItem(source);
        }

        @Override
        public ImgItem[] newArray(int size) {
            return new ImgItem[size];
        }
    };
}
