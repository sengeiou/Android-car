package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author:Jenny
 * Date:2017/5/18
 * E-mail:fishloveqin@gmail.com
 * 用户动态
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDynamicModel {

    /**
     * list : [{"model":"测试内容5i74","view_count":0,"reply_count":0,"time":"02-12","id":328,"cover":"/coupon/1701/12/647529cb5ac96e101959212617c53f78.jpg","title":"骑车去西藏","like_count":0},{"model":"测试内容5i74","view_count":0,"reply_count":0,"time":"01-17","id":327,"cover":"/coupon/1701/12/647529cb5ac96e101959212617c53f78.jpg","title":"骑车去云南","like_count":0}]
     * is_own : 0
     * count : 2
     */

    @JsonProperty("is_own")
    public int            isOwn;
    public int            count;
    public List<TopicBean> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TopicBean {
        /**
         * model : 测试内容5i74
         * view_count : 0
         * reply_count : 0
         * time : 02-12
         * id : 328
         * cover : /coupon/1701/12/647529cb5ac96e101959212617c53f78.jpg
         * title : 骑车去西藏
         * like_count : 0
         */

        public String model;
        @JsonProperty("view_count")
        public int    viewCount;
        @JsonProperty("reply_count")
        public int    replyCount;

        @JsonProperty("create_time")
        public String time;
        public int    id;
        public String cover;
        public String title;
        @JsonProperty("like_count")
        public int    likeCount;
    }
}
