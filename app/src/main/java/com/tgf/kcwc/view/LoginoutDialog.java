package com.tgf.kcwc.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.tgf.kcwc.R;

/**
 * Auther: Scott
 * Date: 2017/3/14 0014
 * E-mail:hekescott@qq.com
 */

public class LoginoutDialog extends Dialog implements View.OnClickListener {


    private TextView mCancelTv;
    private TextView loginoutOktv = null;
    private IOnclickLisenter mIOnclickLisenter;

    public LoginoutDialog(@NonNull Context context) {
        super(context);
//        super(context, R.style.MyAlertDialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loginout_dialog);
        mCancelTv = (TextView) findViewById(R.id.loginout_cancletv);
        mCancelTv.setOnClickListener(this);
        loginoutOktv = (TextView) findViewById(R.id.loginout_oktv);
        loginoutOktv.setOnClickListener(this);
        setCanceledOnTouchOutside(false);

    }


    public void setOnLoginOutClickListener(IOnclickLisenter onclickLisenter) {
        this.mIOnclickLisenter = onclickLisenter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginout_cancletv:
                if(mIOnclickLisenter!=null)
                mIOnclickLisenter.CancleClick();
                break;
            case R.id.loginout_oktv:
                if(mIOnclickLisenter!=null)
                mIOnclickLisenter.OkClick();
                break;
        }
    }

    public interface IOnclickLisenter {
        void OkClick();

        void CancleClick();
    }
}
