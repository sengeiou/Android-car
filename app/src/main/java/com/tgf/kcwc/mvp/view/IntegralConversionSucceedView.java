package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.IntegralConversionGoodDetailsBean;
import com.tgf.kcwc.mvp.model.IntegralExchangeBean;
import com.tgf.kcwc.mvp.model.IntegralExchangeSucceedBean;
import com.tgf.kcwc.mvp.model.IntegralGoodListBean;
import com.tgf.kcwc.mvp.model.IntegralUserinfoBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface IntegralConversionSucceedView extends WrapView {

    void DataBuyProductSucceed(IntegralExchangeSucceedBean baseBean); //兑换成功

    void dataListDefeated(String msg); //列表数据返回失败
}
