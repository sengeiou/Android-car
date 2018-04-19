package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.CustomizedCollectModel;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/8/18
 * @describle
 */
public interface CustomizedCollectView extends WrapView{
    /**
     * 显示定制
     * @param listItem
     */
    void showCustomized(ArrayList<CustomizedCollectModel.CustomMadeItem.ListItem> listItem);

    /**
     * 显示收藏
     * @param collectItem
     */
    void showCollect(CustomizedCollectModel.CollectItem collectItem);

}
