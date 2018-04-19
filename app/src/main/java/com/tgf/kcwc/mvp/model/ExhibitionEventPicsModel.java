package com.tgf.kcwc.mvp.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 展会活动图库
 * Auther: Scott
 * Date: 2017/1/19 0019
 * E-mail:hekescott@qq.com
 */

public class ExhibitionEventPicsModel {
    @JsonProperty("title")
    public String   title;
    @JsonProperty("start_time")
    public String   startTime;
    @JsonProperty("end_time")
    public String   endTime;
    @JsonProperty("img_list")
    public ImgePage imgPage;

    public static class ImgePage extends BasePageModel {
        @JsonProperty("list")
        public ArrayList<ImgItem> imglist;
    }

}
