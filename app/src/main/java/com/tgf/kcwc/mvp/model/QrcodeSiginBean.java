package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/5/31.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class QrcodeSiginBean {
    public int code;

    public String msg;

    public Data data;

    public static class Data {
        @JsonProperty("check_node_info")
        public CheckNodeInfo checkNodeInfo;
        @JsonProperty("qr_code")
        public String qrCode;
        @JsonProperty("already_check_user")
        public List<AlreadyCheckUser> alreadyCheckUser;
        @JsonProperty("not_check_user")
        public List<NotCheckUser> notCheckUser;

    }

    public static class CheckNodeInfo {
        public int order;

        public String address;
    }

    public static class AlreadyCheckUser {
        @JsonProperty("user_id")
        public int userId;
        @JsonProperty("add_time")
        public String addTime;

        public String nickname;

        public String avatar;
    }

    public static class NotCheckUser {

        @JsonProperty("user_id")
        public int userId;

        public String tel;

        public String nickname;

        public String avatar;
    }


}
