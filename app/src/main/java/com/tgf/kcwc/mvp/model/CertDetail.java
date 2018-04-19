package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author：Jenny
 * Date:2017/2/8 17:13
 * E-mail：fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class CertDetail implements Parcelable {

    /**
     * event_cover : /event/1701/12/47f61e29a887ca180280323d0ef40641.jpg
     * check_fail_reason :
     * event_name : 邓丹第一届汽车展
     * company : 重庆公司
     * is_can_lose : 0
     * is_print : 1
     * cert_sn : 838941851429925795
     * cert_start_time : 2017-01-01
     * name : 哈哈
     * status : 2
     * times : 20
     * is_first : 0
     * times_check : 0
     * profile : cert/user/1.jpg
     * cert_name : 1111
     * check_status : 1
     * brand : 宝马
     * serial_sn : A0001000020
     * cert_end_time : 2017-03-01
     * is_can_print : 1
     * times_type : 1
     * cid : 3
     */

    @JsonProperty("event_cover")
    public String eventCover;
    @JsonProperty("check_fail_reason")
    public String checkFailReason;
    @JsonProperty("event_name")
    public String eventName;
    public String company;
    @JsonProperty("is_can_lose")
    public int isCanLose;
    @JsonProperty("is_print")
    public int isPrint;
    @JsonProperty("cert_sn")
    public String certSN;
    @JsonProperty("cert_start_time")
    public String certStartTime;
    public String name;
    public int status;
    public int times;
    @JsonProperty("is_first")
    public int isFirst;
    @JsonProperty("times_check")
    public int timesCheck;
    public String profile;
    @JsonProperty("cert_name")
    public String certName;
    @JsonProperty("check_status")
    public int checkStatus;
    public String brand;
    @JsonProperty("serial_sn")
    public String serialSN;
    @JsonProperty("cert_end_time")
    public String certEndTime;
    @JsonProperty("is_can_print")
    public int isCanPrint;
    @JsonProperty("times_type")
    public int timesType;
    public int cid;
    public int id;

    public String color;
    @JsonProperty("event_id")
    public int eventId;
    @JsonProperty("print_sn")
    public String printSN;
    @JsonProperty("user_mobile")
    public String mobile;
    @JsonProperty("need_brand")
    public int needBrand;

    @JsonProperty("is_on")
    public int isON;
    public CertDetail() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.eventCover);
        dest.writeString(this.checkFailReason);
        dest.writeString(this.eventName);
        dest.writeString(this.company);
        dest.writeInt(this.isCanLose);
        dest.writeInt(this.isPrint);
        dest.writeString(this.certSN);
        dest.writeString(this.certStartTime);
        dest.writeString(this.name);
        dest.writeInt(this.status);
        dest.writeInt(this.times);
        dest.writeInt(this.isFirst);
        dest.writeInt(this.timesCheck);
        dest.writeString(this.profile);
        dest.writeString(this.certName);
        dest.writeInt(this.checkStatus);
        dest.writeString(this.brand);
        dest.writeString(this.serialSN);
        dest.writeString(this.certEndTime);
        dest.writeInt(this.isCanPrint);
        dest.writeInt(this.timesType);
        dest.writeInt(this.cid);
        dest.writeInt(this.id);
        dest.writeString(this.color);
    }

    protected CertDetail(Parcel in) {
        this.eventCover = in.readString();
        this.checkFailReason = in.readString();
        this.eventName = in.readString();
        this.company = in.readString();
        this.isCanLose = in.readInt();
        this.isPrint = in.readInt();
        this.certSN = in.readString();
        this.certStartTime = in.readString();
        this.name = in.readString();
        this.status = in.readInt();
        this.times = in.readInt();
        this.isFirst = in.readInt();
        this.timesCheck = in.readInt();
        this.profile = in.readString();
        this.certName = in.readString();
        this.checkStatus = in.readInt();
        this.brand = in.readString();
        this.serialSN = in.readString();
        this.certEndTime = in.readString();
        this.isCanPrint = in.readInt();
        this.timesType = in.readInt();
        this.cid = in.readInt();
        this.id = in.readInt();
        this.color = in.readString();
    }

    public static final Creator<CertDetail> CREATOR = new Creator<CertDetail>() {
        @Override
        public CertDetail createFromParcel(Parcel source) {
            return new CertDetail(source);
        }

        @Override
        public CertDetail[] newArray(int size) {
            return new CertDetail[size];
        }
    };
}
