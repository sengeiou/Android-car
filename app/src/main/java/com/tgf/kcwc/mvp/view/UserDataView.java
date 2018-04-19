package com.tgf.kcwc.mvp.view;

/**
 * Author:Jenny
 * Date:2017/5/15
 * E-mail:fishloveqin@gmail.com
 */

public interface UserDataView<T> extends WrapView {

    public void showDatas(T t);

    //void showBussinessMenu(List<UserHomeDataModel.ButtonBean> bussinessMenu);
}
