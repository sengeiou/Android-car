package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Jenny
 * Date:2017/5/20
 * E-mail:fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class HobbyTag implements Parcelable {

    /**
     * id : 2
     * name : 一级分类2
     * parent_id : 0
     * second : [{"id":4,"name":"二级分类2","parent_id":2,"checked":0},{"id":11,"name":"二级分类1","parent_id":2,"checked":0},{"id":12,"name":"二级分类3","parent_id":2,"checked":0}]
     */

    public int            id;
    public String         name;
    @JsonProperty("parent_id")
    public int            parentId;

    public int            checked;
    public List<HobbyTag> second;

    public  boolean isSelected=false;

    public  boolean isAll=false;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.parentId);
        dest.writeInt(this.checked);
        dest.writeList(this.second);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isAll ? (byte) 1 : (byte) 0);
    }

    public HobbyTag() {
    }

    protected HobbyTag(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.parentId = in.readInt();
        this.checked = in.readInt();
        this.second = new ArrayList<HobbyTag>();
        in.readList(this.second, HobbyTag.class.getClassLoader());
        this.isSelected = in.readByte() != 0;
        this.isAll = in.readByte() != 0;
    }

    public static final Parcelable.Creator<HobbyTag> CREATOR = new Parcelable.Creator<HobbyTag>() {
        @Override
        public HobbyTag createFromParcel(Parcel source) {
            return new HobbyTag(source);
        }

        @Override
        public HobbyTag[] newArray(int size) {
            return new HobbyTag[size];
        }
    };
}
