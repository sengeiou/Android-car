package com.tgf.kcwc.me.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.SharedPreferenceUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.SettingSelectedLayoutView;

/**
 * @anthor noti
 * @time 2017/8/22
 * @describle
 */
public class PrivateActivity extends BaseActivity {
    private SettingSelectedLayoutView mContactsView;
    private SettingSelectedLayoutView mContactsAddView;
    private SettingSelectedLayoutView mMainPageView;
    private SettingSelectedLayoutView mChatView;
    private SettingSelectedLayoutView mSavePhotoView;
    private SettingSelectedLayoutView mHintView;
    private SettingSelectedLayoutView mNearView;

    private RelativeLayout backListRl;

    @Override
    protected void setUpViews() {
        mContactsView = (SettingSelectedLayoutView) findViewById(R.id.private_contacts);
        mContactsView.setStatus(SharedPreferenceUtil.getContacts(this));
        mContactsView.setOnChangelistener(new SettingSelectedLayoutView.OnChangerLisener() {
            @Override
            public void onChange() {
                SharedPreferenceUtil.setContacts(PrivateActivity.this,mContactsView.getStauts());
            }
        });
        mContactsAddView = (SettingSelectedLayoutView) findViewById(R.id.private_contacts_add);
        mContactsAddView.setStatus(SharedPreferenceUtil.getContactsAdd(this));
        mContactsAddView.setOnChangelistener(new SettingSelectedLayoutView.OnChangerLisener() {
            @Override
            public void onChange() {
                SharedPreferenceUtil.setContactsAdd(PrivateActivity.this,mContactsAddView.getStauts());
            }
        });
        mMainPageView = (SettingSelectedLayoutView) findViewById(R.id.private_main_page);
        mMainPageView.setStatus(SharedPreferenceUtil.getMainPage(this));
        mMainPageView.setOnChangelistener(new SettingSelectedLayoutView.OnChangerLisener() {
            @Override
            public void onChange() {
                SharedPreferenceUtil.setMainPage(PrivateActivity.this,mMainPageView.getStauts());
            }
        });
        mChatView = (SettingSelectedLayoutView) findViewById(R.id.private_not_attention_chat);
        mChatView.setStatus(SharedPreferenceUtil.getChat(this));
        mChatView.setOnChangelistener(new SettingSelectedLayoutView.OnChangerLisener() {
            @Override
            public void onChange() {
                SharedPreferenceUtil.setChat(PrivateActivity.this,mChatView.getStauts());
            }
        });
        mSavePhotoView = (SettingSelectedLayoutView) findViewById(R.id.private_save_photo);
        mSavePhotoView.setStatus(SharedPreferenceUtil.getSavePhoto(this));
        mSavePhotoView.setOnChangelistener(new SettingSelectedLayoutView.OnChangerLisener() {
            @Override
            public void onChange() {
                SharedPreferenceUtil.setSavePhoto(PrivateActivity.this,mSavePhotoView.getStauts());
            }
        });
        mHintView = (SettingSelectedLayoutView) findViewById(R.id.private_hint);
        mHintView.setStatus(SharedPreferenceUtil.getHint(this));
        mHintView.setOnChangelistener(new SettingSelectedLayoutView.OnChangerLisener() {
            @Override
            public void onChange() {
                SharedPreferenceUtil.setHint(PrivateActivity.this,mHintView.getStauts());
            }
        });

        mNearView = (SettingSelectedLayoutView) findViewById(R.id.private_near);

        backListRl = (RelativeLayout) findViewById(R.id.private_back_list);
        backListRl.setOnClickListener(this);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("隐私设置");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.private_back_list://黑名单
                CommonUtils.startNewActivity(this,BackListActivity.class);
                break;
        }
    }
}
