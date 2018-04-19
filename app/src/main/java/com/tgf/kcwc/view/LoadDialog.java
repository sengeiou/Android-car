package com.tgf.kcwc.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.tgf.kcwc.R;

/**
 * Auther: Scott
 * Date: 2017/3/14 0014
 * E-mail:hekescott@qq.com
 */

public class LoadDialog extends Dialog {
    public LoadView mLoadview;
    private static  LoadDialog loadDialog ;
    public static   LoadDialog  getInstance(Context context){
        loadDialog = new LoadDialog(context);
        return  loadDialog;
    }

    private LoadDialog(@NonNull Context context) {
        super(context, R.style.MyAlertDialog);
        setContentView(R.layout.dialog_loading);
        mLoadview = (LoadView) findViewById(R.id.loaddialog_loadview);
        mLoadview.setVisibility(View.GONE);
        setCanceledOnTouchOutside(false);

    }
    @Override
    public void dismiss() {
        mLoadview.setVisibility(View.GONE);
        super.dismiss();
    }

    @Override
    public void show() {
        mLoadview.setVisibility(View.VISIBLE);
        super.show();

    }
    @Override
    public void cancel() {
        dismiss();
    }
}
