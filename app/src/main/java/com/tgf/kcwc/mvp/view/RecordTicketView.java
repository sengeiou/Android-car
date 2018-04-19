package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.RecordSeeCarModel;
import com.tgf.kcwc.mvp.model.RecordTicketModel;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/8/14
 * @describle
 */
public interface RecordTicketView extends WrapView{
    /**
     * 显示票证
     * @param list
     */
    void showTicket(ArrayList<RecordTicketModel.ListItem> list);
    /**
     * 显示票证头部
     * @param userItem
     */
    void showTicketHead(RecordTicketModel.UserItem userItem);
}
