package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：Jenny
 * Date:2016/12/13 11:04
 * E-mail：fishloveqin@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Brand extends BaseIndexPinyinBean implements Parcelable {

    /**
     * id : 0
     * vehicle_type : 
     * name : 
     */

    public int           id;
    public int           pid;
    @JsonProperty("vehicle_type")
    public String        vehicleType;
    public String        name;

    @JsonProperty("letter")
    public String        letter;

    @JsonProperty("brand_name")
    public String        brandName;
    @JsonProperty("brand_id")
    public int           brandId;
    @JsonProperty("real_factory_id")
    public int           factoryId;
    @JsonProperty("brand_logo")
    public String        brandLogo;
    @JsonProperty("car_list")
    public List<CarBean> carList;
    @JsonProperty("brand_star")
    public String        star;
    @JsonProperty("create_time")
    public String        createTime;
    public boolean       isSplitLine;
    public boolean       isSelected;
    public boolean       isTop;      //是否是最上面的 不需要被转化成拼音的

    public List<Beauty>  beautlist;  //该品牌下的模特

    public String        releaseTime;
    public String        stars;
    @JsonProperty("booth_id")
    public String        boothId; //展台ID
    @JsonProperty("hall_id")
    public String        hallId; //展馆ID

    @JsonProperty("area_name")
    public String        areaName;
    @JsonProperty("adcode")
    public String        adcode;

    @JsonProperty("area_id")
    public int           areaId;

    @Override
    public String getTarget() {
        return brandName;
    }

    @Override
    public boolean isNeedToPinyin() {
        return false;
    }

    @Override
    public boolean isShowSuspension() {
        return !isTop;
    }

    public Brand() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.pid);
        dest.writeString(this.vehicleType);
        dest.writeString(this.name);
        dest.writeString(this.letter);
        dest.writeString(this.brandName);
        dest.writeInt(this.brandId);
        dest.writeInt(this.factoryId);
        dest.writeString(this.brandLogo);
        dest.writeTypedList(this.carList);
        dest.writeString(this.star);
        dest.writeString(this.createTime);
        dest.writeByte(this.isSplitLine ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isTop ? (byte) 1 : (byte) 0);
        dest.writeList(this.beautlist);
        dest.writeString(this.releaseTime);
        dest.writeString(this.stars);
        dest.writeString(this.boothId);
        dest.writeString(this.hallId);
        dest.writeString(this.areaName);
        dest.writeString(this.adcode);
        dest.writeInt(this.areaId);
    }

    protected Brand(Parcel in) {
        this.id = in.readInt();
        this.pid = in.readInt();
        this.vehicleType = in.readString();
        this.name = in.readString();
        this.letter = in.readString();
        this.brandName = in.readString();
        this.brandId = in.readInt();
        this.factoryId = in.readInt();
        this.brandLogo = in.readString();
        this.carList = in.createTypedArrayList(CarBean.CREATOR);
        this.star = in.readString();
        this.createTime = in.readString();
        this.isSplitLine = in.readByte() != 0;
        this.isSelected = in.readByte() != 0;
        this.isTop = in.readByte() != 0;
        this.beautlist = new ArrayList<Beauty>();
        in.readList(this.beautlist, Beauty.class.getClassLoader());
        this.releaseTime = in.readString();
        this.stars = in.readString();
        this.boothId = in.readString();
        this.hallId = in.readString();
        this.areaName = in.readString();
        this.adcode = in.readString();
        this.areaId = in.readInt();
    }

    public static final Creator<Brand> CREATOR = new Creator<Brand>() {
        @Override
        public Brand createFromParcel(Parcel source) {
            return new Brand(source);
        }

        @Override
        public Brand[] newArray(int size) {
            return new Brand[size];
        }
    };
}
