package com.tgf.kcwc.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.util.StringUtils;

/**
 * @anthor noti
 * @time 2017/8/8 9:22
 */

public class BackListDialog extends Dialog implements View.OnClickListener {
    ImageView close;
    Button cancel,sure;
    TextView nameTv;

    public OnClearItemClickListener onClearItemClickListener;

    public interface OnClearItemClickListener{
       void onClearClick();
    }

    public void setOnClearItemClickListener(OnClearItemClickListener onClearItemClickListener) {
        this.onClearItemClickListener = onClearItemClickListener;
    }

    public BackListDialog(@NonNull Context context,String nickname,OnClearItemClickListener clearItem) {
        super(context);
        onClearItemClickListener = clearItem;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_back_list);

        nameTv = (TextView) findViewById(R.id.dialog_name);
        close = (ImageView) findViewById(R.id.dialog_close);
        cancel = (Button) findViewById(R.id.dialog_know);
        sure = (Button) findViewById(R.id.dialog_know);

        if (StringUtils.checkNull(nickname)){
            nameTv.setText(Html.fromHtml(R.string.dialog_nick_left+"<font color=\"#4E81BA\">"+nickname+"</font>"+R.string.dialog_nick_right));
        }
        close.setOnClickListener(this);
        sure.setOnClickListener(this);
        cancel.setOnClickListener(this);
        setCanceledOnTouchOutside(true);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_close://关闭
                dismiss();
                break;
            case R.id.dialog_cancel://取消
                dismiss();
                break;
            case R.id.dialog_sure://移除
                if (null != onClearItemClickListener){
                    onClearItemClickListener.onClearClick();
                }
                dismiss();
                break;
        }
    }
}
