package com.tgf.kcwc.login;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tgf.kcwc.R;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.me.setting.AgreementActivity;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.presenter.UserManagerPresenter;
import com.tgf.kcwc.mvp.view.LoginView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.LocationUtil;
import com.tgf.kcwc.util.SharedPreferenceUtil;
import com.tgf.kcwc.view.FunctionView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Author：Jenny
 * Date:2016/12/8 21:27
 * E-mail：fishloveqin@gmail.com
 * 登录页面
 */

public class LoginActivity extends BaseActivity
        implements LoginView, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "LoginActivity";
    AutoCompleteTextView mPhonenuvtv;
    EditText mVercodeED;
    private RadioGroup loginType;
    //是否是密码登录
    private boolean isPWD = true;
    private View pwdLayout;
    private View phoneLayout;
    private EditText pwdED;
    private boolean cansSeePwd;
    //    private LocationClient   mLocationClient;
    private double mLatitude = 0;
    private double mLongitude = 0;
    //请求读取位置
    private static final int ACCESS_COARSE_LOCATION_REQUEST_CODE = 102;
    private final int REQUEST_EDITINFO_CODE = 12;
    private AMapLocationClient aMapLocationClient;
    private IWXAPI api;
    private TextView countDowntTv;
    private Button loginSendMsgBtn;
    private RadioButton phoneLoginBtn;
    private KPlayCarApp app;
    private SimpleDraweeView icLanucherSimple;
    private CheckBox checkBoxLicense;
    private TextView userProtocol;
    private TextView voiceVercodetv;


    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        aMapLocationClient = LocationUtil.getGaodeLocationClient(this);
        findViewById(R.id.login_wechativ).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        aMapLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation location) {
                if (null != location) {
                    mLongitude = location.getLongitude();
                    mLatitude = location.getLatitude();
                }
            }
        });
        aMapLocationClient.startLocation();
        app = ((KPlayCarApp) getApplication());
        api = app.getMsgApi();
        phoneLoginBtn = (RadioButton) findViewById(R.id.phone_login);
        phoneLoginBtn.performClick();
//        icLanucherSimple = (SimpleDraweeView) findViewById(R.id.imageView);
//        GenericDraweeHierarchy hierarchy =  icLanucherSimple.getHierarchy();
//        hierarchy.setPlaceholderImage(R.drawable.ic_launcher);
//        RoundingParams roundingParams = hierarchy.getRoundingParams();
//        roundingParams.setCornersRadius(10);
//        hierarchy.setRoundingParams(roundingParams);
    }

    @Override
    protected void setUpViews() {

        voiceVercodetv = (TextView) findViewById(R.id.voice_vercodetv);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        loginSendMsgBtn = (Button) findViewById(R.id.btn_get_verification_code);
        loginSendMsgBtn.setOnClickListener(this);
        countDowntTv = (TextView) findViewById(R.id.login_countdowTv);
        findViewById(R.id.seepwd_bt).setOnClickListener(this);
        findViewById(R.id.forget_pwdtv).setOnClickListener(this);
        mPhonenuvtv = (AutoCompleteTextView) findViewById(R.id.moblie_phone);
        pwdLayout = findViewById(R.id.pwd_layout);
        pwdED = (EditText) findViewById(R.id.pwd_code_ed);
        phoneLayout = findViewById(R.id.phone_layout);
        checkBoxLicense = (CheckBox) findViewById(R.id.checkBox_license);
        voiceVercodetv.setOnClickListener(this);
        mVercodeED = (EditText) findViewById(R.id.verification_code);
        loginType = (RadioGroup) findViewById(R.id.login_type);
        userProtocol = (TextView) findViewById(R.id.userProtocol);
        userProtocol.setOnClickListener(this);
        loginType.setOnCheckedChangeListener(this);
        mPresenter = new UserManagerPresenter();
        mPresenter.attachView(this);
        voiceVercodetv
                .setText(Html.fromHtml(mRes.getString(R.string.promt_vervoice)+"<font color=\"#4e92df\">接收语音验证</font>"));
    }

    private UserManagerPresenter mPresenter;

    @Override
    public void success(Account a) {
        Constants.myInfo = a.userInfo;
        ObjectOutputStream objectOutputStream = null;
        FileOutputStream outputStream = null;
        JPushInterface.setAliasAndTags(getApplicationContext(),
                "user" + a.userInfo.id,
                null,
                mAliasCallback);
        try {
            outputStream = openFileOutput(Constants.KEY_ACCOUNT_FILE, Context.MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(a);
            objectOutputStream.flush();

            IOUtils.clearData();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(objectOutputStream);
            IOUtils.close(outputStream);
        }
        if (TextUtils.isEmpty(a.userInfo.nickName)) {
            Intent toIntent = new Intent(mContext, EditMyinfoActivity.class);
            startActivityForResult(toIntent, REQUEST_EDITINFO_CODE);
        } else {
//            if (a != null && a.userInfo.loginNum.equals("1")) {
//                Map<String, Serializable> args = new HashMap<String, Serializable>();
//                args.put(Constants.IntentParams.DATA, true);
//                CommonUtils.startNewActivity(mContext, args, FilterCustomizationActivity.class);
//            }
        }
        startService(new Intent(getContext(), LoginSevice.class));
        finish();
    }

    /**
     * 极光推送别名
     */
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
            if (code == 0)
                SharedPreferenceUtil.setIsJpush(getContext(), true);
        }
    };

    @Override
    public void failure(String msg) {
        CommonUtils.showToast(this,msg);
    }

    @Override
    public void sendMsgSuccess() {
        CommonUtils.showToast(getContext(), "发送短信成功请耐心等待");
        voiceVercodetv.setVisibility(View.VISIBLE);
        loginSendMsgBtn.setVisibility(View.GONE);
        countDowntTv.setVisibility(View.VISIBLE);
        CountDownTimer countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countDowntTv.setText((millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                countDowntTv.setVisibility(View.GONE);
                loginSendMsgBtn.setVisibility(View.VISIBLE);
            }
        };
        countDownTimer.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_EDITINFO_CODE:
                finish();
                break;
        }
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.userProtocol://用户协议
                startActivity(new Intent(getContext(), AgreementActivity.class));
                break;
            case R.id.sign_in_button:
                if (!checkBoxLicense.isChecked()) {
                    CommonUtils.showToast(getContext(), "请选择用户协议");
                    return;
                }
                if (isPWD) {
                    mPresenter.pwd(mPhonenuvtv.getText() + "", pwdED.getText() + "", mLatitude, mLongitude);
                } else {
                    mPresenter.verify(mPhonenuvtv.getText() + "", mVercodeED.getText() + "", mLatitude, mLongitude);
                }
                break;
            case R.id.voice_vercodetv:
                mPresenter.sendVoice(mPhonenuvtv.getText() + "");
                break;
            case R.id.sign_out_button:
                finish();
//                app.loginOut(this);
                break;
            case R.id.seepwd_bt:
                cansSeePwd = !cansSeePwd;

                if (cansSeePwd) {
                    pwdED.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    pwdED.setInputType(
                            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }

                break;
            case R.id.btn_get_verification_code:
                mPresenter.sendSMS(mPhonenuvtv.getText() + "");
                break;
            case R.id.forget_pwdtv:
                CommonUtils.startNewActivity(mContext, FindBackPWDActivity.class);
                break;

            case R.id.login_wechativ:
                WXTextObject wxTextObject = new WXTextObject();
                // send oauth request
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = Constants.WX_LOGIN_STATE;
                api.sendReq(req);
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group.getId() == R.id.login_type) {
            switch (checkedId) {
                case R.id.pwd_login:
                    isPWD = true;
                    cansSeePwd = false;
                    pwdLayout.setVisibility(View.VISIBLE);
                    phoneLayout.setVisibility(View.GONE);
                    break;
                case R.id.phone_login:
                    isPWD = false;
                    cansSeePwd = false;
                    pwdLayout.setVisibility(View.GONE);
                    phoneLayout.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ACCESS_COARSE_LOCATION_REQUEST_CODE == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
//                                gotoCarema();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (null != aMapLocationClient) {
            aMapLocationClient.onDestroy();
            aMapLocationClient = null;
        }
        super.onDestroy();
    }


    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }

}
