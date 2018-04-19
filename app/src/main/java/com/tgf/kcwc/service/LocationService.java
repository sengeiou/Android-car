package com.tgf.kcwc.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.tgf.kcwc.callback.IWifiAutoCloseDelegate;
import com.tgf.kcwc.callback.WifiAutoCloseDelegate;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.driving.track.DrivingHomeActivity;
import com.tgf.kcwc.entity.RideParamEntity;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.MyMapLocation;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.model.RideAutoData;
import com.tgf.kcwc.mvp.model.RideData;
import com.tgf.kcwc.mvp.presenter.RidingRunPresenter;
import com.tgf.kcwc.mvp.view.WrapperRidingRunView;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.GsonUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.NetUtil;
import com.tgf.kcwc.util.NumFormatUtil;
import com.tgf.kcwc.util.PowerManagerUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 包名： com.amap.locationservicedemo
 * <p>
 * 创建时间：2016/10/27
 * 项目名称：LocationServiceDemo
 *
 * @author guibao.ggb
 * @email guibao.ggb@alibaba-inc.com
 * <p>
 * 类说明：后台服务定位
 * <p>
 * <p>
 * modeified by liangchao , on 2017/01/17
 * update:
 * 1. 只有在由息屏造成的网络断开造成的定位失败时才点亮屏幕
 * 2. 利用notification机制增加进程优先级
 * </p>
 */
public class LocationService extends NotiService {

    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;

    private RidingRunPresenter mPresenter;
    /**
     * 处理息屏关掉wifi的delegate类
     */
    private IWifiAutoCloseDelegate mWifiAutoCloseDelegate = new WifiAutoCloseDelegate();

    /**
     * 记录是否需要对息屏关掉wifi的情况进行处理
     */
    private boolean mIsWifiCloseable = false;

    private double mTempMaxSpeed = 0;                          //极速

    private double mTotalMile = 0;                          //总里程
    private int mTimeCounter = 0;                          //时间
    private double mCurrentMile = 0;

    private ArrayList<RideData> mRideDatas = new ArrayList<RideData>();

    public LocationService() {

        Logger.e("Init location service!");
    }

    private boolean isEnd = false;
    private String userId = "";//用户id,默认为用户的手机号

    private void resetData() {

        mTempMaxSpeed = 0; //极速

        mTotalMile = 0; //总里程
        mTimeCounter = 0; //时间
        mCurrentMile = 0;
        mRideDatas.clear();
        mRideId = -1;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        applyNotiKeepMech(); //开启利用notification提高进程优先级的机制

        if (mWifiAutoCloseDelegate.isUseful(getApplicationContext())) {
            mIsWifiCloseable = true;
            mWifiAutoCloseDelegate.initOnServiceStarted(getApplicationContext());
        }

        if (intent != null) {
            isGo = intent.getBooleanExtra(Constants.IntentParams.IS_GO, false);
            userId = intent.getStringExtra(Constants.IntentParams.USER_ID);
        }
        if (intent != null) {

            isEnd = intent.getBooleanExtra(Constants.IntentParams.IS_END, false);
        }
        if (isEnd) {
            Logger.e("Ride结束");
            resetData();
        }

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.e("LocationService:onCreate()");
        mPresenter = new RidingRunPresenter();
        mPresenter.attachView(ridingRunView);
        //服务创建的时候开始定位
        startLocation();
        registerReceiver(mReceiver, new IntentFilter(SAVE_DATA_ACTION));
    }

    private WrapperRidingRunView ridingRunView = new WrapperRidingRunView() {

        @Override
        public void autoCmtRideData(ResponseMessage<Object> responseMessage) {

            Logger.d("自动上传行驶数据:" + responseMessage.data);

        }

        @Override
        public void setLoadingIndicator(boolean active) {

        }

        @Override
        public void showLoadingTasksError() {

        }

        @Override
        public Context getContext() {
            return LocationService.this;
        }
    };

    @Override
    public void onDestroy() {
        unApplyNotiKeepMech();
        stopLocation();

        super.onDestroy();

        unregisterReceiver(mReceiver);
        Logger.e("LocationService:onDestroy()");
    }

    /**
     * 添加Ride的点
     *
     * @param location
     */
    private RideData addPoint(MyMapLocation location) {

        final RideData data = new RideData();
        data.latitude = location.mapLocation.getLatitude();
        data.longitude = location.mapLocation.getLongitude();
        data.speed = location.speed;
        data.totalMile = location.mTotalMile;
        data.maxSpeed = location.mTempMaxSpeed;
        data.alt = location.mapLocation.getAltitude();
        data.dipAngle = location.mapLocation.getBearing();
        data.address = location.mapLocation.getAddress();
        data.cityName = location.mapLocation.getCity();
        data.time = location.mTimeCounter;
        data.rideId = location.mRideId;
        mRideDatas.add(data);
        return data;
    }

    /**
     * 添加Ride的点
     *
     * @param location
     */
    private RideData addPoint(AMapLocation location) {

        final RideData data = new RideData();
        data.latitude = location.getLatitude();
        data.longitude = location.getLongitude();
        data.speed = location.getSpeed();
        mRideDatas.add(data);
        return data;
    }

    /**
     * 启动定位
     */
    void startLocation() {
        stopLocation();

        if (null == mLocationClient) {
            mLocationClient = new AMapLocationClient(this.getApplicationContext());
        }

        mLocationOption = new AMapLocationClientOption();
        // 使用连续
        mLocationOption.setOnceLocation(false);
        mLocationOption.setLocationCacheEnable(false);
        mLocationOption.setSensorEnable(true);//启动传感器获取海拔
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//高精度定位模式
        // 每秒定位一次
        mLocationOption.setInterval(1 * 1000);
        // 地址信息
        mLocationOption.setNeedAddress(true);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.setLocationListener(locationListener);
        mLocationClient.startLocation();
    }

    /**
     * 停止定位
     */
    void stopLocation() {
        if (null != mLocationClient) {
            mLocationClient.stopLocation();
        }
    }

    private boolean isGo;

    private ArrayList<RideAutoData> autoDatas = new ArrayList<RideAutoData>();

    /**
     * 1分钟提交一次数据
     *
     * @return
     */
    private Map<String, String> autoCmtRidePostParams(int rideId) {

        Map<String, String> params = new HashMap<String, String>();

        String json = GsonUtil.objToJson(autoDatas);

        params.put("driveData", json);

        params.put("ride_id", "" + rideId);
        params.put("token", IOUtils.getToken(this));
        Logger.d("autoCmt-->rideId:" + rideId);
        return params;
    }

    private LatLng mPrevLatLng;   //上一个结点


    /**
     * 采集行驶数据的点 每秒采集一个点，每5秒上传一次
     */
    private void collectionRidePoints(MyMapLocation myMapLocation) {

        RideAutoData data = new RideAutoData();
        data.address = myMapLocation.mapLocation.getAddress();
        data.alt = myMapLocation.mapLocation.getAltitude();
        data.ba = myMapLocation.mapLocation.getBearing();
        data.city_name = myMapLocation.mapLocation.getCity();
        data.lat = myMapLocation.mapLocation.getLatitude();
        data.lng = myMapLocation.mapLocation.getLongitude();
        data.speed = myMapLocation.speed;
        data.mileage = myMapLocation.mTotalMile;
        data.speed_max = myMapLocation.mTempMaxSpeed;
        data.add_time = DateFormatUtil.getText3(System.currentTimeMillis());
        data.ride_time = mTimeCounter;
        autoDatas.add(data);

        if ((mTimeCounter % 5 == 0) && (mRideId > 0)) {

            mPresenter.autoCmtRideData(autoCmtRidePostParams(mRideId));

            autoDatas.clear();
        }

    }

    private int mRideId;
    private static final double MIN_SPEED = 5.0; //数据采集最小速度为5
    AMapLocationListener locationListener =

            new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation aMapLocation) {

                    if (aMapLocation == null) {

                        Logger.d(
                                "定位失败：Location is null!!!!!!!");
                        sendLocationBroadcast();
                        return;
                    }
                    int locationType = aMapLocation
                            .getLocationType();
                    Logger.d("当前定位位置:"
                            + aMapLocation.getAddress() + ":定位类型:" + locationType + ",userId:" + userId);

                    int errorCode = aMapLocation
                            .getErrorCode();
                    if (aMapLocation != null
                            && (AMapLocation.LOCATION_SUCCESS == errorCode)) {

                        //行为日志测试
                        //ActionLog.recordActionInfoLog(new RideActionInfo(aMapLocation.getAddress(), aMapLocation.getSpeed() + ""));

                        LatLng curLatlng = new LatLng(
                                aMapLocation.getLatitude(),
                                aMapLocation.getLongitude());
                        int gpsstatus = aMapLocation
                                .getGpsAccuracyStatus();
                        double speed = aMapLocation.getSpeed()
                                * 3.6f;


                        speed = NumFormatUtil
                                .getFmtTwoNum(speed);

                        sendLocationBroadcast(aMapLocation);

                        if (isGo) {
                            //判断定位类型为GPS
                            if (AMapLocation.LOCATION_TYPE_GPS == locationType) {


                                if (speed < MIN_SPEED) {
                                    sendLocationBroadcast(
                                    );
                                } else {


                                    //调用行驶参数计算方法，保证数据一致性
                                    calcRideParams(speed, curLatlng);
                                    MyMapLocation location = new MyMapLocation(
                                            aMapLocation, mTotalMile,
                                            mTimeCounter,
                                            mCurrentMile,
                                            mTempMaxSpeed, speed,
                                            mRideId);

                                    collectionRidePoints(
                                            location);
                                    RideData data = addPoint(
                                            location);

                                    //写入数据到本地
                                    IOUtils.writePointsToSD(addPoint(location), userId);
                                    //发送结果的通知
                                    sendLocationBroadcast(
                                            location);
                                }

                            }
                        } else {
                            sendLocationBroadcast(
                            );
                        }

                    }

                    if (!mIsWifiCloseable) {
                        return;
                    }

                    if (AMapLocation.LOCATION_SUCCESS == errorCode) {
                        mWifiAutoCloseDelegate
                                .onLocateSuccess(
                                        getApplicationContext(),
                                        PowerManagerUtil.getInstance()
                                                .isScreenOn(
                                                        getApplicationContext()),
                                        NetUtil.getInstance()
                                                .isMobileAva(
                                                        getApplicationContext()));
                    } else {
                        mWifiAutoCloseDelegate.onLocateFail(
                                getApplicationContext(),
                                errorCode,
                                PowerManagerUtil.getInstance()
                                        .isScreenOn(
                                                getApplicationContext()),
                                NetUtil.getInstance().isWifiCon(
                                        getApplicationContext()));
                    }

                }

            };


    public static final String FILE_RIDE_PART = "ride_param";

    /**
     * @param currentSpeed 当前速度
     * @param curLatLng    当前经纬度
     */
    private void calcRideParams(double currentSpeed, LatLng curLatLng) {

        RideParamEntity rideParamEntity = null;
        //反序列化对象从文件
        String fileName = FILE_RIDE_PART + userId;
        rideParamEntity = IOUtils.getRideObject(fileName);


        if (rideParamEntity != null) {
            mTempMaxSpeed = rideParamEntity.maxSpeed;
            mTotalMile = rideParamEntity.totalMile;
            mTimeCounter = rideParamEntity.rideTimer;
            mPrevLatLng = new LatLng(rideParamEntity.lat, rideParamEntity.lng);
        } else {
            rideParamEntity = new RideParamEntity();
        }
        //极速
        mTempMaxSpeed = Math.max(
                mTempMaxSpeed, currentSpeed);

        //当前里程
        mCurrentMile = AMapUtils
                .calculateLineDistance(
                        mPrevLatLng,
                        curLatLng) / 1000;
        mPrevLatLng = curLatLng;

        //总里程
        mTotalMile += mCurrentMile;

        //运行时间
        mTimeCounter++;

        rideParamEntity.maxSpeed = mTempMaxSpeed;
        rideParamEntity.lat = mPrevLatLng.latitude;
        rideParamEntity.lng = mPrevLatLng.longitude;
        rideParamEntity.totalMile = mTotalMile;
        rideParamEntity.rideTimer = mTimeCounter;
        //序列化对象到文件
        IOUtils.writeRideObject(rideParamEntity, fileName);
    }

    /**
     * 发送正常行驶参数
     *
     * @param myMapLocation
     */
    private void sendLocationBroadcast(MyMapLocation myMapLocation) {

        Intent mIntent = new Intent();
        mIntent.setAction(DrivingHomeActivity.RECEIVER_ACTION);
        mIntent.putExtra(Constants.IntentParams.DATA, myMapLocation);
        mIntent.putExtra(Constants.IntentParams.IS_NORMAL_STATE, true);
        //发送广播
        sendBroadcast(mIntent);

    }

    /**
     * 发送定位失败或者速度为0参数
     */
    private void sendLocationBroadcast() {

        Intent mIntent = new Intent();
        mIntent.setAction(DrivingHomeActivity.RECEIVER_ACTION);
        mIntent.putExtra(Constants.IntentParams.IS_NO_SPEED_STATE, true);
        sendBroadcast(mIntent);
    }

    /**
     * 发送定位基本参数
     *
     * @param aMapLocation
     */
    private void sendLocationBroadcast(AMapLocation aMapLocation) {

        Intent mIntent = new Intent();
        mIntent.setAction(DrivingHomeActivity.RECEIVER_ACTION);
        mIntent.putExtra(Constants.IntentParams.DATA, aMapLocation);
        mIntent.putExtra(Constants.IntentParams.IS_BASE_STATE, true);
        sendBroadcast(mIntent);
    }

    public static final String SAVE_DATA_ACTION = "com.tgf.kcwc.savedataaction";

    public static final String CLEAR_DATA_ACTION = "com.tgf.kcwc.cleardataaction";
    public static final String RIDE_LAST_POINT_DATA_ACTION = "com.tgf.kcwc.ridelastpointdata.action";
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context,
                              Intent intent) {

            String action = intent.getAction();
            if (SAVE_DATA_ACTION
                    .equals(action)) {

                int rideId = intent.getIntExtra(
                        Constants.IntentParams.RIDE_ID, -1);
                mRideId = rideId;
                Logger.e("rideId:" + rideId);
            } else if (CLEAR_DATA_ACTION.equals(action)) {

                resetData();
            } else if (RIDE_LAST_POINT_DATA_ACTION.equals(action)) {

                RideData rideData = intent.getParcelableExtra(Constants.IntentParams.DATA5);
                mTotalMile = rideData.totalMile;
                mTimeCounter = (int) rideData.time;
                mTempMaxSpeed = rideData.maxSpeed;
                mRideId = rideData.rideId;
                mPrevLatLng = new LatLng(rideData.latitude, rideData.longitude);
            }
        }
    };
}
