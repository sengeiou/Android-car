package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author：Jenny
 * Date:2017/1/9 15:53
 * E-mail：fishloveqin@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketItem implements Parcelable {
    @JsonProperty("serial_sn")
    public String serialSN;
    /**
     * 0.未使用 1.已使用 2已失效 3.已过期 4已取消 5.退款中 6.已退款 7退款失败
     */
    @JsonProperty("status")
    public int status;
    @JsonProperty("ticket_sn")
    public String ticketSN;
    @JsonProperty("id")
    public int id;
    @JsonProperty("checkout_time")
    public String checkoutTime;

    @JsonProperty("apply_refund_time")
    public String applyRefundTime;

    @JsonProperty("give_time")
    public String giveTime;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.serialSN);
        dest.writeInt(this.status);
        dest.writeString(this.ticketSN);
        dest.writeInt(this.id);
        dest.writeString(this.checkoutTime);
        dest.writeString(this.applyRefundTime);
    }

    public TicketItem() {
    }

    protected TicketItem(Parcel in) {
        this.serialSN = in.readString();
        this.status = in.readInt();
        this.ticketSN = in.readString();
        this.id = in.readInt();
        this.checkoutTime = in.readString();
        this.applyRefundTime = in.readString();
    }

    public static final Parcelable.Creator<TicketItem> CREATOR = new Parcelable.Creator<TicketItem>() {
        @Override
        public TicketItem createFromParcel(Parcel source) {
            return new TicketItem(source);
        }

        @Override
        public TicketItem[] newArray(int size) {
            return new TicketItem[size];
        }
    };
}
