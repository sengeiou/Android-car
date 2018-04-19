package com.tgf.kcwc.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.tgf.kcwc.R;

/**
 * @anthor noti
 * @time 2017/8/8 9:22
 */

public class DecryptDialog extends Dialog implements View.OnClickListener {
    ImageView close;
    Button konw;

    public DecryptDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_decrypt);
        close = (ImageView) findViewById(R.id.dialog_close);
        konw = (Button) findViewById(R.id.dialog_know);
        close.setOnClickListener(this);
        konw.setOnClickListener(this);
        setCanceledOnTouchOutside(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_close:
                dismiss();
                break;
            case R.id.dialog_know:
                dismiss();
                break;
        }
    }
}
