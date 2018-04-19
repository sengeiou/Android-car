package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/1/13 0013
 * E-mail:hekescott@qq.com
 */

public class ExhibitionListModel {
    @JsonProperty("exhibition_list")
  public   ArrayList<Exhibition> exhibitionList;
}
