package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author:Jenny
 * Date:2017/5/20
 * E-mail:fishloveqin@gmail.com
 *
 * 用户详情信息
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDetailDataModel {


    /**
     * tel : 18716636959
     * hobby : [{"parent_id":6,"name":"西藏","id":7},{"parent_id":6,"name":"川西","id":9},{"parent_id":6,"name":"甘南","id":10},{"parent_id":6,"name":"云南","id":8}]
     * create_time : 2017-02-23 15:33:07
     * birth : 1990-11-15
     * avatar : /project/1703/01/906f74df289b747be3ad25571d79d243.jpg
     * register_area : 重庆
     * sign_text : 从你的全世界路过。。。。
     * nickname : 落叶111
     * sex : 1
     * id : 110
     */

    public String tel;
    @JsonProperty("create_time")
    public String createTime;
    public String birth;
    public String avatar;
    @JsonProperty("register_area")
    public String registerArea;
    @JsonProperty("sign_text")
    public String signText;
    public String nickname;
    public int sex;
    public int id;
    public List<HobbyBean> hobby;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HobbyBean {
        /**
         * parent_id : 6
         * name : 西藏
         * id : 7
         */

        @JsonProperty("parent_id")
        public int parentId;
        public String name;
        public int id;
    }
}
