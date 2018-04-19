package com.tgf.kcwc.ticket;

import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.view.FunctionView;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Author：Jenny
 * Date:2017/1/3 20:44
 * E-mail：fishloveqin@gmail.com
 */

public class AboutExhibitionActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about_exhibition);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText(R.string.about_ex);
    }

    @Override
    protected void setUpViews() {

    }
}
