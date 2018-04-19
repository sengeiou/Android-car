package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tgf.kcwc.entity.NodeEntity;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/5/4 0004
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EditbookModel {
    @JsonProperty("book_desc")
    public String content;
    @JsonProperty("book_title")
    public String title;
    public String cover;
    public int status;
    public int book_status ;
    public String end_adds;
    public String start_adds;
    @JsonProperty("tag_list")
    public ArrayList<Topic> topiclist;
    @JsonProperty("node_list")
    public ArrayList<RoadLine> lineList;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RoadLine{
        public  int id;
        public String address;
        @JsonProperty("book_line")
        public NodeEntity nodeEntity;
        public String lng;
        public String lat;
    }
}
