package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author：Jenny
 * Date:2017/1/20 15:34
 * E-mail：fishloveqin@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentModel {

    @JsonProperty("list")
    public List<Comment> comments;

    @JsonProperty("count")
    public  int count;

    @JsonProperty("avg_star")
    public  String avgStar;
}
