package com.tgf.kcwc.login;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.util.JPushUtil;
import com.tgf.kcwc.util.SharedPreferenceUtil;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Auther: Scott
 * Date: 2017/4/20 0020
 * E-mail:hekescott@qq.com
 */

public class LoginOutSevice extends Service {
    private static final String TAG = "LoginOutSevice";
    private Context mContext;
    private  final int HANDLER_MSG_JPUSHOUT = 1;
    private  final int HANDLER_MSG_CHATOUT = 2;
    boolean isJpushOut =false;
    boolean isChatOut =false;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
                 switch (msg.what){
                             case HANDLER_MSG_JPUSHOUT :
                                 isJpushOut =true;
                                 break;
                             case HANDLER_MSG_CHATOUT :
                                 isChatOut =true;
                                 break;
                             default:
                                 break;
                         }
            if(isJpushOut&&isChatOut){
                LoginOutSevice.this.stopSelf();
            }
            return false;
        }
    });
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        clearPush();

        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Logger.d("easechat loginout 成功");
                mHandler.sendEmptyMessage(HANDLER_MSG_CHATOUT);
            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub
                Logger.d("easechat loginout ing");
            }

            @Override
            public void onError(int code, String message) {
                // TODO Auto-generated method stub
                Logger.d("easechat loginout 失败 code =="+code+" msg=="+message);
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    private void clearPush() {
        if (SharedPreferenceUtil.getIsJpush(mContext)) {
            JPushInterface.setAliasAndTags(mContext, null, null, new TagAliasCallback() {
                @Override
                public void gotResult(int code, String s, Set<String> set) {
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    if (code == 0){
                        SharedPreferenceUtil.setIsJpush(mContext, false);
                        mHandler.sendEmptyMessage(HANDLER_MSG_JPUSHOUT);
                    }else {
                        clearPush();
                    }
                }
            });
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
