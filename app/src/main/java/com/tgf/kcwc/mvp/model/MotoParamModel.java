package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/1/4 0004
 * E-mail:hekescott@qq.com
 */

public class MotoParamModel {
    @JsonProperty("param")
   public   List<MotoParamTitleItem> myparamList;
}
