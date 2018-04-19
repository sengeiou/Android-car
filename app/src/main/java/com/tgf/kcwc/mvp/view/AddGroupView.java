package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.AddGroupModel;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/8/8 17:25
 */

public interface AddGroupView extends WrapView {
    /**
     * 加组成功
     * @param list
     */
    void addGroupSuccess(ArrayList<AddGroupModel.ListItem> list);

    /**
     * 加组失败
     * @param msg
     */
    void addGroupFail(String msg);
}
