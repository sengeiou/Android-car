package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.ExhibitionApplyModel;

/**
 * @anthor noti
 * @time 2017/9/15
 * @describle
 */
public interface ExhibitionApplyView extends WrapView {
    /**
     * 提交成功
     * @param model
     */
    void commitApplySuccess(ExhibitionApplyModel model);

    /**
     * 提交失败
     */
    void commitApplyFail(ExhibitionApplyModel model);
}
