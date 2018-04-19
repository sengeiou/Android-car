package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author：Jenny
 * Date:2017/1/5 20:43
 * E-mail：fishloveqin@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order implements Parcelable {
    @JsonProperty("amount")
    public String amount;
    @JsonProperty("pay_status")
    public int status;
    @JsonProperty("event_id")
    public int eventId;
    @JsonProperty("event_name")
    public String eventName;
    @JsonProperty("order_sn")
    public String orderSN;
    @JsonProperty("amount_ori")
    public String amountOri;

    @JsonProperty("order_id")
    public String orderId;

    @JsonProperty("order_title")
    public String orderTitle;
    @JsonProperty("amount_discount")
    public double amountDiscount;

    @JsonProperty("amount_pay")
    public float amountPay;

    @JsonProperty("event_cover")
    public String cover;

    @JsonProperty("media_type")
    public int mediaType;

    @JsonProperty("pay_time")
    public String payTime;

    @JsonProperty("create_time")
    public String createTime;
    @JsonProperty("pay_type")
    public int payType;

    public int type;

    @JsonProperty("user_coupon_code")
    public String userCouponCode;

    @JsonProperty("coupon_code")
    public String couponCode;

    @JsonProperty("refund_set")
    public int refundSet;

    public Order() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.amount);
        dest.writeInt(this.status);
        dest.writeInt(this.eventId);
        dest.writeString(this.eventName);
        dest.writeString(this.orderSN);
        dest.writeString(this.amountOri);
        dest.writeString(this.orderId);
        dest.writeDouble(this.amountDiscount);
        dest.writeFloat(this.amountPay);
        dest.writeString(this.cover);
        dest.writeInt(this.mediaType);
        dest.writeString(this.payTime);
        dest.writeString(this.createTime);
        dest.writeInt(this.payType);
        dest.writeInt(this.type);
        dest.writeString(this.userCouponCode);
        dest.writeString(this.couponCode);
        dest.writeInt(this.refundSet);
    }

    protected Order(Parcel in) {
        this.amount = in.readString();
        this.status = in.readInt();
        this.eventId = in.readInt();
        this.eventName = in.readString();
        this.orderSN = in.readString();
        this.amountOri = in.readString();
        this.orderId = in.readString();
        this.amountDiscount = in.readDouble();
        this.amountPay = in.readFloat();
        this.cover = in.readString();
        this.mediaType = in.readInt();
        this.payTime = in.readString();
        this.createTime = in.readString();
        this.payType = in.readInt();
        this.type = in.readInt();
        this.userCouponCode = in.readString();
        this.couponCode = in.readString();
        this.refundSet = in.readInt();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
