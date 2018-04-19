package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.AppListBean;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.FoundListBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface FoundListView extends WrapView {

    void dataListSucceed(FoundListBean foundListBean); //返回数据列表成功

    void dataSucceed(BaseBean baseBean); //取消成功

    void dataListDefeated(String msg); //列表数据返回失败
}
