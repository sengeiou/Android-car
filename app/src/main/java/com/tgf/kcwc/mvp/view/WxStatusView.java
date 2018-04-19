package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.WxStatusModel;

/**
 * Created by Administrator on 2017/11/14.
 */

public interface WxStatusView extends WrapView {
    void wxStatusSuccess(String model);
    void wxStatusFail(String model);
}
