package com.tgf.kcwc.mvp.view;


import com.tgf.kcwc.mvp.model.TicketManageDetailModel;

/**
 * Auther: Scott
 * Date: 2017/2/6 0006
 * E-mail:hekescott@qq.com
 */

public interface TicketManageDetailView extends WrapView {


    void showTicketDetail(TicketManageDetailModel.Detail ticketManageDetailModel);

    void failedMessage(String localizedMessage);
}
