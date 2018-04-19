package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/1/23 0023
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrgModel implements Parcelable {
    public double distance;
    public String full_name;
    public int id;
    public String name;
    public String logo;
    public String address;
    public String tel;
    public String longitude;
    public String latitude;
    public int is_coupon;
    public int is_discount;
    public int is_package;
    public double price;
    public ArrayList<SalerList> salelist;

    public static class SalerList {
        public int id;
        public String real_name;
        public String nickname;
        public String avatar;
        public String star;
    }

    public OrgModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.distance);
        dest.writeString(this.full_name);
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.logo);
        dest.writeString(this.address);
        dest.writeString(this.tel);
        dest.writeString(this.longitude);
        dest.writeString(this.latitude);
        dest.writeInt(this.is_coupon);
        dest.writeInt(this.is_discount);
        dest.writeInt(this.is_package);
        dest.writeDouble(this.price);
        dest.writeList(this.salelist);
    }

    protected OrgModel(Parcel in) {
        this.distance = in.readDouble();
        this.full_name = in.readString();
        this.id = in.readInt();
        this.name = in.readString();
        this.logo = in.readString();
        this.address = in.readString();
        this.tel = in.readString();
        this.longitude = in.readString();
        this.latitude = in.readString();
        this.is_coupon = in.readInt();
        this.is_discount = in.readInt();
        this.is_package = in.readInt();
        this.price = in.readDouble();
        this.salelist = new ArrayList<SalerList>();
        in.readList(this.salelist, SalerList.class.getClassLoader());
    }

    public static final Creator<OrgModel> CREATOR = new Creator<OrgModel>() {
        @Override
        public OrgModel createFromParcel(Parcel source) {
            return new OrgModel(source);
        }

        @Override
        public OrgModel[] newArray(int size) {
            return new OrgModel[size];
        }
    };
}
