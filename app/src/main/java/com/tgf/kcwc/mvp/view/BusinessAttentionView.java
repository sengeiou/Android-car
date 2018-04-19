package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.FriendGroupingModel;
import com.tgf.kcwc.mvp.model.FriendListModel;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/8/7 13:54
 */

public interface BusinessAttentionView extends WrapView{
    /**
     * 展示分组
     */
    void showGrouping(ArrayList<FriendGroupingModel.ListItem> list);
    /**
     * 展示好友列表
     */
    void showFriendList(ArrayList<FriendListModel.ListItem> list);
}
