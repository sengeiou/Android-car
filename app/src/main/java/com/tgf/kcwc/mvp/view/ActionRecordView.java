package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.ActionRecordModel;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/8/1 11:21
 */

public interface ActionRecordView extends WrapView{
    /**
     * 显示行为分析
     * @param listItem
     */
    void showActionRecord(ActionRecordModel listItem);
}
