package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author：Jenny
 * Date:2017/1/9 15:49
 * E-mail：fishloveqin@gmail.com
 */

public class Sense implements Parcelable {

    @JsonProperty("id")
    public int    id;
    @JsonProperty("name")
    public String name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    public Sense() {
    }

    protected Sense(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<Sense> CREATOR = new Parcelable.Creator<Sense>() {
        @Override
        public Sense createFromParcel(Parcel source) {
            return new Sense(source);
        }

        @Override
        public Sense[] newArray(int size) {
            return new Sense[size];
        }
    };
}
