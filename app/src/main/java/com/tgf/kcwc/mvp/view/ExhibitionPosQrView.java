package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.ExhibitionPosQrModel;

/**
 * @anthor noti
 * @time 2017/10/10
 * @describle
 */
public interface ExhibitionPosQrView extends WrapView {
    /**
     * 打卡成功
     * @param model
     */
    void exhibitionSuccess(String model);

    /**
     * 打卡失败
     * @param model
     */
    void exhibitionFail(String model);
}
