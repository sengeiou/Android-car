package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.LimitDiscountNewDetailsLimitModel;
import com.tgf.kcwc.mvp.model.LimitDiscountNewDetailsModel;
import com.tgf.kcwc.mvp.model.SelectExbitionModel;

import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/4/25 0025
 * E-mail:hekescott@qq.com
 */

public interface LimitDiscountDetailsView extends WrapView {
    void showEveLimitList(LimitDiscountNewDetailsModel data);
    void showStoreLimitList(LimitDiscountNewDetailsLimitModel data);

    void showLimitListError();
}
