package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.BaseArryBean;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.IntegralPurchaseRecordListBean;
import com.tgf.kcwc.mvp.model.IntegralRecordBean;
import com.tgf.kcwc.mvp.model.IntegralRecordListBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface IntegralPublishView extends WrapView {

    void DataSucceed(BaseArryBean baseBean); //发布成功



    void dataListDefeated(String msg); //列表数据返回失败
}
