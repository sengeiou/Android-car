package com.tgf.kcwc.app;

import android.content.Context;
import android.preference.PreferenceManager;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.util.EMLog;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.SharedPreferenceUtil;

import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/8/4 0004
 * E-mail:hekescott@qq.com
 */

public class EaseManger {

    private static final String TAG = "EaseManger";
    private EMMessageListener messageListener;
    private KPlayCarApp appContext;
    private EaseUI easeUI;
    private static EaseManger instance;

    public void init(KPlayCarApp context){
        EMOptions  options = initChatOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        //初始化
        EMClient.getInstance().init(context, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
        if (EaseUI.getInstance().init(context, options)) {
            appContext = context;
            easeUI = EaseUI.getInstance();
            easeUI.init(appContext, new EMOptions());
            setEaseUIProviders();
            registerMessageListener();
        }


    }

    private void setEaseUIProviders() {
        easeUI.setSettingsProvider(new EaseUI.EaseSettingsProvider() {

            @Override
            public boolean isSpeakerOpened() {
                return true;
            }

            @Override
            public boolean isMsgVibrateAllowed(EMMessage message) {
                return SharedPreferenceUtil.getIsVibrate(appContext);
            }

            @Override
            public boolean isMsgSoundAllowed(EMMessage message) {
                return SharedPreferenceUtil.getIsVoice(appContext);
            }

            @Override
            public boolean isMsgNotifyAllowed(EMMessage message) {

                return SharedPreferenceUtil.getIsChatNoti(appContext);
//                if(message == null){
//                    return demoModel.getSettingMsgNotification();
//                }
//                if(!demoModel.getSettingMsgNotification()){
//                    return false;
//                }else{
//                    String chatUsename = null;
//                    List<String> notNotifyIds = null;
//                    // get user or group id which was blocked to show message notifications
//                    if (message.getChatType() == ChatType.Chat) {
//                        chatUsename = message.getFrom();
//                        notNotifyIds = demoModel.getDisabledIds();
//                    } else {
//                        chatUsename = message.getTo();
//                        notNotifyIds = demoModel.getDisabledGroups();
//                    }
//
//                    if (notNotifyIds == null || !notNotifyIds.contains(chatUsename)) {
//                        return true;
//                    } else {
//                        return false;
//                    }
//                }
            }
        });

    }

    private EaseManger() {
    }


    public synchronized static EaseManger getInstance() {
        if (instance == null) {
            instance = new EaseManger();
        }
        return instance;
    }

    public EaseNotifier getNotifier(){
        return easeUI.getNotifier();
    }
    private EMOptions initChatOptions(){
        EMOptions options = new EMOptions();
        return options;
    }
    protected void registerMessageListener() {
        messageListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                Logger.d("onMessageReceived");
                for (EMMessage message : messages) {
                    EMLog.d(TAG, "onMessageReceived id : " + message.getMsgId());
                    // in background, do not refresh UI, notify it in notification bar
                    getNotifier().onNewMsg(message);
                    Logger.d("onMessageReceived 1");
                    Logger.d("onMessageReceived 2"+message.getBody().toString());
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageRead(List<EMMessage> list) {

            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {

            }

            @Override
            public void onMessageRecalled(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {

            }
        };
     EMChatManager chatManager = EMClient.getInstance().chatManager();
        chatManager.addMessageListener(messageListener);

    }

}
