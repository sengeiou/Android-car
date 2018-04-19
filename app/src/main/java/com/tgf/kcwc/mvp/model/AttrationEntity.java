package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tgf.kcwc.view.richeditor.SEditorData;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/5/6 0006
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AttrationEntity implements Parcelable {
    public int                    org_id =0;
    public int                    type;       //1 景点 2餐馆 3住宿
    public String                 title;
    public ArrayList<SEditorData> content;

    public AttrationEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.org_id);
        dest.writeInt(this.type);
        dest.writeString(this.title);
        dest.writeTypedList(this.content);
    }

    protected AttrationEntity(Parcel in) {
        this.org_id = in.readInt();
        this.type = in.readInt();
        this.title = in.readString();
        this.content = in.createTypedArrayList(SEditorData.CREATOR);
    }

    public static final Creator<AttrationEntity> CREATOR = new Creator<AttrationEntity>() {
        @Override
        public AttrationEntity createFromParcel(Parcel source) {
            return new AttrationEntity(source);
        }

        @Override
        public AttrationEntity[] newArray(int size) {
            return new AttrationEntity[size];
        }
    };
}
