package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.TicketDetailModel;

/**
 * Author：Jenny
 * Date:2017/1/9 16:05
 * E-mail：fishloveqin@gmail.com
 */

public interface FreeTicketDetailView extends WrapView {

    void showFreeTicketDetail(TicketDetailModel model);

    void receiveTicket(boolean isSuccess);
}
