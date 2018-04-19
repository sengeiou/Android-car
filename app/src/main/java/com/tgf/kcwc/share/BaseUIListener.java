package com.tgf.kcwc.share;

import android.content.Context;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.tgf.kcwc.util.CommonUtils;

/**
 * Author:Jenny
 * Date:2017/5/23
 * E-mail:fishloveqin@gmail.com
 */

public class BaseUIListener implements IUiListener {

    private Context mContext;

    public BaseUIListener(Context context) {
        this.mContext = context;
    }

    @Override
    public void onComplete(Object o) {

        CommonUtils.showToast(mContext, "分享成功");
    }

    @Override
    public void onError(UiError uiError) {
        CommonUtils.showToast(mContext, "分享失败:" + uiError.errorMessage);
    }

    @Override
    public void onCancel() {

        CommonUtils.showToast(mContext, "已取消分享");
    }

}
