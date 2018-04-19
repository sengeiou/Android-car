package com.tgf.kcwc.share;

import android.content.Context;

import com.sina.weibo.sdk.share.WbShareCallback;
import com.tgf.kcwc.util.CommonUtils;

/**
 * Author:Jenny
 * Date:2017/5/23
 * E-mail:fishloveqin@gmail.com
 */

public class SinaWBCallback implements WbShareCallback {

    private Context mContext;

    public SinaWBCallback(Context context) {
        this.mContext = context;
    }

    @Override
    public void onWbShareSuccess() {

        CommonUtils.showToast(mContext, "分享成功");
    }

    @Override
    public void onWbShareCancel() {
        CommonUtils.showToast(mContext, "已取消分享");
    }

    @Override
    public void onWbShareFail() {
        CommonUtils.showToast(mContext, "分享失败");
    }

}
