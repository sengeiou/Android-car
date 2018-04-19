package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.AppListBean;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.DrivDetailsBean;
import com.tgf.kcwc.mvp.model.DrivingRoadBookBean;
import com.tgf.kcwc.mvp.model.DrvingListModel;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface DrivingDetailsView extends WrapView {
    void detailsDataSucceed(DrivDetailsBean drivDetailsBean); //数据返回成功
    void DetailsSucceed(DrivingRoadBookBean drivingRoadBookBean); //路书签到数据返回成功
    void ApplyCancelSucceed(BaseBean baseBean); //取消报名成功
    void detailsDataFeated(String msg); //数据返回失败
}
