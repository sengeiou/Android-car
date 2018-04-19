package com.tgf.kcwc.mvp.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Administrator on 2017/4/21.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransmitWinningCrunchiesBean {
    public int    code;

    public String msg;

    public Data   data;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        public List<DataList> list;

        public int            count;

        public Me             me;

        public String         time;

    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataList {
        public int      id;

        public String   score;
        @JsonProperty("prize_name_sub")
        public String   prizeNameSub;

        public int      uid;
        @JsonProperty("user_info")
        public UserInfo userInfo;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserInfo {
        public int    id;

        public String nickname;

        public String avatar;

        public int sex;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Me {
        public int    id;

        public int    index;

        public String avatar;

        public String nickname;

        public int    score;
        @JsonProperty("is_join")
        public int    isJoin;

        public int    sex;
    }

}
