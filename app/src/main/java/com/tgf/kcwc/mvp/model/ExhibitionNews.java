package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Auther: Scott
 * Date: 2016/12/28 0028
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExhibitionNews {
    @JsonProperty("id")
    public int    id;
    @JsonProperty("title")
    public String title;
    @JsonProperty("create_time")
    public String date;
    @JsonProperty("copyfrom")
    public String copyfrom;
    @JsonProperty("author")
    public String author;
    @JsonProperty("cover")
    public String coverUrl;
    @JsonProperty("content")
    public  String article;
    @JsonProperty("clicks")
    public  int clicksNum;
    public int comment_num;
    public int is_praise;
}
