package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.IntegralOrderDetailBean;
import com.tgf.kcwc.mvp.model.OrderModel;
import com.tgf.kcwc.mvp.model.OrderPayParam;

/**
 * Author：Jenny
 * Date:2017/1/5 20:55
 * E-mail：fishloveqin@gmail.com
 */

public interface IntegralOrderDetailView extends WrapView {

    void showOrderDetails(IntegralOrderDetailBean model);

    void showZfbDetails(String model);

    void showWechatPayDetails(OrderPayParam model);

    void dataDefeated(String msg); //返回失败
}
