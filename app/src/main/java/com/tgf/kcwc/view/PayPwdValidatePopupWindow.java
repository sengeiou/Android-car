package com.tgf.kcwc.view;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.me.mybalance.forgetpaypwd.ForgetPayPwdActivity;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.presenter.MyWalletPresenter;
import com.tgf.kcwc.mvp.view.MyWalletView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.EncryptUtil;
import com.tgf.kcwc.util.IOUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Author:Jenny
 * Date:2017/10/23
 * E-mail:fishloveqin@gmail.com
 * <p>
 * 验证支付密码弹出框
 */

public class PayPwdValidatePopupWindow extends BottomPushPopupWindow implements MyWalletView<ResponseMessage> {


    protected View rootView;
    protected ImageView ivClose;
    protected PayPwdInputView password;

    private MyWalletPresenter mWalletPresenter;

    public PayPwdValidatePopupWindow(Context context, Object object) {
        super(context, object);
        mWalletPresenter = new MyWalletPresenter();
        mWalletPresenter.attachView(this);
    }

    @Override
    protected View generateCustomView(Object object) {
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(context).inflate(R.layout.pay_pwd_popupwindow, null, false);
        initView(view);
        //必须加这两行，不然不会显示在键盘上方
        setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        CommonUtils.openSoftKeyword(password);
        return view;
    }

    @Override
    public void show(Activity activity) {
        showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void initView(View rootView) {
        ivClose = (ImageView) rootView.findViewById(R.id.iv_close);
        TextView forgetPwdTv = rootView.findViewById(R.id.forgetPwdTv);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dismiss();
            }
        });
        password = (PayPwdInputView) rootView.findViewById(R.id.password);

        password.setPwdLengthListener(new PayPwdInputView.PwdLengthListener() {
            @Override
            public void onLength(String pwd) {

                if (pwd.trim().length() == Constants.PWD_MAX_LENGTH) {
                    //验证密码
                    mWalletPresenter.validatePayPwd(builderReqParams(EncryptUtil.encode(pwd)));
                }


            }
        });


        forgetPwdTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();
                CommonUtils.startNewActivity(context, ForgetPayPwdActivity.class);
            }
        });

    }

    private Map<String, String> builderReqParams(String pwd) {

        Map<String, String> params = new HashMap<String, String>();

        params.put("token", IOUtils.getToken(context));
        params.put("pay_password", pwd);
        return params;
    }

    @Override
    public void showData(ResponseMessage rMsg) {

        int code = rMsg.statusCode;

        if (mListener != null) {
            if (Constants.NetworkStatusCode.SUCCESS == code) {
                mListener.onSuccess(password.getPasswordString());
            } else {
                password.setText("");
                mListener.onFailure(password.getPasswordString());
            }

        }
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return context;
    }

    public interface PayPwdValidateListener {

        void onSuccess(String pwd);

        void onFailure(String pwd);
    }


    private PayPwdValidateListener mListener;

    public void setValidateListener(PayPwdValidateListener listener) {

        this.mListener = listener;
    }

    @Override
    public void dismiss() {
        CommonUtils.closeSoftKeyword(password);
        super.dismiss();



    }
}
