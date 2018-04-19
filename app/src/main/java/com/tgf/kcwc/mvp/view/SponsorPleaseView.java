package com.tgf.kcwc.mvp.view;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface SponsorPleaseView extends WrapView {

   void dataListSucceed(); //返回数据列表成功
    void dataListDefeated(String msg); //列表数据返回失败
}
