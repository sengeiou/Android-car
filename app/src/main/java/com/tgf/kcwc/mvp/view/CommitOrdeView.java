package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.CarColor;

import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/2/20 0020
 * E-mail:hekescott@qq.com
 */

public interface CommitOrdeView extends WrapView {

    void showSuccess(int orderId);
    void showCommitOrderFailed(String msg);

    void showOutLook(List<CarColor> images);

    void showInLook(List<CarColor> data);
}
