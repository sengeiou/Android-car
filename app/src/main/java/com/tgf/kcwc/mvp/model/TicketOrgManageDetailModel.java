package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tgf.kcwc.logger.Logger;

/**
 * Auther: Scott
 * Date: 2017/10/19 0019
 * E-mail:hekescott@qq.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketOrgManageDetailModel {

    public Details details;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Details {
        public String event_name;
        public String event_cover;
        public String ticket_name;
        public String price;
        public String color;
        public String remarks;
        public int id;
        public int nums;
        public int have_nums;
        public int ht_nums_saler;
        public int ht_nums_org;
        public int receive_nums_all_person;
        public int use_nums_person;
        public int use_nums;
        public int receive_nums_all;
        public int ht_user_nums_all;
        public int status;
        public int event_id;
        public int user_have_nums;
        public String source;

        public String getNums() {
            if(nums ==-1){
                return "不限";
            }else {
                return String.valueOf(nums);
            }
        }
        public String getHaveNums() {
            if(have_nums==-1){
                return  "不限";
            }else {
                return   String.valueOf(have_nums);
            }
        }
        public String getUserHaveNums() {
            if(have_nums==-1){
                return  "不限";
            }else {
                return   String.valueOf(user_have_nums);
            }
        }

        public String getPrice() {

            return   price.split("\\.")[0];
        }
    }

}
