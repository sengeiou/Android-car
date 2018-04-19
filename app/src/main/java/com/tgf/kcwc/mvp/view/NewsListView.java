package com.tgf.kcwc.mvp.view;

import java.util.List;

import com.tgf.kcwc.mvp.model.ExhibitionNews;


/**
 * Auther: Scott
 * Date: 2017/1/18 0018
 * E-mail:hekescott@qq.com
 */

public  interface NewsListView extends WrapView {
    void showEventList(List<ExhibitionNews> exhibitionNewsList);
    void showNomore(String msg);

}
