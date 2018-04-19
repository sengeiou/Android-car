package com.tgf.kcwc.app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.tgf.kcwc.R;
import com.tgf.kcwc.app.plussign.PopupMenuView;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.driving.track.DrivingHomeActivity;
import com.tgf.kcwc.fragments.TabHomeFragment;
import com.tgf.kcwc.fragments.TabMeFragment;
import com.tgf.kcwc.fragments.TabPlayHomeFragment;
import com.tgf.kcwc.fragments.TabSeeFragment;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.RideRunStateModel;
import com.tgf.kcwc.mvp.presenter.RideDataPresenter;
import com.tgf.kcwc.mvp.view.RideDataView;
import com.tgf.kcwc.service.ErgodicCityService;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.JPushUtil;
import com.tgf.kcwc.util.LocationUtil;
import com.tgf.kcwc.util.RxBus;
import com.tgf.kcwc.util.ShowAlertDialogUtil;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.wxapi.EaseLoginOutActivity;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.functions.Action1;

/**
 * Author:Jenny
 * Date:16/12/9  11:29
 * E-mail:fishloveqin@gmail.com
 * 应用全局导航页
 */
public class MainActivity extends BaseActivity {
    private final String TAG = MainActivity.class.getName();
    private FragmentTabHost mTabHost;
    private int currentTab = 0;
    private static final int REQUEST_CODE = 1;
    private int skipType = -1;
    private KPlayCarApp kPlayCarApp;
    private String permissions[] = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };

    private RideDataPresenter mPresenter;
    private AMapLocationClient locationClient;
    private AMapLocationListener aMapLocationListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (!TextUtil.isEmpty(IOUtils.getToken(mContext))) {
            mPresenter = new RideDataPresenter();
            mPresenter.attachView(mRideView);
            mPresenter.getRideRunState(IOUtils.getToken(mContext));
        }

        // 申请权限。
        AndPermission.with(this).requestCode(100).permission(permissions).callback(mPListener)
                .start();
        setContentView(R.layout.activity_main);
        initBottomTabs();

        kPlayCarApp = (KPlayCarApp) getApplication();
        //        ServiceCityPresenter serviceCityPresenter = new ServiceCityPresenter();
        //        serviceCityPresenter.attachView(this);
        //        serviceCityPresenter.getServiceCity(kPlayCarApp.getLattitude(), kPlayCarApp.getLongitude());
        //极光设置别名
        JPushUtil.setJpushTag(getContext());

        RxBus.$().register(Constants.IntentParams.SELECT_DRIVING)
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        String type = (String) o;
                        if (type.equals(Constants.PlayTabSkip.DRIVINGFRAGMENT)) {
                            mhHandler.sendEmptyMessageDelayed(1, 100);//跳转开车去
                        } else if (type.equals(Constants.PlayTabSkip.PLEASEPLAYFRAGMENT)) {
                            mhHandler.sendEmptyMessageDelayed(2, 100);//跳转请你玩
                        }else if (type.equals(Constants.PlayTabSkip.TRIPBOOKELIST)) {
                            mhHandler.sendEmptyMessageDelayed(3, 100);//跳转请你玩
                        }
                    }
                });
    }


    Handler mhHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        skipPlay(Constants.PlayTabSkip.DRIVINGFRAGMENT);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    break;
                case 2:
                    skipPlay(Constants.PlayTabSkip.PLEASEPLAYFRAGMENT);
                    break;
                case 3:
                    skipPlay(Constants.PlayTabSkip.TRIPBOOKELIST);
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.$().unregister(Constants.IntentParams.SELECT_EXHIBITION);
        RxBus.$().unregister(Constants.IntentParams.SELECT_DRIVING);
        if(locationClient!=null&&aMapLocationListener!=null){
            locationClient.unRegisterLocationListener(aMapLocationListener);
            aMapLocationListener =null;
        }
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    protected void setUpViews() {

    }

    private PermissionListener mPListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // Successfully.
            if (requestCode == 100) {
                initLocation();
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // Failure.
            if (requestCode == 100) {

                finish();
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (REQUEST_CODE == requestCode && grantResults != null && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        }

    }

    private void initBottomTabs() {
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        View tabIndicator = null;
        TextView title = null;
        ImageView ivBtn;

        //首页
        tabIndicator = getLayoutInflater().inflate(R.layout.tab_main_indicator,
                mTabHost.getTabWidget(), false);
        title = (TextView) tabIndicator.findViewById(R.id.tvTabMainIndicator);
        title.setText(getResources().getText(R.string.home).toString());
        ivBtn = (ImageView) tabIndicator.findViewById(R.id.ivTabMain);
        ivBtn.setImageResource(R.drawable.tab_home_selector);
        mTabHost.addTab(
                mTabHost.newTabSpec(Constants.Navigation.HOME_TAB).setIndicator(tabIndicator),
                TabHomeFragment.class, null);

        //看车
        tabIndicator = getLayoutInflater().inflate(R.layout.tab_main_indicator,
                mTabHost.getTabWidget(), false);
        title = (TextView) tabIndicator.findViewById(R.id.tvTabMainIndicator);
        title.setText(getResources().getText(R.string.see).toString());
        ivBtn = (ImageView) tabIndicator.findViewById(R.id.ivTabMain);
        ivBtn.setImageResource(R.drawable.tab_see_selector);
        mTabHost.addTab(
                mTabHost.newTabSpec(Constants.Navigation.SEE_TAB).setIndicator(tabIndicator),
                TabSeeFragment.class, null);
        //发帖
        tabIndicator = getLayoutInflater().inflate(R.layout.tab_posted_indicator,
                mTabHost.getTabWidget(), false);
        mTabHost.addTab(
                mTabHost.newTabSpec(Constants.Navigation.POSTED_TAB).setIndicator(tabIndicator), null,
                null);
        tabIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentTab == 1) {
                    TabSeeFragment tabSeeFragment = (TabSeeFragment) getSupportFragmentManager().findFragmentByTag(Constants.Navigation.SEE_TAB);

                    int currentItem = tabSeeFragment.getPager().getCurrentItem();

                    if (currentItem == 1) {
                        KPlayCarApp.putValue(Constants.KeyParams.KEY_DATA, Constants.TopicParams.EXHIBITION_PARAM);
                    } else {
                        KPlayCarApp.removeValue(Constants.KeyParams.KEY_DATA);
                    }
                } else {
                    KPlayCarApp.removeValue(Constants.KeyParams.KEY_DATA);
                }
                PopupMenuView.getInstance()._show(mContext, mTabHost);

            }
        });

        //玩车

        tabIndicator = getLayoutInflater().inflate(R.layout.tab_main_indicator,
                mTabHost.getTabWidget(), false);
        title = (TextView) tabIndicator.findViewById(R.id.tvTabMainIndicator);
        title.setText(getResources().getText(R.string.play).toString());
        ivBtn = (ImageView) tabIndicator.findViewById(R.id.ivTabMain);
        ivBtn.setImageResource(R.drawable.tab_play_selector);
        mTabHost.addTab(
                mTabHost.newTabSpec(Constants.Navigation.PLAY_TAB_TAB).setIndicator(tabIndicator),
                TabPlayHomeFragment.class, null);

        //我
        tabIndicator = getLayoutInflater().inflate(R.layout.tab_main_indicator,
                mTabHost.getTabWidget(), false);
        title = (TextView) tabIndicator.findViewById(R.id.tvTabMainIndicator);
        title.setText(getResources().getText(R.string.me).toString());
        ivBtn = (ImageView) tabIndicator.findViewById(R.id.ivTabMain);
        ivBtn.setImageResource(R.drawable.tab_me_selector);
        mTabHost.addTab(mTabHost.newTabSpec(Constants.Navigation.ME_TAB).setIndicator(tabIndicator),
                TabMeFragment.class, null);
        int index = getIntent().getIntExtra(Constants.IntentParams.INDEX, -1);
        if (index != -1) {
            mTabHost.setCurrentTab(index);
        } else {
            mTabHost.setCurrentTab(0);
        }
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabName) {

                switchToTab(tabName);

            }

        });
        if (getIntent().getIntExtra(Constants.IntentParams.SHOW_LOGINOUT, 0) == 1) {
                CommonUtils.startNewActivity(getContext(), EaseLoginOutActivity.class);
        }
//        startService(new Intent(getContext(), LoginSevice.class));
    }

    public void switchToTab(String tabName) {

        switch (tabName) {
            case Constants.Navigation.HOME_TAB:
                mTabHost.setCurrentTab(0);
                currentTab = 0;
                break;
            case Constants.Navigation.SEE_TAB:
                mTabHost.setCurrentTab(1);
                currentTab = 1;
                break;
            case Constants.Navigation.POSTED_TAB:
                mTabHost.setCurrentTab(2);
                break;
            case Constants.Navigation.PLAY_TAB_TAB:
                currentTab = 3;
                mTabHost.setCurrentTab(3);
                if (skipType == -1) {
                    KPlayCarApp.putValue(Constants.IntentParams.PLAYINDEX, Constants.PlayTabSkip.TABPLAYFRAGMENT);
                }
                break;
            case Constants.Navigation.ME_TAB:
                if (!IOUtils.isLogin(mContext)) {
                    mTabHost.setCurrentTab(currentTab);
                    return;
                }
                mTabHost.setCurrentTab(4);
                currentTab = 4;
                break;
        }
        skipType = -1;
    }

    public void skipPlay(String type) {
        skipType = 1;
        KPlayCarApp.putValue(Constants.IntentParams.PLAYINDEX, type);
        mTabHost.setCurrentTab(3);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        int index = intent.getIntExtra(Constants.IntentParams.INDEX, -1);
        if (index != -1) {
            mTabHost.setCurrentTab(index);
        } else {
            mTabHost.setCurrentTab(0);
        }

        BaseFragment fragment = (BaseFragment) getSupportFragmentManager()
                .findFragmentByTag(Constants.Navigation.ME_TAB);

        if (fragment != null) {

            fragment.onShare(Constants.InteractionCode.REQUEST_CODE, RESULT_OK, intent,
                    Constants.InteractionCode.WEB_BO_TYPE);
            return;
        }
        fragment = (BaseFragment) getSupportFragmentManager()
                .findFragmentByTag(Constants.Navigation.SEE_TAB);
        if (fragment != null) {
            fragment.onShare(Constants.InteractionCode.REQUEST_CODE, RESULT_OK, intent,
                    Constants.InteractionCode.WEB_BO_TYPE);
            return;
        }
        fragment = (BaseFragment) getSupportFragmentManager()
                .findFragmentByTag(Constants.Navigation.HOME_TAB);
        if (fragment != null) {

            Logger.d("新浪微博分享");
            fragment.onShare(Constants.InteractionCode.REQUEST_CODE, RESULT_OK, intent,
                    Constants.InteractionCode.WEB_BO_TYPE);
            return;
        }
    }

    public Context getContext() {
        return this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        BaseFragment fragment = null;

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.InteractionCode.REQUEST_CODE_HOME) {
            fragment = (BaseFragment) getSupportFragmentManager()
                    .findFragmentByTag(Constants.Navigation.HOME_TAB);
            if (fragment != null) {
                //                fragment.onShare(requestCode, resultCode, data, 3);
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        } else if (requestCode == Constants.InteractionCode.QQ_SHARE_REQUEST_CODE) {
            fragment = (BaseFragment) getSupportFragmentManager()
                    .findFragmentByTag(Constants.Navigation.ME_TAB);
            if (fragment == null) {
                fragment = (BaseFragment) getSupportFragmentManager()
                        .findFragmentByTag(Constants.Navigation.SEE_TAB);
            }
            if (fragment == null) {
                fragment = (BaseFragment) getSupportFragmentManager()
                        .findFragmentByTag(Constants.Navigation.HOME_TAB);
            }
            if (fragment != null)
                fragment.onShare(requestCode, resultCode, data,
                        Constants.InteractionCode.QQ_OR_ZONE_TYPE);
        } else if (requestCode == Constants.InteractionCode.QQ_ZONE_SHARE_REQUEST_CODE) {

            fragment = (BaseFragment) getSupportFragmentManager()
                    .findFragmentByTag(Constants.Navigation.ME_TAB);
            if (fragment == null) {
                fragment = (BaseFragment) getSupportFragmentManager()
                        .findFragmentByTag(Constants.Navigation.SEE_TAB);
            }
            if (fragment == null) {
                fragment = (BaseFragment) getSupportFragmentManager()
                        .findFragmentByTag(Constants.Navigation.HOME_TAB);
            }
            if (fragment != null)
                fragment.onShare(requestCode, resultCode, data,
                        Constants.InteractionCode.QQ_OR_ZONE_TYPE);
        } else if (requestCode == Constants.InteractionCode.MAIN_PLAY_DRIVING_START || requestCode == Constants.InteractionCode.MAIN_PLAY_DRIVING_END) {
            fragment = (BaseFragment) getSupportFragmentManager()
                    .findFragmentByTag(Constants.Navigation.PLAY_TAB_TAB);
            if (fragment != null) {
                fragment.onAddress(requestCode, resultCode, data);
            }
        }

    }

    /**
     * 递归调用，对所有的子Fragment生效
     *
     * @param fragment
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void handleResult(Fragment fragment, int requestCode, int resultCode, Intent data) {
        fragment.onActivityResult(requestCode, resultCode, data);//调用每个Fragment的onActivityResult
        Log.e(TAG, "MyBaseFragmentActivity");
        List<Fragment> childFragment = fragment.getChildFragmentManager().getFragments(); //找到第二层Fragment
        if (childFragment != null)
            for (Fragment f : childFragment)
                if (f != null) {
                    handleResult(f, requestCode, resultCode, data);
                }
        if (childFragment == null)
            Log.e(TAG, "MyBaseFragmentActivity1111");
    }

    private void initLocation() {
        locationClient = LocationUtil.getGaodeLocationClient(getContext());
        //对比当前城市
        aMapLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation bdLocation) {
                if (!TextUtils.isEmpty(bdLocation.getCity())) {
                    kPlayCarApp.latitude = bdLocation.getLatitude() + "";
                    kPlayCarApp.longitude = bdLocation.getLongitude() + "";
                    kPlayCarApp.locCityName = bdLocation.getCity();
                    kPlayCarApp.mAddressInfo = bdLocation.getPoiName();
                    kPlayCarApp.adcode = bdLocation.getAdCode();
                    //对比当前城市
                    Intent intent = new Intent(getBaseContext(), ErgodicCityService.class);
                    startService(intent);
                }

            }
        };
        locationClient.setLocationListener(aMapLocationListener);
        locationClient.startLocation();
    }

    private static Boolean isQuit = false;
    private long mExitTime = 0;

    /**
     * 监听键盘键值，实现点击丙次退出应用
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {//
                // 如果两次按键时间间隔大于2000毫秒，则不退出
                CommonUtils.showToast(mContext, "再按一次退出程序");
                mExitTime = System.currentTimeMillis();// 更新mExitTime
            } else {
                KPlayCarApp.loginOut();//当前退出只是finish栈里的Activity,不是严格意义上的退出
                //System.exit(0);// 否则退出程序

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    private RideRunStateModel mModel;
    private RideDataView<RideRunStateModel> mRideView = new RideDataView<RideRunStateModel>() {
        @Override
        public void showDatas(RideRunStateModel rideRunStateModel) {

            mModel = rideRunStateModel;
            if (mModel.rideId > 0) {
                ShowAlertDialogUtil
                        .showRideOperatorDialog(
                                mContext,
                                mGlobalContext
                                        .getAlertDialogEntities()
                                        .get(
                                                4),
                                mSweetEndClickListener,
                                mSweetResumeClickListener);
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
            return mContext;
        }
    };

    SweetAlertDialog.OnSweetClickListener mSweetEndClickListener = new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog sweetAlertDialog) {

            sweetAlertDialog
                    .dismissWithAnimation();
            jumpToRidePage(true);

        }
    };

    SweetAlertDialog.OnSweetClickListener mSweetResumeClickListener = new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog sweetAlertDialog) {

            sweetAlertDialog
                    .dismissWithAnimation();
            jumpToRidePage(false);
        }
    };

    private void jumpToRidePage(boolean isEnd) {

        Intent toDrivingIntent = new Intent(getContext(), DrivingHomeActivity.class);
        toDrivingIntent.putExtra(Constants.IntentParams.BG_RIDE_TYPE, 1);
        toDrivingIntent.putExtra(Constants.IntentParams.ID, mModel.rideId);

        String model = mModel.module;
        String data = "";
        switch (model) {
            case Constants.RideTypes.RIDE_BOOK:
                data = Constants.IntentParams.RIDE_BOOK_MODULE;
                break;
            case Constants.RideTypes.CYCLE:
                data = Constants.IntentParams.DRIVER_CAR_MODULE;
                toDrivingIntent.putExtra(Constants.IntentParams.THREAD_ID, mModel.threadId);
                break;
            case Constants.RideTypes.PLAY:
                data = Constants.IntentParams.PLAY_CAR_MODULE;
                toDrivingIntent.putExtra(Constants.IntentParams.THREAD_ID, mModel.threadId);
                break;
        }
        toDrivingIntent.putExtra(Constants.IntentParams.DATA,
                data);
        toDrivingIntent.putParcelableArrayListExtra(Constants.IntentParams.DATA3, mModel.nodeList);
        toDrivingIntent.putExtra(Constants.IntentParams.IS_END, isEnd);
        startActivity(toDrivingIntent);
    }

}
