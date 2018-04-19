package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.SelectExhibitionModel;

/**
 * @anthor noti
 * @time 2017/9/13
 * @describle
 */
public interface ExhibitionView extends WrapView {
    /**
     * 显示展会列表
     * @param model
     */
    void showExhibitionList(SelectExhibitionModel model);
}
