package com.tgf.kcwc.me.setting;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.tgf.kcwc.R;
import com.tgf.kcwc.app.MainActivity;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.download.DownloadUtils;
import com.tgf.kcwc.me.mybalance.resetpaypwd.ResetPwdActivity;
import com.tgf.kcwc.me.mybalance.setpaypwd.SettingPayPwdActivity;
import com.tgf.kcwc.mvp.model.AccountBalanceModel;
import com.tgf.kcwc.mvp.model.UpdateModel;
import com.tgf.kcwc.mvp.presenter.MyWalletPresenter;
import com.tgf.kcwc.mvp.presenter.StartPagePresenter;
import com.tgf.kcwc.mvp.view.MyWalletView;
import com.tgf.kcwc.mvp.view.UpdateView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.FileUtil;
import com.tgf.kcwc.util.GlideConfiguration;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SharedPreferenceUtil;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.LoginoutDialog;
import com.tgf.kcwc.view.SettingSelectedLayoutView;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Auther: Scott
 * Date: 2017/5/20 0020
 * E-mail:hekescott@qq.com
 */

public class SetHomeActivity extends BaseActivity implements UpdateView {

    private TextView sizeTv;
    private LoginoutDialog mLoginoutDialog;
    private TextView versionTv;
    private StartPagePresenter startPagePresenter;
    private View updateLayout;
    private PackageInfo pi;
    private View versionLayout;
    private MyWalletPresenter mWalletPresenter;
    private SettingSelectedLayoutView setNonPaymentView;
    private int isFreePay = -1;
    private int isPwd = -1;

    @Override
    protected void setUpViews() {
        findViewById(R.id.settinghome_loginouttv).setOnClickListener(this);
        findViewById(R.id.sethome_userfakui).setOnClickListener(this);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        text.setText("设置");
        backEvent(back);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sethome);
        SharedPreferenceUtil.putUpdateUrl(getContext(), "");
        findViewById(R.id.setting_msgbtn).setOnClickListener(this);
        findViewById(R.id.setting_account).setOnClickListener(this);
        findViewById(R.id.setting_private).setOnClickListener(this);
        findViewById(R.id.setting_help_feedback_rl).setOnClickListener(this);
        findViewById(R.id.setting_about).setOnClickListener(this);
        findViewById(R.id.setting_help).setOnClickListener(this);
        findViewById(R.id.setting_clearbtn).setOnClickListener(this);
        findViewById(R.id.setting_online).setOnClickListener(this);
        findViewById(R.id.userAgreementView).setOnClickListener(this);
        findViewById(R.id.payMgrLayout).setOnClickListener(this);
        setNonPaymentView = findViewById(R.id.setNonConfidentialPaymentView);
        setNonPaymentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isFreePay == -1) {
                    CommonUtils.showToast(mContext, "正在加载数据，请稍候!");
                    return;
                }
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.DATA, isFreePay);
                CommonUtils.startNewActivity(mContext,args, SetNonPwdPaymentActivity.class);

            }
        });
        versionLayout = findViewById(R.id.setting_versionbtn);
        versionLayout.setOnClickListener(this);
        updateLayout = findViewById(R.id.update_layout);
        findViewById(R.id.sethome_userfakui).setOnClickListener(this);
        setUpdate();
        startPagePresenter = new StartPagePresenter();
        startPagePresenter.attachView(this);
        PackageManager pm = getPackageManager();
        try {
            pi = pm.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (TextUtil.isEmpty(SharedPreferenceUtil.getUpdateUrl(this))) {
            startPagePresenter.getUpdateApi(pi.versionCode + "");
        }
        versionTv = (TextView) findViewById(R.id.sethome_versionnumtv);
        sizeTv = (TextView) findViewById(R.id.sethome_sizetv);
        Fresco.getImagePipelineFactory().getMainDiskStorageCache().getSize();
        long cacheSize = Fresco.getImagePipelineFactory().getMainDiskStorageCache().getSize();
        String glidePath = getCacheDir() + "/" + GlideConfiguration.GLIDE_CARCH_DIR;
        try {
            cacheSize += FileUtil.getFolderSize(new File(glidePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        sizeTv.setText(FileUtil.getFormatSize(cacheSize) + "");
        versionTv.setText("v" + pi.versionName);

        mWalletPresenter = new MyWalletPresenter();
        mWalletPresenter.attachView(myWalletView);

    }


    @Override
    protected void onResume() {
        super.onResume();

        mWalletPresenter.getAccountBalances(IOUtils.getToken(mContext));
    }

    private MyWalletView<AccountBalanceModel> myWalletView = new MyWalletView<AccountBalanceModel>() {
        @Override
        public void showData(AccountBalanceModel accountBalanceModel) {


            showPayMgrInfo(accountBalanceModel);
        }

        @Override
        public void setLoadingIndicator(boolean active) {

            showLoadingIndicator(active);
        }

        @Override
        public void showLoadingTasksError() {

            dismissLoadingDialog();
        }

        @Override
        public Context getContext() {
            return mContext;
        }
    };

    private void showPayMgrInfo(AccountBalanceModel accountBalanceModel) {

        TextView payTitleTv = findViewById(R.id.payTitleTv);

        String payTitle = "";
        isPwd = accountBalanceModel.isPwd;
        if (isPwd == 1) {
            payTitle = "修改支付密码";
        } else if (isPwd == 0) {
            payTitle = "设置支付密码";
        }
        payTitleTv.setText(payTitle);

        isFreePay = accountBalanceModel.isFreePay;
        if (isFreePay == 1) {

            setNonPaymentView.setStatus(true);
        } else if (isFreePay == 0) {
            setNonPaymentView.setStatus(false);
        }
    }

    private void setUpdate() {

        updateLayout.setOnClickListener(this);
        if (TextUtil.isEmpty(SharedPreferenceUtil.getUpdateUrl(this))) {
            updateLayout.setVisibility(View.GONE);
        }
        if (updateLayout.getVisibility() == View.VISIBLE) {
            versionLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.settinghome_loginouttv://退出登录
                loginOut();
                break;
            case R.id.setting_msgbtn://免打扰设置
                startActivity(new Intent(getContext(), MsgSetActivity.class));
                break;
            case R.id.sethome_userfakui://用户反馈
                startActivity(new Intent(getContext(), FanKuiActivity.class));
                break;
            case R.id.setting_clearbtn://清除缓存
                ImagePipeline imagePipeline = Fresco.getImagePipeline();
                imagePipeline.clearCaches();
                FileUtil.clearCacheDiskSelf();
                sizeTv.setText("0Byte(s)");
                break;
            case R.id.update_layout://版本更新
                startUpdate();
                break;
            case R.id.setting_versionbtn://关于版本
                startUpdate();
                break;
            case R.id.setting_account://账号与安全
                startActivity(new Intent(getContext(), AccountActivity.class));
                break;
            case R.id.setting_private://隐私设置
                startActivity(new Intent(getContext(), PrivateActivity.class));
                break;
            case R.id.setting_help_feedback_rl://帮助与反馈
                startActivity(new Intent(getContext(), PrivateActivity.class));
                break;
            case R.id.setting_about://关于我们
                startActivity(new Intent(getContext(), AboutActivity.class));
                break;
            case R.id.setting_help://帮助中心
                startActivity(new Intent(getContext(), HelpCenterActivity.class));
                break;
            case R.id.setting_online://设置在线
                startActivity(new Intent(getContext(), OnlineActivity.class));
                break;
            case R.id.userAgreementView:
                startActivity(new Intent(getContext(), AgreementActivity.class));
                break;
            case R.id.payMgrLayout:
                if (isPwd == -1) {
                    CommonUtils.showToast(mContext, "数据加载中，请稍候...");
                    return;
                }
                if (isPwd == 1) {
                    CommonUtils.startNewActivity(mContext, ResetPwdActivity.class);
                } else {
                    CommonUtils.startNewActivity(mContext, SettingPayPwdActivity.class);
                }

                break;
            default:
                break;
        }
    }

    private void startUpdate() {
        if (TextUtil.isEmpty(SharedPreferenceUtil.getUpdateUrl(getContext()))) {
            showNoNewVersionUpdateDialog();
        } else {
            showNewVersionUpdateDialog(pi.versionName, SharedPreferenceUtil.getUpdateVer(getContext()));
        }


    }

    public void showNoNewVersionUpdateDialog() {
        String verName = pi.versionName;
        StringBuffer sb = new StringBuffer()
                .append("当前版本：v").append(verName)
                .append("\n已是最新版本");
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("软件更新")
                .setMessage(sb.toString())
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    /**
     * 更新版本
     */
    public void showNewVersionUpdateDialog(String oldVerName, String newVerName) {
        StringBuffer sb = new StringBuffer()
                .append("当前版本：v").append(oldVerName)
                .append("\n发现版本：v").append(newVerName)
                .append("\n是否更新?");
        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setCancelable(false)
                .setTitle("软件更新")
                .setMessage(sb.toString())
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DownloadUtils downloadUtils = new DownloadUtils(getContext());
                        downloadUtils.downloadAPK(SharedPreferenceUtil.getUpdateUrl(getContext()), "新版本");
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        alertDialog.show();
    }

    private void loginOut() {
        if (mLoginoutDialog == null) {
            mLoginoutDialog = new LoginoutDialog(getContext());
            mLoginoutDialog.setOnLoginOutClickListener(new LoginoutDialog.IOnclickLisenter() {
                @Override
                public void OkClick() {
                    boolean isOutSuccess = IOUtils.loginOut(getContext(), Constants.KEY_ACCOUNT_FILE);
                    if (isOutSuccess) {
                        CommonUtils.showToast(getContext(), "退出登录成功");
                        finish();
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setClass(SetHomeActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        CommonUtils.showToast(getContext(), "退出登录失败");
                    }
                }

                @Override
                public void CancleClick() {
                    mLoginoutDialog.dismiss();
                }
            });
        }
        mLoginoutDialog.show();
    }

    public Context getContext() {
        return SetHomeActivity.this;
    }

    @Override
    public void showUpdate(UpdateModel update) {
        SharedPreferenceUtil.putUpdateUrl(getContext(), update.download_url);
        SharedPreferenceUtil.putUpdateVer(getContext(), update.version_name);
        setUpdate();
    }

    @Override
    public void showNoUpdate() {
        SharedPreferenceUtil.putUpdateUrl(getContext(), "");
        setUpdate();
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }
}
