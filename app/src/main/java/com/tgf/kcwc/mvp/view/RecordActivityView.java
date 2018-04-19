package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.RecordActivityModel;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/8/14
 * @describle
 */
public interface RecordActivityView extends WrapView{
    /**
     * 显示活动列表
     * @param list
     */
    void showActivity(ArrayList<RecordActivityModel.ListItem> list);

    /**
     * 显示活动头部
     * @param userItem
     */
    void showActivityHead(RecordActivityModel.UserItem userItem);
}
