package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.IntegralPurchaseRecordListBean;
import com.tgf.kcwc.mvp.model.IntegralRecordBean;
import com.tgf.kcwc.mvp.model.IntegralRecordListBean;
import com.tgf.kcwc.mvp.model.OrgIntegralRecordListBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface OrgIntegralRecordView extends WrapView {

    void DatalistSucceed(OrgIntegralRecordListBean integralListBean); //返回数据列表成功

    void RecordSucceed(IntegralRecordBean integralListBean); //返回详情成功

    void PurchaseRecordSucceed(IntegralPurchaseRecordListBean integralListBean); //返回购买数据列表成功

    void dataListDefeated(String msg); //列表数据返回失败
}
