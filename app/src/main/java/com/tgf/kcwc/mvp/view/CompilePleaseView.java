package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.CompileDrivingBean;
import com.tgf.kcwc.mvp.model.CompilePleaseBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface CompilePleaseView extends WrapView {

   void dataListSucceed(); //提交数据成功
    void dataSucceed(CompilePleaseBean compilePleaseBean); //返回数据成功
    void dataListDefeated(String msg); //上传数据返回失败
    void dataDefeated(String msg); //获取数据返回失败
}
