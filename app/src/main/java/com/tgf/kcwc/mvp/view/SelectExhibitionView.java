package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.DiscountLimitNewModel;
import com.tgf.kcwc.mvp.model.LimitDiscountEveModel;
import com.tgf.kcwc.mvp.model.SelectExbitionModel;

import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/4/25 0025
 * E-mail:hekescott@qq.com
 */

public interface SelectExhibitionView extends WrapView {
    void showEveLimitList(List<SelectExbitionModel> data);

    void showLimitListError();
}
