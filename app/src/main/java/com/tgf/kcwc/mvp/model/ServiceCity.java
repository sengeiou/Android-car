package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/1/13 0013
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceCity {
    public City select;
    public ArrayList<City> list;
   public static class City{
       public int id;
       public String adcode;
       //市
       public String name;
       //省
       public String alias;
   }
}
