package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.BaseArryBean;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.ContinueBean;
import com.tgf.kcwc.mvp.model.DrivingRoadBookBean;
import com.tgf.kcwc.mvp.model.QrcodeSiginBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface ContinueView extends WrapView {
    void DetailsSucceed(ContinueBean baseBean); //续接签到数据返回成功
    void DetaSucceed(BaseBean baseBean); //续接签到数据返回成功
    void detailsDataFeated(String msg); //数据返回失败
}
