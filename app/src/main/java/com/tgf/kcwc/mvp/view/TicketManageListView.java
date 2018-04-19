package com.tgf.kcwc.mvp.view;


import com.tgf.kcwc.mvp.model.TicketManageListModel;

/**
 * Auther: Scott
 * Date: 2017/2/6 0006
 * E-mail:hekescott@qq.com
 */

public interface TicketManageListView extends WrapView {


    void showTicketList(TicketManageListModel data);
}
