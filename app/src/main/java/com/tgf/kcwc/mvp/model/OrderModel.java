package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：Jenny
 * Date:2017/1/5 20:47
 * E-mail：fishloveqin@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderModel implements Parcelable {

    @JsonProperty("details")
    public Order details;
    @JsonProperty("coupon")
    public List<BaseCoupon> coupons;
    @JsonProperty("ticket_list")
    public List<Ticket> tickets;

    @JsonProperty("user")
    public Account.UserInfo user;

    @JsonProperty("handout")
    public Handout handout;

    public static class Handout implements Parcelable {

        public String mobile;

        public String time;

        public int id;

        public String nickname;

        @JsonProperty("real_name")
        public String realName;

        public int nums;

        @JsonProperty("organizationl_name")
        public String organizationlName;

        @JsonProperty("receive_time_limit")
        public String receiveTimeLimit;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mobile);
            dest.writeString(this.time);
            dest.writeInt(this.id);
            dest.writeString(this.nickname);
            dest.writeString(this.realName);
            dest.writeInt(this.nums);
            dest.writeString(this.organizationlName);
            dest.writeString(this.receiveTimeLimit);
        }

        public Handout() {
        }

        protected Handout(Parcel in) {
            this.mobile = in.readString();
            this.time = in.readString();
            this.id = in.readInt();
            this.nickname = in.readString();
            this.realName = in.readString();
            this.nums = in.readInt();
            this.organizationlName = in.readString();
            this.receiveTimeLimit = in.readString();
        }

        public static final Creator<Handout> CREATOR = new Creator<Handout>() {
            @Override
            public Handout createFromParcel(Parcel source) {
                return new Handout(source);
            }

            @Override
            public Handout[] newArray(int size) {
                return new Handout[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.details, flags);
        dest.writeList(this.coupons);
        dest.writeList(this.tickets);
        dest.writeSerializable(this.user);
        dest.writeParcelable(this.handout, flags);
    }

    public OrderModel() {
    }

    protected OrderModel(Parcel in) {
        this.details = in.readParcelable(Order.class.getClassLoader());
        this.coupons = new ArrayList<BaseCoupon>();
        in.readList(this.coupons, BaseCoupon.class.getClassLoader());
        this.tickets = new ArrayList<Ticket>();
        in.readList(this.tickets, Ticket.class.getClassLoader());
        this.user = (Account.UserInfo) in.readSerializable();
        this.handout = in.readParcelable(Handout.class.getClassLoader());
    }

    public static final Parcelable.Creator<OrderModel> CREATOR = new Parcelable.Creator<OrderModel>() {
        @Override
        public OrderModel createFromParcel(Parcel source) {
            return new OrderModel(source);
        }

        @Override
        public OrderModel[] newArray(int size) {
            return new OrderModel[size];
        }
    };
}
