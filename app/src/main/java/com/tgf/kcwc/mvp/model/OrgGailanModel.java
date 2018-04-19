package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/10/17 0017
 * E-mail:hekescott@qq.com
 */

public class OrgGailanModel {
    @JsonProperty("event_info")
    public  TicketmExhibitModel ticketmExhibitModel;
    @JsonProperty("list")
    public ArrayList<HandTongji> handTongjilist;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HandTongji{

        public String name;
        public String color;
        public int nums;
        @JsonProperty("have_nums")
        public int haveNums;
        @JsonProperty("ht_nums_saler")
        public int htNumsSaler;
        @JsonProperty("ht_nums_org")
        public int htNumsOrg;
        @JsonProperty("receive_nums_all")
        public int receiveNumsAll;
        @JsonProperty("ht_user_nums_all")
        public int htUserNumsAll;
        @JsonProperty("use_nums")
        public int useNums;
        @JsonProperty("receive_nums_all_person")
        public int receiveNumsAllPerson;
        @JsonProperty("ht_user_nums_all_person")
        public int htUserNumsAllPerson;
        @JsonProperty("use_nums_person")
        public int useNumsPerson;

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
