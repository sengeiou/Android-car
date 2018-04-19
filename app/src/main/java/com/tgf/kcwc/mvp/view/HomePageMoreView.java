package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.BannerModel;
import com.tgf.kcwc.mvp.model.HomeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/5/15 0015
 * E-mail:hekescott@qq.com
 */

public interface HomePageMoreView extends WrapView {
    void showBannerView(List<BannerModel.Data> data);
}
