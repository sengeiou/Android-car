package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.BaseArryBean;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.IntegralOrderPayParam;
import com.tgf.kcwc.mvp.model.OrderPayModel;
import com.tgf.kcwc.mvp.model.ResponseMessage;

/**
 * @anthor noti
 * @time 2017/9/18
 * @describle
 */
public interface IntegraOrderPaysView extends WrapView {
    /**
     * 显示积分订单详情
     *
     * @param model
     */
    void showIntegralOrderDetail(BaseArryBean model);
}
