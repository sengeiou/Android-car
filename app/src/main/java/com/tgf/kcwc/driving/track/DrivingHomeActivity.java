package com.tgf.kcwc.driving.track;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lzy.imagepicker.ImagePicker;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.me.myline.RideReportActivity;
import com.tgf.kcwc.membercenter.AddTravelSpotActivity;
import com.tgf.kcwc.mvp.model.AlertDialogEntity;
import com.tgf.kcwc.mvp.model.MyMapLocation;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.RideAutoData;
import com.tgf.kcwc.mvp.model.RideData;
import com.tgf.kcwc.mvp.model.RideDataNodeItem;
import com.tgf.kcwc.mvp.model.RideLinePreviewModel;
import com.tgf.kcwc.mvp.model.RideReportDetailModel;
import com.tgf.kcwc.mvp.presenter.FileUploadPresenter;
import com.tgf.kcwc.mvp.presenter.RidingRunPresenter;
import com.tgf.kcwc.mvp.view.FileUploadView;
import com.tgf.kcwc.mvp.view.RidingRunView;
import com.tgf.kcwc.posting.PostingActivity;
import com.tgf.kcwc.receiver.Utils;
import com.tgf.kcwc.service.LocationService;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.FileUtil;
import com.tgf.kcwc.util.GpsUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.NumFormatUtil;
import com.tgf.kcwc.util.ScreenShotHelper;
import com.tgf.kcwc.util.SensorEventHelper;
import com.tgf.kcwc.util.ShowAlertDialogUtil;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.ThreadPoolProxy;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.InstrumentView;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.File;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.tgf.kcwc.seecar.WaittingPriceActivity.LOCATION_MARKER_FLAG;

/**
 * Author:Jenny
 * Date:2017/4/25
 * E-mail:fishloveqin@gmail.com
 * <p>
 * 驾途主页
 */

public class DrivingHomeActivity extends BaseActivity
        implements RidingRunView, AMap.OnMapScreenShotListener {

    protected ImageButton titleBarBack;
    protected ImageButton cameraBtn;
    protected ImageButton locationBtn;
    protected ImageButton settingBtn;
    protected RelativeLayout titleBar;
    protected RelativeLayout titleLayout;
    protected GridView grid;
    protected RelativeLayout trackingDataLayout;
    protected ImageView postBtn;
    protected TextView readyBtn;
    protected ImageView navBtn;
    protected RelativeLayout bottomLayout;
    protected LinearLayout handle;
    protected LinearLayout content;
    protected SlidingDrawer slidingdrawer;
    protected LinearLayout handleContentLayout;
    protected InstrumentView instrumentView;
    protected SimpleDraweeView myImageView;
    protected TextView gpsStatusTv;
    protected TextView weatherTv;
    protected RelativeLayout baseInfoLayout;
    protected TextView counterTv;
    protected GridView grid2;
    protected RelativeLayout trackingDataLayout2;
    protected TextView navBtn2;
    protected ImageView readyBtn2;
    protected TextView postBtn2;
    protected RelativeLayout bottomLayout2;
    private Typeface typeFace;
    private List<DataItem> mTrackDatas = new ArrayList<DataItem>();
    private List<DataItem> mTrack2Datas = new ArrayList<DataItem>();

    private WrapAdapter<DataItem> mTrackAdapter1;
    private WrapAdapter<DataItem> mTrackAdapter2;

    private MapView mapView;
    private AMap aMap;

    private Marker locationMarker;
    private Polyline mPolyline;                                                         // 画多条线段
    private PolylineOptions mPOptions;                                                         //线段参数对象
    private SensorEventHelper mSensorHelper;
    private int[] mGPSDrawables = {R.drawable.gps_signal_none,
            R.drawable.gps_siignal_one,
            R.drawable.gps_signal_two,
            R.drawable.gps_signal_three,};

    private static final int WHAT_VALUE1 = 0x0001;
    private static final int WHAT_VALUE3 = 0x0003;
    private static final int WHAT_VALUE4 = 0x0004;
    private static final int WHAT_VALUE5 = 0x0005;
    private static final int WHAT_VALUE6 = 0x0006;
    private static final int WHAT_VALUE8 = 0x0008;
    private static final int REFRESH_DATA_WHAT = 0x0007;
    private String permissions[] = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE

    };

    //是否是第一次点击开始骑行
    private RidingRunPresenter mPresenter;

    private FileUploadPresenter mUploadPresenter = null;
    public static final String RECEIVER_ACTION = "com.tgf.kcwc.riderunreceiver";

    private RelativeLayout mMapRootLayout;

    private ImageView mTrafficImgView;

    private StringBuilder mNodeInfo = null;

    private static final int MIN_KMILE = 1;                                           //最小必须为1km才开始记录

    private int mRideId = -1;

    private String mData;

    private int mData2;

    private ImageView mMyLocationBtn;

    private boolean isForceEnd;

    //private boolean                     isGoOn;

    private ArrayList<RideDataNodeItem> mData3 = null;

    private RideRunWrapper mRideRunWrappter;

    List<AlertDialogEntity> alertDialogEntities = null;

    private boolean isGoOut = false;

    private boolean isEnd;
    private int mRideType = -1;
    private boolean isFlag = true;                                        //线程的标志位

    private String backType = ""; //载入路线返回类型
    private RideLinePreviewModel mPrevModel;
    private String mPoiName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //实始化对话框需要的数据
        alertDialogEntities = mGlobalContext.getAlertDialogEntities();
        tel = IOUtils.getAccount(mContext).userInfo.tel;
        //骑行Presenter
        mPresenter = new RidingRunPresenter();
        mPresenter.attachView(this);
        //文件上传Presenter;
        mUploadPresenter = new FileUploadPresenter();
        mUploadPresenter.attachView(fileUploadView);
        Intent intent = getIntent();

        mRideId = intent.getIntExtra(Constants.IntentParams.ID, -1);
        mData = intent.getStringExtra(Constants.IntentParams.DATA);
        mData2 = intent.getIntExtra(Constants.IntentParams.DATA2, -1);
        mData3 = intent.getParcelableArrayListExtra(Constants.IntentParams.DATA3);
        mThreadId = intent.getIntExtra(Constants.IntentParams.THREAD_ID, -1);
        mRideType = intent.getIntExtra(Constants.IntentParams.BG_RIDE_TYPE, -1);
        isEnd = intent.getBooleanExtra(Constants.IntentParams.IS_END, false);
        if (mRideType == 1) {
            isBg = true;
        }
        isTitleBar = false;

        super.setContentView(R.layout.activity_track_driving_home);

        mapView.onCreate(savedInstanceState);

        // 申请权限。
        AndPermission.with(this).requestCode(100).permission(permissions).callback(mPListener)
                .start();

        String lat = mGlobalContext.getLattitude();
        String lng = mGlobalContext.getLongitude();
        if (TextUtil.isEmpty(lat)) {
            lat = "29.03";
        }
        if (TextUtil.isEmpty(lng)) {

            lng = "106.12";
        }
        mLocationLat = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

        switch (mData) {
            case Constants.IntentParams.RIDE_BOOK_MODULE:
                mRideRunWrappter = new RideRunWrapper(new RideBookImp(mPresenter));

                break;
            case Constants.IntentParams.DRIVER_CAR_MODULE:
                mRideRunWrappter = new RideRunWrapper(new DriverGoImp(mPresenter));
                break;

            case Constants.IntentParams.PLAY_CAR_MODULE:
                mRideRunWrappter = new RideRunWrapper(new PlayImp(mPresenter));
                break;
        }

        readyBtn.setEnabled(false);
        readyBtn2.setEnabled(false);
        if (!GpsUtil.isOPen(mContext)) {
            showSettingsAlert();
        } else {

            //打开定位服务
            startLocationService(true, false);
            //进入驾图页面、设置当前为骑行状态
            readyBtn2.setSelected(true);
            readyBtn.setSelected(true);
            setDrivingState(readyBtn.isSelected(), false);

            if (mData3 != null && mData3.size() > 0) {

                loadPointsDatas();
            }
            ThreadPoolProxy.getInstance().executeTask(mGetLocationInfoRunnable);
        }


    }

    private boolean isBg = false;


    private void loadPointsDatas() {

        ThreadPoolProxy.getInstance().executeTask(

                new Runnable() {
                    @Override
                    public void run() {

                        int size = mData3.size();
                        RideDataNodeItem itemNode = mData3.get(size - 1);
                        //取出总里程、总行驶时间、极速、海拔
                        mTotalMile = itemNode.mileage;
                        mTimeCounter = itemNode.rideTime;
                        mTempMaxSpeed = itemNode.speedMax;
                        mAltitude = itemNode.altitude;
                        cityName = itemNode.cityName;
                        mAddress = itemNode.address;

                        if (TextUtil.isEmpty(itemNode.lng) || TextUtil.isEmpty(itemNode.lat)) {
                            mLat = 109.13;
                            mLng = 29.03;
                        } else {
                            mLat = Double.parseDouble(itemNode.lat);
                            mLng = Double.parseDouble(itemNode.lng);
                        }


                        for (RideDataNodeItem item : mData3) {

                            final RideData data = new RideData();
                            data.latitude = Double.parseDouble(item.lat);
                            data.longitude = Double.parseDouble(item.lng);
                            data.speed = item.speed;
                            data.totalMile = item.mileage;
                            data.maxSpeed = item.speedMax;
                            data.alt = item.altitude;
                            data.dipAngle = item.bendingAngle;
                            data.address = item.address;
                            data.cityName = item.cityName;
                            data.time = item.rideTime;
                            data.rideId = mRideId;
                            mRideDatas.add(data);

                        }
                        Message msg = mHandler.obtainMessage();


                        ArrayList<RideData> datas = IOUtils.readPointsFromSD(tel);
                        if (datas != null && datas.size() > 0) {
                            msg.obj = datas;
                            mRideDatas = datas;
                            RideData data = mRideDatas.get(datas.size() - 1);
                            //取出总里程、总行驶时间、极速、海拔
                            mTotalMile = data.totalMile;
                            mTimeCounter = (int) data.time;
                            mTempMaxSpeed = data.maxSpeed;
                            mAltitude = data.alt;
                            cityName = data.cityName;
                            mAddress = data.address;
                            msg.obj = mRideDatas;
                        } else {
                            msg.obj = mRideDatas;
                        }
                        mHandler.sendEmptyMessage(WHAT_VALUE4);
                        msg.what = WHAT_VALUE1;
                        mHandler.sendMessage(msg);
                    }
                });

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        }
        //注册广播
        regReceiver();

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();

        if (locationChangeBroadcastReceiver != null)

            unregisterReceiver(locationChangeBroadcastReceiver);
        isFlag = false;
        if (mSensorHelper != null) {
            mSensorHelper.unRegisterSensorListener();
            //mSensorHelper.setCurrentMarker(null);
            // mSensorHelper = null;
        }
        Logger.e("DrivingHomeActivity:onPause");
    }

    @Override
    protected void onStop() {

        super.onStop();
        Logger.e("DrivingHomeActivity:onStop");


        //ThreadPoolProxy.getInstance().removeTask(mGetLocationInfoRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

        if (mLocMarker != null) {
            mLocMarker.destroy();
        }
    }

    private Runnable mGetLocationInfoRunnable = new Runnable() {
        @Override
        public void run() {

            while (isFlag) {

                try {
                    Thread.sleep(1000);
                    Logger.e("正在定位:");
                    if (aMapLocation != null) {
                        Logger.e("第一次定位:" + aMapLocation.getAddress());
                        mPoiName = aMapLocation.getPoiName();
                        mAddress = aMapLocation.getAddress();
                        if (mRideId != -1) {
                            sendSaveDataBroadcast();

                        }
                        Message msg = mHandler.obtainMessage();
                        msg.obj = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                        msg.what = WHAT_VALUE6;
                        mHandler.sendMessage(msg);
                        execute();
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }
    };


    private void execute() {

        if (mRideType == 1) {


            if (isEnd) {
                mHandler.sendEmptyMessage(WHAT_VALUE5);

            } else {
                if (mData3 != null && mData3.size() > 0) {
                    keepUpRide(false);
                }

            }

        }

        if (Constants.IntentParams.ALL_NEWLINE == mData2) {
            mRideRunWrappter.start(builderRideStartRequestParams(aMapLocation));
        } else if (Constants.IntentParams.CONTINUE_NEWLINE == mData2) {
            isForceEnd = true;
            startLocationService(false, true);
            mRideRunWrappter.stop(rodeEndParams()); //结束之前未完成的线路
            clearCacheData();
            mRideRunWrappter.start(builderRideStartRequestParams(aMapLocation)); //开始新的线路

        } else if (Constants.IntentParams.CONTINUE_CONTINUELINE == mData2) {
            if (mData3 != null && mData3.size() > 0) {
                keepUpRide(true);

            }
        }

        if (mThreadId != -1 && mRideType != 1) {
            mRideRunWrappter.start(builderRideStartRequestParams(aMapLocation));
        }

    }

    private PermissionListener mPListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // Successfully.
            if (requestCode == 100) {

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
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    protected void setUpViews() {
        mapView = (MapView) findViewById(R.id.map);
        initView();
        initTrackDatas();
        typeFace = mGlobalContext.getTypeface();

        readyBtn.setTypeface(typeFace);
        readyBtn.setTextSize(45);
        //初始化码表盘
        instrumentView.setProgress(0);
        instrumentView.setTypeface(typeFace);
        counterTv.setTypeface(typeFace);

        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }

        searchLiveWeather();//调用天气接口获取天气数据
    }

    private FileUploadView<DataItem> fileUploadView = new FileUploadView<DataItem>() {
        @Override
        public void resultData(DataItem item) {

            int code = item.code;
            if (code == Constants.NetworkStatusCode.SUCCESS) {

                if (shotCoverType == 2) {
                    mPresenter.rideEnd(rodeEndParams(item.content));
                }
                if (shotCoverType == 1) {

                    RideData rideData = new RideData();
                    rideData.address = mAddress;
                    rideData.alt = mAltitude;
                    rideData.totalMile = mTotalMile;
                    rideData.rideId = mRideId;
                    rideData.latitude = mLat;
                    rideData.longitude = mLng;
                    rideData.speed = mSpeed;
                    rideData.time = mTimeCounter;
                    rideData.cityName = cityName;
                    rideData.maxSpeed = mTempMaxSpeed;
                    mPresenter.ridePause(rideRunParams(rideData, item.content));

                }
            } else {

                CommonUtils.showToast(mContext, "上传数据失败，请稍候重试!");
            }

        }

        @Override
        public void setLoadingIndicator(boolean active) {

        }

        @Override
        public void showLoadingTasksError() {

            dismissLoadingDialog2();
        }

        @Override
        public Context getContext() {
            return mContext;
        }
    };

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private boolean isGo = false;//是否是Ride 行驶状态

    private void setDrivingState(boolean flag, boolean isEnd) {

        startLocationService(flag, isEnd);
        isGo = flag;
        if (flag) {
            readyBtn.setText("STOP");
            readyBtn.setTextColor(mRes.getColor(R.color.text_color12));
            readyBtn2.setImageResource(R.drawable.track_pause_icon);
            bottomLayout.setBackgroundColor(mRes.getColor(R.color.style_bg5));
            //startTimer();
        } else {
            readyBtn.setText("GO");
            readyBtn.setTextColor(mRes.getColor(R.color.text_color11));
            readyBtn2.setImageResource(R.drawable.track_go_icon);
            bottomLayout.setBackgroundColor(mRes.getColor(R.color.style_bg1));
            //stopTimer();
        }
    }


    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {

        aMap.setMapType(AMap.MAP_TYPE_NORMAL); //设置正常模式
        aMap.getUiSettings().setZoomControlsEnabled(true);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(false);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.getUiSettings().setScaleControlsEnabled(true);
        aMap.getUiSettings().setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
        aMap.getUiSettings().setZoomInByScreenCenter(true);
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        //设置地图绽放比例
        //aMap.moveCamera(CameraUpdateFactory.zoomTo(16f));
    }

    private ImageView mWeatherImg;

    private TextView mTitleTv;

    private void initView() {
        mSensorHelper = new SensorEventHelper(this);
        mMapRootLayout = (RelativeLayout) findViewById(R.id.mapRootLayout);
        mTrafficImgView = (ImageView) findViewById(R.id.trafficBtn);
        mTitleTv = (TextView) findViewById(R.id.title_bar_text);
        mTrafficImgView.setOnClickListener(this);
        titleBarBack = (ImageButton) findViewById(R.id.title_bar_back);
        titleBarBack.setOnClickListener(this);
        cameraBtn = (ImageButton) findViewById(R.id.cameraBtn);
        locationBtn = (ImageButton) findViewById(R.id.locationBtn);
        settingBtn = (ImageButton) findViewById(R.id.settingBtn);
        titleBar = (RelativeLayout) findViewById(R.id.titleBar);
        titleLayout = (RelativeLayout) findViewById(R.id.titleLayout);
        grid = (GridView) findViewById(R.id.grid);
        trackingDataLayout = (RelativeLayout) findViewById(R.id.trackingDataLayout);
        navBtn = (ImageView) findViewById(R.id.navBtn);
        readyBtn = (TextView) findViewById(R.id.readyBtn);
        postBtn = (ImageView) findViewById(R.id.postBtn);
        mMyLocationBtn = (ImageView) findViewById(R.id.myLocationBtn);
        mMyLocationBtn.setOnClickListener(this);
        bottomLayout = (RelativeLayout) findViewById(R.id.bottomLayout);
        handle = (LinearLayout) findViewById(R.id.handle);
        content = (LinearLayout) findViewById(R.id.content);
        slidingdrawer = (SlidingDrawer) findViewById(R.id.slidingdrawer);
        mWeatherImg = (ImageView) findViewById(R.id.weatherImg);
        slidingdrawer.setOnDrawerCloseListener(mCloseListener);
        slidingdrawer.setOnDrawerOpenListener(mOpenListener);

        cameraBtn.setOnClickListener(this);
        locationBtn.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        navBtn.setOnClickListener(this);
        readyBtn.setOnClickListener(this);
        postBtn.setOnClickListener(this);
        postBtn.setTag("click_intercepted");
        readyBtn.setTag("click_intercepted");
        navBtn.setTag("click_intercepted");
        handleContentLayout = (LinearLayout) findViewById(R.id.handleContentLayout);
        instrumentView = (InstrumentView) findViewById(R.id.instrumentView);
        myImageView = (SimpleDraweeView) findViewById(R.id.my_image_view);
        gpsStatusTv = (TextView) findViewById(R.id.gpsStatusTv);
        weatherTv = (TextView) findViewById(R.id.weatherTv);
        weatherTv.setText("14\u2103"); //\u2109
        baseInfoLayout = (RelativeLayout) findViewById(R.id.baseInfoLayout);
        counterTv = (TextView) findViewById(R.id.counterTv);
        grid2 = (GridView) findViewById(R.id.grid2);
        trackingDataLayout2 = (RelativeLayout) findViewById(R.id.trackingDataLayout2);
        navBtn2 = (TextView) findViewById(R.id.navBtn2);
        navBtn.setOnClickListener(this);
        navBtn2.setOnClickListener(this);
        readyBtn2 = (ImageView) findViewById(R.id.readyBtn2);
        postBtn2 = (TextView) findViewById(R.id.postBtn2);
        readyBtn2.setOnClickListener(this);
        postBtn2.setOnClickListener(this);
        bottomLayout2 = (RelativeLayout) findViewById(R.id.bottomLayout2);

        slidingdrawer.open();//默认打开

        ViewUtil.canvasTextDrawRight(mContext, mGPSDrawables[0], gpsStatusTv, 20);
    }

    private String tel;

    /**
     * 注册广播
     */
    private void regReceiver() {
        if (locationChangeBroadcastReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RECEIVER_ACTION);
            registerReceiver(locationChangeBroadcastReceiver, intentFilter);
        }


    }

    /**
     * 开始定位服务
     *
     * @param isGo  是否开始
     * @param isEnd 是否结束
     */
    private void startLocationService(boolean isGo, boolean isEnd) {


        Intent intent = new Intent();
        intent.setClass(this, LocationService.class);
        intent.putExtra(Constants.IntentParams.IS_GO, isGo);
        intent.putExtra(Constants.IntentParams.USER_ID, tel);
        intent.putExtra(Constants.IntentParams.IS_END, isEnd);
        startService(intent);
    }

    /**
     * 将之前的数据传递到后台服务
     */
    private void setRideExistData(RideData rideExistData) {

        Intent intent = new Intent(LocationService.RIDE_LAST_POINT_DATA_ACTION);
        intent.putExtra(Constants.IntentParams.DATA5, rideExistData);
        sendBroadcast(intent);
    }

    private void sendSaveDataBroadcast() {

        Intent intent = new Intent(LocationService.SAVE_DATA_ACTION);
        intent.putExtra(Constants.IntentParams.RIDE_ID, mRideId);
        sendBroadcast(intent);
    }

    /**
     * 关闭服务
     * 先关闭守护进程，再关闭定位服务
     */
    private void stopLocationService() {
        sendBroadcast(Utils.getCloseBrodecastIntent());
        //LocationStatusManager.getInstance().resetToInit(getApplicationContext());

    }

    private BroadcastReceiver locationChangeBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context,
                              Intent intent) {
            String action = intent
                    .getAction();
            if (action.equals(
                    RECEIVER_ACTION)) {

                boolean isNoSpeedState = intent
                        .getBooleanExtra(
                                Constants.IntentParams.IS_NO_SPEED_STATE,
                                false);
                boolean isBaseState = intent.getBooleanExtra(Constants.IntentParams.IS_BASE_STATE, false);
                boolean isNormalState = intent.getBooleanExtra(Constants.IntentParams.IS_NORMAL_STATE, false);
                if (isBaseState) {

                    AMapLocation location = intent
                            .getParcelableExtra(
                                    Constants.IntentParams.DATA);
                    if (!isGo) {
                        mTitleTv.setText("" + location.getAoiName());
                    } else {
                        mTitleTv.setText("驾驶中...");
                    }
                    aMapLocation = location;
                    int gpsstatus = location
                            .getGpsAccuracyStatus();
                    setGPSStatus(gpsstatus);
                    mHandler.sendEmptyMessage(9);
                    LatLng curLatlng = new LatLng(
                            aMapLocation
                                    .getLatitude(),
                            aMapLocation
                                    .getLongitude());
//                    aMap.moveCamera(
//                            CameraUpdateFactory
//                                    .newLatLngZoom(
//                                            curLatlng,
//                                            18f));

                    if (!mFirstFix
                            && mSensorHelper != null) {
                        mFirstFix = true;
                        addCircle(
                                curLatlng,
                                location
                                        .getAccuracy());          //添加定位精度圆
                        addMarker(
                                curLatlng);                   //添加定位图标

                        mSensorHelper
                                .setCurrentMarker(
                                        mLocMarker);              //定位图标旋转
//                        aMap.moveCamera(
//                                CameraUpdateFactory
//                                        .newLatLngZoom(
//                                                curLatlng,
//                                                18));
                    } else {
                        if (mCircle != null) {
                            mCircle
                                    .setCenter(
                                            curLatlng);
                            mCircle
                                    .setRadius(
                                            location
                                                    .getAccuracy());
                            mLocMarker
                                    .setPosition(
                                            curLatlng);
//                            aMap.moveCamera(
//                                    CameraUpdateFactory
//                                            .changeLatLng(
//                                                    curLatlng));
                        }
                    }
                }
                if (isNoSpeedState) {


                    mSpeed = 0;
                    refreshRideInfo();
                    if (isGo) {

                        mTitleTv.setText("驾驶中...");
                    }

                }
                if (isNormalState) {

                    MyMapLocation myMapLocation = intent
                            .getParcelableExtra(
                                    Constants.IntentParams.DATA);


                    if (aMapLocation != null
                            ) {

                        if (isGo) {
                            mTitleTv.setText("驾驶中...");
                        }
                        updateLocationInfo(
                                myMapLocation
                        );
                    }
                }

            }
        }
    };

    private List<RideData> mRideDatas = new ArrayList<RideData>();

    /**
     * 获取gps状态
     *
     * @param gpsStatus 定位状态
     */
    private void setGPSStatus(int gpsStatus) {

        int gpsStrength = 0;
        int drawableIcon = 0;
        switch (gpsStatus) {
            case AMapLocation.GPS_ACCURACY_UNKNOWN:
                gpsStrength = 0;
                break;
            case AMapLocation.GPS_ACCURACY_BAD:
                gpsStrength = 1;
                break;
            case AMapLocation.GPS_ACCURACY_GOOD:
                gpsStrength = 3;
                break;
        }

        drawableIcon = mGPSDrawables[gpsStrength];
        ViewUtil.canvasTextDrawRight(mContext, drawableIcon, gpsStatusTv, 20);
    }

    /**
     * 抽屉关闭事件监听
     */
    private SlidingDrawer.OnDrawerCloseListener mCloseListener = new SlidingDrawer.OnDrawerCloseListener() {
        @Override
        public void onDrawerClosed() {

            handleContentLayout
                    .setVisibility(
                            View.VISIBLE);

            titleLayout.setTag("type");
            slidingdrawer
                    .setClickable(true);

            mMapRootLayout.setVisibility(
                    View.VISIBLE);

        }
    };

    /**
     * 抽屉打开事件监听
     */
    private SlidingDrawer.OnDrawerOpenListener mOpenListener = new SlidingDrawer.OnDrawerOpenListener() {
        @Override
        public void onDrawerOpened() {
            handleContentLayout
                    .setVisibility(
                            View.GONE);
            titleLayout
                    .setTag("title_tag");
            slidingdrawer
                    .setClickable(true);
            mMapRootLayout.setVisibility(
                    View.GONE);

        }
    };

    private LatLngBounds.Builder builder = new LatLngBounds.Builder();

    /**
     * 继续驾途
     *
     * @param isResume 是否继续开始
     */
    private void keepUpRide(boolean isResume) {

        RideData rideData = new RideData();
        int size = mData3.size();
        RideDataNodeItem item = mData3.get(size - 1);

        rideData.rideId = item.rideId;
        rideData.maxSpeed = item.speedMax;
        rideData.address = item.address;
        rideData.alt = item.altitude;
        rideData.cityName = item.cityName;
        rideData.time = item.rideTime;
        if (TextUtil.isEmpty(item.lat) || TextUtil.isEmpty(item.lng)) {
            mLat = aMapLocation.getLatitude();
            mLng = aMapLocation.getLongitude();

        } else {
            mLat = Double.parseDouble(item.lat);
            mLng = Double.parseDouble(item.lng);
        }
        rideData.latitude = mLat;
        rideData.longitude = mLng;
        rideData.time = item.rideTime;
        rideData.speed = mSpeed;
        rideData.totalMile = item.mileage;
        if (isResume) {
            mRideRunWrappter.resume(rideRunParams(rideData, null));
        }


    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {

            case R.id.title_bar_back:

                if (isGo) {

                    ShowAlertDialogUtil.showRideOperatorDialog(mContext, alertDialogEntities.get(3),
                            sweetGoOutEndListener, sweetGoToBgListener);

                } else {
                    finish();
                }

                break;
            case R.id.cameraBtn:

                break;
            case R.id.settingBtn:
                break;
            case R.id.locationBtn:
                break;
            case R.id.navBtn:
            case R.id.navBtn2:

                CommonUtils.startNewActivity(mContext, NavMainActivity.class);
                break;
            case R.id.readyBtn:
            case R.id.readyBtn2:


                switch (mData) {

                    case Constants.IntentParams.RIDE_BOOK_MODULE:

                        rideBookModuleStatus(view);
                        break;
                    case Constants.IntentParams.DRIVER_CAR_MODULE:
                        driverCarModuleStatus(view);
                        break;
                    case Constants.IntentParams.PLAY_CAR_MODULE:
                        playCarModuleStatus(view);
                        break;
                }


                break;
            case R.id.postBtn:
            case R.id.postBtn2:

                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.RIDE_ID, mRideId);
                CommonUtils.startNewActivity(mContext, args, PostingActivity.class);

                break;
            case R.id.trafficBtn:

                boolean isSelected = mTrafficImgView.isSelected();

                if (isSelected) {
                    mTrafficImgView.setSelected(false);
                    aMap.setTrafficEnabled(false);
                } else {
                    mTrafficImgView.setSelected(true);
                    aMap.setTrafficEnabled(true);
                }
                break;
            case R.id.myLocationBtn:

                if (mLocationLat != null) {
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocationLat, 18));
                }

                break;
        }

    }

    private void playCarModuleStatus(View view) {
        Logger.e("请你玩:playCarModuleStatus");
        driverCarModuleStatus(view);
    }

    private void driverCarModuleStatus(View view) {

        Logger.e("开车去:driverCarModuleStatus");
        if (isGo) {
            if (mTotalMile > MIN_KMILE) {
                ShowAlertDialogUtil.showRideOperatorDialog(mContext, alertDialogEntities.get(2), new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        sweetAlertDialog.dismiss();
                    }
                }, new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        mRideRunWrappter.stop(rodeEndParams());
                    }
                });
            } else {
                ShowAlertDialogUtil.showRideOperatorDialog(mContext,
                        alertDialogEntities.get(0), sweetCancelListener, sweetConfirmListener);
            }

        } else {
            readyBtn2.setSelected(true);
            readyBtn.setSelected(true);
            setDrivingState(view.isSelected(), false);
            if (mRideId == -1) {

                mPresenter.rideStart(builderRideStartRequestParams(aMapLocation));
            } else {

                ThreadPoolProxy.getInstance().executeTask(new Runnable() {
                    @Override
                    public void run() {

                        mRideDatas = IOUtils.readPointsFromSD(tel);

                        mHandler.sendEmptyMessage(WHAT_VALUE8);

                    }
                });


            }

        }

    }

    private void rideBookModuleStatus(View view) {

        Logger.e("路书:rideBookModuleStatus");
        if (isGo) {
            if (mTotalMile > MIN_KMILE) {
                ShowAlertDialogUtil.showRideOperatorDialog(mContext,
                        alertDialogEntities.get(1), sweetPauseClickListener,
                        sweetEndClickListener);
            } else {
                ShowAlertDialogUtil.showRideOperatorDialog(mContext,
                        alertDialogEntities.get(0), sweetCancelListener, sweetConfirmListener);
            }

        } else {
            readyBtn2.setSelected(true);
            readyBtn.setSelected(true);
            setDrivingState(view.isSelected(), false);
            if (mRideId == -1) {

                mPresenter.rideStart(builderRideStartRequestParams(aMapLocation));
            } else {

                ThreadPoolProxy.getInstance().executeTask(new Runnable() {
                    @Override
                    public void run() {
                        mRideDatas = IOUtils.readPointsFromSD(tel);

                        if (mRideDatas != null && mRideDatas.size() > 0) {
                            setRideExistData(mRideDatas.get(mRideDatas.size() - 1));
                        }
                        mHandler.sendEmptyMessage(WHAT_VALUE8);
                    }
                });


            }

        }
    }

    private Map<String, String> rideRunParams(RideData rideData, String cover) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("address", rideData.address + "");
        params.put("alt", rideData.alt + "");
        params.put("mileage", rideData.totalMile + "");
        params.put("add_time", DateFormatUtil.getText3(System.currentTimeMillis()));
        params.put("ride_id", rideData.rideId + "");
        params.put("speed", rideData.speed + "");
        params.put("lat", rideData.latitude + "");
        params.put("lng", rideData.longitude + "");
        params.put("token", IOUtils.getToken(mContext));
        params.put("city_name", cityName);
        params.put("ride_time", rideData.time + "");
        params.put("speed_max", rideData.maxSpeed + "");
        params.put("poi", mPoiName);
        if (!TextUtil.isEmpty(cover)) {
            params.put("cover", cover);
        }

        return params;
    }

    private SweetAlertDialog.OnSweetClickListener sweetCancelListener = new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog sweetAlertDialog) {

            sweetAlertDialog
                    .dismiss();
            //取消默认只做对话框关闭处理
        }
    };

    private SweetAlertDialog.OnSweetClickListener sweetConfirmListener = new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog sweetAlertDialog) {

            sweetAlertDialog
                    .dismiss();
            resetState(true);
            mSpeed = 0;
            mTimeCounter = 0;
            mAltitude = 0;
            mTotalMile = 0;
            mCucrentMile = 0;
            mTempMaxSpeed = 0;
            refreshRideInfo();
            String rideId = ""
                    + mRideId;

            mRideId = -1;
            isGo = false;
            mRideDatas.clear();//删除的时候要清空
            //清空地图画线
            for (Polyline polyline : mPolylines) {

                polyline.remove();

            }
            mPolylines.clear();
            deleteFile();
            mPresenter
                    .deleteRideData(
                            rideId,
                            IOUtils
                                    .getToken(
                                            mContext));

        }
    };

    private SweetAlertDialog.OnSweetClickListener sweetPauseClickListener = new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog sweetAlertDialog) {
            sweetAlertDialog
                    .dismiss();
            resetState(false);

            showLoadingDialog(
                    "正在处理数据,请稍候...");
            isGo = false;
            shotCoverType = 1;
            setMapBoundsZone();
            if (slidingdrawer
                    .isOpened()) {
                slidingdrawer
                        .close();
            }
            aMap.getMapScreenShot(
                    DrivingHomeActivity.this);

        }
    };

    private SweetAlertDialog.OnSweetClickListener sweetEndClickListener = new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog sweetAlertDialog) {
            sweetAlertDialog
                    .dismiss();
            resetState(true);

            //stopLocationService();
            showLoadingDialog(
                    "正在保存数据,请稍候...");
            shotCoverType = 2;
            isGo = false;
            setMapBoundsZone();
            if (slidingdrawer
                    .isOpened()) {
                slidingdrawer
                        .close();
            }

            aMap.getMapScreenShot(
                    DrivingHomeActivity.this);

        }
    };

    private SweetAlertDialog.OnSweetClickListener sweetGoOutEndListener = new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog sweetAlertDialog) {

            sweetAlertDialog
                    .dismiss();

            isGoOut = true;
            if (isGo) {

                if (mTotalMile > MIN_KMILE) {
                    ShowAlertDialogUtil
                            .showRideOperatorDialog(
                                    mContext,
                                    alertDialogEntities
                                            .get(
                                                    1),
                                    sweetPauseClickListener,
                                    sweetEndClickListener);
                } else {
                    ShowAlertDialogUtil
                            .showRideOperatorDialog(
                                    mContext,
                                    alertDialogEntities
                                            .get(
                                                    0),
                                    sweetCancelListener,
                                    sweetConfirmListener);
                }
            }

        }
    };

    private SweetAlertDialog.OnSweetClickListener sweetGoToBgListener = new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog sweetAlertDialog) {
            sweetAlertDialog
                    .dismiss();

            finish();

        }
    };

    /**
     * 暂停或者结束时，需要重置状态
     */
    private void resetState(boolean isEnd) {
        readyBtn2.setSelected(false);
        readyBtn.setSelected(false);

        setDrivingState(false, isEnd);
    }

    /**
     * 正常结束
     *
     * @param cover
     * @return
     */
    private Map<String, String> rodeEndParams(String cover) {

        Map<String, String> params = new HashMap<String, String>();

        params.put("actual_time", mTimeCounter + "");
        params.put("alt", mAltitude + "");
        params.put("cover", cover);
        params.put("end_adds", mAddress);
        params.put("end_lat", mLat + "");
        params.put("end_lng", mLng + "");
        params.put("four_hundred_time", "");
        params.put("hundred_time", "");
        params.put("mileage", mTotalMile + "");
        params.put("city_name", cityName);
        params.put("type", "1");
        params.put("ride_id", "" + mRideId);
        params.put("title", "");
        params.put("poi", mPoiName);
        params.put("add_time", DateFormatUtil.getText3(System.currentTimeMillis()));
        params.put("adcode", aMapLocation.getAdCode());
        params.put("token", IOUtils.getToken(mContext));
        return params;
    }

    /**
     * 强制结束
     *
     * @return
     */
    private Map<String, String> rodeEndParams() {

        Map<String, String> params = new HashMap<String, String>();

        params.put("type", "2");

        params.put("ride_id", "" + mRideId);
        params.put("token", IOUtils.getToken(mContext));

        return params;
    }

    /**
     * 清空本地保存的数据
     */
    private void clearCacheData() {
        mRideId = -1;
    }

    private ArrayList<RideAutoData> autoDatas = new ArrayList<RideAutoData>();

    private void initTrackDatas() {

        DataItem dataItem = new DataItem();
        dataItem.title = "总里程";
        dataItem.content = "0";
        mTrackDatas.add(dataItem);

        dataItem = new DataItem();
        dataItem.title = "当前速度";
        dataItem.content = "0";
        mTrackDatas.add(dataItem);

        dataItem = new DataItem();
        dataItem.title = "行驶时间";
        dataItem.content = "00:00:00";
        mTrackDatas.add(dataItem);
        mTrackAdapter1 = new WrapAdapter<DataItem>(mContext, R.layout.track_item, mTrackDatas) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {

                helper.setText(R.id.title, item.title);
                TextView contentTv = helper.getView(R.id.content);
                contentTv.setText(item.content);
                //设置自定义字体
                contentTv.setTypeface(typeFace);
                contentTv.setTextSize(30);

            }
        };
        grid.setAdapter(mTrackAdapter1);

        dataItem = new DataItem();
        dataItem.title = "极速(km/h)";
        dataItem.content = "0";
        mTrack2Datas.add(dataItem);

        dataItem = new DataItem();
        dataItem.title = "里程(km)";
        dataItem.content = "0";
        mTrack2Datas.add(dataItem);

        dataItem = new DataItem();
        dataItem.title = "海拔(m)";
        dataItem.content = "0";
        mTrack2Datas.add(dataItem);

        mTrackAdapter2 = new WrapAdapter<DataItem>(mContext, R.layout.track_item2, mTrack2Datas) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {

                helper.setText(R.id.title, item.title);
                TextView contentTv = helper.getView(R.id.content);
                contentTv.setText(item.content);
                //设置自定义字体
                contentTv.setTypeface(typeFace);
                contentTv.setTextSize(45);

            }
        };

        grid2.setAdapter(mTrackAdapter2);

        //禁用GridView的点击事件
        grid.setClickable(false);
        grid.setPressed(false);
        grid.setEnabled(false);

        grid2.setClickable(false);
        grid2.setPressed(false);
        grid2.setEnabled(false);
    }


    private ArrayList<Polyline> mPolylines = new ArrayList<Polyline>();


    public static class UpdateUIHandler extends Handler {

        private final WeakReference<DrivingHomeActivity> mWR;

        public UpdateUIHandler(DrivingHomeActivity activity) {

            mWR = new WeakReference<DrivingHomeActivity>(activity);
        }


        @Override
        public void handleMessage(Message msg) {

            DrivingHomeActivity activity = mWR.get();
            if (activity != null) {
                activity.handleMsg(msg);
            }

        }
    }

    private void handleMsg(Message msg) {


        int what = msg.what;

        switch (what) {
            case WHAT_VALUE1:
                drawRideLines(msg);
                break;

            case WHAT_VALUE3:
                File file = (File) msg.obj;
                uploadImage(file);
                break;
            case WHAT_VALUE4:
                refreshRideInfo();
                break;

            case WHAT_VALUE5:

                showDialogByState();
                break;
            case WHAT_VALUE6:

                readyBtnEnabled(true);

                LatLng curLatLng = (LatLng) msg.obj;
                aMap.moveCamera(
                        CameraUpdateFactory
                                .newLatLngZoom(
                                        curLatLng,
                                        18f));

            case REFRESH_DATA_WHAT:

                // refreshData(msg);
                break;
            case WHAT_VALUE8:
                int size = mRideDatas.size();
                if (size > 0) {

                    RideData item = mRideDatas.get(size - 1);

                    //取出总里程
                    mTotalMile = item.totalMile;
                    mTimeCounter = (int) item.time;
                    mTempMaxSpeed = item.maxSpeed;
                    refreshRideInfo();
                    mPresenter.rideRestart(rideRunParams(item, null));

                }

                break;
        }

    }

    private void refreshData(Message msg) {

        mTimeCounter = msg.arg1;
        updateDisplayTime();
    }

    /**
     * @param isEnabled 开始/结束按钮是否可点击
     */
    private void readyBtnEnabled(boolean isEnabled) {

        readyBtn2.setEnabled(isEnabled);
        readyBtn.setEnabled(isEnabled);
    }


    private void showDialogByState() {

        if (mTotalMile > MIN_KMILE) {
            ShowAlertDialogUtil
                    .showRideOperatorDialog(
                            mContext,
                            alertDialogEntities
                                    .get(1),
                            sweetPauseClickListener,
                            sweetEndClickListener);
        } else {
            ShowAlertDialogUtil
                    .showRideOperatorDialog(
                            mContext,
                            alertDialogEntities
                                    .get(0),
                            sweetCancelListener,
                            sweetConfirmListener);
        }
    }

    /**
     * 绘制行驶的路线
     *
     * @param msg
     */
    private void drawRideLines(Message msg) {

        List<RideData> datas = (List<RideData>) msg.obj;
        if (datas == null) return;
        List<LatLng> latLngs = new ArrayList<LatLng>();

        for (Polyline polyline : mPolylines) {

            polyline.remove();

        }
        mPolylines.clear();
        for (RideData d : datas) {

            latLngs.add(new LatLng(
                    d.latitude, d.longitude));
        }

        Polyline polyline = aMap
                .addPolyline(
                        new PolylineOptions()
                                .addAll(latLngs)
                                .geodesic(true)
                                .color(mRes.getColor(
                                        R.color.style_bg1)));

        mPolylines.add(polyline);
    }

    private UpdateUIHandler mHandler = new UpdateUIHandler(this);

    /**
     * 显示时间格式化
     *
     * @param cnt 当前时间 单位秒
     * @return
     */

    private String getStringTime(int cnt) {
        int hour = cnt / 3600;
        int min = cnt % 3600 / 60;
        int second = cnt % 60;
        return String.format(Locale.CHINA, "%02d:%02d:%02d", hour, min, second);
    }

    /**
     * 更新显示的时间（ride计时器）
     */
    private void updateDisplayTime() {
        String timeStr = getStringTime(mTimeCounter);
        counterTv.setText(timeStr);

        mTrackDatas.get(2).content = timeStr;
        mTrackAdapter1.notifyDataSetChanged();

    }

    private void uploadImage(File file) {

        mUploadPresenter.uploadAvatarFile(RequestBody.create(MediaType.parse("image/png"), file),
                RequestBody.create(MediaType.parse("text/plain"), "avatar"),
                RequestBody.create(MediaType.parse("text/plain"), ""),
                RequestBody.create(MediaType.parse("text/plain"), IOUtils.getToken(getContext())));
    }

    @Override
    public void finish() {
        super.finish();
    }

    private double mSpeed = 0;    //速度

    private double mAltitude;            //海拔;

    private double mTempMaxSpeed;

    private double mTotalMile = 0;    //总里程
    private int mTimeCounter = 0;    //时间
    private double mCucrentMile;

    private String mAddress = "";

    private double mLat, mLng;

    private String cityName = "";

    private int shotCoverType = 0;    //截图封面类型 1、为暂停，2为结束
    private boolean mFirstFix = false;

    private LatLng mLocationLat;

    private AMapLocation aMapLocation;

    private Timer mTimer = null;
    private TimerTask mTimerTask = null;

    /**
     * 设置地图可见区域
     */
    private void setMapBoundsZone() {

        LatLngBounds latlngBounds = getLatLngBounds();

        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, 100));

    }

    //根据中心点和自定义内容获取缩放bounds 
    private LatLngBounds getLatLngBounds() {

        LatLngBounds.Builder b = LatLngBounds.builder();
        for (RideData data : mRideDatas) {

            LatLng p = new LatLng(data.latitude, data.longitude);
            b.include(p);
        }

        return b.build();
    }

    private int mThreadId = -1; //帖子Id

    private Map<String, String> builderRideStartRequestParams(AMapLocation amapLocation) {

        Map<String, String> params = new HashMap<String, String>();
        if (mThreadId != -1) {
            params.put("thread_id", mThreadId + "");
        }

        params.put("start_adds", "" + amapLocation.getAddress());
        params.put("poi", amapLocation.getPoiName());
        params.put("alt", "" + amapLocation.getAltitude());
        params.put("start_lat", "" + amapLocation.getLatitude());
        params.put("start_lng", "" + amapLocation.getLongitude());
        params.put("add_time", DateFormatUtil.getText3(System.currentTimeMillis()));
        params.put("city_name", cityName);
        params.put("adcode", amapLocation.getAdCode());
        params.put("token", "" + IOUtils.getToken(mContext));
        return params;

    }

    private void

    updateLocationInfo(final MyMapLocation myMapLocation) {

        AMapLocation amapLocation = myMapLocation.mapLocation;
        this.aMapLocation = amapLocation;
        float accuracy = amapLocation.getAccuracy();
        float bear = amapLocation.getBearing();
        mLat = amapLocation.getLatitude();
        mLng = amapLocation.getLongitude();
        cityName = amapLocation.getCity();
        double altitude = amapLocation.getAltitude();
        mAddress = amapLocation.getAddress();
        mPoiName = amapLocation.getPoiName();
        LatLng curLatlng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
        mLocationLat = curLatlng;

        if (mCircle != null) {
            mCircle.setCenter(curLatlng);
            mCircle.setRadius(amapLocation.getAccuracy());
            mLocMarker.setPosition(curLatlng);
        }

        mCucrentMile = myMapLocation.mCucrentMile;
        mPrevLatLng = curLatlng;
        mTotalMile = myMapLocation.mTotalMile;
        mSpeed = myMapLocation.speed;
        mTempMaxSpeed = myMapLocation.mTempMaxSpeed;
        mTimeCounter = myMapLocation.mTimeCounter;
        mAltitude = altitude;


        ThreadPoolProxy.getInstance().executeTask(new Runnable() {
            @Override
            public void run() {

                ArrayList<RideData> datas = IOUtils.readPointsFromSD(tel);
                if (datas != null && datas.size() > 0) {

//                    if (isBg) {
//                        mRideDatas.clear();
//                        mRideDatas.addAll(datas);
//                    } else {
//                        mRideDatas.add(data);
//                    }
                    mRideDatas.clear();
                    mRideDatas.addAll(datas);
                    Message msg = mHandler.obtainMessage();
                    msg.obj = mRideDatas;
                    msg.what = WHAT_VALUE1;
                    mHandler.sendMessage(msg);

                }
            }
        });


        refreshRideInfo();
    }

    /**
     * 更新界面显示信息
     */
    private void refreshRideInfo() {

        String strTimer = getStringTime(mTimeCounter);
        mTrackDatas.get(0).content = NumFormatUtil.getFmtTwoNum(mTotalMile) + "";
        mTrackDatas.get(1).content = NumFormatUtil.getFmtTwoNum(mSpeed) + "";
        mTrackDatas.get(2).content = strTimer;
        mTrack2Datas.get(0).content = NumFormatUtil.getFmtTwoNum(mTempMaxSpeed) + "";
        mTrack2Datas.get(1).content = NumFormatUtil.getFmtTwoNum(mTotalMile) + "";
        mTrack2Datas.get(2).content = mAltitude + "";//获取海拔信息
        counterTv.setText(strTimer);
        instrumentView.setProgress(mSpeed);
        instrumentView.setGo(isGo);
        mTrackAdapter1.notifyDataSetChanged();
        mTrackAdapter2.notifyDataSetChanged();
    }


    int time;

    private void startTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
        }

        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {

                    ArrayList<RideData> datas = IOUtils.readPointsFromSD(tel);
                    sendDataByMsg(time);
                    time++;
                }
            };
        }

        mTimer.schedule(mTimerTask, 0, REFRESH_TIME);

    }

    /**
     * 通过Handler传递数据，并更新UI
     *
     * @param time
     */
    private void sendDataByMsg(int time) {
        Message msg = mHandler.obtainMessage();
        msg.what = REFRESH_DATA_WHAT;
        msg.arg1 = time;
        mHandler.sendMessage(msg);
    }

    private static final int REFRESH_TIME = 1000;

    private void stopTimer() {

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }


    }


    private LatLng mPrevLatLng; //上一个结点

    //private RideData startRideData;

    @Override
    public void ridingStart(ResponseMessage<RideData> responseMessage) {

        startLocationService(true, false);
        mRideId = responseMessage.data.rideId;
        sendSaveDataBroadcast();
        Logger.d("ridingStart" + mRideId);
    }

    @Override
    public void ridingPause(ResponseMessage<RideData> responseMessage) {

        dismissLoadingDialog2();
        if (isGoOut) {
            isGoOut = false;
        }

        Logger.d("ridingPause" + mRideId);

        Map<String, Serializable> args = new HashMap<String, Serializable>();

        args.put(Constants.IntentParams.ID, mRideId);
        args.put(Constants.IntentParams.FROM_TYPE, Constants.IntentParams.PART_RIDE_REP);
        CommonUtils.startNewActivity(mContext, args, RideReportActivity.class);

    }

    @Override
    public void ridingRestart(Object obj) {

        Logger.d("ridingRestart" + mRideId);
    }

    @Override
    public void rodeEnd(Object obj) {

        switch (mData) {

            case Constants.IntentParams.RIDE_BOOK_MODULE:

                ridebookEndCall();
                break;
            case Constants.IntentParams.DRIVER_CAR_MODULE:
                driverCarEndCall();
                break;
            case Constants.IntentParams.PLAY_CAR_MODULE:
                playCarEndCall();
                break;
        }

    }

    private void playCarEndCall() {
        Logger.e("请你玩:playCarEndCall");
        dismissLoadingDialog2();
        if (isGoOut) {
            isGoOut = false;
        }
        if (isForceEnd) {

            isForceEnd = false;

        }
        ShowAlertDialogUtil.showRideOperatorDialog(mContext, alertDialogEntities.get(5), new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                finish();
            }
        }, new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.RIDE_ID, mRideId);
                CommonUtils.startNewActivity(mContext, args, AddTravelSpotActivity.class);
                finish();
            }
        });

    }

    private void driverCarEndCall() {
        Logger.e("开车去:driverCarEndCall");
        dismissLoadingDialog2();
        if (isGoOut) {
            isGoOut = false;
        }
        if (isForceEnd) {

            isForceEnd = false;

        }

        ShowAlertDialogUtil.showRideOperatorDialog(mContext, alertDialogEntities.get(5), new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                finish();
            }
        }, new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.RIDE_ID, mRideId);
                CommonUtils.startNewActivity(mContext, args, AddTravelSpotActivity.class);
                finish();
            }
        });


    }


    private void deleteFile() {
        File file = ImagePicker
                .createFile(new File(Constants.RIDE_LINE_CACHE),
                        tel + ".txt");
        File file1 = ImagePicker
                .createFile(new File(Constants.RIDE_LINE_CACHE),
                        LocationService.FILE_RIDE_PART + tel);
        boolean flag = FileUtil.deleteFile(file.getAbsolutePath());
        //删除驾途参数文件
        boolean flag1 = FileUtil.deleteFile(file1.getAbsolutePath());
        Logger.e("删除驾途文件:flag==" + flag + ",flag1==" + flag1);
    }

    private void ridebookEndCall() {

        Logger.e("路书:ridebookEndCall");
        deleteFile();
        dismissLoadingDialog2();
        if (isGoOut) {
            isGoOut = false;
        }
        Logger.d("rodeEnd" + mRideId);
        if (isForceEnd) {

            isForceEnd = false;
            return;
        }


        Map<String, Serializable> args = new HashMap<String, Serializable>();

        args.put(Constants.IntentParams.ID, mRideId);
        args.put(Constants.IntentParams.FROM_TYPE, Constants.IntentParams.ALL_RIDE_REP);
        CommonUtils.startNewActivity(mContext, args, RideReportActivity.class);

        mTotalMile = 0;
        mTimeCounter = 0;
        mAltitude = 0;
        clearCacheData();
        finish();
    }

    @Override
    public void autoCmtRideData(ResponseMessage<Object> responseMessage) {

        Logger.d("自动上传行驶数据:" + responseMessage.data);
    }

    @Override
    public void getRideState(RideAutoData data) {

        Logger.d("骑行状态:" + data);
    }

    @Override
    public void deleteRideData(Object object) {

        if (isGoOut) {
            finish();
        }
        Logger.d("删除不足一公里骑行路线" + mRideId);
        //删除成功，将开始相关参数重置
        mRideId = -1;
        isGo = false;
    }

    @Deprecated
    @Override
    public void RideReportData(RideReportDetailModel rideReportDetailModel) {

    }

    @Deprecated
    @Override
    public void showloadRideDatas(RideLinePreviewModel rideLinePreviewModel) {

    }


    @Override
    public void setLoadingIndicator(boolean active) {
        if (!active) {
            dismissLoadingDialog2();
        }

    }

    @Override
    public void showLoadingTasksError() {

        dismissLoadingDialog2();
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void onMapScreenShot(Bitmap bitmap) {
        ScreenShotHelper.saveScreenShot(bitmap, mMapRootLayout, mapView, mHandler, mTrafficImgView);

    }

    @Override
    public void onMapScreenShot(Bitmap bitmap, int i) {

    }

    private WeatherSearch.OnWeatherSearchListener mWeatherListener = new WeatherSearch.OnWeatherSearchListener() {
        @Override
        public void onWeatherLiveSearched(LocalWeatherLiveResult weatherLiveResult, int rCode) {

            if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                if (weatherLiveResult != null && weatherLiveResult.getLiveResult() != null) {
                    weatherLive = weatherLiveResult.getLiveResult();

                    String temperature = weatherLive.getTemperature() + "\u2103";
                    weatherTv.setText(temperature);

                    String weather = weatherLive.getWeather();

                    int weatherDrawableId = R.drawable.icon_nothing;
                    if ("晴".equals(weather)) {

                        weatherDrawableId = R.drawable.icon_sun;
                    }

                    if (weather.contains("雨")) {
                        weatherDrawableId = R.drawable.icon_rain;

                    }

                    if (weather.contains("雪")) {
                        weatherDrawableId = R.drawable.icon_snow;
                    }

                    if (weather.contains("霾")) {
                        weatherDrawableId = R.drawable.icon_wumai;
                    }
                    if (weather.contains("雾")) {

                        weatherDrawableId = R.drawable.icon_wu;
                    }

                    if (weather.contains("风")) {
                        weatherDrawableId = R.drawable.icon_wind;
                    }

                    if (weather.equals("多云")) {
                        weatherDrawableId = R.drawable.weatcher_icon;
                    }
                    mWeatherImg.setImageResource(weatherDrawableId);
                }
            }
        }

        @Override
        public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult,
                                              int rCode) {

        }
    };

    private void searchLiveWeather() {
        mQuery = new WeatherSearchQuery(mGlobalContext.locCityName,
                WeatherSearchQuery.WEATHER_TYPE_LIVE);//检索参数为城市和天气类型，实时天气为1、天气预报为2
        mWeatherSearch = new WeatherSearch(this);
        mWeatherSearch.setOnWeatherSearchListener(mWeatherListener);
        mWeatherSearch.setQuery(mQuery);
        mWeatherSearch.searchWeatherAsyn();
    }

    private WeatherSearchQuery mQuery;
    private WeatherSearch mWeatherSearch;
    private LocalWeatherLive weatherLive;

    /**
     * Function to show settings alert dialog
     */
    private void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setCancelable(false);

        // Setting Dialog Title
        alertDialog.setTitle("提示");

        // Setting Dialog Message
        alertDialog.setMessage("开启GPS可以获取更准确的行驶数据，是否开启GPS?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("开启", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, GPS_CODE);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    private static int GPS_CODE = 1;
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private Marker mLocMarker;
    private Circle mCircle;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (GPS_CODE == requestCode) {
            if (!GpsUtil.isOPen(mContext)) {

                CommonUtils.showToast(mContext, "使用Ride功能需要GPS服务哦!");
                finish();
            } else {
                //打开定位服务
                startLocationService(false, false);
                ThreadPoolProxy.getInstance().executeTask(mGetLocationInfoRunnable);
            }
        }
    }

    private void addCircle(LatLng latlng, double radius) {
        CircleOptions options = new CircleOptions();
        options.strokeWidth(1f);
        options.fillColor(FILL_COLOR);
        options.strokeColor(STROKE_COLOR);
        options.center(latlng);
        options.radius(radius);
        mCircle = aMap.addCircle(options);
    }

    private void addMarker(LatLng latlng) {
        if (mLocMarker != null) {
            return;
        }
        MarkerOptions options = new MarkerOptions();
        options.icon(BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(this.getResources(), R.mipmap.navi_map_gps_locked)));
        options.anchor(0.5f, 0.5f);
        options.position(latlng);
        mLocMarker = aMap.addMarker(options);
        mLocMarker.setTitle(LOCATION_MARKER_FLAG);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (KeyEvent.KEYCODE_BACK == keyCode) {
            //监听返回键， 暂不处理

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
