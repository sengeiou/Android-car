package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.IntegralDiamondListBean;
import com.tgf.kcwc.mvp.model.IntegralPurchaseBean;
import com.tgf.kcwc.mvp.model.OrgDetailsBean;
import com.tgf.kcwc.mvp.model.ResponseMessage;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface OrgIntegralDiamondView extends WrapView {

    void userDataSucceed(OrgDetailsBean integralUserinfoBean); //返回用户数据成功

    void DatalistSucceed(IntegralDiamondListBean integralGoodListBean); //返回数据列表成功

    void DataPurchaseSucceed(IntegralPurchaseBean baseBean); //购买详情
    void DataOrdeSucceed(ResponseMessage<String> baseBean); //生成订单

    void dataListDefeated(String msg); //列表数据返回失败
}
