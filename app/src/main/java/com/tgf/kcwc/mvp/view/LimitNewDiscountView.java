package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.DiscountLimitModel;
import com.tgf.kcwc.mvp.model.DiscountLimitNewModel;
import com.tgf.kcwc.mvp.model.LimitDiscountEveModel;

import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/4/25 0025
 * E-mail:hekescott@qq.com
 */

public interface LimitNewDiscountView extends WrapView {
    void showLimitList(List<DiscountLimitNewModel.LimitDiscountItem> limitModellist);
    void showEveLimitList(LimitDiscountEveModel data);

    void showLimitListError();
}
