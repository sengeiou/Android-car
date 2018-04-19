package com.tgf.kcwc.mvp.model;

import com.tgf.kcwc.mvp.view.WrapView;

/**
 * @anthor noti
 * @time 2017/9/18
 * @describle
 */
public interface ExhibitionIntervalView extends WrapView {
    /**
     * 显示展位时段
     * @param model
     */
    void showExhibitionInterval(ExhibitionIntervalModel model);
}
