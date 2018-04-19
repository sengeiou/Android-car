package com.tgf.kcwc.mvp.view;


import com.tgf.kcwc.mvp.model.TicketSendOrgRecordModel;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/10/24 0024
 * E-mail:hekescott@qq.com
 */

public interface SendOrgRecordView  extends  WrapView{
    void showSendOrgRecorList(ArrayList<TicketSendOrgRecordModel.OrgRecordItem> list);
}
