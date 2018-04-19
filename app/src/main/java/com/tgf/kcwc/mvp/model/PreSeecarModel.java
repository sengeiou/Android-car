package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/3/9 0009
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PreSeecarModel {

    public ArrayList<PreSeecarItem> list;
   public Pagination pagination;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PreSeecarItem{
        public String avatar;
        public int id;
        public String order_sn;
        public int order_id;
        public int org_id;
        public int sale_id;
        @JsonProperty("im_username")
        public String user_name;
        public String offer;
        public String integral;
        public int type;
        public int msg_num;
        public String create_time;
        public String off_note;
        public String contact;
        public String tel;
        public String car_name;
        public int status;
        @JsonProperty("out_color_name")
        public String outColorName;
        @JsonProperty("in_color_name")
        public String inColorName;
    }

}
