package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/10/18 0018
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrgDaifaModel {

    public ArrayList<UserInfo> list;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserInfo{
        @JsonProperty("org_name")
        public String realName;
        @JsonProperty("org_logo")//// TODO: 2017/10/18 0018 差字段
        public String avatar;
        @JsonProperty("list")
        public ArrayList<FenfaStatistics> fenfaStatisticslist;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FenfaStatistics{
        public String name;
        public String color;
        public int nums;
        @JsonProperty("have_nums")
        public int haveNums;
        @JsonProperty("ht_user_nums_all")
        public int htUserNumsAll;
        @JsonProperty("receive_nums_all")
        public int receiveNumsAll;
        @JsonProperty("use_nums")
        public int useNums;


        public String getNums() {
            if(nums==-1){
                return  "不限";
            }else {
                return   String.valueOf(nums);
            }
        }

        public String getHaveNums() {
            if(haveNums==-1){
                return  "不限";
            }else {
                return   String.valueOf(haveNums);
            }
        }
    }
}
