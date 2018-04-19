package com.tgf.kcwc.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tgf.kcwc.view.richeditor.SEditorData;

import java.io.Serializable;
import java.util.List;

/**
 * Author：Jenny
 * Date:2017/2/24 10:58
 * E-mail：fishloveqin@gmail.com
 * 富文本编辑器数据结构实体类
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RichEditorEntity implements Parcelable, Serializable {

    public List<SEditorData> mEditorDatas;
    public String            title;

    protected RichEditorEntity(Parcel in) {
        mEditorDatas = in.createTypedArrayList(SEditorData.CREATOR);
        title = in.readString();
    }

    public RichEditorEntity() {
    }

    public static final Creator<RichEditorEntity> CREATOR = new Creator<RichEditorEntity>() {
        @Override
        public RichEditorEntity createFromParcel(Parcel in) {
            return new RichEditorEntity(in);
        }

        @Override
        public RichEditorEntity[] newArray(int size) {
            return new RichEditorEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mEditorDatas);
        dest.writeString(title);
    }
}
