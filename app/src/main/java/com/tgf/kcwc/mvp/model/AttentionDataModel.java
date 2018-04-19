package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author:Jenny
 * Date:2017/5/18
 * E-mail:fishloveqin@gmail.com
 *
 * 关注列表数据模型
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class AttentionDataModel {

    /**
     * recommend_list : [{"avatar":"","id":174,"sex":1,"is_doyen":0,"register_area":"河南","nickname":"","is_model":0,"sign_text":"","is_master":0,"logo":""},{"avatar":"","id":170,"sex":1,"is_doyen":0,"register_area":"重庆","nickname":"","is_model":0,"sign_text":"","is_master":0,"logo":""},{"avatar":"/thread/1705/09/473f72ce3a0494ea10506d831ef5e8ee.jpg","id":335,"sex":1,"is_doyen":0,"register_area":"北京","nickname":"测试003","is_model":0,"sign_text":"摩托车爱好者","is_master":0,"logo":""}]
     * user_id : 110
     * list : [{"is_model":0,"register_area":"重庆","is_doyen":1,"relation":"already_concern","follow_id":221,"follow_time":"2017-05-11 13:41:42","nickname":"Ribbit","logo":"","sex":1,"remark":"","is_master":0,"avatar":"/thread/1705/11/48cac0e91be1a478bcc3a652e8527ebe.png","sign_text":"好得很"},{"is_model":1,"register_area":"重庆","is_doyen":0,"relation":"already_concern","follow_id":120,"follow_time":"2017-03-17 14:49:29","nickname":"tlbb","logo":"","sex":1,"remark":"","is_master":0,"avatar":"","sign_text":""},{"is_model":0,"register_area":"黑龙江","is_doyen":0,"relation":"already_concern","follow_id":119,"follow_time":"2017-03-17 14:49:27","nickname":"佳佳","logo":"","sex":1,"remark":"","is_master":0,"avatar":"","sign_text":""},{"is_model":0,"register_area":"重庆","is_doyen":0,"relation":"already_concern","follow_id":118,"follow_time":"2017-03-17 14:49:25","nickname":"seven","logo":"","sex":2,"remark":"","is_master":0,"avatar":"","sign_text":""},{"is_model":1,"register_area":"河南","is_doyen":0,"relation":"mutual_concern","follow_id":117,"follow_time":"2017-03-17 14:49:23","nickname":"落单的猫儿","logo":"","sex":1,"remark":"","is_master":0,"avatar":"/avatar/1703/15/313c8c8cc72c8b0af436452a9fa47b69.png","sign_text":""},{"is_model":0,"register_area":"重庆","is_doyen":0,"relation":"already_concern","follow_id":116,"follow_time":"2017-03-17 14:49:21","nickname":"app测试","logo":"","sex":1,"remark":"","is_master":0,"avatar":"","sign_text":""},{"is_model":0,"register_area":"河北","is_doyen":0,"relation":"already_concern","follow_id":115,"follow_time":"2017-03-17 14:49:18","nickname":"熊小燕","logo":"","sex":1,"remark":"","is_master":0,"avatar":"","sign_text":""},{"is_model":0,"register_area":"重庆","is_doyen":0,"relation":"mutual_concern","follow_id":114,"follow_time":"2017-03-17 14:49:16","nickname":"英雄联盟","logo":"","sex":1,"remark":"","is_master":0,"avatar":"/avatar/1703/24/59816da719c2753083a9e9c5c3765ad3.jpg","sign_text":""},{"is_model":1,"register_area":"重庆","is_doyen":0,"relation":"already_concern","follow_id":113,"follow_time":"2017-03-17 14:49:14","nickname":"邓丹车行账户","logo":"","sex":1,"remark":"","is_master":0,"avatar":"","sign_text":""},{"is_model":0,"register_area":"重庆","is_doyen":0,"relation":"mutual_concern","follow_id":112,"follow_time":"2017-03-17 14:49:12","nickname":"杨郇","logo":"","sex":1,"remark":"","is_master":0,"avatar":"","sign_text":""},{"is_model":0,"register_area":"重庆","is_doyen":0,"relation":"already_concern","follow_id":109,"follow_time":"2017-03-17 14:49:00","nickname":"A let1","logo":"","sex":1,"remark":"","is_master":0,"avatar":"http://wx.qlogo.cn/mmopen/qd3u5IHSYT8GSiaxnRIZmzuWNt2LKmpbc4mxFnNTzrxpVwbrCel1PugBxQ6icnNlzrrYZtiaicEXWibCpkrvAt3tuEooHyFficSQl0/0","sign_text":"是的是的"},{"is_model":0,"register_area":"北京","is_doyen":0,"relation":"already_concern","follow_id":108,"follow_time":"2017-03-17 14:48:58","nickname":"邓丹丹","logo":"","sex":1,"remark":"","is_master":0,"avatar":"","sign_text":""},{"is_model":1,"register_area":"重庆","is_doyen":0,"relation":"already_concern","follow_id":2,"follow_time":"2017-03-17 14:48:54","nickname":"123","logo":"","sex":1,"remark":"","is_master":0,"avatar":"/avatar/1703/24/ccab54caaf2b16ea94cc4405ff884c1c.jpg","sign_text":"这是一个神奇的账号"},{"is_model":0,"register_area":"山东","is_doyen":1,"relation":"already_concern","follow_id":1,"follow_time":"2017-03-17 14:48:49","nickname":"重庆展览中心总账号","logo":"/brand/1701/20/44b5fc2bdabd9356006b7b6bd118fe4c.jpg","sex":1,"remark":"","is_master":1,"avatar":"/avatar/1704/21/67ca7221ea1312d91e78cd8279ed810b.jpg","sign_text":""}]
     * pagination : {"page":1,"count":14}
     */

    @JsonProperty("user_id")
    public int            id;
    public Pagination     pagination;
    @JsonProperty("recommend_list")
    public List<UserInfo> recommendList;
    public List<UserInfo> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserInfo {
        /**
         * avatar : 
         * id : 174
         * sex : 1
         * is_doyen : 0
         * register_area : 河南
         * nickname : 
         * is_model : 0
         * sign_text : 
         * is_master : 0
         * logo : 
         */

        public int     id;
        @JsonProperty("sign_text")
        public String  signText;
        @JsonProperty("is_master")
        public int     isMaster;
        @JsonProperty("is_model")
        public int     isModel;
        @JsonProperty("register_area")
        public String  registerArea;
        @JsonProperty("is_doyen")
        public int     isDoyen;
        public String  relation;
        @JsonProperty("follow_id")
        public int     followId;
        @JsonProperty("follow_time")
        public String  followTime;
        public String  nickname;
        public String  logo;
        public int     sex;
        @JsonProperty("remark")
        public String  remark;
        public int     is_master;
        public String  avatar;
        @JsonProperty("user_id")
        public int     userId;
        public boolean isSelected;
    }

}
