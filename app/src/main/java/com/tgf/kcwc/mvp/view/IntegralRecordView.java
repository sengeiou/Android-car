package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.IntegralListBean;
import com.tgf.kcwc.mvp.model.IntegralPurchaseRecordListBean;
import com.tgf.kcwc.mvp.model.IntegralRecordBean;
import com.tgf.kcwc.mvp.model.IntegralRecordListBean;
import com.tgf.kcwc.mvp.model.IntegralUserinfoBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface IntegralRecordView extends WrapView {

    void DatalistSucceed(IntegralRecordListBean integralListBean); //返回数据列表成功

    void RecordSucceed(IntegralRecordBean integralListBean); //返回详情成功

    void PurchaseRecordSucceed(IntegralPurchaseRecordListBean integralListBean); //返回购买数据列表成功

    void dataListDefeated(String msg); //列表数据返回失败
}
