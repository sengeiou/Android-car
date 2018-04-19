package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tgf.kcwc.entity.RichEditorEntity;

/**
 * Author：Jenny
 * Date:2017/2/28 14:54
 * E-mail：fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoodsEditModel {


    /**
     * id : 386
     * price : 3232.00
     * cover : cover.png
     * title : 321
     * buy_year : 2016
     * road_haul : 170000.00
     * phone : 366666625
     * describe : {"mEditorDatas":[{"imagePath":"/storage/emulated/0/DCIM/Camera/IMG_2017-02-21 21:00:37.jpg","imageUrl":"/thread/1702/28/36e0dbea9e57263c209e649ff2eaa29d.png","inputStr":""},{"imagePath":"","imageUrl":"","inputStr":""},{"imagePath":"","imageUrl":"","inputStr":"321321321f"},{"imagePath":"","imageUrl":"","inputStr":""},{"imagePath":"","imageUrl":"","inputStr":""}],"title":"1212"}
     * failure_time : 2
     * local_address : 重庆市九龙坡区渝州路街道
     * longitude : 106.4934825897217
     * latitude : 29.525510555002036
     * goods_type : {"id":1,"title":"整车"}
     * car : {"id":2,"name":"CBR1000RR"}
     * brand : {"id":83,"name":"本田"}
     */

    public int id;
    public String price;
    public String cover;
    public String title;
    @JsonProperty("buy_year")
    public int buyYear;
    @JsonProperty("road_haul")
    public String roadHaul;
    public String phone;
    public RichEditorEntity describe;
    @JsonProperty("failure_time")
    public int failureTime;
    @JsonProperty("local_address")
    public String localAddress;
    public String longitude;
    public String latitude;
    @JsonProperty("goods_type")
    public GoodsTypeBean goodsType;
    public CarBean car;
    public BrandBean brand;



    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GoodsTypeBean {
        /**
         * id : 1
         * title : 整车
         */

        public int id;
        public String title;
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BrandBean {
        /**
         * id : 83
         * name : 本田
         */

        public int id;
        public String name;
    }
}
