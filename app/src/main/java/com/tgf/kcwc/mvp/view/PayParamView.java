package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.PayParamModel;

/**
 * @anthor noti
 * @time 2017/9/21
 * @describle
 */
public interface PayParamView extends WrapView {
    void payParamSuccess(PayParamModel.Data data);
    void payParamFail(PayParamModel model);
}
