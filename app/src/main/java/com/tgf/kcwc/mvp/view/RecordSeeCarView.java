package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.RecordActivityModel;
import com.tgf.kcwc.mvp.model.RecordCouponModel;
import com.tgf.kcwc.mvp.model.RecordSeeCarModel;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/8/14
 * @describle
 */
public interface RecordSeeCarView extends WrapView{
    /**
     * 显示看车
     * @param list
     */
    void showSeeCar(ArrayList<RecordSeeCarModel.ListItem> list);
    /**
     * 显示看车头部
     * @param userItem
     */
    void showSeeCarHead(RecordSeeCarModel.UserItem userItem);
}
