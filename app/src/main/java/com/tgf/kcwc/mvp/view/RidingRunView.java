package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.RideAutoData;
import com.tgf.kcwc.mvp.model.RideData;
import com.tgf.kcwc.mvp.model.RideLinePreviewModel;
import com.tgf.kcwc.mvp.model.RideReportDetailModel;

/**
 * Author:Jenny
 * Date:2017/5/3
 * E-mail:fishloveqin@gmail.com
 */

public interface RidingRunView extends WrapView {

    /**
     * 驾途开始
     * @param responseMessage
     */
    public void ridingStart(ResponseMessage<RideData> responseMessage);

    /**
     * 驾途暂停
     * @param responseMessage
     */
    public void ridingPause(ResponseMessage<RideData> responseMessage);

    /**
     * 驾途继续
     * @param responseMessage
     */
    public void ridingRestart(Object responseMessage);

    /**
     * 驾途结束
     * @param object
     */
    public void rodeEnd(Object object);

    /**
     * 自动提交数据
     * @param responseMessage
     */
    public void autoCmtRideData(ResponseMessage<Object> responseMessage);

    /**
     * 获取驾途状态
     * @param data
     */
    public void getRideState(RideAutoData data);

    /**
     * 删除驾途状态
     * @param object
     */
    public void deleteRideData(Object object);

    public void RideReportData(RideReportDetailModel rideReportDetailModel);

    public void showloadRideDatas(RideLinePreviewModel rideLinePreviewModel);
}
