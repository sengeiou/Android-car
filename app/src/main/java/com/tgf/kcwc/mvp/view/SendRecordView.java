package com.tgf.kcwc.mvp.view;


import com.tgf.kcwc.mvp.model.TicketSendRecordModel;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/2/8 0008
 * E-mail:hekescott@qq.com
 */

public interface SendRecordView extends WrapView {
    void showSendRecorList(ArrayList<TicketSendRecordModel.RecordItem> senRecordList);
}
