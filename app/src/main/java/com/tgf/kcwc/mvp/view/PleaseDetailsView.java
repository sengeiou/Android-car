package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.AppListBean;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.DrivingRoadBookBean;
import com.tgf.kcwc.mvp.model.PleaseDetailsBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface PleaseDetailsView extends WrapView {

    void dataListSucceed(PleaseDetailsBean pleaseDetailsBean); //返回数据列表成功

    void DetailsSucceed(DrivingRoadBookBean drivingRoadBookBean); //返回路书数据列表成功

    void ApplyCancelSucceed(BaseBean baseBean); //取消报名成功

    void dataListDefeated(String msg); //列表数据返回失败
}
