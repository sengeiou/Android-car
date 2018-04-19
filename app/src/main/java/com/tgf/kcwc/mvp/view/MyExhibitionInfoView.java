package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.MyExhibitionInfoModel;

/**
 * @anthor noti
 * @time 2017/10/9
 * @describle
 */
public interface MyExhibitionInfoView extends WrapView {
    /**
     * 显示展会信息
     * @param model
     */
    void showInfo(MyExhibitionInfoModel model);
}
