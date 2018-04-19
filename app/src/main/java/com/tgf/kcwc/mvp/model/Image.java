package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author：Jenny
 * Date:2017/1/12 13:49
 * E-mail：fishloveqin@gmail.com
 * 图片模型
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Image implements Parcelable {

    @JsonProperty("appearance_img")
    public String appearanceImg;
    @JsonProperty("interior_img")
    public String interiorImg;

    @JsonProperty("link_url")
    public String link;

    @JsonProperty("name")
    public String name;

    @JsonProperty("id")
    public int    id;
    @JsonProperty("type")
    public int    type;
    @JsonProperty("create_time")
    public String createTime;
    @JsonProperty("is_recommend")
    public int    isRecommend;

    @JsonProperty("sort")
    public int    sort;
    @JsonProperty("create_by")
    public int    createBy;
    @JsonProperty("status")
    public int    status;

    @JsonProperty("thread_id")
    public int    threadId;

    @JsonProperty("url")
    public String url;
    @JsonProperty("img_url")
    public String imgUrl;

    public Image() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.appearanceImg);
        dest.writeString(this.interiorImg);
        dest.writeString(this.link);
        dest.writeString(this.name);
        dest.writeInt(this.id);
        dest.writeInt(this.type);
        dest.writeString(this.createTime);
        dest.writeInt(this.isRecommend);
        dest.writeInt(this.sort);
        dest.writeInt(this.createBy);
        dest.writeInt(this.status);
        dest.writeInt(this.threadId);
        dest.writeString(this.url);
        dest.writeString(this.imgUrl);
    }

    protected Image(Parcel in) {
        this.appearanceImg = in.readString();
        this.interiorImg = in.readString();
        this.link = in.readString();
        this.name = in.readString();
        this.id = in.readInt();
        this.type = in.readInt();
        this.createTime = in.readString();
        this.isRecommend = in.readInt();
        this.sort = in.readInt();
        this.createBy = in.readInt();
        this.status = in.readInt();
        this.threadId = in.readInt();
        this.url = in.readString();
        this.imgUrl = in.readString();
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}
