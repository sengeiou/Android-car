package com.tgf.kcwc.mvp.view;


import com.tgf.kcwc.mvp.model.TicketSendObjModel;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/2/6 0006
 * E-mail:hekescott@qq.com
 */

public interface TicketSendObjView extends WrapView {


    void showSendUserList(ArrayList<TicketSendObjModel.User> tickeUserlist);

    void showSendTicketSuccess(int pos);

    void faildeMessage(String statusMessage);
}
