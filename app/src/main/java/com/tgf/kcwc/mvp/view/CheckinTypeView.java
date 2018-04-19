package com.tgf.kcwc.mvp.view;

import com.tgf.kcwc.mvp.model.CertResultModel;
import com.tgf.kcwc.mvp.model.CheckinTypeModel;

import static com.tgf.kcwc.R.id.msg;

/**
 * Author：Jenny
 * Date:2017/2/7 15:13
 * E-mail：fishloveqin@gmail.com
 */

public interface CheckinTypeView extends WrapView {

    public void showDatas(CheckinTypeModel model);

    public void showCommitResult(int code, String msg);

    public void showCertDesc(CertResultModel model);
}
