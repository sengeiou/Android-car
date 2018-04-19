package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：Jenny
 * Date:2016/12/13 11:06
 * E-mail：fishloveqin@gmail.com
 * 具体Moto车型
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarBean extends BaseModel implements Parcelable {

    @JsonProperty("factory_name")
    public String factoryName;
    @JsonProperty("factory_logo")
    public String factoryLogo;
    @JsonProperty("series_name")
    public String seriesName;
    @JsonProperty("match_num")
    public int matchNums;
    @JsonProperty("series_id")
    public int seriesId;

    public boolean isAdded;          //是否添加
    @JsonProperty("id")
    public int id;
    @JsonProperty("vehicle_type")
    public String type;
    @JsonProperty("brand_name")
    public String brandName;
    @JsonProperty("reference_price")
    public String referencePrice;
    @JsonProperty("brand_id")
    public int brandId;
    @JsonProperty("factory_id")
    public int factoryId;
    @JsonProperty("car_series_id")
    public int carSeriesId;
    @JsonProperty("seat_num")
    public int seatNum;
    @JsonProperty("status")
    public int status;


    //    燃油1 电动2
    @JsonProperty("power_forms")
    public int powerForms;
    @JsonProperty("cc")
    public String cc;
    @JsonProperty("battery")
    public String battery;
    @JsonProperty("couponPrice")
    public String couponPrice;
    @JsonProperty("car_id")
    public int carId;
    @JsonProperty("drive")
    public String driver;

    @JsonProperty("car_name")
    public String carName;
    @JsonProperty("name")
    public String name;
    @JsonProperty("power")
    public String power;
    @JsonProperty("car_color")
    public CarColor colors;
    @JsonProperty("car_img")
    public String img;
    @JsonProperty("car_img_list")
    public List<Img> imgList;

    @JsonProperty("current_time")
    public String currentTime;
    @JsonProperty("create_time")
    public String createTime;
    @JsonProperty("guide_price")
    public String guidePrice;

    @JsonProperty("sales_status")
    public int salesStatus;
    @JsonProperty("cover")
    public String cover;
    @JsonProperty("create_by")
    public String createBy;
    @JsonProperty("update_time")
    public String updateTime;
    @JsonProperty("update_by")
    public String updateBy;

    @JsonProperty("price")
    public String price;

    @JsonProperty("is_collection")
    public int isFav;

    @JsonProperty("is_organization_car")
    public int isOrganizationCar;

    public static class Img {
        @JsonProperty("car_id")
        public int imgId;
        @JsonProperty("car_link")
        public String link;

    }

    @JsonProperty("brand_logo")
    public String brandLogo;
    @JsonProperty("car_level_name")
    public String carLevelName;

    @JsonProperty("market_time")
    public String marketTime;
    @JsonProperty("ofc")
    public String oilWear;
    @JsonProperty("battery_spec")
    public String batterySpec;  //电池规格
    @JsonProperty("engine_power")
    public String enginePower;  //发动机公里
    @JsonProperty("img_nums")
    public int imgNums;
    @JsonProperty("imgs")
    public List<String> coverImgList;
    @JsonProperty("pic_count")
    public int picCount;

    @JsonProperty("match_carids")
    public ArrayList<CarBean> matchingCars;
    @JsonProperty("total_num")

    public int totalNum;

    @JsonProperty("appearance_img")
    public String appearanceImg;
    @JsonProperty("car_level")
    public String carLevel;

    @JsonProperty("release_info")
    public CarBean releaseInfo;

    @JsonProperty("is_exist")
    public int isExist;
    @JsonProperty("is_show")
    public int isShow;

    /**
     * 摩托车颜色数组
     */
    public static class CarColor implements Parcelable {
        @JsonProperty("out")
        public String exterior;
        @JsonProperty("in")
        public String interior;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.exterior);
            dest.writeString(this.interior);
        }

        public CarColor() {
        }

        protected CarColor(Parcel in) {
            this.exterior = in.readString();
            this.interior = in.readString();
        }

        public static final Creator<CarColor> CREATOR = new Creator<CarColor>() {
            @Override
            public CarColor createFromParcel(Parcel source) {
                return new CarColor(source);
            }

            @Override
            public CarColor[] newArray(int size) {
                return new CarColor[size];
            }
        };
    }

    public static class ImgUrl implements Parcelable {
        @JsonProperty("out")
        public String exteriorUrl;
        @JsonProperty("in")
        public String interiorUrl;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.exteriorUrl);
            dest.writeString(this.interiorUrl);
        }

        public ImgUrl() {
        }

        protected ImgUrl(Parcel in) {
            this.exteriorUrl = in.readString();
            this.interiorUrl = in.readString();
        }

        public static final Creator<ImgUrl> CREATOR = new Creator<ImgUrl>() {
            @Override
            public ImgUrl createFromParcel(Parcel source) {
                return new ImgUrl(source);
            }

            @Override
            public ImgUrl[] newArray(int size) {
                return new ImgUrl[size];
            }
        };
    }

    public CarBean() {
    }

    public CarBean(int carId, String carName) {
        this.carId = carId;
        this.id = carId;
        this.carName = carName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.factoryName);
        dest.writeString(this.factoryLogo);
        dest.writeString(this.seriesName);
        dest.writeInt(this.matchNums);
        dest.writeInt(this.seriesId);
        dest.writeInt(this.id);
        dest.writeString(this.type);
        dest.writeString(this.brandName);
        dest.writeString(this.referencePrice);
        dest.writeInt(this.brandId);
        dest.writeInt(this.factoryId);
        dest.writeInt(this.carSeriesId);
        dest.writeInt(this.seatNum);
        dest.writeInt(this.status);
        dest.writeInt(this.powerForms);
        dest.writeString(this.cc);
        dest.writeString(this.battery);
        dest.writeString(this.couponPrice);
        dest.writeInt(this.carId);
        dest.writeString(this.driver);
        dest.writeString(this.carName);
        dest.writeString(this.name);
        dest.writeString(this.power);
        dest.writeParcelable(this.colors, flags);
        dest.writeString(this.img);
        dest.writeList(this.imgList);
        dest.writeString(this.currentTime);
        dest.writeString(this.createTime);
        dest.writeString(this.guidePrice);
        dest.writeInt(this.salesStatus);
        dest.writeString(this.cover);
        dest.writeString(this.createBy);
        dest.writeString(this.updateTime);
        dest.writeString(this.updateBy);
        dest.writeString(this.price);
        dest.writeInt(this.isFav);
        dest.writeInt(this.isOrganizationCar);
        dest.writeString(this.brandLogo);
        dest.writeString(this.carLevelName);
        dest.writeString(this.marketTime);
        dest.writeString(this.oilWear);
        dest.writeString(this.batterySpec);
        dest.writeString(this.enginePower);
        dest.writeInt(this.imgNums);
        dest.writeStringList(this.coverImgList);
        dest.writeInt(this.picCount);
        dest.writeTypedList(this.matchingCars);
        dest.writeInt(this.totalNum);
        dest.writeString(this.appearanceImg);
        dest.writeString(this.carLevel);
        dest.writeParcelable(this.releaseInfo, flags);
        dest.writeInt(this.isExist);
        dest.writeInt(this.isShow);
    }

    protected CarBean(Parcel in) {
        this.factoryName = in.readString();
        this.factoryLogo = in.readString();
        this.seriesName = in.readString();
        this.matchNums = in.readInt();
        this.seriesId = in.readInt();
        this.id = in.readInt();
        this.type = in.readString();
        this.brandName = in.readString();
        this.referencePrice = in.readString();
        this.brandId = in.readInt();
        this.factoryId = in.readInt();
        this.carSeriesId = in.readInt();
        this.seatNum = in.readInt();
        this.status = in.readInt();
        this.powerForms = in.readInt();
        this.cc = in.readString();
        this.battery = in.readString();
        this.couponPrice = in.readString();
        this.carId = in.readInt();
        this.driver = in.readString();
        this.carName = in.readString();
        this.name = in.readString();
        this.power = in.readString();
        this.colors = in.readParcelable(CarColor.class.getClassLoader());
        this.img = in.readString();
        this.imgList = new ArrayList<Img>();
        in.readList(this.imgList, Img.class.getClassLoader());
        this.currentTime = in.readString();
        this.createTime = in.readString();
        this.guidePrice = in.readString();
        this.salesStatus = in.readInt();
        this.cover = in.readString();
        this.createBy = in.readString();
        this.updateTime = in.readString();
        this.updateBy = in.readString();
        this.price = in.readString();
        this.isFav = in.readInt();
        this.isOrganizationCar = in.readInt();
        this.brandLogo = in.readString();
        this.carLevelName = in.readString();
        this.marketTime = in.readString();
        this.oilWear = in.readString();
        this.batterySpec = in.readString();
        this.enginePower = in.readString();
        this.imgNums = in.readInt();
        this.coverImgList = in.createStringArrayList();
        this.picCount = in.readInt();
        this.matchingCars = in.createTypedArrayList(CarBean.CREATOR);
        this.totalNum = in.readInt();
        this.appearanceImg = in.readString();
        this.carLevel = in.readString();
        this.releaseInfo = in.readParcelable(CarBean.class.getClassLoader());
        this.isExist = in.readInt();
        this.isShow = in.readInt();
    }

    public static final Creator<CarBean> CREATOR = new Creator<CarBean>() {
        @Override
        public CarBean createFromParcel(Parcel source) {
            return new CarBean(source);
        }

        @Override
        public CarBean[] newArray(int size) {
            return new CarBean[size];
        }
    };
}
