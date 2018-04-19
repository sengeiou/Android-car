package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：Jenny
 * Date:2016/12/29 11:11
 * E-mail：fishloveqin@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ticket implements Parcelable {

    @JsonProperty("id")
    public int id;
    public String name;
    @JsonProperty("status")
    public int status;
    public int type;
    @JsonProperty("use_time_start")
    public String beginTime;
    @JsonProperty("use_time_end")
    public String endTime;
    @JsonProperty("price")
    public String price;
    @JsonProperty("nums")
    public int nums;
    public List<String> codes;

    @JsonProperty("color")
    public String color;

    @JsonProperty("total_price")
    public String totalPrice;

    public String remarks;

    @JsonProperty("price_discount")
    public String discount;


    @JsonProperty("is_can_refund")
    public int isCanRefund;


    @JsonProperty("code_list")
    public List<Code> listCodes;

    /**
     * serial_sn : W01671000016
     * user_coupon_code : --
     * status : 0
     * checkout_time : 0000-00-00 00:00:00
     * apply_refund_time : 0000-00-00 00:00:00
     * refund_time : 0000-00-00 00:00:00
     * receive_time : 0000-00-00 00:00:00
     * price_ori : 0.01
     * price : 0.01
     * text : 待使用
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Code implements Parcelable {


        /**
         * id : 10451
         * name : 特票
         * serial_sn : W01671000016
         * user_coupon_code : --
         * status : 0
         * checkout_time : 0000-00-00 00:00:00
         * apply_refund_time : 0000-00-00 00:00:00
         * refund_time : 0000-00-00 00:00:00
         * receive_time : 0000-00-00 00:00:00
         * price_discount : 0.00
         * price_ori : 0.01
         * price : 0.01
         * text : 待使用
         */

        public int id;
        public String name;
        @JsonProperty("serial_sn")
        public String serialSN;
        @JsonProperty("user_coupon_code")
        public String userCouponCode;
        public int status;
        @JsonProperty("checkout_time")
        public String checkoutTime;
        @JsonProperty("apply_refund_time")
        public String applyRefundTime;

        @JsonProperty("give_time")
        public String giveTime;
        @JsonProperty("refund_time")
        public String refundTime;
        @JsonProperty("receive_time")
        public String receiveTime;
        @JsonProperty("price_discount")
        public String priceDiscount;
        @JsonProperty("price_ori")
        public String priceOri;
        public double price;
        public String text;

        public boolean isSelected;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.name);
            dest.writeString(this.serialSN);
            dest.writeString(this.userCouponCode);
            dest.writeInt(this.status);
            dest.writeString(this.checkoutTime);
            dest.writeString(this.applyRefundTime);
            dest.writeString(this.refundTime);
            dest.writeString(this.receiveTime);
            dest.writeString(this.priceDiscount);
            dest.writeString(this.priceOri);
            dest.writeDouble(this.price);
            dest.writeString(this.text);
            dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        }

        public Code() {
        }

        protected Code(Parcel in) {
            this.id = in.readInt();
            this.name = in.readString();
            this.serialSN = in.readString();
            this.userCouponCode = in.readString();
            this.status = in.readInt();
            this.checkoutTime = in.readString();
            this.applyRefundTime = in.readString();
            this.refundTime = in.readString();
            this.receiveTime = in.readString();
            this.priceDiscount = in.readString();
            this.priceOri = in.readString();
            this.price = in.readDouble();
            this.text = in.readString();
            this.isSelected = in.readByte() != 0;
        }

        public static final Creator<Code> CREATOR = new Creator<Code>() {
            @Override
            public Code createFromParcel(Parcel source) {
                return new Code(source);
            }

            @Override
            public Code[] newArray(int size) {
                return new Code[size];
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
        dest.writeString(this.name);
        dest.writeInt(this.type);
        dest.writeString(this.beginTime);
        dest.writeString(this.endTime);
        dest.writeString(this.price);
        dest.writeInt(this.nums);
        dest.writeStringList(this.codes);
        dest.writeString(this.color);
        dest.writeString(this.totalPrice);
        dest.writeString(this.remarks);
        dest.writeString(this.discount);
        dest.writeInt(this.isCanRefund);
        dest.writeList(this.listCodes);
    }

    public Ticket() {
    }

    protected Ticket(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.type = in.readInt();
        this.beginTime = in.readString();
        this.endTime = in.readString();
        this.price = in.readString();
        this.nums = in.readInt();
        this.codes = in.createStringArrayList();
        this.color = in.readString();
        this.totalPrice = in.readString();
        this.remarks = in.readString();
        this.discount = in.readString();
        this.isCanRefund = in.readInt();
        this.listCodes = new ArrayList<Code>();
        in.readList(this.listCodes, Code.class.getClassLoader());
    }

    public static final Parcelable.Creator<Ticket> CREATOR = new Parcelable.Creator<Ticket>() {
        @Override
        public Ticket createFromParcel(Parcel source) {
            return new Ticket(source);
        }

        @Override
        public Ticket[] newArray(int size) {
            return new Ticket[size];
        }
    };
}
