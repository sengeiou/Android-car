package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.BaseArryBean;
import com.tgf.kcwc.mvp.model.IntegralConversionGoodDetailsBean;
import com.tgf.kcwc.mvp.model.IntegralExchangeBean;
import com.tgf.kcwc.mvp.model.IntegralGoodListBean;
import com.tgf.kcwc.mvp.model.IntegralListBean;
import com.tgf.kcwc.mvp.model.IntegralUserinfoBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface IntegralConversionView extends WrapView {

    void userDataSucceed(IntegralUserinfoBean integralUserinfoBean); //返回用户数据成功

    void DatalistSucceed(IntegralGoodListBean integralGoodListBean); //返回数据列表成功

    void DataDetailsSucceed(IntegralConversionGoodDetailsBean integralConversionGoodDetailsBean); //返回详情数据成功

    void DataBuyProductSucceed(IntegralExchangeBean baseBean); //兑换成功

    void dataListDefeated(String msg); //列表数据返回失败
}
