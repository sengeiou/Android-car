package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author：Jenny
 * Date:2017/1/9 15:46
 * E-mail：fishloveqin@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyTicketListModel {

    public static class MyTicket implements Parcelable {

        @JsonProperty("event_info")
        public Sense senseInfo;

        @JsonProperty("ticket_info")
        public Ticket ticketInfo;

        @JsonProperty("list")
        public List<TicketItem> ticketItems;

        @JsonProperty("type")
        public int type;
        @JsonProperty("id")
        public int id;
        @JsonProperty("ordert_info")
        public Order orderInfo;

        @JsonProperty("org_info")
        public OrgModel orgInfo;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.senseInfo, flags);
            dest.writeParcelable(this.ticketInfo, flags);
            dest.writeTypedList(this.ticketItems);
            dest.writeInt(this.type);
            dest.writeInt(this.id);
            dest.writeParcelable(this.orderInfo, flags);
            dest.writeParcelable(this.orgInfo, flags);
        }

        public MyTicket() {
        }

        protected MyTicket(Parcel in) {
            this.senseInfo = in.readParcelable(Sense.class.getClassLoader());
            this.ticketInfo = in.readParcelable(Ticket.class.getClassLoader());
            this.ticketItems = in.createTypedArrayList(TicketItem.CREATOR);
            this.type = in.readInt();
            this.id = in.readInt();
            this.orderInfo = in.readParcelable(Order.class.getClassLoader());
            this.orgInfo = in.readParcelable(OrgModel.class.getClassLoader());
        }

        public static final Parcelable.Creator<MyTicket> CREATOR = new Parcelable.Creator<MyTicket>() {
            @Override
            public MyTicket createFromParcel(Parcel source) {
                return new MyTicket(source);
            }

            @Override
            public MyTicket[] newArray(int size) {
                return new MyTicket[size];
            }
        };
    }

    @JsonProperty("list")
    public List<MyTicket> tickets;

    @JsonProperty("count")
    public int count;

    @JsonProperty("qrcode_gif")
    public String gif;

    @JsonProperty("is_have_used")
    public int isUsed;


}
