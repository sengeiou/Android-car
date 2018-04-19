package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tgf.kcwc.entity.RichEditorEntity;

import java.util.List;

/**
 * Author：Jenny
 * Date:2017/2/14 14:50
 * E-mail：fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SaleDetailModel {

    /**
     * id : 491
     * title : 3123123
     * cover : ["/thread/1704/17/0037ef804c29533bf2249987d1ae533c.jpg"]
     * view_count : 78
     * is_digest : 0
     * create_time : 2017-04-17 20:57:02
     * local_address : 重庆市渝中区上清寺街道重庆市人民政府
     * create_user : {"id":159,"nickname":"","tel":"13648433330","sex":1,"avatar":"","is_doyen":0,"is_model":0,"count":6}
     * goods : {"goods_type":3,"vehicle_type":"","buy_year":2015,"road_haul":"1212.00","price":1212,"longitude":"106.551557","latitude":"29.56301","area":"渝中区|上清寺街道","phone":"1212","describe":{"mEditorDatas":[{"inputStr":"啊实打实大师","imageUrl":"/thread/1704/17/d6deff1a0c6842ba3b6def6b52e80a8f.jpg"}]},"car":{"id":0,"name":"","cc":"","market_time":"","ofc":"","car_level":"","engine_power":""},"series":{"id":0,"name":""},"brand":{"id":0,"vehicle_type":"","name":""}}
     */

    public int            id;
    @JsonProperty("apply_id")
    public int            applyId;
    public String         title;
    @JsonProperty("view_count")
    public int            viewCount;
    @JsonProperty("is_digest")
    public int            isDigest;
    @JsonProperty("create_time")
    public String         createTime;
    @JsonProperty("local_address")
    public String         address;
    @JsonProperty("create_user")
    public Account.UserInfo user;
    public GoodsBean      goods;

    @JsonProperty("cover")
    public List<String>   covers;

    @JsonProperty("is_collect")
    public int isCollect;
    @JsonProperty("has_event")
    public int hasEvent;
    @JsonProperty("event")
    public SaleListModel.SaleItemBean.Event event;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateUserBean {
        /**
         * id : 159
         * nickname : 
         * tel : 13648433330
         * sex : 1
         * avatar : 
         * is_doyen : 0
         * is_model : 0
         * count : 6
         */

        public int    id;
        public String nickname;
        public String tel;
        public int    sex;
        public String avatar;
        @JsonProperty("is_doyen")
        public int    isDoyen;
        @JsonProperty("is_model")
        public int    isModel;
        public int    count;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GoodsBean {
        /**
         * goods_type : 3
         * vehicle_type : 
         * buy_year : 2015
         * road_haul : 1212.00
         * price : 1212
         * longitude : 106.551557
         * latitude : 29.56301
         * area : 渝中区|上清寺街道
         * phone : 1212
         * describe : {"mEditorDatas":[{"inputStr":"啊实打实大师","imageUrl":"/thread/1704/17/d6deff1a0c6842ba3b6def6b52e80a8f.jpg"}]}
         * car : {"id":0,"name":"","cc":"","market_time":"","ofc":"","car_level":"","engine_power":""}
         * series : {"id":0,"name":""}
         * brand : {"id":0,"vehicle_type":"","name":""}
         */

        @JsonProperty("goods_type")
        public String              goodsType;
        @JsonProperty("vehicle_type")
        public String           vehicleType;
        @JsonProperty("buy_year")
        public int              buyYear;
        @JsonProperty("road_haul")
        public String           roadHaul;
        public int              price;
        public String           longitude;
        public String           latitude;
        public String           area;
        public String           phone;
        public RichEditorEntity describe;
        public CarBean          car;
        public SeriesBean       series;
        public Brand            brand;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class SeriesBean {
            /**
             * id : 0
             * name : 
             */

            public int    id;
            public String name;
        }

    }
}
