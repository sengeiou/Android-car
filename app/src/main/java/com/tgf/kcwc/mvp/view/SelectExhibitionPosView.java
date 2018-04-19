package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.SelectExhibitionPosModel;

/**
 * @anthor noti
 * @time 2017/9/18
 * @describle
 */
public interface SelectExhibitionPosView extends WrapView {
    /**
     * 显示展位相关
     * @param model
     */
    void showExhibitionPos(SelectExhibitionPosModel model);
}
