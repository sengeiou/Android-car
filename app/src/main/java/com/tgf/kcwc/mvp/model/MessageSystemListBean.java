package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/5/19.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageSystemListBean {
    public int code;

    public String msg;

    public Data data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        @JsonProperty("pagination")
        public Pagination pagination;
        public List<DataList> lists;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataList {
        public int id;

        public String title;

        public String content;

        public String createtime;
        @JsonProperty("sender_uid")
        public int senderUid;

        public int user_id;

        public int type;

        public int sex;

        public List<Logo> logo;

        public List<Keylist> keylist;
        @JsonProperty("tips_text")
        public List<TipsText> tipsText;

        public List<Jumplist> jumplist;

        public int postingid;

        public String postinglogo;

        public String postingtitle;

        public String postingoutlining;

        public String model;

        public String label;

        public boolean isDelete = false;

        public boolean isShow = false;

        public Reply reply;
        @JsonProperty("app_content")
        public AppContent appContent;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TipsText {

        public String id;
        @JsonProperty("message_id")
        public String messageId;

        public String label;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AppContent {
        @JsonProperty("pattern_content")
        public String patternContent;

        public List<Content> content;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Content {
        public String text;

        public String color;
        @JsonProperty("url_alias")
        public int urlAlias;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Logo {
        public int id;
        @JsonProperty("link_url")
        public String linkUrl;
        @JsonProperty("link_tag")
        public String linkTag;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Reply {
        @JsonProperty("receiver_uid")
        public String receiverUid;

        public String module;
        @JsonProperty("resource_id")
        public String resourceId;
        @JsonProperty("vehicle_type")
        public String vehicleType;

        public String pid;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Keylist {
        public int id;

        public String key;

        public String val;

        public String colour;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Jumplist {
        public int id;

        public String url;
        @JsonProperty("url_id")
        public int urlId;
        @JsonProperty("url_name")
        public String urlName;
        @JsonProperty("url_alias")
        public int urlAlias;
    }
}
