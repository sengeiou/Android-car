package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tgf.kcwc.entity.RichEditorEntity;

import java.util.List;

/**
 * Author：Jenny
 * Date:2017/3/6 15:23
 * E-mail：fishloveqin@gmail.com
 * 帖子详情模型
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class TopicDetailModel {

    /**
     * digg_count : 0
     * like_count : 0
     * create_time : 2017-03-03 15:54:24
     * book_line_content_list : {"mEditorDatas":[{"inputStr":"第一段","imageUrl":""},{"inputStr":"第二段","imageUrl":"/thread/1703/03/f5448a48657b009bb30e1cf0cc2bbae4.jpg"},{"inputStr":"第三段","imageUrl":"/thread/1703/03/d2f44920d41e70dbaa6428a884c44947.jpg"}]}
     * create_user : {"avatar":"","is_master":0,"age":0,"is_model":0,"id":126,"sex":1,"is_doyen":0,"nickname":"","tel":"18716651953"}
     * title : 普通帖子测试
     * topic : [{"id":14,"title":"骑车去云南"},{"id":15,"title":"骑车去西藏"}]
     * id : 428
     * reply_count : 0
     * is_digest : 0
     * view_count : 5
     */

    @JsonProperty("digg_count")
    public int diggCount;
    @JsonProperty("like_count")
    public int likeCount;
    @JsonProperty("create_time")
    public String createTime;
    public RichEditorEntity content;
    @JsonProperty("create_user")
    public Account.UserInfo user;
    @JsonProperty("title")
    public String title;
    @JsonProperty("id")
    public int id;
    @JsonProperty("reply_count")
    public int replyCount;
    @JsonProperty("is_digest")
    public int isDigest;
    @JsonProperty("view_count")
    public int viewCount;

    @JsonProperty("topic")
    public List<Topic> topic;

    @JsonProperty("honour")
    public List<Honour> honours;

    @JsonProperty("longitude")
    public String longitude;
    @JsonProperty("latitude")
    public String latitude;

    @JsonProperty("local_address")
    public String address;

    @JsonProperty("distance")
    public String distance;

    @JsonProperty("group")
    public Group group;

    @JsonProperty("is_praise")
    public int isPraise;

    @JsonProperty("is_creator")
    public int isCreator;

    @JsonProperty("is_collect")
    public int isCollect;

    /**
     * icon :
     * text : 此帖被“管理员”设为精华帖
     * substr :
     * tag :
     * integral : +10
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Honour {
        public String icon;
        public String text;
        public String substr;
        public String tag;
        public String integral;
        public String iconfont;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Group {

        public int id;
        public String cover;
        public String name;
        public String desc;

    }

    @JsonProperty("content")
    public RichEditorEntity describe;
}
