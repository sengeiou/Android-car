package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.BuyTicketModel;

/**
 * Author：Jenny
 * Date:2017/1/10 16:00
 * E-mail：fishloveqin@gmail.com
 */

public interface BuyTicketView extends WrapView {

    void showBuyTickets(BuyTicketModel model);

    void generateOrderSuccess(String orderId);
    void generateOrderFailure(String msg);
}
