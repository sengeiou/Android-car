package com.tgf.kcwc.mvp.view;

import java.util.List;

import com.tgf.kcwc.mvp.model.DiscountGiftModel;
import com.tgf.kcwc.mvp.model.DiscountLimitModel;

/**
 * Auther: Scott
 * Date: 2017/4/25 0025
 * E-mail:hekescott@qq.com
 */

public interface DiscountGiftView extends WrapView {
    void showLimitList(List<DiscountGiftModel.DiscountGiftItem> limitModellist);
}
