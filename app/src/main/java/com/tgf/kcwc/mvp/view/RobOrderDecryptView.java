package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.OrderProcessModel;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/7/31 11:26
 */

public interface RobOrderDecryptView extends WrapView {
    /**
     * 显示解密订单进度
     */
    void showOrderProcess(ArrayList<OrderProcessModel.OrderProcessItem> list);
    /**
     * 显示用户数据
     */
    void showUserData(OrderProcessModel.UserItem userItems);
}
