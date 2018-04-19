package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tgf.kcwc.util.DataFormatUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/9/12 0012
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HomeModel  {
    public Pagination pagination;
    @JsonProperty("list")
    public ArrayList<HomeModelItem> homeList;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HomeModelItem{

        public int id;
        public String title;
        public String cover;
        public String model;
        public String longitude;
        public String latitude;
        @JsonProperty("local_address")
        public String localAddress;
        @JsonProperty("reply_count")
        public int replyCount;
        @JsonProperty("view_count")
        public int viewCount;
        @JsonProperty("digg_count")
        public int diggCount;
        @JsonProperty("city_id")
        public int cityId;
        @JsonProperty("model_id")
        public int modelId;
        @JsonProperty("create_time")
        public String createTime;
        public double distance;
        @JsonProperty("is_praise")
        public int isPraise;
        @JsonProperty("create_user")
        public CreateUser createUser;
        @JsonProperty("model_data")
        public ModelData modelData;

        public String getViewCount() {
            String resultViewCount = viewCount+"";
            if(resultViewCount.length()<3){

            }
            return resultViewCount;
        }

        public String getReplyCount() {
            return replyCount+"";
        }

        public String getDiggCount() {
            return diggCount+"";
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateUser{

        public int id;
        public String nickname;
        public String avatar;
        public int sex;
        public int age;
        @JsonProperty("is_doyen")
        public int isDoyen;
        @JsonProperty("is_model")
        public int isModel;
        @JsonProperty("is_master")
        public int isMaster;
        @JsonProperty("is_follow")
        public int isFollow;
        @JsonProperty("master_brand")
        public String masterBrand;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ModelData{

        public String destination;
        @JsonProperty("dest_city")
        public String destCity;
        @JsonProperty("title")
        public String title;
        @JsonProperty("begin_time")
        public String beginTime;
        @JsonProperty("start_time")
        public String startTime;
        @JsonProperty("end_time")
        public String endTime;
        @JsonProperty("apply_end_time")
        public String limtEndTime;
        @JsonProperty("limit_max")
        public int limitMax;
        @JsonProperty("apply_num")
        public int applyNum;
        @JsonProperty("intro")
        public String intro;
        @JsonProperty("content")
        public String content;
        @JsonProperty("price")
        public double price;
        @JsonProperty("factory_name")
        public String factoryName;
        @JsonProperty("factory_logo")
        public String factoryLogo;
        @JsonProperty("image")
        public ArrayList<String> imageList;
        public Org org;
        public Topic topic;
        @JsonProperty("benefit_attr")
        public ArrayList<String> benefitAttr;
        public String cover;
        @JsonProperty("speed_max")
        public String speedMax;
        @JsonProperty("speed_average")
        public String speedAverage;
        @JsonProperty("altitude_average")
        public int altitudeAverage;
        public String mileage;
        @JsonProperty("actual_time")
        public String actualTime;
        public String quotient;
        @JsonProperty("navigation")
        public ArrayList<Navigation> navigationList;
        @JsonProperty("factory_id")
        public int factoryId;
        @JsonProperty("series_id")
        public int seriesId;
        @JsonProperty("series_name")
        public String seriesName;
        @JsonProperty("car_id")
        public int carId;
        @JsonProperty("car_name")
        public String carName;
        @JsonProperty("appearance_img")
        public String appearanceImg;
        @JsonProperty("appearance_color_name")
        public String appearanceColorName;
        @JsonProperty("appearance_color_value")
        public String appearanceColorValue;
        @JsonProperty("interior_img")
        public String interiorImg;
        @JsonProperty("interior_color_name")
        public String interiorColorName;
        @JsonProperty("interior_color_value")
        public String interiorColorValue;
        @JsonProperty("front_img")
        public String frontImg;
        @JsonProperty("side_img")
        public String sideImg;
        @JsonProperty("org_id")
        public int orgId;
        @JsonProperty("list")
        public ArrayList<Coupon> couponsList;
        /*新车首发*/
        @JsonProperty("event_id")
        public int eventId;
        @JsonProperty("hall_id")
        public int hallId;
        @JsonProperty("brand_id")
        public int brandId;
        @JsonProperty("release_time")
        public String releaseTime;
        @JsonProperty("product_name")
        public String productName;
        @JsonProperty("product_id")
        public int productId;
        public String star;
        @JsonProperty("event_name")
        public String eventName;
        @JsonProperty("event_cover")
        public String eventCover;
        public String name;
        @JsonProperty("product_logo")
        public String productLogo;
        @JsonProperty("brand_name")
        public String brandName;
        @JsonProperty("event_btn_type")
        public String eventBtnType;
        @JsonProperty("rate_value")
        public String rateValue;
        @JsonProperty("rate_type")
        public int rateType;
        @JsonProperty("sale_num")
        public int saleNum;
        @JsonProperty("activity_status")
        public String activityStatus;


        public String getSpeedMax() {
            return DataFormatUtil.getOneDoubleStr(speedMax);
        }
        public String getSpeedAverage() {
            return DataFormatUtil.getOneDoubleStr(speedAverage);
        }
        public String getQuotient() {
            return DataFormatUtil.getOneDoubleStr(quotient);
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Navigation{
        public int id;
        public String link;
        public int sort;
        public String name;
        public String url;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Org{

        public int id;
        public String name;
        public String logo;
        public String address;
        public String tel;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Topic{
        public int id;
        public String title;
        public String cover;
        @JsonProperty("fans_num")
        public int fansNum;
        @JsonProperty("thread_num")
        public int threadNum;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Coupon{

        public int id;
        public String title;
        public String cover;
        public double price;
        public String denomination;
    }
}
