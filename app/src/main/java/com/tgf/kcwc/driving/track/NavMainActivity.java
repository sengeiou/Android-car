package com.tgf.kcwc.driving.track;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Tip;
import com.tgf.kcwc.R;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.app.SelectAddressActivity;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.me.myline.MyLineMainActivity;
import com.tgf.kcwc.mvp.model.RideDataItem;
import com.tgf.kcwc.mvp.model.RideLinePreviewModel;
import com.tgf.kcwc.mvp.model.RideReportDetailModel;
import com.tgf.kcwc.mvp.presenter.LoadCircuitPresenter;
import com.tgf.kcwc.mvp.view.LoadCircuitView;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.ConvertUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.NumFormatUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MapNavPopWindow;
import com.tgf.kcwc.wrapper.AMapWrapperListener;

import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:Jenny
 * Date:2017/5/2
 * E-mail:fishloveqin@gmail.com
 * <p>
 * 驾图导航主页
 */

public class NavMainActivity extends BaseActivity implements LocationSource, AMapLocationListener, LoadCircuitView, AMap.OnMarkerClickListener {

    protected ImageButton titleBarBack;
    protected RelativeLayout backBtnLayout;
    protected TextView etSearch;
    protected ImageView loadRideLine;
    protected RelativeLayout searchLayout;
    protected TextView orgTitle;
    protected ImageView moreIcon;
    protected TextView orgAddress;
    protected TextView distanceTv;
    protected RelativeLayout titleLayout;
    protected RelativeLayout navLayout;
    private MapView mapView;
    private AMap aMap;

    private Marker locationMarker;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private PoiItem mSelectPoiItem = null;
    private PoiItem mCurrentItem = null;
    private double[] locations = new double[2];

    AMapNavi mAMapNavi;

    private RouteOverLay mRouteOverLay;
    private ImageView mTrafficImgView;

    // 起点终点列表
    private ArrayList<NaviLatLng> mStartPoints = new ArrayList<NaviLatLng>();
    private ArrayList<NaviLatLng> mEndPoints = new ArrayList<NaviLatLng>();
    private List<NaviLatLng> mWayPointList = new ArrayList<NaviLatLng>(); //途经点
    private RelativeLayout mNavInfoLayout;

    private String backType = ""; //载入路线返回类型
    private RideLinePreviewModel mPrevModel;

    private LoadCircuitPresenter mLoadCircuitPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isTitleBar = false;
        super.setContentView(R.layout.activity_nav_main);
        mapView.onCreate(savedInstanceState);

    }

    private AMapWrapperListener mapWrapperListeners = new AMapWrapperListener() {

        @Override
        public void onInitNaviSuccess() {

            System.out.println("onInitSuccess");
            /**
             * 方法:
             *   int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute);
             * 参数:
             * @congestion 躲避拥堵
             * @avoidhightspeed 不走高速
             * @cost 避免收费
             * @hightspeed 高速优先
             * @multipleroute 多路径
             *
             * 说明:
             *      以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
             * 注意:
             *      不走高速与高速优先不能同时为true
             *      高速优先与避免收费不能同时为true
             */
            int strategy = 0;
            try {
                strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onCalculateRouteSuccess() {
            //显示路径或开启导航
            System.out.println("onInitSuccess");
            AMapNaviPath naviPath = mAMapNavi.getNaviPath();
            if (naviPath == null) {
                return;
            }
            // 获取路径规划线路，显示到地图上

            mRouteOverLay.setAMapNaviPath(naviPath);
            mRouteOverLay.addToMap();
            // mAMapNavi.removeAMapNaviListener(mapWrapperListener);
        }

    };

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    protected void setUpViews() {
        initView();
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(mapWrapperListener);
        mRouteOverLay = new RouteOverLay(aMap, null, getApplicationContext());
        final LatLng latLng = new LatLng(Double.parseDouble(mGlobalContext.getLattitude()),
                Double.parseDouble(mGlobalContext.getLongitude()));

        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {

                //startJumpAnimation();
            }
        });
        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                //addMarkerInScreenCenter(latLng);

            }
        });
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        aMap.getUiSettings().setZoomControlsEnabled(true);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.getUiSettings().setScaleControlsEnabled(true);
        aMap.setTrafficEnabled(true);
        aMap.getUiSettings().setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
        //        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
        //            @Override
        //            public void onMapClick(LatLng latLng) {
        //
        //                //System.out.println("lat:" + latLng.latitude + ",long:" + latLng.longitude);
        //
        //                drawMarkPoint(latLng);
        //                if (mCurrentItem != null) {
        //
        //                    LatLng currentLatLng = new LatLng(mCurrentItem.getLatLonPoint().getLatitude(),
        //                        mCurrentItem.getLatLonPoint().getLongitude());
        //                    //计算两点之间的距离
        //                    double mile = AMapUtils.calculateLineDistance(currentLatLng, latLng);
        //
        //                }
        //            }
        //        });
        // aMap.setTrafficEnabled(true);

        //初始化画线相关对象
        //mPOptions=new PolylineOptions();
        //mPolyline=aMap.addPolyline(mPOptions);
    }

    private void drawMarkPoint(LatLng latLng) {

        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
        markerOption.title("西安市").snippet("西安市" + latLng.latitude + "," + latLng.longitude);

        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果
        aMap.addMarker(markerOption);
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            mLocationOption.setOnceLocation(false);

            //设置为GPS设备模式、主要是用于获取海拔
            mLocationOption
                    .setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setSensorEnable(true);//支持不是GPS模式时也返回海拔高度
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);

            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
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
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(final AMapLocation amapLocation) {
        //        Log.i("MY", "onLocationChanged");
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);

                LatLng curLatlng = new LatLng(amapLocation.getLatitude(),
                        amapLocation.getLongitude());

                mCurrentItem = new PoiItem(amapLocation.getAddress(),
                        new LatLonPoint(amapLocation.getLatitude(), amapLocation.getLongitude()), "",
                        "");
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curLatlng, 16f));
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": "
                        + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();

        boolean isSPONSOR = KPlayCarApp.getValue(Constants.PathBack.SPONSORIS);

        if (isSPONSOR) {
            String roadbookId = KPlayCarApp.getValue(Constants.PathBack.SPONSOR);
            backType = KPlayCarApp.getValue(Constants.PathBack.ROADBOOKTYPE);
            if (backType.equals("1")) {
                mLoadCircuitPresenter.loadRideReportDatas(roadbookId + "", "1", IOUtils.getToken(mContext));
            } else {
                mLoadCircuitPresenter.loadRidePlanDatas(roadbookId + "", IOUtils.getToken(mContext));
            }
            KPlayCarApp.putValue(Constants.PathBack.SPONSORIS, false);
        }

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

    private void initView() {
        titleBarBack = (ImageButton) findViewById(R.id.title_bar_back);
        backBtnLayout = (RelativeLayout) findViewById(R.id.backBtnLayout);
        mNavInfoLayout = (RelativeLayout) findViewById(R.id.navInfoLayout);
        etSearch = (TextView) findViewById(R.id.etSearch);
        etSearch.setOnClickListener(this);
        loadRideLine = (ImageView) findViewById(R.id.loadRideLine);
        loadRideLine.setOnClickListener(this);
        searchLayout = (RelativeLayout) findViewById(R.id.searchLayout);
        mapView = (MapView) findViewById(R.id.map);
        orgTitle = (TextView) findViewById(R.id.orgTitle);
        moreIcon = (ImageView) findViewById(R.id.moreIcon);
        orgAddress = (TextView) findViewById(R.id.orgAddress);
        distanceTv = (TextView) findViewById(R.id.distanceTv);
        titleLayout = (RelativeLayout) findViewById(R.id.titleLayout);
        navLayout = (RelativeLayout) findViewById(R.id.navLayout);
        navLayout.setOnClickListener(this);
        mTrafficImgView = (ImageView) findViewById(R.id.trafficBtn);
        mTrafficImgView.setOnClickListener(this);
        backEvent(titleBarBack);

        //初始化是否返回路线
        KPlayCarApp.putValue(Constants.PathBack.SPONSORIS, false);
        mLoadCircuitPresenter = new LoadCircuitPresenter();
        mLoadCircuitPresenter.attachView(this);
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {

            case R.id.navLayout:
                MapNavPopWindow mapNavPopWindow = new MapNavPopWindow(mContext);
                mapNavPopWindow.setOnClickListener(this);
                mapNavPopWindow.show(this);
                break;

            case R.id.aMap:

                try {
                    URLUtil.launcherInnerNavAMap(this, locations[0] + "", locations[1] + "");
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.baiduMap:
                try {
                    URLUtil.launcherInnerNavBaidu(this, locations[0] + "", locations[1] + "");
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.etSearch:
                CommonUtils.startResultNewActivity(NavMainActivity.this, null,
                        SelectAddressActivity.class, Constants.IntentParams.REQUEST_CODE);
                break;
            case R.id.loadRideLine:
                Map<String, Serializable> arg = new HashMap<>();
                arg.put(Constants.IntentParams.INTENT_TYPE, Constants.PathBack.DRIVINGHOME_SUCCEED);
                if (!IsShow) {
                    CommonUtils.startNewActivity(this, arg, MyLineMainActivity.class);
                } else {
                    CommonUtils.showToast(mContext, "您已经展示了一条线路");
                }
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
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Constants.IntentParams.REQUEST_CODE == requestCode && resultCode == RESULT_OK) {

            tip = data.getParcelableExtra(Constants.IntentParams.DATA);

            String city = data.getStringExtra(Constants.IntentParams.DATA2);

            if (tip.getPoint() == null) {

                CommonUtils.showToast(mContext, "目的地范围太大，请重新设置");
                return;
            }
            locations[0] = tip.getPoint().getLatitude();
            locations[1] = tip.getPoint().getLongitude();
            orgAddress.setText(tip.getAddress() + "");
            orgTitle.setText(tip.getName() + "");
            double distance = AMapUtils
                    .calculateLineDistance(new LatLng(mCurrentItem.getLatLonPoint().getLatitude(),
                                    mCurrentItem.getLatLonPoint().getLongitude()),

                            new LatLng(locations[0], locations[1]));

            distanceTv.setText(NumFormatUtil.getFmtTwoNum(distance / 1000) + "km");
            mNavInfoLayout.setVisibility(View.VISIBLE);
        }

    }

    private Tip tip;

/*    private void driveRoute() {

        NaviLatLng startPoint = new NaviLatLng(mCurrentItem.getLatLonPoint().getLatitude(),
                mCurrentItem.getLatLonPoint().getLongitude());
        mStartPoints.clear();
        mStartPoints.add(startPoint);
        mEndPoints.clear();

        NaviLatLng endPoint = new NaviLatLng(tip.getPoint().getLatitude(),
                tip.getPoint().getLongitude());
        mEndPoints.add(endPoint);
        NaviLatLng nl1 = new NaviLatLng(mCurrentItem.getLatLonPoint().getLatitude(),
                mCurrentItem.getLatLonPoint().getLongitude() + 0.5);
        // NaviLatLng nl2 = new NaviLatLng(mCurrentItem.getLatLonPoint().getLatitude(),
        //mCurrentItem.getLatLonPoint().getLongitude());
        drawMarkPoint(new LatLng(nl1.getLatitude(), nl1.getLongitude()));
        // drawMarkPoint(new LatLng(nl2.getLatitude(),nl2.getLongitude()));
        mWayPointList.clear();
        mWayPointList.add(nl1);
        // mWayPointList.add(nl2);

        int strategy = 0;
        try {
            strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAMapNavi.calculateDriveRoute(mStartPoints, mEndPoints, null, strategy);

        aMap.moveCamera(CameraUpdateFactory.zoomTo(12f));

    }*/

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {
        CommonUtils.showToast(mContext, "系统异常");
        setLoadingIndicator(false);
    }

    @Override
    public void RideReportData(RideReportDetailModel rideReportDetailModel) {
        CommonUtils.showToast(mContext, "成功1");
        showMapData(rideReportDetailModel);
    }

    @Override
    public void showloadRideDatas(RideLinePreviewModel rideLinePreviewModel) {
        CommonUtils.showToast(mContext, "成功2");
        mPrevModel = rideLinePreviewModel;
        List<RideDataItem> items = rideLinePreviewModel.dataline;

        int index = 0;
        int size = items.size();

        for (RideDataItem item : items) {
            LatLng latLng = new LatLng(Double.parseDouble(item.lat), Double.parseDouble(item.lng));
            if (index == 0) {
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f));
                updateMarkerState(R.drawable.amap_marker_start, "起");
            } else if (index == size - 1) {
                updateMarkerState(R.drawable.amap_marker_end, "终");
            } else {
                updateMarkerState(R.drawable.amap_marker_away, "" + index);
            }

            Marker m = drawMarkerInAMap(latLng);
            m.setObject(index);
            m.setIcon(
                    BitmapDescriptorFactory.fromBitmap(convertViewToBitmap(mContext, markerPosView)));

            index++;
        }

        driveRoute(items);
    }

    @Override
    public Context getContext() {
        return mContext;
    }


     /*--------------------------------------------------返回路线展示--------------------------------------------------------------------------------*/

    private View markerPosView;
    private TextView markerPosName;
    private boolean IsShow = false;
    List<RideReportDetailModel.RideNodeKeyBean> keybean = new ArrayList<>();

    private void showMapData(RideReportDetailModel rideReportDetailModel) {
        keybean.clear();
        keybean.addAll(rideReportDetailModel.rideNodeKey);
        IsShow = true;
        //画途经点
        List<RideReportDetailModel.RideNodeKeyBean> keyBeans = rideReportDetailModel.rideNodeKey;

        int index = 0;
        int drawableId = R.drawable.map_point_other;
        int size = keyBeans.size();
        for (RideReportDetailModel.RideNodeKeyBean keyBean : keyBeans) {

            String title = "";
            if (index == 0) {
                title = "起";
                drawableId = R.drawable.map_point_start;
            } else if (index == size - 1) {
                title = "终";
                drawableId = R.drawable.map_point_end;
            } else {
                title = "" + index;
                drawableId = R.drawable.map_point_other;
            }
            LatLng latLng = new LatLng(Double.parseDouble(keyBean.lat),
                    Double.parseDouble(keyBean.lng));
            drawMarkPoint(latLng, title, drawableId, index);
            index++;
        }

        //画轨迹
        List<RideReportDetailModel.RideNodeListBean> lists = rideReportDetailModel.rideNodeList;
        List<LatLng> latLngs = new ArrayList<LatLng>();
        for (RideReportDetailModel.RideNodeListBean bean : lists) {

            latLngs.add(new LatLng(Double.parseDouble(bean.lat), Double.parseDouble(bean.lng)));
        }

        Polyline polyline = aMap.addPolyline(new PolylineOptions().addAll(latLngs).geodesic(true)
                .color(mRes.getColor(R.color.style_bg1)));

        LatLngBounds bounds = getLatLngBounds2(latLngs);
        if (bounds != null) {
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        }

    }

    private void drawMarkPoint(LatLng latLng, String title, int drawableId, int num) {

        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
        // markerOption.type(type);
        //.snippet(type + latLng.latitude + "," + latLng.longitude);

        markerOption.icon(updateMarkerState(drawableId, title));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果
        Marker marker = aMap.addMarker(markerOption);
        marker.setObject(num);
    }

    private BitmapDescriptor updateMarkerState(int drawableId, String name) {
        markerPosView = LayoutInflater.from(mContext).inflate(R.layout.marker_pos_layout, null,
                false);
        markerPosName = (TextView) markerPosView.findViewById(R.id.name);

        markerPosName.setText(name);
        RelativeLayout layout = (RelativeLayout) markerPosView.findViewById(R.id.currentPosLayout);
        int currentPosDrawableId = R.drawable.amap_marker_away;
        if ("起".equals(name)) {
            currentPosDrawableId = R.drawable.amap_marker_start;
        } else if ("终".equals(name)) {

            currentPosDrawableId = R.drawable.amap_marker_end;
        } else {
            currentPosDrawableId = R.drawable.amap_marker_away;
        }
        layout.setBackgroundResource(currentPosDrawableId);
        return BitmapDescriptorFactory.fromBitmap(convertViewToBitmap(mContext, markerPosView));

    }

    //view 转bitmap
    public static Bitmap convertViewToBitmap(Context context, View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, BitmapUtils.dp2px(context, 21), BitmapUtils.dp2px(context, 29));
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();

        return bitmap;
    }

    //根据中心点和自定义内容获取缩放bounds
    private LatLngBounds getLatLngBounds2(List<LatLng> latLngs) {

        LatLngBounds.Builder b = LatLngBounds.builder();

        for (LatLng latLng : latLngs) {

            b.include(latLng);

        }
        return b.build();
    }

       /*-----------------------------------------------------规划路线---------------------------------------------------------------------*/

    private AMapNaviPath naviPath;

    /**
     * 根据起点、途经点、终点进行线路规划
     *
     * @param items
     */
    private void driveRoute(List<RideDataItem> items) {

        mStartPoints.clear();
        mEndPoints.clear();
        mWayPointList.clear();

        int size = items.size();

        if (size < 2) {
            return;
        }

        int index = 0;
        for (RideDataItem item : items) {

            NaviLatLng nll = new NaviLatLng(Double.parseDouble(item.lat),
                    Double.parseDouble(item.lng));
            if (index == 0) {

                mStartPoints.add(nll);
            } else if (index == size - 1) {
                mEndPoints.add(nll);
            } else {
                mWayPointList.add(nll);
            }
            index++;

        }

        int strategy = 0;
        try {
            strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAMapNavi.calculateDriveRoute(mStartPoints, mEndPoints, mWayPointList, strategy);

        //aMap.moveCamera(CameraUpdateFactory.zoomTo(12f));

    }


    private AMapWrapperListener mapWrapperListener = new AMapWrapperListener() {

        @Override
        public void onInitNaviSuccess() {

            /**
             * 方法:
             *   int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute);
             * 参数:
             * @congestion 躲避拥堵
             * @avoidhightspeed 不走高速
             * @cost 避免收费
             * @hightspeed 高速优先
             * @multipleroute 多路径
             *
             * 说明:
             *      以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
             * 注意:
             *      不走高速与高速优先不能同时为true
             *      高速优先与避免收费不能同时为true
             */
            int strategy = 0;
            try {
                strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onCalculateRouteSuccess() {
            //显示路径或开启导航
            System.out.println("onInitSuccess");
            naviPath = mAMapNavi.getNaviPath();
            if (naviPath == null) {
                return;
            }
            // 获取路径规划线路，显示到地图上
            //mRouteOverLay.setAMapNaviPath(naviPath);

            // mRouteOverLay.setEndPointBitmap(null);
            //mRouteOverLay.setStartPointBitmap(null);
            //mRouteOverLay.addToMap();
            // mAMapNavi.removeAMapNaviListener(mapWrapperListener);

            List<NaviLatLng> nlls = naviPath.getCoordList();
            drawNavLines(nlls);
            aMap.moveCamera(CameraUpdateFactory.zoomTo(14f));
        }

    };


    private ArrayList<Polyline> mAllPolylines = new ArrayList<Polyline>();

    /**
     * 在地图画出规划线路
     *
     * @param nlls
     */
    private void drawNavLines(List<NaviLatLng> nlls) {

        //先删除已画出的路线

        for (Polyline p : mAllPolylines) {
            p.remove();
        }
        mAllPolylines.clear();

        //遍历地图规划后所有的点，并画出
        PolylineOptions options = new PolylineOptions();
        for (NaviLatLng item : nlls) {

            options.color(mRes.getColor(R.color.style_bg1));
            LatLng latLng1 = new LatLng(item.getLatitude(), item.getLongitude());
            options.add(latLng1);
        }
        Polyline polyline = aMap.addPolyline(options);
        polyline.setWidth(25);
        mAllPolylines.add(polyline);
    }

    private Marker drawMarkerInAMap(LatLng locationLatLng) {

        MarkerOptions options = new MarkerOptions();
        options.position(locationLatLng);
        options.anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromBitmap(convertViewToBitmap(mContext, markerPosView)));
        Marker marker = aMap.addMarker(options);

        return marker;
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        int num = (int) marker.getObject();
        if (backType.equals("1")) {
            String address = keybean.get(num).address;
            String cityName = keybean.get(num).cityName;
            orgAddress.setText(cityName + "");
            orgTitle.setText(address + "");
            double lat = ConvertUtil.convertToDouble(keybean.get(num).lat, 0.0);
            double lng = ConvertUtil.convertToDouble(keybean.get(num).lng, 0.0);
            locations[0] = lat;
            locations[1] = lng;

            double distance = AMapUtils
                    .calculateLineDistance(new LatLng(mCurrentItem.getLatLonPoint().getLatitude(),
                                    mCurrentItem.getLatLonPoint().getLongitude()),

                            new LatLng(lat, lng));

            distanceTv.setText(NumFormatUtil.getFmtTwoNum(distance / 1000) + "km");

        } else {
            List<RideDataItem> dataline = mPrevModel.dataline;
            String address = dataline.get(num).address;
            orgAddress.setText("");
            orgTitle.setText(address + "");
            double lat = ConvertUtil.convertToDouble(dataline.get(num).lat, 0.0);
            double lng = ConvertUtil.convertToDouble(dataline.get(num).lng, 0.0);
            locations[0] = lat;
            locations[1] = lng;

            double distance = AMapUtils
                    .calculateLineDistance(new LatLng(mCurrentItem.getLatLonPoint().getLatitude(),
                                    mCurrentItem.getLatLonPoint().getLongitude()),

                            new LatLng(lat, lng));

            distanceTv.setText(NumFormatUtil.getFmtTwoNum(distance / 1000) + "km");
        }
        mNavInfoLayout.setVisibility(View.VISIBLE);
        return false;
    }

}
