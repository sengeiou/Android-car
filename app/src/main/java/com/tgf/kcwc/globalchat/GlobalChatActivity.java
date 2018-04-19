package com.tgf.kcwc.globalchat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.lzy.imagepicker.ImageDataSource;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.globalchat.runtimepermissions.PermissionsManager;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.view.FunctionView;
import com.umeng.analytics.MobclickAgent;

/**
 * chat activity，EaseChatFragment was used {@link #}
 */
public class GlobalChatActivity extends BaseActivity {
    public static GlobalChatActivity activityInstance;
    private EaseChatFragment chatFragment;
    String toChatUsername;
    String userName = "";
    String userPic = "";


    public static final int REQUEST_PERMISSION_STORAGE = 0x01;
    public static final int REQUEST_PERMISSION_CAMERA = 0x02;
    public static final int REQUEST_PERMISSION_RECORD_AUDIO = 0x03;

    @Override
    public void onCreate(Bundle arg0) {
        isTitleBar = false;
        EaseConstant.IS_PRIVATE_ETTER = true;
        super.onCreate(arg0);
        setContentView(R.layout.em_activity_chat);
        activityInstance = this;
        //get user id or group id
        toChatUsername = getIntent().getExtras().getString(Constant.EXTRA_USER_USERID);
        userName = getIntent().getExtras().getString(Constant.EXTRA_USER_NICKNAME);
        userPic = getIntent().getExtras().getString(Constant.EXTRA_USER_USERPIC);
        //use EaseChatFratFragment
        if (TextUtil.isEmpty(userName)) {
            chatFragment = new GlobalChatFragment();
        } else {
            chatFragment = new GlobalChatFragment(toChatUsername, userName, userPic);
        }
        //pass parameters to chat fragment
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //new ImageDataSource(this, null, this);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION_STORAGE);
            }
            if (checkPermission(Manifest.permission.CAMERA)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
            }
            if (checkPermission(Manifest.permission.RECORD_AUDIO)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_PERMISSION_RECORD_AUDIO);
            }

        }
    }


    public boolean checkPermission(@NonNull String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void showToast(String toastText) {
        Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        //backEvent(back);
        //text.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
        EaseConstant.IS_PRIVATE_ETTER = false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // make sure only one chat activity is opened
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        // umeng
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // umeng
        MobclickAgent.onPause(this);
    }

    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
    }

    public String getToChatUsername() {
        return toChatUsername;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                showToast("权限被禁止，无法选择本地图片");
            }
        } else if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                showToast("权限被禁止，无法打开相机");
            }
        } else if (requestCode == REQUEST_PERMISSION_RECORD_AUDIO) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                showToast("权限被禁止，无法打开录音");
            }
        }
    }
}
