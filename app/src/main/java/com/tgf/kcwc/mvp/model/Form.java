package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Author：Jenny
 * Date:2017/2/16 14:48
 * E-mail：fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Form implements Parcelable {

    public String field;

    public int index;

    public int require;

    public int order;

    public int cartype;
    public String name;

    public String desc = "";//表单编辑的数据

    public int layoutId;//布局Id;

    public int viewTypeId;

    public boolean isEnabled = true;

    public String hintContent = "";

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.field);
        dest.writeInt(this.index);
        dest.writeInt(this.require);
        dest.writeInt(this.order);
        dest.writeString(this.name);
        dest.writeString(this.desc);
        dest.writeInt(this.layoutId);
        dest.writeInt(this.viewTypeId);
    }

    public Form() {
    }

    protected Form(Parcel in) {
        this.field = in.readString();
        this.index = in.readInt();
        this.require = in.readInt();
        this.order = in.readInt();
        this.name = in.readString();
        this.desc = in.readString();
        this.layoutId = in.readInt();
        this.viewTypeId = in.readInt();
    }

    public static final Parcelable.Creator<Form> CREATOR = new Parcelable.Creator<Form>() {
        @Override
        public Form createFromParcel(Parcel source) {
            return new Form(source);
        }

        @Override
        public Form[] newArray(int size) {
            return new Form[size];
        }
    };
}
