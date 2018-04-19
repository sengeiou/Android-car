package com.tgf.kcwc.me.setting;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.util.SharedPreferenceUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.SettingSelectedLayoutView;

/**
 * Auther: Scott
 * Date: 2017/5/22 0022
 * E-mail:hekescott@qq.com
 * 免打扰设置
 */

public class MsgSetActivity extends BaseActivity {

    private SettingSelectedLayoutView mVibrateView;
    private SettingSelectedLayoutView mSoundView;
    private SettingSelectedLayoutView mChatView;
    private SettingSelectedLayoutView mFansView;
    private SettingSelectedLayoutView mReplyView;
    private SettingSelectedLayoutView mZanView;
    private SettingSelectedLayoutView mNightModeView;

    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("免打扰设置");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msgset);
        mVibrateView = (SettingSelectedLayoutView) findViewById(R.id.msgset_vibratelv);
        mVibrateView.setStatus(SharedPreferenceUtil.getIsVibrate(getContext()));
        mVibrateView.setOnChangelistener(new SettingSelectedLayoutView.OnChangerLisener() {
            @Override
            public void onChange() {
                SharedPreferenceUtil.setIsVibrate(getContext(),mVibrateView.getStauts());
            }
        });
        mSoundView = (SettingSelectedLayoutView) findViewById(R.id.msgset_soundlv);
        mSoundView.setStatus(SharedPreferenceUtil.getIsVoice(getContext()));
        mSoundView.setOnChangelistener(new SettingSelectedLayoutView.OnChangerLisener() {
            @Override
            public void onChange() {
                SharedPreferenceUtil.setIsVoice(getContext(),mSoundView.getStauts());
            }
        });
        mChatView = (SettingSelectedLayoutView) findViewById(R.id.msgset_chatlv);
        mChatView.setStatus(SharedPreferenceUtil.getIsChatNoti(getContext()));
        mChatView.setOnChangelistener(new SettingSelectedLayoutView.OnChangerLisener() {
            @Override
            public void onChange() {
                SharedPreferenceUtil.setIsChatNoti(getContext(),mChatView.getStauts());
            }
        });

        mFansView = (SettingSelectedLayoutView) findViewById(R.id.msgset_new_fans);
        mFansView.setStatus(SharedPreferenceUtil.getIsNewFans(getContext()));
        mFansView.setOnChangelistener(new SettingSelectedLayoutView.OnChangerLisener() {
            @Override
            public void onChange() {
                SharedPreferenceUtil.setIsNewFans(getContext(),mFansView.getStauts());
            }
        });
        mReplyView = (SettingSelectedLayoutView) findViewById(R.id.msgset_comment_reply);
        mReplyView.setStatus(SharedPreferenceUtil.getIsCommentReply(getContext()));
        mReplyView.setOnChangelistener(new SettingSelectedLayoutView.OnChangerLisener() {
            @Override
            public void onChange() {
                SharedPreferenceUtil.setIsCommentReply(getContext(),mReplyView.getStauts());
            }
        });
        mZanView = (SettingSelectedLayoutView) findViewById(R.id.msgset_zan);
        mZanView.setStatus(SharedPreferenceUtil.getIsZan(getContext()));
        mZanView.setOnChangelistener(new SettingSelectedLayoutView.OnChangerLisener() {
            @Override
            public void onChange() {
                SharedPreferenceUtil.setIsZan(getContext(),mZanView.getStauts());
            }
        });
        mNightModeView = (SettingSelectedLayoutView) findViewById(R.id.msgset_night_mode);
        mNightModeView.setStatus(SharedPreferenceUtil.getNightMode(getContext()));
        mNightModeView.setOnChangelistener(new SettingSelectedLayoutView.OnChangerLisener() {
            @Override
            public void onChange() {
                SharedPreferenceUtil.setNightMode(getContext(),mNightModeView.getStauts());
            }
        });
    }

    public Context getContext() {
        return MsgSetActivity.this;
    }
}
