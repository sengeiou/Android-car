package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Author：Scott
 * Date:2016/12/13 15:27
 * E-mail：hekescott@qq.com
 * 经销商列表
 */

public class OrganizationList {
    @JsonProperty("organization_list")
    public ArrayList<Organization> organizationList;
//    @JsonProperty("organization_info")
//    public Organization orgInfo;

        public static class Organization{

        @JsonProperty("id")
        private String id;
        @JsonProperty("has_coupon")
        public String  hasCoupon;
        @JsonProperty("distance")
        public String  distance;
        @JsonProperty("address")
        public String  address;
        @JsonProperty("phone")
        public String  phone;
//        @JsonProperty("title")
        public String  title;
    }



}
