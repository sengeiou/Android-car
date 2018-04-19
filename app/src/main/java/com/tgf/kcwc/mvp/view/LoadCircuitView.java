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

public interface LoadCircuitView extends WrapView {

    public void RideReportData(RideReportDetailModel rideReportDetailModel);

    public void showloadRideDatas(RideLinePreviewModel rideLinePreviewModel);
}
