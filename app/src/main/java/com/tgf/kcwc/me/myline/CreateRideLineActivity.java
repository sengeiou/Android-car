package com.tgf.kcwc.me.myline;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
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
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Tip;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.SelectAddressActivity;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.callback.OnSingleClickListener;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.entity.MarkerPosItem;
import com.tgf.kcwc.mvp.model.RideData;
import com.tgf.kcwc.mvp.model.RideDataItem;
import com.tgf.kcwc.mvp.model.RideLinePreviewModel;
import com.tgf.kcwc.mvp.presenter.FileUploadPresenter;
import com.tgf.kcwc.mvp.presenter.RideDataPresenter;
import com.tgf.kcwc.mvp.view.FileUploadView;
import com.tgf.kcwc.mvp.view.RideDataView;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.GsonUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.LocationUtil;
import com.tgf.kcwc.util.ScreenShotHelper;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.wrapper.AMapWrapperListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Author:Jenny
 * Date:2017/5/10
 * E-mail:fishloveqin@gmail.com
 * 创建线路
 */

public class CreateRideLineActivity extends BaseActivity
        implements LocationSource, AMapLocationListener,
        RideDataView<RideData>, AMap.OnMapScreenShotListener {
    protected TextView etSearch;
    protected RelativeLayout searchLayout;
    protected MapView mapView;
    protected RelativeLayout mapRootLayout;
    protected SwipeMenuListView list;
    protected View currentPosLayout;
    protected ImageView indicatorImg1;
    protected ImageView indicatorImg2;
    private AMap aMap;

    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private Marker locationStartMarker;
    private Marker locationEndMarker;
    private Marker locationAwayMarker;
    private RelativeLayout mMakerCenterLayout;

    private ImageView mConfirmBtn;

    private TextView mMarkerImg;

    private View markerPosView;

    private TextView markerPosName;
    private CameraPosition mCPosition;

    AMapNavi mAMapNavi;

    private RouteOverLay mRouteOverLay;
    private ImageView mTrafficImgView;

    // 起点终点列表
    private ArrayList<NaviLatLng> mStartPoints = new ArrayList<NaviLatLng>();
    private ArrayList<NaviLatLng> mEndPoints = new ArrayList<NaviLatLng>();
    private List<NaviLatLng> mWayPointList = new ArrayList<NaviLatLng>(); //途经点

    private RideDataPresenter mPresenter;
    private FileUploadPresenter mUploadPresenter = null;

    private RideLinePreviewModel mPrevModel;

    private String rideId = "";
    private String title = "线路规划";

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("线路规划");

        function.setTextResource("完成", R.color.white, 16);

        function.setOnClickListener(new OnSingleClickListener(1000) {
            @Override
            protected void onSingleClick(View view) {

                if (locationStartMarker == null || locationEndMarker == null) {

                    CommonUtils.showToast(mContext, "请设置起点或者终点");
                    return;
                }


                setPointsBounds();

                aMap.getMapScreenShot(CreateRideLineActivity.this);
            }
        });
    }

    private void setPointsBounds() {

        ArrayList<LatLng> latLngs = new ArrayList<LatLng>();
        for (Marker m : mDatas) {
            latLngs.add(m.getPosition());
        }
        LatLngBounds bounds = getLatLngBounds2(latLngs);
        if (bounds != null) {
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        }

    }

    //根据中心点和自定义内容获取缩放bounds
    private LatLngBounds getLatLngBounds2(List<LatLng> latLngs) {

        LatLngBounds.Builder b = LatLngBounds.builder();

        for (LatLng latLng : latLngs) {

            b.include(latLng);

        }
        return b.build();
    }

    private void uploadImage(File file) {

        mUploadPresenter.uploadAvatarFile(RequestBody.create(MediaType.parse("image/png"), file),
                RequestBody.create(MediaType.parse("text/plain"), "avatar"),
                RequestBody.create(MediaType.parse("text/plain"), ""),
                RequestBody.create(MediaType.parse("text/plain"), IOUtils.getToken(getContext())));
    }

    @Override
    protected void setUpViews() {

        initView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mPrevModel = intent.getParcelableExtra(Constants.IntentParams.DATA);
        super.setContentView(R.layout.activity_create_ride_line);

        mapView.onCreate(savedInstanceState);
    }

    private void initView() {
        etSearch = (TextView) findViewById(R.id.etSearch);
        searchLayout = (RelativeLayout) findViewById(R.id.searchLayout);
        searchLayout.setOnClickListener(this);
        mapView = (MapView) findViewById(R.id.map);
        mapRootLayout = (RelativeLayout) findViewById(R.id.mapRootLayout);
        list = (SwipeMenuListView) findViewById(R.id.list);
        mMarkerImg = (TextView) findViewById(R.id.markerImg);
        mMarkerImg.setOnClickListener(this);
        mConfirmBtn = (ImageView) findViewById(R.id.confirmBtn);
        mConfirmBtn.setOnClickListener(this);
        init();
        currentPosLayout = findViewById(R.id.currentPosLayout);
        indicatorImg1 = (ImageView) findViewById(R.id.indicatorImg1);
        indicatorImg2 = (ImageView) findViewById(R.id.indicatorImg2);

        //设置侧滑菜项
        list.setMenuCreator(creator);
        list.setOnMenuItemClickListener(onMenuItemClickListener);

        if (mPrevModel != null) {

            List<RideDataItem> items = mPrevModel.dataline;

            rideId = mPrevModel.id + "";
            title = mPrevModel.title + "";
            int index = 0;
            int size = items.size();
            for (RideDataItem item : items) {

                MarkerPosItem markerPosItem = new MarkerPosItem();
                markerPosItem.address = item.address;
                markerPosItem.latLng = new LatLng(Double.parseDouble(item.lat),
                        Double.parseDouble(item.lng));

                if (index == 0) {
                    updateMarkerState(R.drawable.amap_marker_start, "起");
                    markerPosItem.type = 1;
                    locationStartMarker = drawMarkerInAMap(markerPosItem.latLng);
                    mPosItems.addFirst(markerPosItem);
                    mDatas.addFirst(locationStartMarker);
                    mStartPoints.clear();
                    mStartPoints.add(new NaviLatLng(markerPosItem.latLng.latitude,
                            markerPosItem.latLng.longitude));
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosItem.latLng, 14f));

                } else if (index == size - 1) {
                    updateMarkerState(R.drawable.amap_marker_end, "终");
                    locationEndMarker = drawMarkerInAMap(markerPosItem.latLng);
                    markerPosItem.type = 2;
                    mPosItems.addLast(markerPosItem);
                    mDatas.addLast(locationEndMarker);
                } else {

                    currentPos++;
                    updateMarkerState(R.drawable.amap_marker_away, "" + currentPos);
                    locationAwayMarker = drawMarkerInAMap(markerPosItem.latLng);
                    markerPosItem.type = 3;

                    mPosItems.add(currentPos, markerPosItem);
                    mDatas.add(currentPos, locationAwayMarker);

                }

                index++;

            }

            driveRoute();

        }

        mAdapter = new WrapAdapter<MarkerPosItem>(mContext, mPosItems, R.layout.marker_point_item) {

            private TextView address;
            private TextView name;
            private RelativeLayout currentPosLayout;

            @Override
            public void convert(final ViewHolder helper, final MarkerPosItem item) {

                currentPosLayout = helper.getView(R.id.currentPosLayout);
                name = helper.getView(R.id.name);
                int type = item.type;
                String nameTxt = "";
                int currentPosDrawableId = R.drawable.amap_marker_away;
                switch (type) {
                    case 1:
                        nameTxt = "起";
                        currentPosDrawableId = R.drawable.amap_marker_start;
                        break;
                    case 2:
                        nameTxt = "终";
                        currentPosDrawableId = R.drawable.amap_marker_end;
                        break;
                    case 3:
                        nameTxt = "" + (helper.getPosition());
                        currentPosDrawableId = R.drawable.amap_marker_away;
                        break;
                }
                name.setText(nameTxt);
                currentPosLayout.setBackgroundResource(currentPosDrawableId);

                address = helper.getView(R.id.address);
                address.setText(item.address);

            }
        };
        list.setAdapter(mAdapter);

        mPresenter = new RideDataPresenter();
        mPresenter.attachView(this);

        mUploadPresenter = new FileUploadPresenter();
        mUploadPresenter.attachView(fileUploadView);
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            int what = msg.what;
            switch (what) {

                case UPLOAD_COVER_SUCCESS:

                    uploadImage((File) msg.obj);
                    break;
            }
        }
    };

    @Override
    public void onMapScreenShot(Bitmap bitmap) {
        View v = null;
        ScreenShotHelper.saveScreenShot(bitmap, mapRootLayout, mapView, mHandler, v);
        //Toast.makeText(getApplicationContext(), "SD卡下查看截图后的文件", Toast.LENGTH_SHORT).show();

    }

    private static final int UPLOAD_COVER_SUCCESS = 3;

    @Override
    public void onMapScreenShot(Bitmap bitmap, int i) {

    }

    private FileUploadView<DataItem> fileUploadView = new FileUploadView<DataItem>() {
        @Override
        public void resultData(DataItem item) {

            int code = item.code;
            if (code == Constants.NetworkStatusCode.SUCCESS) {

                mPresenter.createRidePlan(buildRequestParams(item.content));
            } else {

                CommonUtils.showToast(mContext, "上传数据失败，请稍候重试!");
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

    /**
     * 上传线路参数
     *
     * @param cover
     * @return
     */
    private Map<String, String> buildRequestParams(String cover) {

        Map<String, String> params = new HashMap<String, String>();

        params.put("cover", cover);
        List<RideDataItem> rideItems = new ArrayList<RideDataItem>();

        int index = 0;
        int size = mPosItems.size();
        for (MarkerPosItem item : mPosItems) {

            RideDataItem rideDataItem = new RideDataItem();
            rideDataItem.address = item.address;
            rideDataItem.lat = item.latLng.latitude + "";
            rideDataItem.lng = item.latLng.longitude + "";
            rideDataItem.orders = index + "";
            String title = "";
            if (index == 0) {
                title = "起点";
            } else if (index == size - 1) {
                title = "终点";
            } else {
                title = "途经点:" + index;
            }
            rideDataItem.title = title;
            rideItems.add(rideDataItem);
            index++;

        }
        String json = GsonUtil.objToJson(rideItems);

        params.put("dataline_type", "json");
        params.put("mileage", (naviPath.getAllLength() / 1000.0f) + "");
        params.put("rideId", rideId);
        params.put("title", title);
        params.put("dataline", json);
        params.put("token", IOUtils.getToken(mContext));

        return params;
    }

    SwipeMenuListView.OnMenuItemClickListener onMenuItemClickListener = new SwipeMenuListView.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

            if (position == 0) {
                CommonUtils.showToast(mContext, "亲，起点不允许删除哦！");
                return true;
            }
            deleteItem(position);

            return true;
        }
    };

    private void deleteItem(int position) {

        resetAllMarkersPos(position);
    }

    private List<LatLng> tempLatLngs = new LinkedList<LatLng>();

    private void resetAllMarkersPos(int position) {

        Marker marker = mDatas.remove(position);

        marker.remove();
        mPosItems.remove(position);
        mAdapter.notifyDataSetChanged();
        int index = 0;
        int size = mDatas.size();

        for (Marker m : mDatas) {

            if (index == 0) {

                updateMarkerState(R.drawable.amap_marker_start, "起");
                mPosItems.get(index).type = 1;
            } else if (index == size - 1) {
                updateMarkerState(R.drawable.amap_marker_end, "终");
                mPosItems.get(index).type = 2;
            } else {
                updateMarkerState(R.drawable.amap_marker_away, "" + index);
                mPosItems.get(index).type = 3;
            }
            m.setIcon(
                    BitmapDescriptorFactory.fromBitmap(convertViewToBitmap(mContext, markerPosView)));
            index++;
        }

        currentPos--;
        if (size == 1) {
            currentPos = 0;
            locationEndMarker = null;

        }

        if (size >= 2) {
            driveRoute();
        } else {

            for (Polyline p : mAllPolylines) {
                p.remove();
            }
            mAllPolylines.clear();
        }
    }

    private AMapNaviPath naviPath;
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
                strategy = mAMapNavi.strategyConvert(
                        true, false, false, false,
                        false);
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

            List<NaviLatLng> nlls = naviPath
                    .getCoordList();
            drawNavLines(nlls);
        }

    };

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
        for (NaviLatLng latLng : nlls) {

            options.color(mRes.getColor(R.color.style_bg1));
            LatLng latLng1 = new LatLng(latLng.getLatitude(), latLng.getLongitude());
            options.add(latLng1);
        }
        Polyline polyline = aMap.addPolyline(options);
        polyline.setWidth(25);
        mAllPolylines.add(polyline);

    }

    private void driveRoute() {

        int size = mDatas.size();

        if (size < 2) {
            return;
        }
        Marker endMarker = mDatas.get(size - 1);
        NaviLatLng endPoint = new NaviLatLng(endMarker.getPosition().latitude,
                endMarker.getPosition().longitude);
        mEndPoints.clear();
        mEndPoints.add(endPoint);
        mWayPointList.clear();

        int index = 0;
        for (Marker m : mDatas) {

            if (index != 0 && index != size - 1) {
                NaviLatLng awayPoint = new NaviLatLng(m.getPosition().latitude,
                        m.getPosition().longitude);
                mWayPointList.add(awayPoint);
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

    /**
     * 初始化
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }

        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(mapWrapperListener);
        mRouteOverLay = new RouteOverLay(aMap, null, getApplicationContext());
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                mConfirmBtn.setVisibility(View.GONE);
                //indicatorImg1.setVisibility(View.GONE);
                //indicatorImg2.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {

                mCPosition = cameraPosition;
                //startJumpAnimation();
                mConfirmBtn.setVisibility(View.VISIBLE);
                //indicatorImg1.setVisibility(View.VISIBLE);
                //indicatorImg2.setVisibility(View.GONE);

            }
        });

        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                //addMarkerInScreenCenter(null);
            }
        });
        aMap.getUiSettings().setScrollGesturesEnabled(true);

    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.getUiSettings().setZoomControlsEnabled(false);
        if (mPrevModel == null) {
            aMap.setLocationSource(this);// 设置定位监听
            aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        }

        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示

        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);

    }

    private void updateMarkerState(int drawableId, String name) {
        markerPosView = LayoutInflater.from(mContext).inflate(R.layout.marker_pos_layout, null,
                false);
        markerPosName = (TextView) markerPosView.findViewById(R.id.name);
        markerPosView.setBackgroundResource(drawableId);
        markerPosName.setText(name);
        int currentPosDrawableId = R.drawable.amap_marker_away;
        if ("起".equals(name)) {
            mMarkerImg.setText("终");
            currentPosDrawableId = R.drawable.amap_marker_end;
        } else if ("终".equals(name)) {

            if (currentPos == 0) {
                mMarkerImg.setText("1");
            }
            currentPosDrawableId = R.drawable.amap_marker_away;
        } else {
            mMarkerImg.setText((Integer.parseInt(name) + 1) + "");
            currentPosDrawableId = R.drawable.amap_marker_away;
        }
        System.out.println("name:" + name);
        currentPosLayout.setBackgroundResource(currentPosDrawableId);

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }

        if (mPresenter != null) {
            mPresenter.detachView();
        }

        if (mUploadPresenter != null) {
            mUploadPresenter.detachView();

        }
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        Log.e("MY", "onLocationChanged");
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);

                LatLng curLatlng = new LatLng(amapLocation.getLatitude(),
                        amapLocation.getLongitude());
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curLatlng, 16f));

            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": "
                        + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
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
            //设置为高精度定位模式
            mLocationOption.setOnceLocation(true);
            mLocationOption
                    .setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
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

    private Marker drawMarkerInAMap(LatLng locationLatLng, int drawaleId) {

        MarkerOptions options = new MarkerOptions();
        options.position(locationLatLng);
        options.anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromResource(drawaleId));
        Marker marker = aMap.addMarker(options);

        return marker;
    }

    private Marker drawMarkerInAMap(LatLng locationLatLng) {

        MarkerOptions options = new MarkerOptions();
        options.position(locationLatLng);
        options.anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromBitmap(convertViewToBitmap(mContext, markerPosView)));
        Marker marker = aMap.addMarker(options);

        return marker;
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

    private LinkedList<MarkerPosItem> mPosItems = new LinkedList<MarkerPosItem>();
    private WrapAdapter<MarkerPosItem> mAdapter = null;

    private int currentPos = 0;

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {

            case R.id.confirmBtn:

                mCPosition = aMap.getCameraPosition();
                if (locationStartMarker == null) {
                    updateMarkerState(R.drawable.amap_marker_start, "起");
                    locationStartMarker = drawMarkerInAMap(mCPosition.target);
                    mDatas.addFirst(locationStartMarker);
                    MarkerPosItem item = new MarkerPosItem();
                    item.latLng = locationStartMarker.getPosition();
                    item.type = 1;
                    mPosItems.addFirst(item);
                    setAddress(item);

                    NaviLatLng startPoint = new NaviLatLng(
                            locationStartMarker.getPosition().latitude,
                            locationStartMarker.getPosition().longitude);
                    mStartPoints.clear();
                    mStartPoints.add(startPoint);
                } else if (locationStartMarker != null && locationEndMarker == null) {
                    updateMarkerState(R.drawable.amap_marker_end, "终");
                    locationEndMarker = drawMarkerInAMap(mCPosition.target);
                    mDatas.addLast(locationEndMarker);
                    MarkerPosItem item = new MarkerPosItem();
                    item.latLng = locationEndMarker.getPosition();
                    item.type = 2;
                    mPosItems.addLast(item);
                    setAddress(item);

                } else {
                    currentPos++;
                    updateMarkerState(R.drawable.amap_marker_away, "" + currentPos);
                    locationAwayMarker = drawMarkerInAMap(mCPosition.target);
                    mDatas.add(currentPos, locationAwayMarker);
                    MarkerPosItem item = new MarkerPosItem();
                    item.latLng = locationAwayMarker.getPosition();
                    item.type = 3;
                    mPosItems.add(currentPos, item);
                    setAddress(item);

                }

                driveRoute();
                break;

            case R.id.searchLayout:

                CommonUtils.startResultNewActivity(this, null, SelectAddressActivity.class,
                        Constants.IntentParams.REQUEST_CODE);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.IntentParams.REQUEST_CODE && RESULT_OK == resultCode) {

            Tip tip = data.getParcelableExtra(Constants.IntentParams.DATA);

            String city = data.getStringExtra(Constants.IntentParams.DATA2);

            LatLonPoint point = tip.getPoint();
            if (point == null) {

                CommonUtils.showToast(mContext, "目的地范围太大，请重新设置");
                return;
            }

            LatLng locationLatLng = new LatLng(point.getLatitude(), point.getLongitude());

            if (locationStartMarker == null) {
                updateMarkerState(R.drawable.amap_marker_start, "起");
                locationStartMarker = drawMarkerInAMap(locationLatLng);
                mDatas.addFirst(locationStartMarker);
                MarkerPosItem item = new MarkerPosItem();
                item.latLng = locationStartMarker.getPosition();
                item.type = 1;
                mPosItems.addFirst(item);
                setAddress(item);

                NaviLatLng startPoint = new NaviLatLng(locationStartMarker.getPosition().latitude,
                        locationStartMarker.getPosition().longitude);
                mStartPoints.clear();
                mStartPoints.add(startPoint);
            } else if (locationStartMarker != null && locationEndMarker == null) {
                updateMarkerState(R.drawable.amap_marker_end, "终");
                locationEndMarker = drawMarkerInAMap(locationLatLng);
                mDatas.addLast(locationEndMarker);
                MarkerPosItem item = new MarkerPosItem();
                item.latLng = locationEndMarker.getPosition();
                item.type = 2;
                mPosItems.addLast(item);
                setAddress(item);

            } else {
                currentPos++;
                updateMarkerState(R.drawable.amap_marker_away, "" + currentPos);
                locationAwayMarker = drawMarkerInAMap(locationLatLng);
                mDatas.add(currentPos, locationAwayMarker);
                MarkerPosItem item = new MarkerPosItem();
                item.latLng = locationAwayMarker.getPosition();
                item.type = 3;
                mPosItems.add(currentPos, item);
                setAddress(item);

            }

            driveRoute();

        }

    }

    private void setAddress(final MarkerPosItem item) {

        LatLng latLng = item.latLng;
        double locs[] = {latLng.latitude, latLng.longitude};
        final GeocodeSearch.OnGeocodeSearchListener listener = new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

                if (regeocodeResult != null) {

                    item.address = regeocodeResult.getRegeocodeAddress().getFormatAddress();
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        };
        LocationUtil.reverseGeo(mContext, locs, listener);
    }

    private LinkedList<Marker> mDatas = new LinkedList<Marker>();

    private ArrayList<Polyline> mAllPolylines = new ArrayList<Polyline>();

    /**
     * 创建侧滑菜单项
     */
    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {

            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            deleteItem.setBackground(new ColorDrawable(
                    Color.rgb(0xF9, 0x3F, 0x25)));
            // set item width
            deleteItem.setWidth(
                    BitmapUtils.dp2px(mContext, 90));
            // set a icon
            deleteItem.setIcon(R.drawable.delete_icon);
            // add to menu
            menu.addMenuItem(deleteItem);
        }
    };

    @Override
    public void showDatas(RideData rideData) {

        CommonUtils.showToast(mContext, "线路创建成功！");

        finish();
    }

    @Override
    public void setLoadingIndicator(boolean active) {

        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

        dismissLoadingDialog();
    }

    @Override
    public Context getContext() {
        return mContext;
    }
}
