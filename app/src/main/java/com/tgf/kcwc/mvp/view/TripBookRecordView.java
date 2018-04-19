package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.TripBookRecordModel;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/9/29
 * @describle
 */
public interface TripBookRecordView extends WrapView {
    /**
     * 显示路书记录
     * @param model
     */
    void showTripBookRecord(ArrayList<TripBookRecordModel.Data> model);
}
