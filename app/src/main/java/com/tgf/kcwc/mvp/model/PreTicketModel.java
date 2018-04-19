package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：Jenny
 * Date:2017/2/16 14:26
 * E-mail：fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class PreTicketModel {

    /**
     * list : [{"id":6,"tid":4,"can_get_nums":0,"receive_time_limit":"0","ticket_name":"房交会-工作日","type":1,"nums":0,"nums_used":0,"nums_send":0,"qd_id":0,"apply_items":[],"ticket_info":{"id":4,"use_time_start":"2017-01-18 00:00:00","use_time_end":"2017-01-31 00:00:00","remarks":"进场一次作废，注意随身物品安全","price":"0.01","color":"#cfa6a5"}},{"id":5,"tid":3,"can_get_nums":5,"receive_time_limit":"0","ticket_name":"2016年重庆摩托车展会-媒体日","type":2,"nums":1000,"nums_used":0,"nums_send":0,"qd_id":3,"apply_items":[{"field":"address","order":0,"require":1,"name":"详细地址","index":9},{"field":"age","order":0,"require":1,"name":"年龄","index":2},{"field":"brand","order":0,"require":1,"name":"品牌","index":3},{"field":"car","order":0,"require":1,"name":"意向车型","index":4},{"field":"company","order":0,"require":1,"name":"单位","index":5},{"field":"dept","order":0,"require":1,"name":"所在部门","index":6},{"field":"industry","order":0,"require":1,"name":"职业","index":11},{"field":"location","order":0,"require":1,"name":"所在地","index":8},{"field":"name","order":0,"require":1,"name":"姓名","index":0},{"field":"position","order":0,"require":1,"name":"职务","index":7},{"field":"price","order":0,"require":1,"name":"购车预算","index":10},{"field":"sex","order":0,"require":1,"name":"性别","index":1}],"ticket_info":{"id":3,"use_time_start":"2017-01-10 08:30:00","use_time_end":"2017-01-19 17:00:00","remarks":"进场一次作废，注意观展安全","price":"0.01","color":"#9dc19d"}},{"id":4,"tid":3,"can_get_nums":5,"receive_time_limit":"0","ticket_name":"2016年重庆摩托车展会-媒体日","type":1,"nums":0,"nums_used":0,"nums_send":0,"qd_id":0,"apply_items":[],"ticket_info":{"id":3,"use_time_start":"2017-01-10 08:30:00","use_time_end":"2017-01-19 17:00:00","remarks":"进场一次作废，注意观展安全","price":"0.01","color":"#9dc19d"}},{"id":3,"tid":1,"can_get_nums":8,"receive_time_limit":"0","ticket_name":"日票","type":2,"nums":104,"nums_used":0,"nums_send":0,"qd_id":2,"apply_items":[],"ticket_info":{"id":1,"use_time_start":"2017-01-26 00:00:00","use_time_end":"2017-02-28 00:00:00","remarks":"日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票","price":"0.01","color":"#2087d2"}},{"id":2,"tid":1,"can_get_nums":3,"receive_time_limit":"0","ticket_name":"日票","type":2,"nums":3000,"nums_used":1,"nums_send":20,"qd_id":3,"apply_items":[{"field":"price","order":4,"require":1,"name":"购车预算","index":10},{"field":"age","order":3,"require":1,"name":"年龄","index":2},{"field":"sex","order":2,"require":1,"name":"性别","index":1},{"field":"name","order":1,"require":1,"name":"姓名","index":0}],"ticket_info":{"id":1,"use_time_start":"2017-01-26 00:00:00","use_time_end":"2017-02-28 00:00:00","remarks":"日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票","price":"0.01","color":"#2087d2"}},{"id":1,"tid":1,"can_get_nums":8,"receive_time_limit":"12","ticket_name":"日票","type":1,"nums":0,"nums_used":4,"nums_send":0,"qd_id":0,"apply_items":[],"ticket_info":{"id":1,"use_time_start":"2017-01-26 00:00:00","use_time_end":"2017-02-28 00:00:00","remarks":"日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票日票","price":"0.01","color":"#2087d2"}}]
     * count : 6
     */

    public int            count;
    public ArrayList<TicketTypeBean> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TicketTypeBean implements Parcelable {
        /**
         * id : 6
         * tid : 4
         * can_get_nums : 0
         * receive_time_limit : 0
         * ticket_name : 房交会-工作日
         * type : 1
         * nums : 0
         * nums_used : 0
         * nums_send : 0
         * qd_id : 0
         * apply_items : []
         * ticket_info : {"id":4,"use_time_start":"2017-01-18 00:00:00","use_time_end":"2017-01-31 00:00:00","remarks":"进场一次作废，注意随身物品安全","price":"0.01","color":"#cfa6a5"}
         */

        public int            id;
        public int            tid;
        @JsonProperty("can_get_nums")
        public int            canGetNums;
        @JsonProperty("receive_time_limit")
        public String         receiveTimeLimit;
        @JsonProperty("ticket_name")
        public String         ticketName;
        public int            type;
        public int            nums;
        @JsonProperty("nums_used")
        public int            numsUsed;
        @JsonProperty("nums_send")
        public int            numsSend;
        @JsonProperty("qd_id")
        public int            qdId;
        @JsonProperty("ticket_info")
        public TicketInfoBean info;

        @JsonProperty("apply_items")
        public List<Form>     items;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class TicketInfoBean implements Parcelable {
            /**
             * id : 4
             * use_time_start : 2017-01-18 00:00:00
             * use_time_end : 2017-01-31 00:00:00
             * remarks : 进场一次作废，注意随身物品安全
             * price : 0.01
             * color : #cfa6a5
             */

            public int    id;
            @JsonProperty("use_time_start")
            public String useTimeStart;
            @JsonProperty("use_time_end")
            public String useTimeEnd;
            public String remarks;
            public String price;
            public String color;

            public  boolean isSelected;

            public TicketInfoBean() {
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.id);
                dest.writeString(this.useTimeStart);
                dest.writeString(this.useTimeEnd);
                dest.writeString(this.remarks);
                dest.writeString(this.price);
                dest.writeString(this.color);
                dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
            }

            protected TicketInfoBean(Parcel in) {
                this.id = in.readInt();
                this.useTimeStart = in.readString();
                this.useTimeEnd = in.readString();
                this.remarks = in.readString();
                this.price = in.readString();
                this.color = in.readString();
                this.isSelected = in.readByte() != 0;
            }

            public static final Creator<TicketInfoBean> CREATOR = new Creator<TicketInfoBean>() {
                @Override
                public TicketInfoBean createFromParcel(Parcel source) {
                    return new TicketInfoBean(source);
                }

                @Override
                public TicketInfoBean[] newArray(int size) {
                    return new TicketInfoBean[size];
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
            dest.writeInt(this.tid);
            dest.writeInt(this.canGetNums);
            dest.writeString(this.receiveTimeLimit);
            dest.writeString(this.ticketName);
            dest.writeInt(this.type);
            dest.writeInt(this.nums);
            dest.writeInt(this.numsUsed);
            dest.writeInt(this.numsSend);
            dest.writeInt(this.qdId);
            dest.writeParcelable(this.info, flags);
            dest.writeList(this.items);
        }

        public TicketTypeBean() {
        }

        protected TicketTypeBean(Parcel in) {
            this.id = in.readInt();
            this.tid = in.readInt();
            this.canGetNums = in.readInt();
            this.receiveTimeLimit = in.readString();
            this.ticketName = in.readString();
            this.type = in.readInt();
            this.nums = in.readInt();
            this.numsUsed = in.readInt();
            this.numsSend = in.readInt();
            this.qdId = in.readInt();
            this.info = in.readParcelable(TicketInfoBean.class.getClassLoader());
            this.items = new ArrayList<Form>();
            in.readList(this.items, Form.class.getClassLoader());
        }

        public static final Parcelable.Creator<TicketTypeBean> CREATOR = new Parcelable.Creator<TicketTypeBean>() {
            @Override
            public TicketTypeBean createFromParcel(Parcel source) {
                return new TicketTypeBean(source);
            }

            @Override
            public TicketTypeBean[] newArray(int size) {
                return new TicketTypeBean[size];
            }
        };
    }
}
