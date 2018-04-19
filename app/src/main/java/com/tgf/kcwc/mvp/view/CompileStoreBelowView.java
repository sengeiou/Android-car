package com.tgf.kcwc.mvp.view;

import java.util.List;

import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.CarColor;
import com.tgf.kcwc.mvp.model.CompileStoreBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface CompileStoreBelowView extends WrapView {

    void dataSucceed(BaseBean baseBean); //数据返回成功
    void gainDataSucceed(CompileStoreBean baseBean); //数据返回成功
    void dataColourSucceed(List<CarColor> datalist); //数据返回成功

    void dataListDefeated(String msg); //列表数据返回失败
}
