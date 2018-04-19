package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author：Jenny
 * Date:2017/4/13 10:39
 * E-mail：fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreShowCarDetailModel {

    /**
     * show_count : 10
     * gift : [{"title":"礼包标题a","id":5,"address":"礼包标题aaa"}]
     * interior_color_name : 黑色
     * reference_price : 12.00
     * type_show : 1
     * org_activity : [{"create_time":"2017-03-21 15:29:22","title":"自驾西藏","id":2},{"create_time":"2017-03-22 15:30:09","title":"买车优惠","id":3}]
     * appearance_color_name : 红色
     * org_id : 20
     * interior_img : /car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg
     * car_id : 1005
     * limit_time : {"address":"限时优惠","countdown":1756536,"rate":7,"id":21,"rate_type":2,"end_time":"2017-04-27 20:25:48","title":"店铺限时优惠"}
     * oc_car_name :
     * appearance_img : /car/1703/21/070665cfd98603b0c947d5ac64dbc3df.jpg
     * factory_name : 上汽大众
     * on_sale : 1
     * id : 41
     * saler : [{"id":110,"avatar":"/project/1703/01/906f74df289b747be3ad25571d79d243.jpg","real_name":"龚创","nickname":"龚创"},{"id":180,"avatar":"","real_name":"张XX","nickname":"店内销售"}]
     * exist_list : [{"car_name":"没有车型","id":12,"car_id":0,"appearance_img":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg"},{"car_name":"2017款奥迪A6L 技术型","id":47,"car_id":1001,"appearance_img":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg"},{"car_name":"2017款 1.6L 自动风尚版","id":50,"car_id":1005,"appearance_img":"/car/1703/21/070665cfd98603b0c947d5ac64dbc3df.jpg"}]
     * series_id : 20
     * car_name : 2017款 1.6L 自动风尚版
     * coupon : [{"id":3,"cover":"/dealer/1701/05/ad80f658b020fb70a6130e4ecb75746f.jpg","denomination":"20.00","price":"1.00","count":2,"title":"QQ飞车新款20元代金券"}]
     * diff_conf :
     * series_name : 朗逸
     * show_list : [{"car_name":"宽履 500 LX雪地车","id":6,"appearance_img":"","car_id":50},{"car_name":"2017款 380THP 豪华版","id":9,"appearance_img":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg","car_id":52},{"car_name":"途观2015","id":48,"appearance_img":"/car/1701/19/50e6acb6262f70f8578bc1f6dda98454.jpg","car_id":1003}]
     * exist_count : 12
     * type_exist : 1
     * org : {"address":"重庆市高新区科园四路269号 ","latitude":"29.525106","name":"展览中心","id":20,"tel":"","longitude":"106.492902"}
     */

    @JsonProperty("show_count")
    public int showCount;
    @JsonProperty("interior_color_name")
    public String interiorColorName;
    @JsonProperty("reference_price")
    public String referencePrice;
    @JsonProperty("reference_price_max")
    public String referencePriceMax;
    @JsonProperty("type_show")
    public int typeShow;
    @JsonProperty("is_series")
    public int isSeries;
    @JsonProperty("is_collect")
    public int isFav;
    @JsonProperty("appearance_color_name")
    public String appearanceColorName;
    @JsonProperty("org_id")
    public int orgId;
    @JsonProperty("interior_img")
    public String interiorImg;
    @JsonProperty("car_id")
    public int carId;
    @JsonProperty("limit_time")
    public LimitTimeBean limitTime;
    @JsonProperty("oc_car_name")
    public String ocCarName;
    @JsonProperty("appearance_img")
    public String appearanceImg;
    @JsonProperty("factory_name")
    public String factoryName;
    @JsonProperty("on_sale")
    public int onSale;
    public int id;
    @JsonProperty("series_id")
    public int seriesId;
    @JsonProperty("car_name")
    public String carName;
    @JsonProperty("diff_conf")
    public String diffConf;
    @JsonProperty("series_name")
    public String seriesName;
    @JsonProperty("exist_count")
    public int existCount;
    @JsonProperty("type_exist")
    public int typeExist;
    public OrgBean org;
    public List<GiftBean> gift;
    @JsonProperty("org_activity")
    public List<Topic> orgActivity;
    public List<Account.UserInfo> saler;
    @JsonProperty("exist_list")
    public List<CarBean> existList;
    public List<Coupon> coupon;
    @JsonProperty("show_list")
    public List<CarBean> showList;

    @JsonProperty("rate_type")
    public int rateType;
    public String rate;

    @JsonProperty("end_time")
    public String endTime;

    @JsonProperty("current_time")
    public String currentTime;

    @JsonProperty("is_limit_time")
    public int isLimitTime;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LimitTimeBean {
        /**
         * address : 限时优惠
         * countdown : 1756536
         * rate : 7
         * id : 21
         * rate_type : 2
         * end_time : 2017-04-27 20:25:48
         * title : 店铺限时优惠
         */

        public String desc;
        public long countdown;
        public String rate;
        public int id;
        @JsonProperty("rate_type")
        public int rateType;
        @JsonProperty("end_time")
        public String endTime;
        public String title;


        /**
         * series_id : 65
         * car_id : 174904
         * content : <p>宝沃合资-可能限时优惠详情</p>
         * type : limit_time
         * rate : 6.9
         * current_time : 2017-10-24 16:11:24
         * benefit : 现金折扣 6.9折
         * countdown : 1324116
         */

        @JsonProperty("series_id")
        public int seriesId;
        @JsonProperty("car_id")
        public int carId;
        public String content;
        public String type;
        @JsonProperty("current_time")
        public String currentTime;
        public String benefit;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrgBean {
        /**
         * address : 重庆市高新区科园四路269号
         * latitude : 29.525106
         * name : 展览中心
         * id : 20
         * tel :
         * longitude : 106.492902
         */

        public String address;
        public String latitude;
        public String name;
        public int id;
        public String tel;
        public String longitude;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GiftBean {
        /**
         * title : 礼包标题a
         * id : 5
         * address : 礼包标题aaa
         */

        public String title;
        public int id;
        public String desc;
    }

}
