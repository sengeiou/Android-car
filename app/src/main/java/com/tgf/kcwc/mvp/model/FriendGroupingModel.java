package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/8/7 15:00
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FriendGroupingModel {
    @JsonProperty("code")
    public int statusCode;
    @JsonProperty("msg")
    public String statusMessage;
    @JsonProperty("data")
    public ArrayList<FriendGroupingModel.ListItem> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListItem {
        @JsonProperty("friend_group_id")
        public int friendGroupId;
        public String name;
        public boolean isSelect = false;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }
    }
}
