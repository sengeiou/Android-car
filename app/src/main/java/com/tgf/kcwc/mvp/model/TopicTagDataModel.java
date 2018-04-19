package com.tgf.kcwc.mvp.model;

import android.os.Parcel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Author:Jenny
 * Date:2017/9/4
 * E-mail:fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class TopicTagDataModel extends Brand{

    public String cover;

    public String title;

    public  String fromType;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.cover);
        dest.writeString(this.title);
    }

    public TopicTagDataModel() {
    }

    protected TopicTagDataModel(Parcel in) {
        super(in);
        this.cover = in.readString();
        this.title = in.readString();
    }

    public static final Creator<TopicTagDataModel> CREATOR = new Creator<TopicTagDataModel>() {
        @Override
        public TopicTagDataModel createFromParcel(Parcel source) {
            return new TopicTagDataModel(source);
        }

        @Override
        public TopicTagDataModel[] newArray(int size) {
            return new TopicTagDataModel[size];
        }
    };
}
