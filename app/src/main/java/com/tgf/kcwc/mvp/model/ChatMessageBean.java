package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author：Jenny
 * Date:2017/2/28 20:58
 * E-mail：fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatMessageBean {

    /**
     * type : receive
     * createtime : 2017-02-28 12:00:40
     * id : 24
     * book_line_content_list : 这个车卖不卖
     */

    public int  type;
    @JsonProperty("createtime")
    public String createTime;
    public int    id;
    public String content;
    public  int msgType;
}
