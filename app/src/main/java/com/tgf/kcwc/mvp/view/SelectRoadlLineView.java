package com.tgf.kcwc.mvp.view;

import java.util.List;

import com.tgf.kcwc.mvp.model.RideDataModel;

/**
 * Auther: Scott
 * Date: 2017/5/5 0005
 * E-mail:hekescott@qq.com
 */

public interface SelectRoadlLineView extends WrapView {
    void showRoadLines(List<RideDataModel.RideData> rideDataList);

    void showContinue(RideDataModel rideDataModel);
    void showUnStop(RideDataModel rideDataModel);
}
