package com.tgf.kcwc.login;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.globalchat.Constant;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.model.EaseLoginModel;
import com.tgf.kcwc.mvp.presenter.EaseLoginPresenter;
import com.tgf.kcwc.mvp.view.EaseLoginView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SharedPreferenceUtil;
import com.tgf.kcwc.util.SystemUtils;
import com.tgf.kcwc.util.TextUtil;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Auther: Scott
 * Date: 2017/4/20 0020
 * E-mail:hekescott@qq.com
 */

public class LoginSevice extends Service implements EaseLoginView {
    private static final String TAG = "LoginSevice";
    private Context mContext;
    private EaseLoginPresenter mEaseLoginPresenter;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mEaseLoginPresenter = new EaseLoginPresenter();
        mEaseLoginPresenter.attachView(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(!TextUtil.isEmpty(IOUtils.getToken(mContext))){
            if(SystemUtils.isEmulator(mContext)){
                stopSelf();
            }else {
                mEaseLoginPresenter.getEaseLogin(IOUtils.getToken(mContext),"kcwc");
            }
        }else {
            stopSelf();
        }




        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void login(String username,String attached ) {
        SharedPreferenceUtil.putEaseaUserName(mContext,username);
        SharedPreferenceUtil.putEaseAttached(mContext,attached);
        Logger.d("easechat login username=="+username);
        EMClient.getInstance().login( username, attached, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                Logger.d("easechat login 登录聊天服务器成功！");
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                stopSelf();
            }

            @Override
            public void onProgress(int progress, String status) {
                Logger.d("easechat login == onProgress！");
            }

            @Override
            public void onError(int code, String message) {
                Logger.d("easechat   progress= "+code);
                stopSelf();
            }
        });
    }


    @Override
    public void showEaseLoginSuccess(EaseLoginModel easeLoginModel) {

                if((!TextUtils.isEmpty(easeLoginModel.mUserName))&&(!TextUtils.isEmpty(easeLoginModel.mAttached))){
                    login(easeLoginModel.mUserName,easeLoginModel.mAttached);
                }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mEaseLoginPresenter.detachView();
    }
}
