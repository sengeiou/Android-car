package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.OrderPayModel;

/**
 * @anthor noti
 * @time 2017/9/18
 * @describle
 */
public interface OrderPaysView extends WrapView {
    /**
     * 显示订单详情
     * @param model
     */
    void showOrderDetail(OrderPayModel model);
}
