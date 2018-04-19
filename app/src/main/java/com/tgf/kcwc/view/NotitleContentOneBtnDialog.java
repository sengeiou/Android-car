package com.tgf.kcwc.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.tgf.kcwc.R;

/**
 * Auther: Scott
 * Date: 2017/3/14 0014
 * E-mail:hekescott@qq.com
 */

public class NotitleContentOneBtnDialog extends Dialog implements View.OnClickListener {


    private TextView yesTv = null;
    private IOnclickLisenter mIOnclickLisenter;
    private final TextView mContentTv;

    public NotitleContentOneBtnDialog(@NonNull Context context) {
        super(context);
//        super(context, R.style.MyAlertDialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.notitle_onebtn_dialog);
        mContentTv = (TextView) findViewById(R.id.diloag_contenttv);
        yesTv = (TextView) findViewById(R.id.loginout_oktv);
        yesTv.setOnClickListener(this);
        setCanceledOnTouchOutside(false);

    }


    public void setContentText(String contentText){
        mContentTv.setText(contentText);
    }
    public void setHtmlContentText(String contentText){
        mContentTv.setText(Html.fromHtml(contentText));
    }
    public void setYesText(String yesText){
        yesTv.setText(yesText);
    }

    public void setOnLoginOutClickListener(IOnclickLisenter onclickLisenter) {
        this.mIOnclickLisenter = onclickLisenter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginout_oktv:
                if(mIOnclickLisenter!=null)
                mIOnclickLisenter.OkClick();
                break;
        }
    }

    public interface IOnclickLisenter {
        void OkClick();

    }
}
