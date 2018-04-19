package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：Jenny
 * Date:2017/4/5 16:30
 * E-mail：fishloveqin@gmail.com
 * 车系、车型图库模型
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelGalleryModel {

    /**
     * id : 1
     * name : 雅阁
     * nums : {"in":4,"out":0,"event":10}
     * in : [{"img_url":"/car/1703/30/70f28d695fae3d42021b45a6edccbe6f.jpg"},{"img_url":"/car/1703/30/d7124174b87310e08e637fbf7ff4b357.jpg"},{"img_url":"/car/1703/30/4743a5fbcb3be2423658cb41e6f1dc6a.jpg"},{"img_url":"/car/1703/30/4743a5fbcb3be2423658cb41e6f1dc6a.jpg"}]
     * out : []
     * event : [{"img_url":"/brand/1701/20/44b5fc2bdabd9356006b7b6bd118fe4c.jpg"},{"img_url":"/brand/1701/20/44b5fc2bdabd9356006b7b6bd118fe4c.jpg"},{"img_url":"/brand/1701/20/44b5fc2bdabd9356006b7b6bd118fe4c.jpg"},{"img_url":"/brand/1701/20/44b5fc2bdabd9356006b7b6bd118fe4c.jpg"},{"img_url":"/event/1703/21/0a8d735b734cfd2c7db770e0333e41bc.png"},{"img_url":"/store/1703/17/b3b0ae00ab952ef2b61f242d5a8ef70e.jpg"}]
     */

    public String           id;
    public String           name;
    public NumsBean         nums;
    public ArrayList<Image> in;
    public ArrayList<Image> out;
    public ArrayList<Image> event;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NumsBean {
        /**
         * in : 4
         * out : 0
         * event : 10
         */

        public int in;
        public int out;
        public int event;
    }

}
