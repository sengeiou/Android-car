package com.tgf.kcwc.mvp.view;


import com.tgf.kcwc.mvp.model.CheckSendSeeModel;
import com.tgf.kcwc.mvp.model.Event;
import com.tgf.kcwc.mvp.model.HeadEvent;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/2/13 0013
 * E-mail:hekescott@qq.com
 */

public interface TicketSendSeeView extends WrapView {

    void showSendSeehead(HeadEvent event);

    void showSendTicketSuccess();

    void showCheckTicket(ArrayList<CheckSendSeeModel> checkSendSeeModelArrayList);

    void failedMessage(String statusMessage);
}