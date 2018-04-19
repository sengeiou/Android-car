package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.ResponseMessage;

/**
 * @anthor noti
 * @time 2017/9/14
 * @describle
 */
public interface ExhibitionDepositView extends WrapView {
    /**
     * 投诉成功
     */
    void depositSuccess(ResponseMessage message);

    /**
     * 投诉失败
     */
    void depositFail(ResponseMessage message);
}
