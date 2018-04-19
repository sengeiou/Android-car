package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Auther: Scott
 * Date: 2017/3/29 0029
 * E-mail:hekescott@qq.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class CarOwnerSalerModel {
    public int id;
    public String title;
    public String cover;
    public String price;
    public int buy_year;
    public String road_haul;
}
