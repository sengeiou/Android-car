package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.SearchModel;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/8/18
 * @describle
 */
public interface SearchView extends WrapView{
    /**
     * 显示品牌,显示价格
     * @param searchModels
     */
    void showSearch(SearchModel searchModels);
}
