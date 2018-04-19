package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.AgreementModel;
import com.tgf.kcwc.mvp.model.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface SingUpView extends WrapView {

    void dataListSucceed(BaseBean attentionBean); //返回数据成功

    void dataSucceed(List<AgreementModel> data); //返回报名协议

    void dataListDefeated(String msg); //数据返回失败
}
