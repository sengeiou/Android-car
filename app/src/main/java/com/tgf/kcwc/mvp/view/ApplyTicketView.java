package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.ApplyTicketModel;
import com.tgf.kcwc.mvp.model.PreTicketModel;
import com.tgf.kcwc.mvp.model.TicketDescModel;

/**
 * Author:Jenny
 * Date:2017/11/6
 * E-mail:fishloveqin@gmail.com
 */

public interface ApplyTicketView extends WrapView {


    public void showForms(PreTicketModel formsModel);//显示预报名表单信息

    public void showApplyList(ApplyTicketModel applyModel);//显示参展信息

    public void showAD(String adText);//显示广告

    public void showTicketDesc(TicketDescModel model);//票的描述
}
