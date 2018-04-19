package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.CarCertDetailModel;

/**
 * @anthor noti
 * @time 2017/10/18
 * @describle
 */
public interface CarCertDetailView extends WrapView{
    /**
     * 显示新增证件详情
     * @param model
     */
    void showCertDetail(CarCertDetailModel model);
}
