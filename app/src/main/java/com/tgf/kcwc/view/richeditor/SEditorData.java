package com.tgf.kcwc.view.richeditor;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * Created by hitomi on 2016/6/3.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SEditorData implements Parcelable, Serializable {

    public String           inputStr;
    @JsonIgnore
    private String           imagePath;
    public String           imageUrl;
    private transient Bitmap bitmap;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getInputStr() {
        return inputStr;
    }

    public void setInputStr(String inputStr) {
        this.inputStr = inputStr;
    }

    @JsonIgnore
    @JsonSerialize
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public String toString() {
        return "SEditorData{" + "inputStr='" + inputStr + '\'' + ", imagePath='" + imagePath + '\''
               + ", imageUrl='" + imageUrl + '\'' + ", bitmap=" + bitmap + '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.inputStr);
        dest.writeString(this.imagePath);
        dest.writeString(this.imageUrl);
    }

    public SEditorData() {
    }

    protected SEditorData(Parcel in) {
        this.inputStr = in.readString();
        this.imagePath = in.readString();
        this.imageUrl = in.readString();
    }

    public static final Parcelable.Creator<SEditorData> CREATOR = new Parcelable.Creator<SEditorData>() {
        @Override
        public SEditorData createFromParcel(Parcel source) {
            return new SEditorData(source);
        }

        @Override
        public SEditorData[] newArray(int size) {
            return new SEditorData[size];
        }
    };
}
