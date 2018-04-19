package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Jenny
 * Date:2017/5/12
 * E-mail:fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class RideLinePreviewModel implements Parcelable {


    /**
     * id : 203
     * cover : /car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg
     * title : 测试JSON
     * dataline : [{"address":"科园四路路口1","lng":"106.492149","title":"起点","id":37,"ride_id":203,"lat":"29.524883","orders":0},{"address":"重庆铁山坪森林公园2","lng":"106.680238","title":"途经点1","id":38,"ride_id":203,"lat":"29.605508","orders":1},{"address":"长寿古镇3","lng":"107.067668","title":"途经点2","id":39,"ride_id":203,"lat":"29.851229","orders":2},{"address":"重庆市梁平区桂东路4","lng":"107.780882","title":"途经点3","id":40,"ride_id":203,"lat":"30.654126","orders":3},{"address":"万州机场5","lng":"108.436494","title":"终点","id":41,"ride_id":203,"lat":"30.799035","orders":4}]
     * mileage : 1000.00
     */

    public int id;
    public String cover;
    public String title;
    public String mileage;
    public List<RideDataItem> dataline;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.cover);
        dest.writeString(this.title);
        dest.writeString(this.mileage);
        dest.writeList(this.dataline);
    }

    public RideLinePreviewModel() {
    }

    protected RideLinePreviewModel(Parcel in) {
        this.id = in.readInt();
        this.cover = in.readString();
        this.title = in.readString();
        this.mileage = in.readString();
        this.dataline = new ArrayList<RideDataItem>();
        in.readList(this.dataline, RideDataItem.class.getClassLoader());
    }

    public static final Parcelable.Creator<RideLinePreviewModel> CREATOR = new Parcelable.Creator<RideLinePreviewModel>() {
        @Override
        public RideLinePreviewModel createFromParcel(Parcel source) {
            return new RideLinePreviewModel(source);
        }

        @Override
        public RideLinePreviewModel[] newArray(int size) {
            return new RideLinePreviewModel[size];
        }
    };
}
