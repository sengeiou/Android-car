package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author:Jenny
 * Date:2017/9/14
 * E-mail:fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class TopicSearchModel {


    @JsonProperty("match_num")
    public int matchNum;

    public List<Topic> list;


}
