package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.BaseArryBean;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.DrivDetailsBean;
import com.tgf.kcwc.mvp.model.DrivingRoadBookBean;
import com.tgf.kcwc.mvp.model.QrcodeSiginBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface SignInView extends WrapView {
    void DetailsSucceed(DrivingRoadBookBean drivingRoadBookBean); //路书签到数据返回成功
    void CreateChecksSucceed(QrcodeSiginBean qrcodeSiginBean); //二维码数据返回成功
    void EndAcitivitySucceed(BaseArryBean baseBean); //取消活动成功
    void LightenSucceed(BaseBean baseBean); //取消活动成功
    void detailsDataFeated(String msg); //数据返回失败
}
