package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.MyTicketListModel;

/**
 * Author：Jenny
 * Date:2017/1/9 16:05
 * E-mail：fishloveqin@gmail.com
 */

public interface MyTicketListView extends WrapView {

    void showMyTickets(MyTicketListModel model);
}
