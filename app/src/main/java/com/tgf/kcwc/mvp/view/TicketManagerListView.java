package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.TicketmExhibitModel;
import com.tgf.kcwc.mvp.model.TicketmListModel;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/10/16 0016
 * E-mail:hekescott@qq.com
 */

public interface TicketManagerListView extends WrapView {
    void showExhibitList(ArrayList<TicketmExhibitModel> ticketmExhibitModelList);

    void showHandTicketList(ArrayList<TicketmListModel.HandleTicket> handleList);

    void showHead(TicketmExhibitModel eventInfo,TicketmListModel.Num nums);
}
