package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.PayParamModel;

/**
 * Created by Administrator on 2017/11/13.
 */

public interface AlipayView extends WrapView {
    void alipaySuccess(String model);
    void alipayFail(String model);
}
