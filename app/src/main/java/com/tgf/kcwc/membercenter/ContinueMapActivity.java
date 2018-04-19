package com.tgf.kcwc.membercenter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.NaviLatLng;
import com.google.gson.Gson;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.ContinueBean;
import com.tgf.kcwc.mvp.presenter.ContinuePresenter;
import com.tgf.kcwc.mvp.presenter.FileUploadPresenter;
import com.tgf.kcwc.mvp.view.ContinueView;
import com.tgf.kcwc.mvp.view.FileUploadView;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.ScreenShotHelper;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.wrapper.AMapWrapperListener;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/7/14.
 */

public class ContinueMapActivity extends BaseActivity implements LocationSource,
                                 AMapLocationListener, ContinueView, AMap.OnMapScreenShotListener {

    private static final int                           WHAT_VALUE3      = 3;

    private ContinuePresenter                          mContinuePresenter;

    protected MapView                                  mapView;
    private AMap                                       aMap;
    //定位
    private OnLocationChangedListener                  mListener;                                      //定位数据
    private AMapLocationClient                         mlocationClient;
    private AMapLocationClientOption                   mLocationOption;

    //路线规划
    AMapNavi                                           mAMapNavi;
    private AMapNaviPath                               naviPath;
    private View                                       markerPosView;                                  //添加在地图上的marker

    // 起点终点列表
    private ArrayList<NaviLatLng>                      mStartPoints     = new ArrayList<NaviLatLng>(); //起点
    private ArrayList<NaviLatLng>                      mEndPoints       = new ArrayList<NaviLatLng>(); //终点
    private List<NaviLatLng>                           mWayPointList    = new ArrayList<NaviLatLng>(); //途经点

    private ContinueBean                               mNewContinueBean;                               //处理过的对象
    private ContinueBean.ActivityLineNode              activityLineNode;
    private int                                        ranking          = 0;

    private WrapAdapter<ContinueBean.ActivityLineNode> mAdapter         = null;
    private List<ContinueBean.ActivityLineNode>        datalist         = new ArrayList<>();
    private ListView                                   mListView;
    private RelativeLayout                             mMapRootLayout;
    private FileUploadPresenter                        mUploadPresenter = null;

    private LatLng                                     mLocationLat;

    @Override
    protected void setUpViews() {
        mapView = (MapView) findViewById(R.id.map);
        mListView = (ListView) findViewById(R.id.list);
        mMapRootLayout = (RelativeLayout) findViewById(R.id.mapRootLayout);
        mNewContinueBean = (ContinueBean) getIntent()
            .getSerializableExtra(Constants.IntentParams.DATA);
        ranking = getIntent().getIntExtra(Constants.IntentParams.ID, 0);
        activityLineNode = (ContinueBean.ActivityLineNode) getIntent()
            .getSerializableExtra(Constants.IntentParams.ID2);
        mContinuePresenter = new ContinuePresenter();
        mContinuePresenter.attachView(this);
        //文件上传Presenter;
        mUploadPresenter = new FileUploadPresenter();
        mUploadPresenter.attachView(fileUploadView);
        init();
        int index = 0;
        int size = mNewContinueBean.data.activityLineNode.size();
        for (ContinueBean.ActivityLineNode item : mNewContinueBean.data.activityLineNode) {
            item.orders = index;
            LatLng latLng = new LatLng(Double.parseDouble(item.lat), Double.parseDouble(item.lng));
            if (index == 0) {
                // aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f));
                updateMarkerState(R.drawable.amap_marker_start, "起");
                item.title = "起点";
            } else if (index == size - 1) {
                updateMarkerState(R.drawable.amap_marker_end, "终");
                item.title = "终点";
            } else {
                updateMarkerState(R.drawable.amap_marker_away, "" + index);
                item.title = "途经点" + index;
            }
            Marker m = drawMarkerInAMap(latLng);
            m.setIcon(
                BitmapDescriptorFactory.fromBitmap(convertViewToBitmap(mContext, markerPosView)));
            index++;
        }

        mAdapter = new WrapAdapter<ContinueBean.ActivityLineNode>(mContext, datalist,
            R.layout.marker_point_item) {

            private TextView       address;
            private TextView       name;
            private RelativeLayout currentPosLayout;

            @Override
            public void convert(final ViewHolder helper, final ContinueBean.ActivityLineNode item) {
                int position = helper.getPosition();
                currentPosLayout = helper.getView(R.id.currentPosLayout);
                name = helper.getView(R.id.name);
                String nameTxt = "";
                int currentPosDrawableId = R.drawable.amap_marker_away;
                if (position == 0) {
                    nameTxt = "起";
                    currentPosDrawableId = R.drawable.amap_marker_start;
                } else if (position == datalist.size() - 1) {
                    nameTxt = "终";
                    currentPosDrawableId = R.drawable.amap_marker_end;
                } else {
                    nameTxt = "" + (position);
                    currentPosDrawableId = R.drawable.amap_marker_away;
                }
                name.setText(nameTxt);
                currentPosLayout.setBackgroundResource(currentPosDrawableId);

                address = helper.getView(R.id.address);
                address.setText(item.address);
            }
        };
        mListView.setAdapter(mAdapter);

        datalist.addAll(mNewContinueBean.data.activityLineNode);
        mAdapter.notifyDataSetChanged();

        List<ContinueBean.ActivityLineNode> activitys = new ArrayList<>();
        activitys.addAll(mNewContinueBean.data.activityLineNode);
        activitys.add(ranking, activityLineNode);
        driveRoute(activitys);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);

        text.setText("线路预览");

        function.setTextResource("完成", R.color.white, 16);

        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mNewContinueBean == null) {
                    CommonUtils.showToast(mContext, "正在加载线路，无法编辑，请稍候重试！");
                    return;
                }
                setLoadingIndicator(true);

                List<ContinueBean.ActivityLineNode> activityLineNode = mNewContinueBean.data.activityLineNode;
                int size = activityLineNode.size() / 2;
                for (int i = 0; i < activityLineNode.size(); i++) {
                    if (i == 0) {
                        mNewContinueBean.data.activityLine.mileage = 0;
                        activityLineNode.get(0).mileage = 0;
                    } else {
                        if (size == i) {
                            mLocationLat = new LatLng(
                                Double.parseDouble(activityLineNode.get(i).lat),
                                Double.parseDouble(activityLineNode.get(i).lng));
                        }
                        double mCucrentMile = AMapUtils.calculateLineDistance(
                            new LatLng(Double.parseDouble(activityLineNode.get(i - 1).lat),
                                Double.parseDouble(activityLineNode.get(i - 1).lng)),
                            new LatLng(Double.parseDouble(activityLineNode.get(i).lat),
                                Double.parseDouble(activityLineNode.get(i).lng))) / 1000;
                        activityLineNode.get(i).mileage = mCucrentMile;
                    }
                    mNewContinueBean.data.activityLine.mileage += activityLineNode.get(i).mileage;
                }
                aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(getLatLngBounds(), 40));
                aMap.getMapScreenShot(ContinueMapActivity.this);
            }
        });
    }

    //根据中心点和自定义内容获取缩放bounds
    private LatLngBounds getLatLngBounds() {

        LatLngBounds.Builder b = LatLngBounds.builder();
        int size = mNewContinueBean.data.activityLineNode.size();
        if (mLocationLat != null) {
            for (int i = 0; i < size; i++) {
                ContinueBean.ActivityLineNode rideData = mNewContinueBean.data.activityLineNode
                    .get(i);
                LatLng p = new LatLng(Double.parseDouble(rideData.lat),
                    Double.parseDouble(rideData.lng));
                LatLng p1 = new LatLng((mLocationLat.latitude * 2) - p.latitude,
                    (mLocationLat.longitude * 2) - p.longitude);
                b.include(p);
                b.include(p1);
            }
        }
        return b.build();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_continue_ride_line);
        mapView.onCreate(savedInstanceState);
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
        aMap.getUiSettings().setScrollGesturesEnabled(true);
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示

        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
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
            //aMap.moveCamera(CameraUpdateFactory.zoomTo(14f));
        }

    };

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
            // mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
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

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mContinuePresenter != null) {
            mContinuePresenter.detachView();
        }
        mapView.onDestroy();

    }

    /**
     * 根据起点、途经点、终点进行线路规划
     *
     * @param items
     */
    private void driveRoute(List<ContinueBean.ActivityLineNode> items) {

        mStartPoints.clear();
        mEndPoints.clear();
        mWayPointList.clear();

        int size = items.size();

        if (size < 2) {
            return;
        }

        int index = 0;
        for (ContinueBean.ActivityLineNode item : items) {

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

        String lat = mGlobalContext.getLattitude();
        String lng = mGlobalContext.getLongitude();

        if (TextUtil.isEmpty(lat)) {
            lat = "29.03";
        }
        if (TextUtil.isEmpty(lng)) {
            lng = "106.12";
        }
        mLocationLat = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(getLatLngBounds(), 40));

        //aMap.moveCamera(CameraUpdateFactory.zoomTo(12f));

    }

    /**
     * Marker的布局
     *
     * @param drawableId
     * @param name
     */
    private void updateMarkerState(int drawableId, String name) {
        markerPosView = LayoutInflater.from(mContext).inflate(R.layout.marker_pos_layout, null,
            false);
        TextView markerPosName = (TextView) markerPosView.findViewById(R.id.name);
        RelativeLayout currentPosLayout = (RelativeLayout) markerPosView
            .findViewById(R.id.currentPosLayout);
        currentPosLayout.setBackgroundResource(drawableId);
        markerPosName.setText(name);
    }

    /**
     * 生成Marker
     *
     * @param locationLatLng
     * @return
     */
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

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void DetailsSucceed(ContinueBean baseBean) {

    }

    @Override
    public void DetaSucceed(BaseBean baseBean) {
        CommonUtils.showToast(mContext, "上传成功");
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void detailsDataFeated(String msg) {
        CommonUtils.showToast(mContext, msg);
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void onMapScreenShot(Bitmap bitmap) {
        ScreenShotHelper.saveScreenShot(bitmap, mMapRootLayout, mapView, mHandler);
    }

    @Override
    public void onMapScreenShot(Bitmap bitmap, int i) {

    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            int what = msg.what;

            if (what == WHAT_VALUE3) {

                File file = (File) msg.obj;

                uploadImage(file);
            }

        }
    };

    private void uploadImage(File file) {

        mUploadPresenter.uploadAvatarFile(RequestBody.create(MediaType.parse("image/png"), file),
            RequestBody.create(MediaType.parse("text/plain"), "avatar"),
            RequestBody.create(MediaType.parse("text/plain"), ""),
            RequestBody.create(MediaType.parse("text/plain"), IOUtils.getToken(getContext())));
    }

    private FileUploadView<DataItem> fileUploadView = new FileUploadView<DataItem>() {
        @Override
        public void resultData(DataItem item) {
            int code = item.code;
            if (code == Constants.NetworkStatusCode.SUCCESS) {
                CommonUtils.showToast(mContext, "成功");
                mNewContinueBean.data.activityLine.cover = item.resp.data.path;
                Gson gson = new Gson();
                String json = gson.toJson(mNewContinueBean.data);
                mContinuePresenter.getMergeLine(IOUtils.getToken(mContext), json);
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

}
