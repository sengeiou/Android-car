package com.tgf.kcwc.mvp.view;

        import com.tgf.kcwc.mvp.model.OrderModel;

/**
 * Author：Jenny
 * Date:2017/1/5 20:55
 * E-mail：fishloveqin@gmail.com
 */

public interface OrderDetailView extends WrapView {

    void showOrderDetails(OrderModel model);
}
