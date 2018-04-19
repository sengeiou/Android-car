package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.RecordActivityModel;
import com.tgf.kcwc.mvp.model.RecordCouponModel;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/8/14
 * @describle
 */
public interface RecordCouponView extends WrapView{
    /**
     * 显示代金卷
     * @param list
     */
    void showCoupon(ArrayList<RecordCouponModel.ListItem> list);
    /**
     * 显示代金卷头部
     * @param userItem
     */
    void showCouponHead(RecordCouponModel.UserItem userItem);
}
