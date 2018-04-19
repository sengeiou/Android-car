package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tgf.kcwc.entity.DataItem;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/2/6 0006
 * E-mail:hekescott@qq.com
 */

public class CouponDistanceFilterModel {
    @JsonProperty("nearby")
   public ArrayList<DataItem> nearby;
    @JsonProperty("journey")
  public   ArrayList<DataItem> journey;
}
