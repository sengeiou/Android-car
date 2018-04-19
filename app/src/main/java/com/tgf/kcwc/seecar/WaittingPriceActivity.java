package com.tgf.kcwc.seecar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.MorePopupwindowBean;
import com.tgf.kcwc.mvp.model.NewPriceModel;
import com.tgf.kcwc.mvp.model.WaittingPriceModel;
import com.tgf.kcwc.mvp.presenter.WaittingPricePresenter;
import com.tgf.kcwc.mvp.view.WaittingPriceView;
import com.tgf.kcwc.see.dealer.DealerHomeActivity;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SensorEventHelper;
import com.tgf.kcwc.util.SystemInvoker;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MapNavPopWindow;
import com.tgf.kcwc.view.MorePopupWindow;
import com.tgf.kcwc.view.NotitleContentDialog;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/2/20 0020
 * E-mail:hekescott@qq.com
 */

public class WaittingPriceActivity extends BaseActivity implements LocationSource,
        AMapLocationListener, WaittingPriceView {
    private static final String INTENT_KEY_ORG = "orglist";
    private TextView loacText;
    private MapView mapView;
    private double lat;
    private double lon;
    private AMap aMap;
    private SensorEventHelper mSensorHelper;
    private Marker mLocMarker;
    public static final String LOCATION_MARKER_FLAG = "mylocation";
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    //    private Circle mCircle;
    private boolean mFirstFix;
    private MyPoiOverlay poiOverlay;
    private UiSettings mUiSetting;
    private SlidingDrawer slidingDrawer;
    private TextView numTv;
    private ListView slideLv;
    private ArrayList<WaittingPriceModel.Org> orgList;
    private WaittingPriceModel.Org mSelectedOrg;
    private Chronometer mChronometer;
    private MapNavPopWindow mapNavPopWindow;

    private LatLng startLanlng;

    private View orgItemView;
    private WaittingPricePresenter waittingPricePresenter;
    private Intent fromIntent;
    private final String INTENT_KEY_ORDER = "order";
    private TextView orgTitleTv;
    private TextView orgsAddressTv;
    private TextView orgDistanceTv;
    private String phoneNum;
    //导航 true 路线 false
    private boolean isNavi = true;
    private MyWaittingPricePushReciever myWaittingPricePushReciever;
    private TextView mCarNametV;
    private String brandName;
    private View mNoticeLayout;
    private ImageView mInnerIv;
    private ImageView mOutIv;
    private ArrayList<MorePopupwindowBean> dataList = new ArrayList<>();
    private MorePopupWindow morePopupWindow;
    private MorePopupWindow.MoreOnClickListener mMoreOnClickListener = new MorePopupWindow.MoreOnClickListener() {

        @Override
        public void moreOnClickListener(int position, MorePopupwindowBean bean) {
            switch (dataList.get(position).title) {
                case "取消订单":
                    showCancelDialog();
                    break;
            }
        }
    };
    private NotitleContentDialog mCancelOrderDialog;
    private RelativeLayout hintRl;

    private void showCancelDialog() {
        if (mCancelOrderDialog == null) {


            mCancelOrderDialog = new NotitleContentDialog(getContext());
            mCancelOrderDialog.setContentText(" 经销商正火速赶来，\n" +
                    "确认要取消么？");
            mCancelOrderDialog.setCancelText("暂不取消");
            mCancelOrderDialog.setYesText("确认取消");
            mCancelOrderDialog.setOnLoginOutClickListener(new NotitleContentDialog.IOnclickLisenter() {
                @Override
                public void OkClick() {
                    waittingPricePresenter.cancelOrderApi(token, ordeId);
                    mCancelOrderDialog.dismiss();
                }

                @Override
                public void CancleClick() {
                    mCancelOrderDialog.dismiss();
                }
            });
        }
        mCancelOrderDialog.show();
    }

    @Override
    protected void titleBarCallback(ImageButton back, final FunctionView function, TextView text) {
//        backEvent(back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent toMainMeAct = new Intent(getContext(), MainActivity.class);
//                toMainMeAct.putExtra(Constants.IntentParams.INDEX, 0);
//                toMainMeAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(toMainMeAct);
                CommonUtils.gotoMainPage(mContext,Constants.Navigation.HOME_INDEX);
            }
        });
        text.setText("等待抢单");
        function.setImageResource(R.drawable.global_nav_n);
        MorePopupwindowBean morePopupwindowBean = null;
        morePopupwindowBean = new MorePopupwindowBean();
        morePopupwindowBean.title = "取消订单";
        morePopupwindowBean.id = 0;
        dataList.add(morePopupwindowBean);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dataList != null && dataList.size() != 0) {
                    morePopupWindow = new MorePopupWindow(WaittingPriceActivity.this,
                            dataList, mMoreOnClickListener);
                    morePopupWindow.showPopupWindow(function);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
//        Intent toMainMeAct = new Intent(getContext(), MainActivity.class);
//        toMainMeAct.putExtra(Constants.IntentParams.INDEX, 0);
//        toMainMeAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(toMainMeAct);
        CommonUtils.gotoMainPage(mContext,Constants.Navigation.HOME_INDEX);
    }

    private PlacePoint mPlacePiont;

    @Override
    protected void setUpViews() {
    }

    private String token;
    private int ordeId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waittingprice);
        myWaittingPricePushReciever = new MyWaittingPricePushReciever();
        IntentFilter intentFilter = new IntentFilter("com.tgf.kcwc.waittingpricebr");
        registerReceiver(myWaittingPricePushReciever, intentFilter);
        mCarNametV = (TextView) findViewById(R.id.waittingprice_carname);
        token = IOUtils.getToken(getContext());
        fromIntent = getIntent();
        mPlacePiont = fromIntent.getParcelableExtra(INTENT_KEY_ORDER);
        ordeId = fromIntent.getIntExtra(Constants.IntentParams.ID, 0);
        brandName = fromIntent.getStringExtra(Constants.IntentParams.TITLE);
        mCarNametV.setText("目标:" + brandName);
        waittingPricePresenter = new WaittingPricePresenter();
        waittingPricePresenter.attachView(this);
        findViewById(R.id.waitingprice_carinforl).setOnClickListener(this);
        loacText = (TextView) findViewById(R.id.waitiongprice_notice);
        mOutIv = (ImageView) findViewById(R.id.waitingprice_outimg);
        mInnerIv = (ImageView) findViewById(R.id.waitingprice_inimg);
        loacText.setOnClickListener(this);
        waittingPricePresenter.getWaittingPrice(token, ordeId, mPlacePiont.myLalng.latitude + "",
                mPlacePiont.myLalng.longitude + "");
        initView();
        init(savedInstanceState);
    }

    private void initView() {
        findViewById(R.id.waitingnotice_closeiv).setOnClickListener(this);
        findViewById(R.id.waittingprice_route).setOnClickListener(this);
        findViewById(R.id.waittingprice_navirl).setOnClickListener(this);
        findViewById(R.id.waittingpice_callphone).setOnClickListener(this);
        findViewById(R.id.org_addresslayout).setOnClickListener(this);
        findViewById(R.id.org_titlelayout).setOnClickListener(this);
        orgItemView = findViewById(R.id.waittingprice_orgdetailll);
        orgTitleTv = (TextView) findViewById(R.id.org_title);
        orgsAddressTv = (TextView) findViewById(R.id.orgs_addresstv);
        mNoticeLayout = findViewById(R.id.waitiongprice_noticerl);
        mNoticeLayout.setOnClickListener(this);
        orgDistanceTv = (TextView) findViewById(R.id.waitingpric_distance);
        slidingDrawer = (SlidingDrawer) findViewById(R.id.slidingdrawer);
        mChronometer = (Chronometer) findViewById(R.id.waitingpric_time);
        numTv = (TextView) findViewById(R.id.waitingpric_numtv);
        slideLv = (ListView) findViewById(R.id.waittingpriceact_lv);
        //等待抢单提示
        hintRl = (RelativeLayout) findViewById(R.id.hintRl);
        findViewById(R.id.hintX).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hintRl.setVisibility(View.GONE);
            }
        });

        slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                numTv.setVisibility(View.GONE);
            }
        });
        slidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                numTv.setVisibility(View.VISIBLE);
            }
        });
        slidingDrawer.setOnDrawerScrollListener(new SlidingDrawer.OnDrawerScrollListener() {
            @Override
            public void onScrollStarted() {
                Logger.d("waititng onScrollStarted");
            }

            @Override
            public void onScrollEnded() {
                Logger.d("waititng onScrollEnded");
            }
        });
        slideLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                slidingDrawer.close();
                WaittingPriceModel.Org org = mOrgArrayList.get(position);
                aMap.moveCamera(CameraUpdateFactory
                        .newCameraPosition(CameraPosition.fromLatLngZoom(org.getmLatLng(), 18)));
                showChooseOrg(position, org);
            }
        });
    }

    private final int KEY_STARTACTIVITY_REQUEST = 101;

    private void init(Bundle savedInstanceState) {

        orgList = new ArrayList<>();
        orgList.clear();
        for (int i = 0; i < 10; i++) {
            orgList.add(new WaittingPriceModel.Org());
        }

        slideLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int getLastVisiblePosition = 0, lastVisiblePositionY = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // 判断是否滚动到底部
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        //加载更多功能的代码

                        View v = (View) view.getChildAt(view.getChildCount() - 1);
                        int[] location = new int[2];
                        v.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
                        int y = location[1];
                        Log.e("x" + location[0], "y" + location[1]);
                        if (view.getLastVisiblePosition() != getLastVisiblePosition
                                && lastVisiblePositionY != y)//第一次拖至底部
                        {
                            getLastVisiblePosition = view.getLastVisiblePosition();
                            lastVisiblePositionY = y;
                            return;
                        } else if (view.getLastVisiblePosition() == getLastVisiblePosition
                                && lastVisiblePositionY == y)//第二次拖至底部
                        {

                            getLastVisiblePosition = 0;
                            lastVisiblePositionY = 0;
                            Intent toIntent = new Intent(getContext(), NearOrgListActivity.class);
                            toIntent.putParcelableArrayListExtra(INTENT_KEY_ORG, mOrgArrayList);
                            startActivityForResult(toIntent, KEY_STARTACTIVITY_REQUEST);
                        }
                    }
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {

            }
        });
        mapView = (MapView) findViewById(R.id.waittingprice_map);
        mapView.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = mapView.getMap();
            mUiSetting = aMap.getUiSettings();
            setUpMap();
        }
        mSensorHelper = new SensorEventHelper(this);
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK) {

            switch (requestCode) {
                case KEY_STARTACTIVITY_REQUEST:
                    int index = data.getIntExtra(Constants.IntentParams.INDEX, -1);
                    if (index >= 0) {
                        WaittingPriceModel.Org org = mOrgArrayList.get(index);
                        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                                CameraPosition.fromLatLngZoom(org.getmLatLng(), 18)));
                        showChooseOrg(index, org);
                    }
                    break;
                default:
                    break;
            }
        }

    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        mapNavPopWindow = new MapNavPopWindow(mContext);
        mapNavPopWindow.setOnClickListener(this);
        aMap.setLocationSource(this);// 设置定位监听
        mUiSetting.setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        mUiSetting.setScaleControlsEnabled(true);
        mUiSetting.setCompassEnabled(true);
        aMap.setOnMarkerClickListener(markerClickListener);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {
            case R.id.waitiongprice_notice:
                Intent toYuyueIntent = new Intent(getContext(), YuyueSeecarActivity.class);
                toYuyueIntent.putExtra(Constants.IntentParams.ID, ordeId);
                startActivity(toYuyueIntent);
                //              waittingPricePresenter.getWaittingPrice("G4XdaFutrBjbGOuZKnXWRdmdfT59HmyH", 103, mPlacePiont.myLalng.latitude + "", mPlacePiont.myLalng.longitude + "");
                //                waittingPricePresenter.getWaittingPrice(token, ordeId, mPlacePiont.myLalng.latitude + "", mPlacePiont.myLalng.longitude + "");
                break;
            case R.id.waitingnotice_closeiv:
                mNoticeLayout.setVisibility(View.GONE);

                break;
            case R.id.waitiongprice_noticerl:
                Intent toIntent = new Intent(getContext(), YuyueSeecarActivity.class);
                toIntent.putExtra(Constants.IntentParams.ID, ordeId);
                startActivity(toIntent);
                //               CommonUtils.startNewActivity(this,OrderFellowActivity.class);
                break;
            case R.id.waittingprice_route:
                isNavi = false;
                mapNavPopWindow.show(this);
                //                poiOverlay.removeFromMap();
                break;
            case R.id.waittingprice_navirl:
                isNavi = true;
                mapNavPopWindow.show(this);
                break;
            case R.id.aMap:
                if (isNavi) {
                    try {
                        URLUtil.launcherInnerNavAMap(this, mSelectedOrg.latitude + "",
                                mSelectedOrg.longitude + "");
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        URLUtil.launcherInnerRouteAMap(this, mSelectedOrg.fullName, startLanlng,
                                mSelectedOrg.getmLatLng());
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                }
                break;

            case R.id.baiduMap:
                if (isNavi) {
                    try {
                        URLUtil.launcherInnerNavBaidu(this, mSelectedOrg.latitude + "",
                                mSelectedOrg.longitude + "");
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        URLUtil.launcherInnerRouteBaidu(this, mSelectedOrg.fullName, startLanlng,
                                mSelectedOrg.getmLatLng());
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.cancel:
                mapNavPopWindow.dismiss();
                break;
            case R.id.waittingpice_callphone:
                SystemInvoker.launchDailPage(getContext(), phoneNum);
                break;
            case R.id.waitingprice_carinforl:
                Intent toYuIntent = new Intent(getContext(), YuyueSeecarActivity.class);
                toYuIntent.putExtra(Constants.IntentParams.ID, ordeId);
                startActivity(toYuIntent);
                break;
            case R.id.org_addresslayout:
            case R.id.org_titlelayout:
                Intent toOrgdetail = new Intent(WaittingPriceActivity.this, DealerHomeActivity.class);

                toOrgdetail.putExtra(Constants.IntentParams.ID,     mSelectedOrg.id+ "");
                toOrgdetail.putExtra(Constants.IntentParams.TITLE, mSelectedOrg.fullName);
                toOrgdetail.putExtra(Constants.IntentParams.INDEX, 0);
                startActivity(toOrgdetail);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
        if (mChronometer != null) {
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.stop();
            mChronometer = null;
        }
        if (myWaittingPricePushReciever != null) {
            unregisterReceiver(myWaittingPricePushReciever);
            myWaittingPricePushReciever = null;
        }
        waittingPricePresenter.detachView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        } else {
            mSensorHelper = new SensorEventHelper(this);
            if (mSensorHelper != null) {
                mSensorHelper.registerSensorListener();

                if (mSensorHelper.getCurrentMarker() == null && mLocMarker != null) {
                    mSensorHelper.setCurrentMarker(mLocMarker);
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSensorHelper != null) {
            mSensorHelper.unRegisterSensorListener();
            mSensorHelper.setCurrentMarker(null);
            mSensorHelper = null;
        }
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
        deactivate();
        mFirstFix = false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                lat = amapLocation.getLatitude();
                lon = amapLocation.getLongitude();

                LatLng location = new LatLng(lat, lon);
                startLanlng = location;
                if (!mFirstFix) {
                    mFirstFix = true;
                    addMarker(location);//添加定位图标
                    mSensorHelper.setCurrentMarker(mLocMarker);//定位图标旋转
                }
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18));
            } else {
                String logstr = "定位失败," + amapLocation.getErrorCode() + ": "
                        + amapLocation.getErrorInfo();
                Log.e("AmapErr", logstr);

                String errText = "定位失败,请开启定位功能";
                CommonUtils.showToast(this, errText);
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption
                    .setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mLocationOption.setOnceLocation(true);
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除

        }
        if (mlocationClient != null) {
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    /**
     * 添加Marker
     */
    private void addMarker(LatLng latlng) {
        if (mLocMarker != null) {
            mLocMarker.remove();
        }
        Bitmap bMap = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.navi_map_gps_locked);
        BitmapDescriptor des = BitmapDescriptorFactory.fromBitmap(bMap);
        MarkerOptions options = new MarkerOptions();
        options.icon(des);
        options.anchor(0.5f, 0.5f);
        options.position(latlng);
        mLocMarker = aMap.addMarker(options);
    }

    AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
        // marker 对象被点击时回调的接口
        // 返回 true 则表示接口已响应事件，否则返回false
        @Override
        public boolean onMarkerClick(Marker marker) {

            PoiItem poiItem = (PoiItem) marker.getObject();

//            CommonUtils.showToast(WaittingPriceActivity.this, poiItem.getAdName());
            if (mOrgArrayList != null && mOrgArrayList.size() != 0) {
                for (int i = 0; i < mOrgArrayList.size(); i++) {
                    WaittingPriceModel.Org item = mOrgArrayList.get(i);
                    int id = Integer.parseInt(poiItem.getPoiId());
                    if (id == item.id) {
                        showChooseOrg(i, item);
                    }

                }
            }

            return false;
        }
    };

    private void showChooseOrg(int selectPos, WaittingPriceModel.Org item) {
        mSelectedOrg = item;
        orgItemView.setVisibility(View.VISIBLE);
        selectPos++;
        orgTitleTv.setText(selectPos + ". " + item.fullName);
        orgsAddressTv.setText(item.address);
        orgDistanceTv.setText(item.getDistance());
        phoneNum = item.tel;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (orgItemView.getVisibility() == View.VISIBLE) {
                orgItemView.setVisibility(View.GONE);
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    private ArrayList<WaittingPriceModel.Org> mOrgArrayList;

    @Override
    public void showOrgList(ArrayList<WaittingPriceModel.Org> orgArrayList) {
        mOrgArrayList = orgArrayList;
        ArrayList<PoiItem> poiItems = new ArrayList();
        for (WaittingPriceModel.Org item : orgArrayList) {
            LatLonPoint latLonPoint = new LatLonPoint(Double.parseDouble(item.latitude),
                    Double.parseDouble(item.longitude));
            PoiItem poiItem = new PoiItem(item.id + "", latLonPoint, item.fullName, "");
            poiItems.add(poiItem);
        }
        //清理之前搜索结果的marker
        if (poiOverlay != null) {
            poiOverlay.removeFromMap();
        }
        aMap.clear();
        if (poiOverlay == null)
            poiOverlay = new MyPoiOverlay(aMap, poiItems);
        poiOverlay.addToMap();
        poiOverlay.zoomToSpan();
        if (poiItems.size() != 0) {
            poiOverlay = new MyPoiOverlay(aMap, poiItems);
        }
        addMarker(new LatLng(lat, lon));//添加定位图标
        mSensorHelper.setCurrentMarker(mLocMarker);//定位图标旋转
        showSliding(mOrgArrayList);
    }

    @Override
    public void showNewPrice(NewPriceModel data) {

        NewPriceModel.OfferInfo offerInfo = data.offerInfo;
        String nickname = offerInfo.nickname;
        String price = "￥" + offerInfo.offer;
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("用户昵称")
                .append(nickname).append("响应预约,点我查看\n").append("提车底价 ").append(price);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(mRes.getColor(R.color.white)), 4,
                4 + nickname.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(mRes.getColor(R.color.red)),
                spannableStringBuilder.length() - price.length(), spannableStringBuilder.length(),
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        loacText.setText(spannableStringBuilder);

        mNoticeLayout.setVisibility(View.VISIBLE);

    }

    @Override
    public void showTitle(WaittingPriceModel data) {
        int size = BitmapUtils.dp2px(this, 15);
        long startTime = System.currentTimeMillis() - DateFormatUtil.getTime(data.createTime);
        mChronometer.setFormat("%s");
        mChronometer.setBase(SystemClock.elapsedRealtime() - startTime);
        mChronometer.start();
        mInnerIv.setImageBitmap(
                BitmapUtils.getRectColors(data.inColor.split(","), size, size, R.color.style_bg4, 2));
        mOutIv.setImageBitmap(
                BitmapUtils.getRectColors(data.outColor.split(","), size, size, R.color.style_bg4, 2));
    }

    @Override
    public void cancelSuccess() {
        finish();
        CommonUtils.showToast(getContext(),"取消订单成功");
    }

    @Override
    public void cancleFailed(String statusMessage) {
        CommonUtils.showToast(getContext(),"取消订单失败"+statusMessage);
    }

    private void showSliding(ArrayList<WaittingPriceModel.Org> orgArrayList) {
        numTv.setText("附近有" + orgArrayList.size() + "家经销商");
        slideLv.setAdapter(new WrapAdapter<WaittingPriceModel.Org>(this, orgArrayList,
                R.layout.listview_item_waittingorg) {
            @Override
            public void convert(ViewHolder helper, WaittingPriceModel.Org item) {
                helper.setText(R.id.org_title, item.fullName);
                helper.setText(R.id.orgs_addresstv, item.address);
                helper.setText(R.id.waitingpric_distance, item.getDistance());
            }
        });
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return this;
    }

    /**
     * 自定义PoiOverlay
     */

    private class MyPoiOverlay {

        private int[] markers = {R.drawable.poi_marker_1, R.drawable.poi_marker_2,
                R.drawable.poi_marker_3, R.drawable.poi_marker_4,
                R.drawable.poi_marker_5, R.drawable.poi_marker_6,
                R.drawable.poi_marker_7, R.drawable.poi_marker_8,
                R.drawable.poi_marker_9, R.drawable.poi_marker_10};

        private AMap mamap;
        private List<PoiItem> mPois;
        private ArrayList<Marker> mPoiMarks = new ArrayList<Marker>();

        public MyPoiOverlay(AMap amap, List<PoiItem> pois) {
            mamap = amap;
            mPois = pois;
        }

        /**
         * 添加Marker到地图中。
         *
         * @since V2.1.0
         */
        public void addToMap() {
            for (int i = 0; i < mPois.size(); i++) {
                Marker marker = mamap.addMarker(getMarkerOptions(i));
                PoiItem item = mPois.get(i);
                marker.setObject(item);
                mPoiMarks.add(marker);
            }
        }

        /**
         * 去掉PoiOverlay上所有的Marker。
         *
         * @since V2.1.0
         */
        public void removeFromMap() {
            for (Marker mark : mPoiMarks) {
                mark.remove();
            }
        }

        /**
         * 移动镜头到当前的视角。
         *
         * @since V2.1.0
         */
        public void zoomToSpan() {
            if (mPois != null && mPois.size() > 0) {
                if (mamap == null)
                    return;
                LatLngBounds bounds = getLatLngBounds();
                mamap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            }
        }

        private LatLngBounds getLatLngBounds() {
            LatLngBounds.Builder b = LatLngBounds.builder();
            for (int i = 0; i < mPois.size(); i++) {
                b.include(new LatLng(mPois.get(i).getLatLonPoint().getLatitude(),
                        mPois.get(i).getLatLonPoint().getLongitude()));
            }
            return b.build();
        }

        private MarkerOptions getMarkerOptions(int index) {
            return new MarkerOptions()
                    .position(new LatLng(mPois.get(index).getLatLonPoint().getLatitude(),
                            mPois.get(index).getLatLonPoint().getLongitude()))
                    .icon(getBitmapDescriptor(index));
            //            .title(getTitle(index))
            //            .snippet(getSnippet(index))
        }

        protected String getTitle(int index) {
            return mPois.get(index).getTitle();
        }

        protected String getSnippet(int index) {
            return mPois.get(index).getSnippet();
        }

        /**
         * 从marker中得到poi在list的位置。
         *
         * @param marker 一个标记的对象。
         * @return 返回该marker对应的poi在list的位置。
         * @since V2.1.0
         */
        public int getPoiIndex(Marker marker) {
            for (int i = 0; i < mPoiMarks.size(); i++) {
                if (mPoiMarks.get(i).equals(marker)) {
                    return i;
                }
            }
            return -1;
        }

        /**
         * 返回第index的poi的信息。
         *
         * @param index 第几个poi。
         * @return poi的信息。poi对象详见搜索服务模块的基础核心包（com.amap.api.services.core）中的类 <strong><a href="../../../../../../Search/com/amap/api/services/core/PoiItem.html" selecttitle="com.amap.api.services.core中的类">PoiItem</a></strong>。
         * @since V2.1.0
         */
        public PoiItem getPoiItem(int index) {
            if (index < 0 || index >= mPois.size()) {
                return null;
            }
            return mPois.get(index);
        }

        protected BitmapDescriptor getBitmapDescriptor(int arg0) {
            if (arg0 < 10) {
                BitmapDescriptor icon = BitmapDescriptorFactory
                        .fromBitmap(BitmapFactory.decodeResource(getResources(), markers[arg0]));
                return icon;
            } else {
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.drawable.marker_other_highlight));
                return icon;
            }
        }
    }

    private class MyWaittingPricePushReciever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String buyMotoId = intent.getStringExtra(Constants.IntentParams.ID);
            Logger.d("object_id" + buyMotoId);
            waittingPricePresenter.getNewPrice(token, Integer.parseInt(buyMotoId));
        }
    }

}
