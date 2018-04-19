package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 美女模特model
 * Auther: Scott
 * Date: 2016/12/29 0029
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Beauty {
        @JsonProperty("model_id")
        public   int modelId;
        @JsonProperty("id")
        public   int userId;
        @JsonProperty("model_name")
        public   String name;
        @JsonProperty("cover")
        public   String cover;
        @JsonProperty("brand_name")
        public   String brandName;
        @JsonProperty("brand_logo")
        public   String brandLogo;
        @JsonProperty("booth_name")
        public   String booth_name;
        @JsonProperty("avatar")
        public   String avatar;
        @JsonProperty("sex")
        public   int sex;
        @JsonProperty("fans_num")
        public   int fansNum;
        @JsonProperty("brand_id")
        public   int brand_id;
        @JsonProperty("booth_id")
        public   int boothId;
        @JsonProperty("is_binding")
        public   int isBinding;
        @JsonProperty("is_new")
        public   int isNew;
        @JsonProperty("is_follow")
        public   int isFollow;
        public   boolean isTitle;
        public boolean isSee =true;
}
