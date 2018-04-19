package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.DynamicDetailsBean;

/**
 * Author：Jenny
 * Date:2017/2/15 19:27
 * E-mail：fishloveqin@gmail.com
 */

public interface DynamicDataView extends WrapView {

    void showData(DynamicDetailsBean dynamicDetailsBean);

    void showFollowAddSuccess(BaseBean baseBean); //关注

    void showCancelSuccess(BaseBean baseBean); //取消关注

    void detailsDataFeated(String msg); //数据返回失败
}
