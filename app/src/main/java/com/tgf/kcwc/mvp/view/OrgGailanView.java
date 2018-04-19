package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.OrgGailanModel;
import com.tgf.kcwc.mvp.model.TicketmExhibitModel;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/10/17 0017
 * E-mail:hekescott@qq.com
 */

public interface OrgGailanView extends  WrapView{
    void showOrgGailanTongji(ArrayList<OrgGailanModel.HandTongji> handTongjilist);

    void showHead(TicketmExhibitModel ticketmExhibitModel);
}
