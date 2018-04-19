package com.tgf.kcwc.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.amap.api.location.AMapLocationClient;
import com.easemob.redpacketsdk.RPInitRedPacketCallback;
import com.easemob.redpacketsdk.RPValueCallback;
import com.easemob.redpacketsdk.RedPacket;
import com.easemob.redpacketsdk.bean.RedPacketInfo;
import com.easemob.redpacketsdk.bean.TokenData;
import com.easemob.redpacketsdk.constant.RPConstant;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.tgf.kcwc.R;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.globalchat.utils.GlobalHelper;
import com.tgf.kcwc.imageloader.PicassoImageLoader;
import com.tgf.kcwc.interceptor.RequestInterceptor;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.AlertDialogEntity;
import com.tgf.kcwc.mvp.model.Exhibition;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.RXUtil;
import com.tgf.kcwc.util.SystemUtils;
import com.tgf.kcwc.util.ValueHodler;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.jpush.android.api.JPushInterface;

/**
 * Author：Jenny
 * Date:2016/12/8 15:07
 * E-mail：fishloveqin@gmail.com
 * 应用全局上下文
 */
public class KPlayCarApp extends MultiDexApplication {
    private static final ExecutorService sPool = Executors.newFixedThreadPool(10);
    private static final ValueHodler sGlobalValues = new ValueHodler();
    public String adcode = "500107";
    public String locCityName = "重庆市";
    public int cityId = 290;
    public String latitude = "29.525769";
    public String longitude = "106.493483";
    public Exhibition mExhibitin;
    private static LinkedList<Activity> activityStack = new LinkedList<>();
    private PackageInfo packageInof;
    public static KPlayCarApp instance;

    public static KPlayCarApp getInstance() {
        return instance;
    }

    public String getAddressInfo() {
        return mAddressInfo;
    }

    public String mAddressInfo = "重庆市";
    private AMapLocationClient locationClient;

    public IWXAPI getMsgApi() {
        return msgApi;
    }

    private IWXAPI msgApi = null;

    public String getOrderId() {
        return orderId;
    }

    public String getAdcode() {
        return adcode;
    }

    public String getLattitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    private String orderId = "";

    public Typeface getTypeface() {
        return mTypeface;
    }

    private Typeface mTypeface;

    public Tencent getTencent() {
        return mTencent;
    }

    private Tencent mTencent;

    public List<AlertDialogEntity> getAlertDialogEntities() {
        return alertDialogEntities;
    }

    private List<AlertDialogEntity> alertDialogEntities = null;
    public static RefWatcher getRefWatcher(Context context) {
        KPlayCarApp application = (KPlayCarApp) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = KPlayCarApp.this;
        if(Constants.IS_TEST)
        refWatcher =  LeakCanary.install(this);
        try {
            InputStream inputStream = getAssets().open("cert/kcwc.cer");
            RXUtil.OK_BUILD.sslSocketFactory(RXUtil.getSslSocketFactory(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }

        CommonUtils.saveLog();
        RequestInterceptor.appContext = getApplicationContext();
        mTypeface = Typeface.createFromAsset(getAssets(), "fonts/t1.TTF");


        //        try {
        //            EMClient.getInstance().createAccount("13368246591", "tgf123");
        //        } catch (HyphenateException e) {
        //            e.printStackTrace();
        //        }

        //初始化微信支付API
        msgApi = WXAPIFactory.createWXAPI(getApplicationContext(), Constants.WEIXIN_APP_ID);
        // 将该app注册到微信
        msgApi.registerApp(Constants.WEIXIN_APP_ID);

        //实始化QQ分享平台API
        mTencent = Tencent.createInstance(Constants.TencentShare.APP_KEY, getApplicationContext());

        //新浪微博分享注册

        WbSdk.install(getApplicationContext(),
                new AuthInfo(getApplicationContext(), Constants.SinaShare.APP_KEY,
                        Constants.SinaShare.REDIRECT_URL, Constants.SinaShare.SCOPE));

        //百度地图注册
        //        SDKInitializer.initialize(getApplicationContext());
        //APP全局日志Log初始化
        Logger.init("Exhibition") // default PRETTYLOGGER or use just init()
                .methodCount(3) // default 2
                .hideThreadInfo() // default shown
                //.logLevel(LogLevel.NONE)        // default LogLevel.FULL
                .methodOffset(2);
        boolean isUIProcess = true;
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid && appProcess.processName.contains(":updater")) {
                isUIProcess = false;
            }
        }

        UIProcessInitialize(isUIProcess);
        //Fresco框架初始化
        Fresco.initialize(this);
        //        LocationUtil.getLocationClient()
        //        initLocation();

        //初始化图片选择器，并配置相应参数
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new PicassoImageLoader()); //设置图片加载器
        imagePicker.setShowCamera(true); //显示拍照按钮
        imagePicker.setCrop(true); //允许裁剪（单选才有效）
        imagePicker.setMultiMode(true);
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(9); //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE); //裁剪框的形状
        imagePicker.setFocusWidth(800); //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800); //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
        JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this); // 初始化 JPush

        //环信 模拟器不初始化环信
        if (!SystemUtils.isEmulator(getApplicationContext())) {
//            EaseManger.getInstance().init(this);
            GlobalHelper.getInstance().init(getApplicationContext());
            //red packet code : 初始化红包SDK，开启日志输出开关
            RedPacket.getInstance().initRedPacket(getApplicationContext(), RPConstant.AUTH_METHOD_EASEMOB, new RPInitRedPacketCallback() {

                @Override
                public void initTokenData(RPValueCallback<TokenData> callback) {
                    TokenData tokenData = new TokenData();
                    tokenData.imUserId = EMClient.getInstance().getCurrentUser();
                    //此处使用环信id代替了appUserId 开发者可传入App的appUserId
                    tokenData.appUserId = EMClient.getInstance().getCurrentUser();
                    tokenData.imToken = EMClient.getInstance().getAccessToken();
                    //同步或异步获取TokenData 获取成功后回调onSuccess()方法
                    callback.onSuccess(tokenData);
                }

                @Override
                public RedPacketInfo initCurrentUserSync() {
                    //这里需要同步设置当前用户id、昵称和头像url
                    String fromAvatarUrl = "";
                    String fromNickname = EMClient.getInstance().getCurrentUser();
                    EaseUser easeUser = EaseUserUtils.getUserInfo(fromNickname);
                    if (easeUser != null) {
                        fromAvatarUrl = TextUtils.isEmpty(easeUser.getAvatar()) ? "none" : easeUser.getAvatar();
                        fromNickname = TextUtils.isEmpty(easeUser.getNick()) ? easeUser.getUsername() : easeUser.getNick();
                    }
                    RedPacketInfo redPacketInfo = new RedPacketInfo();
                    redPacketInfo.fromUserId = EMClient.getInstance().getCurrentUser();
                    redPacketInfo.fromAvatarUrl = fromAvatarUrl;
                    redPacketInfo.fromNickName = fromNickname;
                    return redPacketInfo;
                }
            });
            RedPacket.getInstance().setDebugMode(true);
        }
        //end of red packet code

        //初始化对话框数据

        alertDialogEntities = new ArrayList<AlertDialogEntity>();

        String title = getString(R.string.alert_dialog_title);
        String confirmText = getString(R.string.confirm);
        String cancelText = getString(R.string.cancel);
        String pauseText = getString(R.string.pause);
        String stopText = getString(R.string.stop);
        String resumeText = getString(R.string.resume);
        alertDialogEntities.add(new AlertDialogEntity(title,
                getString(R.string.ride_run_prompt_msg1), cancelText, confirmText));

        alertDialogEntities.add(new AlertDialogEntity(title,
                getString(R.string.ride_run_prompt_msg2), pauseText, stopText));

        alertDialogEntities.add(new AlertDialogEntity(title,
                getString(R.string.ride_run_prompt_msg3), cancelText, confirmText));

        alertDialogEntities
                .add(new AlertDialogEntity(title, getString(R.string.ride_run_prompt_msg4),
                        getString(R.string.goout_stop), getString(R.string.goout_resume)));

        alertDialogEntities.add(new AlertDialogEntity(title,
                getString(R.string.ride_run_prompt_msg4), stopText, resumeText));

        //增加请你玩，开车去类型
        alertDialogEntities.add(new AlertDialogEntity(title, getString(R.string.ride_run_prompt_msg5), cancelText, confirmText));


    }

    private void UIProcessInitialize(boolean isUIProcess) {
        if (isUIProcess) {
            //File cacheDir = StorageUtils.getCacheDirectory(this); // default

        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        if (locationClient != null) {
            locationClient.onDestroy();
            locationClient = null;
        }
        MobclickAgent.onKillProcess(this);//在结束进程之前保存数据
        System.gc();

        super.onLowMemory();
        if (msgApi != null) {
            msgApi.detach();
        }

    }

    public void addActivity(Activity aty) {
        activityStack.add(aty);
    }

    public void removeActivity(Activity aty) {
        activityStack.remove(aty);
    }

    public static List<Activity> getActivityStack() {
        return activityStack;
    }

    /**
     * 结束所有Activity
     */
    public static void loginOut() {
        for (Activity activity : activityStack) {
            if (activity != null)
                activity.finish();
        }
    }

    /**
     * 结束Activity数量
     */
    public static void popNum(int topNum) {
        for (int i = 0; i < topNum; i++) {
            Activity activity = activityStack.getLast();
            if (activity != null)
                activity.finish();
        }
    }

    /**
     * 结束当前Activity以外的activity
     */
    public static void endPopNum() {
        for (int i = 0; i < activityStack.size() - 1; i++) {
            Activity activity = activityStack.getLast();
            if (activity != null)
                activity.finish();
        }
    }

    public static void removeValue(String key) {
        sGlobalValues.remove(key);
    }

    public static <T> void putValue(String key, T value) {
        sGlobalValues.putValue(key, value);
    }

    public static <T> T getValue(String key) {
        return sGlobalValues.getValue(key);
    }

    public int getRideId() {
        return mRideId;
    }

    public void setRideId(int mRideId) {
        this.mRideId = mRideId;
    }

    private int mRideId;// 驾图线路id
}
