package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author：Jenny
 * Date:2017/3/8 15:52
 * E-mail：fishloveqin@gmail.com
 * 点赞列表
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class LikeListModel {

    /**
     * pagination : {"page":1,"count":2}
     * list : [{"user":{"nickname":"重庆展览中心总账号","avatar":"","is_model":0,"age":0,"master_brand":"/brand/1701/20/44b5fc2bdabd9356006b7b6bd118fe4c.jpg","sex":1,"is_doyen":0,"tel":"13000000000","id":1,"is_master":1},"id":1,"create_time":"0000-00-00 00:00:00"},{"user":{"nickname":"重庆展览中心总账号","avatar":"","is_model":0,"age":0,"master_brand":"/brand/1701/20/44b5fc2bdabd9356006b7b6bd118fe4c.jpg","sex":1,"is_doyen":0,"tel":"13000000000","id":1,"is_master":1},"id":2,"create_time":"0000-00-00 00:00:00"}]
     */

    public Pagination     pagination;
    public List<LikeBean> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LikeBean {
        /**
         * user : {"nickname":"重庆展览中心总账号","avatar":"","is_model":0,"age":0,"master_brand":"/brand/1701/20/44b5fc2bdabd9356006b7b6bd118fe4c.jpg","sex":1,"is_doyen":0,"tel":"13000000000","id":1,"is_master":1}
         * id : 1
         * create_time : 0000-00-00 00:00:00
         */

        public Account.UserInfo user;
        public int              id;
        @JsonProperty("create_time")
        public String           createTime;

    }
}
