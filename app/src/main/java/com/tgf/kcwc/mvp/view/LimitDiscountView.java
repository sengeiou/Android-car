package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.DiscountLimitModel;

import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/4/25 0025
 * E-mail:hekescott@qq.com
 */

public interface LimitDiscountView extends WrapView {
    void showLimitList(List<DiscountLimitModel.LimitDiscountItem> limitModellist);

    void showLimitListError();
}
