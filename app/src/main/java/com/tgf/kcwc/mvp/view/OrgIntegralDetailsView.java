package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.BaseArryBean;
import com.tgf.kcwc.mvp.model.OrgIntegralDetailsBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface OrgIntegralDetailsView extends WrapView {

    void DataSucceed(OrgIntegralDetailsBean baseBean); //发布成功



    void dataListDefeated(String msg); //列表数据返回失败
}
