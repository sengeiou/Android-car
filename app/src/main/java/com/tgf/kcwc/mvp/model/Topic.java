package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author：Jenny
 * Date:2016/12/22 18:56
 * E-mail：fishloveqin@gmail.com
 * 帖子
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Topic implements Parcelable {

    @JsonProperty("id")
    public int id;
    @JsonProperty("title")
    public String title;
    @JsonProperty("name")
    public String name;
    @JsonProperty("begin_date")
    public String begin;
    @JsonProperty("end_date")
    public String end;
    @JsonProperty("logo")
    public String itemImgUrl;
    @JsonProperty("cover")
    public String cover;
    @JsonProperty("activity_status")
    public int status;
    @JsonProperty("create_time")
    public String createTime;
    @JsonProperty("event_id")
    public String eventId;
    @JsonProperty("model") //cycle开车去 、play 请您玩
    public String model;

    @JsonProperty("is_system")
    public int isSystem; //1、表示是系统标签，0表示非系统标签

    public boolean isSelected;
    public Topic() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.name);
        dest.writeString(this.begin);
        dest.writeString(this.end);
        dest.writeString(this.itemImgUrl);
        dest.writeString(this.cover);
        dest.writeString(this.createTime);
        dest.writeString(this.eventId);
        dest.writeString(this.model);
    }

    protected Topic(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.name = in.readString();
        this.begin = in.readString();
        this.end = in.readString();
        this.itemImgUrl = in.readString();
        this.cover = in.readString();
        this.createTime = in.readString();
        this.eventId = in.readString();
        this.model = in.readString();
    }

    public static final Creator<Topic> CREATOR = new Creator<Topic>() {
        @Override
        public Topic createFromParcel(Parcel source) {
            return new Topic(source);
        }

        @Override
        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };
}
