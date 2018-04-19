package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Jenny
 * Date:2017/5/15
 * E-mail:fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserHomeDataModel implements Parcelable {

    /**
     * id : 109
     * nickname : A let1
     * avatar : http://wx.qlogo.cn/mmopen/qd3u5IHSYT8GSiaxnRIZmzuWNt2LKmpbc4mxFnNTzrxpVwbrCel1PugBxQ6icnNlzrrYZtiaicEXWibCpkrvAt3tuEooHyFficSQl0/0
     * sex : 1
     * level : 0
     * bj_image : /project/1704/24/90cf6ec3d04eabc9c02bd657d59fbdec.png
     * is_doyen : 0
     * is_model : 0
     * is_master : 0
     * follow_num : 5
     * fans_num : 0
     * points_moto : 0
     * points_kcwc : 0
     * exp : 0
     * money : 0.00
     * icon_logo :
     * button : [{"type":"ticket","name":"门票","status":1},{"type":"cert","name":"证件","status":1},{"type":"coupon","name":"代金券","status":1},{"type":"custom_made","name":"定制","status":1},{"type":"order","name":"我要看","status":1},{"type":"cycle","name":"开车去","status":1},{"type":"play","name":"请你玩","status":1},{"type":"goods","name":"车主自售","status":1},{"type":"roadbook","name":"路书","status":1},{"type":"group","name":"群组","status":1},{"type":"collect","name":"收藏","status":1},{"type":"evaluate","name":"我的评测","status":0}]
     * my_car : 0
     * dynamic : 4
     * line : 7
     * ride_data : {"total":3,"time":3,"mileage":7606.5,"duration":"11.7","max":1000,"max_duration":"06:10:22"}
     */

    public int              id;
    public String           nickname;
    public String           avatar;
    public int              sex;
    public int              level;
    @JsonProperty("bj_image")
    public String           cover;
    @JsonProperty("is_doyen")
    public int              isDoyen;
    @JsonProperty("is_model")
    public int              isModel;
    @JsonProperty("is_master")
    public int              isMaster;
    @JsonProperty("follow_num")
    public int              followNum;
    @JsonProperty("fans_num")
    public int              fansNum;
    @JsonProperty("points_moto")
    public int              pointsMoto;
    @JsonProperty("points_kcwc")
    public int              pointsKcwc;
    @JsonProperty("org_id")
    public int              orgId;
    @JsonProperty("kcwc_business_points")
    public String              kcwcBusinessPoints;
    @JsonProperty("kcwc_business_exp")
    public String              kcwcBusinessExp;
    public int              exp;
    public String           money;
    public String           logo;
    @JsonProperty("my_car")
    public int              myCar;
    public int              dynamic;
    public int              line;
    @JsonProperty("ride_data")
    public RideDataBean     rideData;
    public List<ButtonBean> button;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RideDataBean implements Parcelable {
        /**
         * total : 3
         * time : 3
         * mileage : 7606.5
         * duration : 11.7
         * max : 1000
         * max_duration : 06:10:22
         */

        public int    total;
        public int    time;
        public double mileage;
        public String duration;
        public int    max;
        @JsonProperty("max_duration")
        public String maxTime;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.total);
            dest.writeInt(this.time);
            dest.writeDouble(this.mileage);
            dest.writeString(this.duration);
            dest.writeInt(this.max);
            dest.writeString(this.maxTime);
        }

        public RideDataBean() {
        }

        protected RideDataBean(Parcel in) {
            this.total = in.readInt();
            this.time = in.readInt();
            this.mileage = in.readDouble();
            this.duration = in.readString();
            this.max = in.readInt();
            this.maxTime = in.readString();
        }

        public static final Creator<RideDataBean> CREATOR = new Creator<RideDataBean>() {
            @Override
            public RideDataBean createFromParcel(Parcel source) {
                return new RideDataBean(source);
            }

            @Override
            public RideDataBean[] newArray(int size) {
                return new RideDataBean[size];
            }
        };
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ButtonBean implements Parcelable {
        /**
         * type : ticket
         * name : 门票
         * status : 1
         */

        public String type;
        public String name;
        public int    status;
        @JsonProperty("is_business")
        public int    isBusiness;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.type);
            dest.writeString(this.name);
            dest.writeInt(this.status);
            dest.writeInt(this.isBusiness);
        }
        protected ButtonBean(){}
        protected ButtonBean(Parcel in) {
            this.type = in.readString();
            this.name = in.readString();
            this.status = in.readInt();
            this.isBusiness = in.readInt();
        }

        public static final Creator<ButtonBean> CREATOR = new Creator<ButtonBean>() {
            @Override
            public ButtonBean createFromParcel(Parcel source) {
                return new ButtonBean(source);
            }

            @Override
            public ButtonBean[] newArray(int size) {
                return new ButtonBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.nickname);
        dest.writeString(this.avatar);
        dest.writeInt(this.sex);
        dest.writeInt(this.level);
        dest.writeString(this.cover);
        dest.writeInt(this.isDoyen);
        dest.writeInt(this.isModel);
        dest.writeInt(this.isMaster);
        dest.writeInt(this.followNum);
        dest.writeInt(this.fansNum);
        dest.writeInt(this.pointsMoto);
        dest.writeInt(this.pointsKcwc);
        dest.writeInt(this.exp);
        dest.writeString(this.money);
        dest.writeString(this.logo);
        dest.writeInt(this.myCar);
        dest.writeInt(this.dynamic);
        dest.writeInt(this.line);
        dest.writeParcelable(this.rideData, flags);
        dest.writeList(this.button);
    }

    public UserHomeDataModel() {
    }

    protected UserHomeDataModel(Parcel in) {
        this.id = in.readInt();
        this.nickname = in.readString();
        this.avatar = in.readString();
        this.sex = in.readInt();
        this.level = in.readInt();
        this.cover = in.readString();
        this.isDoyen = in.readInt();
        this.isModel = in.readInt();
        this.isMaster = in.readInt();
        this.followNum = in.readInt();
        this.fansNum = in.readInt();
        this.pointsMoto = in.readInt();
        this.pointsKcwc = in.readInt();
        this.exp = in.readInt();
        this.money = in.readString();
        this.logo = in.readString();
        this.myCar = in.readInt();
        this.dynamic = in.readInt();
        this.line = in.readInt();
        this.rideData = in.readParcelable(RideDataBean.class.getClassLoader());
        this.button = new ArrayList<ButtonBean>();
        in.readList(this.button, ButtonBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<UserHomeDataModel> CREATOR = new Parcelable.Creator<UserHomeDataModel>() {
        @Override
        public UserHomeDataModel createFromParcel(Parcel source) {
            return new UserHomeDataModel(source);
        }

        @Override
        public UserHomeDataModel[] newArray(int size) {
            return new UserHomeDataModel[size];
        }
    };
}
