package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.AddCustomerModel;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/8/8 14:28
 */

public interface AddCustomerView extends WrapView{
    /**
     * 添加成功
     */
    void addSuccess(ArrayList<AddCustomerModel.FriendItem> item);

    /**
     * 添加失败
     */
    void addFail(String msg);
}
