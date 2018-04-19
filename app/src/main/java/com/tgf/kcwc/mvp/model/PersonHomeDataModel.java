package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author:Jenny
 * Date:2017/5/17
 * E-mail:fishloveqin@gmail.com
 * 个人主页Model
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonHomeDataModel {

    /**
     * user_id : 110
     * tel : 18716636959
     * level : 0
     * age : 26
     * signs : 天蝎座
     * sign_text : 从你的全世界路过。。。。
     * city_id : 0
     * is_model : 1
     * is_doyen : 1
     * is_master : 1
     * create_time : 2017-02-23 15:33:07
     * register_area : 重庆
     * is_own : 0
     * model : {"id":38,"name":"陌陌","height":178,"bust":99,"waist":80,"hipline":97,"prize":"","cover":"/project/1705/04/be515b233398ebefb30db18cb8696111.jpg"}
     * master : {"id":5,"car_id":1005,"name":"2017款 1.6L 自动风尚版","logo":"/brand/1701/20/44b5fc2bdabd9356006b7b6bd118fe4c.jpg"}
     * hobby : #西藏 ,#川西 ,#甘南 ,#云南
     * group : [{"group_id":6,"is_owner":0,"is_manage":0,"name":"长安福特官方群","cover":"/coupon/1701/12/647529cb5ac96e101959212617c53f78.jpg"},{"group_id":3,"is_owner":1,"is_manage":0,"name":"官方第三群重庆","cover":"/coupon/1701/12/647529cb5ac96e101959212617c53f78.jpg"}]
     */

    @JsonProperty("user_id")
    public int             userId;
    public String          tel;
    public int             level;
    public int             age;
    public String          signs;
    @JsonProperty("sign_text")
    public String          signText;
    @JsonProperty("city_id")
    public int             cityId;
    @JsonProperty("is_model")
    public int             isModel;
    @JsonProperty("is_doyen")
    public int             isDoyen;
    @JsonProperty("is_master")
    public int             isMaster;
    @JsonProperty("create_time")
    public String          createTime;
    @JsonProperty("register_area")
    public String          registerArea;
    @JsonProperty("is_own")
    public int             isOwn;
    public ModelBean       model;
    public MasterBean      master;
    public String          hobby;
    public List<GroupBean> group;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ModelBean {
        /**
         * id : 38
         * name : 陌陌
         * height : 178
         * bust : 99
         * waist : 80
         * hipline : 97
         * prize : 
         * cover : /project/1705/04/be515b233398ebefb30db18cb8696111.jpg
         */

        public int    id;
        public String name;
        public int    height;
        public int    bust;
        public int    waist;
        public int    hipline;
        public String prize;
        public String cover;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MasterBean {
        /**
         * id : 5
         * car_id : 1005
         * name : 2017款 1.6L 自动风尚版
         * logo : /brand/1701/20/44b5fc2bdabd9356006b7b6bd118fe4c.jpg
         */

        public int    id;
        @JsonProperty("car_id")
        public int    carId;
        public String name;
        public String logo;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GroupBean {
        /**
         * group_id : 6
         * is_owner : 0
         * is_manage : 0
         * name : 长安福特官方群
         * cover : /coupon/1701/12/647529cb5ac96e101959212617c53f78.jpg
         */

        @JsonProperty("group_id")
        public int    groupId;
        @JsonProperty("is_owner")
        public int    isOwner;
        @JsonProperty("is_manage")
        public int    isMgr;
        public String name;
        public String cover;
    }
}
