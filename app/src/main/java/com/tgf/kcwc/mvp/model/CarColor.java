package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author：Jenny
 * Date:2017/4/5 17:37
 * E-mail：fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class CarColor implements Parcelable {

    /**
     * color_type : out
     * color_name : 卡其色
     * color_value : #D3865B
     * id : 188
     */

    @JsonProperty("color_type")
    public String type;
    @JsonProperty("color_name")
    public String name;
    @JsonProperty("color_value")
    public String value;
    public int    id;

    public boolean isSelected;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.name);
        dest.writeString(this.value);
        dest.writeInt(this.id);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    public CarColor() {
    }

    protected CarColor(Parcel in) {
        this.type = in.readString();
        this.name = in.readString();
        this.value = in.readString();
        this.id = in.readInt();
        this.isSelected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<CarColor> CREATOR = new Parcelable.Creator<CarColor>() {
        @Override
        public CarColor createFromParcel(Parcel source) {
            return new CarColor(source);
        }

        @Override
        public CarColor[] newArray(int size) {
            return new CarColor[size];
        }
    };
}
