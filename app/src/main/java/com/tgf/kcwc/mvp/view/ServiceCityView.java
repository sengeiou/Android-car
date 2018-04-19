package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.ServiceCity;

import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/1/13 0013
 * E-mail:hekescott@qq.com
 */

public interface ServiceCityView extends  WrapView{
    void showOpenlist(List<ServiceCity.City> serviceList);

    void showSelcetCity(ServiceCity.City select);
}
