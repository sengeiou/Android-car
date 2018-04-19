package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.AddCustomerModel;

/**
 * @anthor Administrator
 * @time 2017/8/9
 * @describle
 */
public interface GroupMoveView extends WrapView{
    /**
     * 组间移动成功
     * @param list
     */
    void moveSuccess(AddCustomerModel list,int position);

    /**
     * 组间移动失败
     * @param msg
     */
    void moveFail(String msg);
}
