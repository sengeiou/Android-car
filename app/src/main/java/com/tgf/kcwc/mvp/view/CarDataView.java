package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.CarBean;

import java.util.List;

/**
 * Author：Jenny
 * Date:2016/12/13 09:20
 * E-mail：fishloveqin@gmail.com
 */

public interface CarDataView<T> extends WrapView {

    public void showData(T  t);

    //public void showModelDatas(List<CarBean> motos);
}
