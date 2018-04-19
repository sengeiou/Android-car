package com.tgf.kcwc.mvp.view;

/**
 * @anthor noti
 * @time 2017/8/15 18:05
 */

public interface EditCustomerView extends WrapView{
    /**
     * 保存资料成功
     */
    void saveSuccess();

    /**
     * 保存资料失败
     * @param msg
     */
    void saveFail(String msg);
}
