package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.RideAutoData;
import com.tgf.kcwc.mvp.model.RideData;
import com.tgf.kcwc.mvp.model.RideLinePreviewModel;
import com.tgf.kcwc.mvp.model.RideReportDetailModel;

/**
 * Author:Jenny
 * Date:2017/11/28
 * E-mail:fishloveqin@gmail.com
 *
 * 驾途运行状态包装类
 */

public abstract class WrapperRidingRunView implements RidingRunView {


    public void ridingStart(ResponseMessage<RideData> responseMessage){}

    public void ridingPause(ResponseMessage<RideData> responseMessage){}

    public void ridingRestart(Object responseMessage){}

    public void rodeEnd(Object object){}

    public void autoCmtRideData(ResponseMessage<Object> responseMessage){}

    public void getRideState(RideAutoData data){}

    public void deleteRideData(Object object){}

    public void RideReportData(RideReportDetailModel rideReportDetailModel){}

    public void showloadRideDatas(RideLinePreviewModel rideLinePreviewModel){}

}
