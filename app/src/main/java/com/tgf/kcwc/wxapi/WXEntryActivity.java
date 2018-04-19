package com.tgf.kcwc.wxapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tgf.kcwc.R;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.login.LoginActivity;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.presenter.ThirdPartyPresenter;
import com.tgf.kcwc.mvp.presenter.WxLoginPresenter;
import com.tgf.kcwc.mvp.view.ThirdPartyView;
import com.tgf.kcwc.mvp.view.WXloginView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SharedPreferenceUtil;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.NoTitleDialog;
import com.tgf.kcwc.view.NotitleContentDialog;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.ResponseBody;

/**
 * Auther: Scott
 * Date: 2017/3/14 0014
 * E-mail:hekescott@qq.com
 */
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler, WXloginView, View.OnClickListener {

    private TextView wxlogintv;
    private WxLoginPresenter wxLoginPresenter;
    private Button loginSendMsgBtn;
    private TextView countDowntTv;
    private EditText mPhonenuvtv;
    private String mOpenId;
    private EditText mVerCodetv;
    private LinearLayout bindRootll;
    private NotitleContentDialog noTitleDialog;
    private Account mAccount;

    private ThirdPartyPresenter mThirdPartyPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wxlogin_result);

//        titleBarCallback();
        loginSendMsgBtn = (Button) findViewById(R.id.btn_get_verification_code);
        loginSendMsgBtn.setOnClickListener(this);
        bindRootll = (LinearLayout) findViewById(R.id.bind_rootll);
        wxlogintv = (TextView) findViewById(R.id.wxlogin_result);
        countDowntTv = (TextView) findViewById(R.id.wxlogin_countdowTv);
        mPhonenuvtv = (EditText) findViewById(R.id.moblie_phone);
        mVerCodetv = (EditText) findViewById(R.id.verification_code);
        findViewById(R.id.wxbind_button).setOnClickListener(this);
        KPlayCarApp rideMotoApp = (KPlayCarApp) getApplication();
        IWXAPI api = rideMotoApp.getMsgApi();
        wxLoginPresenter = new WxLoginPresenter();
        wxLoginPresenter.attachView(this);
        mThirdPartyPresenter = new ThirdPartyPresenter();
        mThirdPartyPresenter.attachView(thirdPartyView);

        //注意：
        //第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，如果返回值为false，则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
        try {
            api.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    protected void setUpViews() {

    }

    private ThirdPartyView<ResponseBody> thirdPartyView = new ThirdPartyView<ResponseBody>() {
        @Override
        public void showData(ResponseBody body) {

            System.out.println(body.toString());
        }

        @Override
        public void setLoadingIndicator(boolean active) {

        }

        @Override
        public void showLoadingTasksError() {

        }

        @Override
        public Context getContext() {
            return mContext;
        }
    };

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("绑定手机号");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getContext(), LoginActivity.class));
        finish();
    }

    @Override
    public void onReq(BaseReq baseReq) {

        switch (baseReq.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                Logger.d("ConstantsAPI  1");
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                Logger.d("ConstantsAPI  2");
                break;
            default:
                break;
        }
    }

    @Override
    public void onResp(BaseResp baseResp) {
        String result = "";

        //分享
        if (baseResp instanceof SendMessageToWX.Resp) {
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    KPlayCarApp.putValue(Constants.IntentParams.INTENT_TYPE, Constants.Share.SHARE_SUCCEED);
                    result = "分享成功";
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    KPlayCarApp.putValue(Constants.IntentParams.INTENT_TYPE, Constants.Share.SHARE_CANCEL);
                    result = "分享取消";
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    KPlayCarApp.putValue(Constants.IntentParams.INTENT_TYPE, Constants.Share.SHARE_REPULSE);
                    result = "分享被拒绝";
                    break;
                default:
                    KPlayCarApp.putValue(Constants.IntentParams.INTENT_TYPE, Constants.Share.SHARE_RETURN);
                    result = "分享返回";
                    break;
            }
            wxlogintv.setText(result);
            finish();
        }

        //要判断类型，不能直接转换，否则会造成第三方分享失败
        if (baseResp instanceof SendAuth.Resp) {
            SendAuth.Resp resp = (SendAuth.Resp) baseResp;
            String code = resp.code;
            Logger.d("onResp" + code);

            String state = resp.state;
            if (Constants.WX_CASH_STATE.equals(state)) {

                wxWithdrawCashResp(resp);
                finish();
            } else if (Constants.WX_LOGIN_STATE.equals(state)) {
                wxLoginResp(resp, code);
            }


//
        }

    }

    private void wxWithdrawCashResp(SendAuth.Resp resp) {

        KPlayCarApp.putValue(Constants.IntentParams.CODE, resp.code);
    }

    private void wxLoginResp(SendAuth.Resp resp, String code) {

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                wxLoginPresenter.getWxLogin(code);
                Logger.d("onResp ERR_OK");
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Logger.d("onResp ERR_USER_CANCEL");
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Logger.d("onResp ERR_AUTH_DENIED");
                finish();
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                Logger.d("onResp ERR_UNSUPPORT");
                finish();
                break;
        }

    }

    @Override
    public void showWxLoginSuccess(Account account) {
        saveAccount(account);
        mAccount = account;
        finish();
    }

    @Override
    public void bindPhoneNum(String openId) {
//            wxLoginPresenter.bindPhone();
        bindRootll.setVisibility(View.VISIBLE);
        mOpenId = openId;
    }

    @Override
    public void showLoginFailed(String statusMessage) {
        CommonUtils.showToast(getContext(), statusMessage);
    }

    @Override
    public void sendMsgSuccess() {
        CommonUtils.showToast(getContext(), "发送短信成功请耐心等待");
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
    public void sendMsgFailure(String statusMessage) {
        CommonUtils.showToast(getContext(), statusMessage);
    }

    @Override
    public void showRebindPhone(Account account) {
        saveAccount(account);
        mAccount = account;
        if (noTitleDialog == null) {

            noTitleDialog = new NotitleContentDialog(getContext());
            noTitleDialog.setContentText(account.text);
            noTitleDialog.setOnLoginOutClickListener(new NotitleContentDialog.IOnclickLisenter() {

                @Override
                public void OkClick() {
                    if (mAccount != null) {
                        wxLoginPresenter.updateBindPhone(mOpenId, mAccount.userInfo.id + "", mAccount.userInfo.token);
                    } else {
                        CommonUtils.showToast(getContext(), "更新失败");
                    }
                    noTitleDialog.dismiss();
                }

                @Override
                public void CancleClick() {
                    noTitleDialog.dismiss();
                }
            });
        }
        noTitleDialog.show();
    }

    @Override
    public void showBindSuccess(Account account) {
        saveAccount(account);
        mAccount = account;
        finish();
    }

    @Override
    public void showReBindSuccess() {
        if (mAccount != null) {
            mAccount.userInfo.tel = mPhonenuvtv.getText() + "";
            saveAccount(mAccount);
        }
        finish();
    }

    private void saveAccount(Account account) {
        ObjectOutputStream objectOutputStream = null;
        FileOutputStream outputStream = null;
        CommonUtils.showToast(mContext, "登录成功");
        JPushInterface.setAliasAndTags(getApplicationContext(),
                "user" + account.userInfo.id,
                null,
                mAliasCallback);
        try {
            outputStream = openFileOutput(Constants.KEY_ACCOUNT_FILE, Context.MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(account);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(objectOutputStream);
            IOUtils.close(outputStream);
        }
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
            if (code == 0)
                SharedPreferenceUtil.setIsJpush(getContext(), true);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wxbind_button:
                if (TextUtil.isEmpty(mPhonenuvtv.getText() + "")) {
                    CommonUtils.showToast(getContext(), "请填写手机号");
                    return;
                }
                if (TextUtil.isEmpty(mVerCodetv.getText() + "")) {
                    CommonUtils.showToast(getContext(), "请填写验证码");
                    return;
                }
                if (!TextUtil.isEmpty(mOpenId)) {
                    wxLoginPresenter.bindPhone(mOpenId, mVerCodetv.getText() + "", mPhonenuvtv.getText() + "");
                } else {
                    CommonUtils.showToast(getContext(), "请重新授权");
                }
                break;
            case R.id.btn_get_verification_code:
                if (TextUtil.isEmpty(mPhonenuvtv.getText() + "")) {
                    CommonUtils.showToast(getContext(), "请填写手机号");
                    return;
                }
                wxLoginPresenter.sendSMS(mPhonenuvtv.getText() + "");
                break;
            default:
                break;
        }
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return WXEntryActivity.this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wxLoginPresenter.detachView();
    }


}
