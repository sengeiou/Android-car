package com.tgf.kcwc.me;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.UserHomeDataModel;
import com.tgf.kcwc.util.QRCodeUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

/**
 * Author:Jenny
 * Date:2017/5/16 10:32
 * E-mail:fishloveqin@gmail.com
 * 我的二维码
 **/
public class MyQCodeActivity extends BaseActivity {

    protected SimpleDraweeView headerImg;
    protected TextView         name;
    protected RelativeLayout   userInfoLayout;
    protected ImageView        qCodeImg;
    protected ImageView        applogo;
    protected TextView         desc;
    UserHomeDataModel          mModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mModel = intent.getParcelableExtra(Constants.IntentParams.DATA);
        super.setContentView(R.layout.activity_my_qcode_layout);

    }

    @Override
    protected void setUpViews() {
        initView();

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("我的二维码");
//        function.setImageResource(R.drawable.global_nav_n);
//        function.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    private ImageView        mQCodeImg;
    private SimpleDraweeView mHeaderImg;
    private TextView         mName;
    private TextView         mTitle;

    private void initView() {

        headerImg = (SimpleDraweeView) findViewById(R.id.headerImg);
        name = (TextView) findViewById(R.id.name);
        userInfoLayout = (RelativeLayout) findViewById(R.id.userInfoLayout);
        qCodeImg = (ImageView) findViewById(R.id.qCodeImg);
        applogo = (ImageView) findViewById(R.id.applogo);
        desc = (TextView) findViewById(R.id.desc);
        if (mModel != null) {

            headerImg.setImageURI(Uri.parse(URLUtil.builderImgUrl(mModel.avatar, 144, 144)));
            name.setText(mModel.nickname);
            Bitmap bitmap = QRCodeUtils.createQRCode(Constants.QRValues.ADD_FRIEND_ID+mModel.id, 600);
            qCodeImg.setImageBitmap(bitmap);
        }


    }
}
