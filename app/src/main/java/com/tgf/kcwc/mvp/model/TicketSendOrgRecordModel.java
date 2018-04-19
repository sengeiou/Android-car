package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/10/24 0024
 * E-mail:hekescott@qq.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketSendOrgRecordModel {

    public ArrayList<OrgRecordItem> list;

    public static class OrgRecordItem {
        public String time;

        public ArrayList<OrgRecordUser> items;
    }
    public static class OrgRecordUser {
        public int nums;
        public String real_name;
        public String tel;
        public String avatar;
        public int user_id;
    }

}
