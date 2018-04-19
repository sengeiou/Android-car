package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tgf.kcwc.util.URLUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：Jenny
 * Date:2017/1/20 11:50
 * E-mail：fishloveqin@gmail.com
 * 评论/评价Header信息
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment {
    @JsonProperty("text")
    public String           content;

    @JsonProperty("imgs")
    public List<String>     imgs;
    @JsonProperty("status")
    public int              status;
    @JsonProperty("fab_num")
    public int           fabNum;

    @JsonProperty("star")
    public String           star;
    @JsonProperty("tag")
    public String           tag;
    @JsonProperty("time")
    public String           time;
    @JsonProperty("sender_info")
    public Account.UserInfo senderInfo;
    @JsonProperty("receiver_info")
    public Account.UserInfo receiverInfo;
    @JsonProperty("reply")
    public List<Comment>    replies;

    @JsonProperty("is_fab")
    public int              isFab;
    @JsonProperty("resource_id")
    public int              resourceId;

    @JsonProperty("reply_count")
    public int              repliesCount;
    @JsonProperty("reply_nickname")
    public String              replyNickname;

    public int              id;

    private List<String>    listImgs = new ArrayList<>();

    public List<String> getImglist() {
        if (imgs != null && listImgs.size() == 0) {
            for (String img : imgs) {
                listImgs.add(URLUtil.builderImgUrl(img, 360, 360));
            }
        }
        return listImgs;
    }
}
