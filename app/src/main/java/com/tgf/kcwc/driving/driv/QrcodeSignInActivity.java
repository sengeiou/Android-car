package com.tgf.kcwc.driving.driv;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapHideAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.BaseArryBean;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.DrivingRoadBookBean;
import com.tgf.kcwc.mvp.model.QrcodeSiginBean;
import com.tgf.kcwc.mvp.presenter.SignInPresenter;
import com.tgf.kcwc.mvp.view.SignInView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MyListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/27.
 */

public class QrcodeSignInActivity extends BaseActivity implements SignInView {

    /**
     * 请求打电话的权限码
     */
    public static final int REQUEST_CODE_ASK_CALL_PHONE = 100;


    private LinearLayout siginmore;//签到更多
    private LinearLayout nonarrivalmore;//未到更多

    private MyListView siginlist; //签到列表
    private MyListView nonarrivallist; //未到列表

    private ImageView mSiginpull; //签到箭头
    private ImageView nonarrivalpull; //未签到箭头

    private WrapHideAdapter<QrcodeSiginBean.AlreadyCheckUser> mSigninAdapter;//已签到
    private List<QrcodeSiginBean.AlreadyCheckUser> checkInDataLists = new ArrayList<>(); //已签到
    private WrapHideAdapter<QrcodeSiginBean.NotCheckUser> mNonarrivallistAdapter;//未签到
    private List<QrcodeSiginBean.NotCheckUser> dataLists = new ArrayList<>();//未签到

    private SignInPresenter mSignInPresenter;
    private DrivingRoadBookBean.RideCheck mDataRideLeader = null;
    private int number = 0;
    private String threadId = "";
    private String title = "";

    private SimpleDraweeView qrcodeurl; //二维码
    private TextView mNumber; //第几站
    private TextView mTitle; //标题
    private TextView siginlistnumber; //签到人数
    private TextView nonarrivallistnumber; //未到人数

    private String mTel = "";

    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText(R.string.qrcodesignin);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcodesignin);
        number = getIntent().getIntExtra(Constants.IntentParams.ID2, 0);
        threadId = getIntent().getStringExtra(Constants.IntentParams.ID);
        title = getIntent().getStringExtra(Constants.IntentParams.ID3);
        mDataRideLeader = (DrivingRoadBookBean.RideCheck) getIntent().getSerializableExtra(Constants.IntentParams.DATA);
        mSignInPresenter = new SignInPresenter();
        mSignInPresenter.attachView(this);
        siginlist = (MyListView) findViewById(R.id.siginlist);
        nonarrivallist = (MyListView) findViewById(R.id.nonarrivallist);
        siginmore = (LinearLayout) findViewById(R.id.siginmore);
        nonarrivalmore = (LinearLayout) findViewById(R.id.nonarrivalmore);
        mSiginpull = (ImageView) findViewById(R.id.siginpull);
        nonarrivalpull = (ImageView) findViewById(R.id.nonarrivalpull);
        qrcodeurl = (SimpleDraweeView) findViewById(R.id.qrcodeurl);
        mNumber = (TextView) findViewById(R.id.number);
        mTitle = (TextView) findViewById(R.id.title);
        siginlistnumber = (TextView) findViewById(R.id.siginlistnumber);
        nonarrivallistnumber = (TextView) findViewById(R.id.nonarrivallistnumber);

        mSigninAdapter = new WrapHideAdapter<QrcodeSiginBean.AlreadyCheckUser>(mContext, R.layout.qrcodesignin_list_item, checkInDataLists, 3) {
            @Override
            public void convert(ViewHolder helper, QrcodeSiginBean.AlreadyCheckUser item) {
                SimpleDraweeView simpleDraweeView = helper.getView(R.id.motodetail_avatar_iv);
                TextView mName = helper.getView(R.id.name);
                TextView mTime = helper.getView(R.id.time);
                simpleDraweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.avatar, 140, 140)));
                mName.setText(item.nickname);
                mTime.setText(item.addTime);
            }
        };
        siginlist.setAdapter(mSigninAdapter);

        mNonarrivallistAdapter = new WrapHideAdapter<QrcodeSiginBean.NotCheckUser>(mContext, R.layout.qrcodenonsignin_list_item, dataLists, 3) {
            @Override
            public void convert(ViewHolder helper, final QrcodeSiginBean.NotCheckUser item) {
                SimpleDraweeView simpleDraweeView = helper.getView(R.id.motodetail_avatar_iv);
                TextView mName = helper.getView(R.id.name);
                ImageView phone = helper.getView(R.id.phone);
                simpleDraweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.avatar, 140, 140)));
                mName.setText(item.nickname);
                phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTel = item.tel;
                        requestPermission();
                    }
                });

            }
        };
        nonarrivallist.setAdapter(mNonarrivallistAdapter);

        siginmore.setOnClickListener(this);
        nonarrivalmore.setOnClickListener(this);
        KPlayCarApp mKPlayCarApp = (KPlayCarApp) getApplication();
        mSignInPresenter.getCreateCheck(IOUtils.getToken(mContext), threadId);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.siginmore:
                if (mSigninAdapter.getCount() == 3) {
                    mSigninAdapter.addItemNum(checkInDataLists.size());
                    mSiginpull
                            .setImageDrawable(getResources().getDrawable(R.drawable.btn_pullup));
                } else {
                    mSigninAdapter.addItemNum(3);
                    mSiginpull
                            .setImageDrawable(getResources().getDrawable(R.drawable.btn_pulldown));
                }
                mSigninAdapter.notifyDataSetChanged();
                break;
            case R.id.nonarrivalmore:
                if (mNonarrivallistAdapter.getCount() == 3) {
                    mNonarrivallistAdapter.addItemNum(dataLists.size());
                    nonarrivalpull.setImageDrawable(getResources().getDrawable(R.drawable.btn_pullup));
                } else {
                    mNonarrivallistAdapter.addItemNum(3);
                    nonarrivalpull.setImageDrawable(getResources().getDrawable(R.drawable.btn_pulldown));
                }
                mNonarrivallistAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void DetailsSucceed(DrivingRoadBookBean drivingRoadBookBean) {

    }

    @Override
    public void CreateChecksSucceed(QrcodeSiginBean qrcodeSiginBean) {
        // CommonUtils.showToast(mContext, "二维码成功");
        if (number < 10) {
            mNumber.setPadding(22, 11, 0, 0);
        } else {
            mNumber.setPadding(14, 11, 0, 0);
        }
        mNumber.setText(number + "");

        qrcodeurl.setImageURI(Uri.parse(qrcodeSiginBean.data.qrCode));
        checkInDataLists.addAll(qrcodeSiginBean.data.alreadyCheckUser);
        dataLists.addAll(qrcodeSiginBean.data.notCheckUser);
        mSigninAdapter.notifyDataSetChanged();
        mNonarrivallistAdapter.notifyDataSetChanged();
        mTitle.setText(title);
        siginlistnumber.setText(checkInDataLists.size() + "");
        if (checkInDataLists.size()<=3){
            siginmore.setVisibility(View.GONE);
        }else {
            nonarrivalmore.setVisibility(View.VISIBLE);
        }
        nonarrivallistnumber.setText(dataLists.size() + "");
        if (dataLists.size()<=3){
            nonarrivalmore.setVisibility(View.GONE);
        }else {
            nonarrivalmore.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void EndAcitivitySucceed(BaseArryBean baseBean) {

    }

    @Override
    public void LightenSucceed(BaseBean baseBean) {

    }

    @Override
    public void detailsDataFeated(String msg) {
        CommonUtils.showToast(mContext, msg);
        showLoadingIndicator(false);
        //finish();
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
        return mContext;
    }


    /**
     * 申请权限
     */
    private void requestPermission() {
        //判断Android版本是否大于23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE);

            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_ASK_CALL_PHONE);
                return;
            } else {
                callPhone();
            }
        } else {
            callPhone();
        }
    }

    /**
     * 注册权限申请回调
     *
     * @param requestCode  申请码
     * @param permissions  申请的权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callPhone();
                } else {
                    // Permission Denied
                    Toast.makeText(mContext, "CALL_PHONE Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 拨号方法
     */
    private void callPhone() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + mTel));
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSignInPresenter != null) {
            mSignInPresenter.detachView();
        }
    }

}
