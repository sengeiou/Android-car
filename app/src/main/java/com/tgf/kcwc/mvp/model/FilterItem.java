package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author：Jenny
 * Date:2017/2/13 20:50
 * E-mail：fishloveqin@gmail.com
 */

public class FilterItem implements Parcelable {

    public int     id;

    public String  name;

    public String  icon;

    public int     pid;       //父级id

    public String  title;

    public boolean isSelected;

    public String  namecode;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.icon);
        dest.writeInt(this.pid);
        dest.writeString(this.title);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    public FilterItem() {
    }

    protected FilterItem(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.icon = in.readString();
        this.pid = in.readInt();
        this.title = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<FilterItem> CREATOR = new Parcelable.Creator<FilterItem>() {
        @Override
        public FilterItem createFromParcel(Parcel source) {
            return new FilterItem(source);
        }

        @Override
        public FilterItem[] newArray(int size) {
            return new FilterItem[size];
        }
    };
}
