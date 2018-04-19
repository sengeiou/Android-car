package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/10/23 0023
 * E-mail:hekescott@qq.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketSendOrgObjModel {

    public ArrayList<SendOrgObj> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SendOrgObj{
        public int id;
        public int nums;
        public int ht_user_nums;
        public int transfer_nums;
        public int have_nums;
        public int receive_nums;
        public int use_nums;
        public int event_id;
        public String real_name;
        public String tel;
        public String avatar;
        public int user_id;
    }

}
