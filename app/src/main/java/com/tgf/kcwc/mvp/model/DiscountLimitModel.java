package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/4/25 0025
 * E-mail:hekescott@qq.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscountLimitModel {
    @JsonProperty("list")
    public List<LimitDiscountItem> mLimitDiscountItemList;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LimitOrg implements Parcelable {
        public int    id;
        public String name;
        public int    points;
        @JsonProperty("end_time")
        public String endTime;
        public int    rate;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.name);
            dest.writeInt(this.points);
            dest.writeString(this.endTime);
            dest.writeInt(this.rate);
        }

        public LimitOrg() {
        }

        protected LimitOrg(Parcel in) {
            this.id = in.readInt();
            this.name = in.readString();
            this.points = in.readInt();
            this.endTime = in.readString();
            this.rate = in.readInt();
        }

        public static final Parcelable.Creator<LimitOrg> CREATOR = new Parcelable.Creator<LimitOrg>() {
            @Override
            public LimitOrg createFromParcel(Parcel source) {
                return new LimitOrg(source);
            }

            @Override
            public LimitOrg[] newArray(int size) {
                return new LimitOrg[size];
            }
        };
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LimitDiscountItem {
        @JsonProperty("car_id")
        public int            carId;
        @JsonProperty("car_name")
        public String         carName;
        @JsonProperty("guide_price")
        public String         guidePrice;
        @JsonProperty("factory_id")
        public int            factoryId;
        @JsonProperty("car_series_name")
        public String         carSeriesName;
        public double            rate;
        @JsonProperty("rate_text")
        public String            rateText;
        @JsonProperty("rate_guide_price")
        public double            rateGuidePrice;
        @JsonProperty("org_id")
        public int            orgId;
        @JsonProperty("org_name")
        public String         orgName;
        @JsonProperty("pri_id")
        public int            priId;
        @JsonProperty("start_time")
        public String         startTime;
        @JsonProperty("end_time")
        public String         endTime;
        @JsonProperty("factory_name")
        public String         factoryName;
        @JsonProperty("car_series_img")
        public String         carSeriesImg;
        @JsonProperty("create_time")
        public String         createTime;
        public String         title;
        public int            special;
        @JsonProperty("org_count")
        public int            orgCount;
        @JsonProperty("org")
        public ArrayList<LimitOrg> orgList;

    }
}
