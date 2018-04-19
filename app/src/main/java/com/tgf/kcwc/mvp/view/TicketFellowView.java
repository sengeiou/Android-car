package com.tgf.kcwc.mvp.view;


import com.tgf.kcwc.mvp.model.TicketFellowModel;

/**
 * Auther: Scott
 * Date: 2017/2/9 0009
 * E-mail:hekescott@qq.com
 */

public interface TicketFellowView extends WrapView {
   void showTickeFellow(TicketFellowModel ticketFellowModel);

    void showSendTicketSuccess();

    void faildeMessage(String statusMessage);
}
