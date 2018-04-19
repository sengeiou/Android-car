package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/8/7 15:00
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FriendListModel {
    @JsonProperty("pagination")
    public Pagination pagination;
    @JsonProperty("list")
    public ArrayList<ListItem> list;

    /**
     * "avatar": "/avatar/1703/24/ccab54caaf2b16ea94cc4405ff884c1c.jpg",
     * "id": 36,
     * "is_allot": 0,
     * "name": "许建平",
     * "source": "收藏车型"
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListItem {
        public String avatar;
        public int id;
        @JsonProperty("is_allot")
        public int isAllot;
        public String name;
        public String source;
        public boolean isSelect;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }
    }
}
