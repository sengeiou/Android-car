package com.tgf.kcwc.ticket.apply;

import com.tgf.kcwc.mvp.model.ApplyTicketModel;
import com.tgf.kcwc.mvp.model.PreTicketModel;
import com.tgf.kcwc.mvp.model.TicketDescModel;

/**
 * Author:Jenny
 * Date:2017/11/6
 * E-mail:fishloveqin@gmail.com
 */

public interface DataCallback {


    public void loadForms(PreTicketModel formsModel);//预报名表单信息

    public void loadApplyList(ApplyTicketModel applyModel);//参展信息

    public void loadAD(String adText);//广告

    public void loadTicketDesc(TicketDescModel model);

}
