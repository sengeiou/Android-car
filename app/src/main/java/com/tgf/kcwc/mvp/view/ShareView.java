package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.ApplyListBean;
import com.tgf.kcwc.mvp.model.ShareSplendBean;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface ShareView extends WrapView {

   void dataListSucceed(ShareSplendBean shareSplendBean); //返回数据列表成功
    void dataListDefeated(String msg); //列表数据返回失败

}
