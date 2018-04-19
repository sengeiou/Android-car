package com.tgf.kcwc.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tgf.kcwc.mvp.model.AttrationEntity;
import com.tgf.kcwc.view.richeditor.SEditorData;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/5/6 0006
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeEntity implements Parcelable {
        public int ride_node_id =193;
        public ArrayList<SEditorData> desc;
        @JsonProperty("content_list")
        public ArrayList<AttrationEntity> book_line_content_list;  //
        public String   adds;
        public String   id="0";

        @Override
        public int describeContents() {
                return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.ride_node_id);
                dest.writeTypedList(this.desc);
                dest.writeTypedList(this.book_line_content_list);
                dest.writeString(this.adds);
                dest.writeString(this.id);
        }

        public NodeEntity() {
        }

        protected NodeEntity(Parcel in) {
                this.ride_node_id = in.readInt();
                this.desc = in.createTypedArrayList(SEditorData.CREATOR);
                this.book_line_content_list = in.createTypedArrayList(AttrationEntity.CREATOR);
                this.adds = in.readString();
                this.id = in.readString();
        }

        public static final Parcelable.Creator<NodeEntity> CREATOR = new Parcelable.Creator<NodeEntity>() {
                @Override
                public NodeEntity createFromParcel(Parcel source) {
                        return new NodeEntity(source);
                }

                @Override
                public NodeEntity[] newArray(int size) {
                        return new NodeEntity[size];
                }
        };
}
