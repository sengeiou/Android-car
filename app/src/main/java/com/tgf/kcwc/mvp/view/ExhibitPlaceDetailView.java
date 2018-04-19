package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.Beauty;
import com.tgf.kcwc.mvp.model.Brand;
import com.tgf.kcwc.mvp.model.ExhibitEvent;
import com.tgf.kcwc.mvp.model.ExhibitPlace;
import com.tgf.kcwc.mvp.model.NewCarBean;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/1/23 0023
 * E-mail:hekescott@qq.com
 */

public interface ExhibitPlaceDetailView extends  WrapView{
    void showHeadImg(ExhibitPlace exhibitPlace);
    void showBrandlist(ArrayList<Brand> includeBrandlist);
    void showNewMotolist(ArrayList<NewCarBean> includeBrandlist);
    void showModellist(ArrayList<Beauty> beautylist);
    void showEventlist(ArrayList<ExhibitEvent> beautylist);
    void showList(ArrayList<NewCarBean> newCarList,ArrayList<Beauty> modelList,ArrayList<ExhibitEvent> eventList);
}
