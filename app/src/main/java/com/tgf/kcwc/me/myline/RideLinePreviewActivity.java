package com.tgf.kcwc.me.myline;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.NaviLatLng;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.RideDataItem;
import com.tgf.kcwc.mvp.model.RideLinePreviewModel;
import com.tgf.kcwc.mvp.presenter.RideDataPresenter;
import com.tgf.kcwc.mvp.view.RideDataView;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.wrapper.AMapWrapperListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Jenny
 * Date:2017/5/12
 * E-mail:fishloveqin@gmail.com
 * <p>
 * 线路预览
 */

public class RideLinePreviewActivity extends BaseActivity
        implements RideDataView<RideLinePreviewModel> {
    protected MapView aMapView;
    protected ImageButton titleBarBack;
    protected TextView titleBarText;
    protected ImageView editLineView;
    protected ImageView deleteLineView;
    private AMap aMap;

    private int rideId;

    private RideDataPresenter mPresenter, mDeletePresenter;
    private View markerPosView;
    AMapNavi mAMapNavi;

    private AMapNaviPath naviPath;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    protected void setUpViews() {
        initView();


    }

    private RideDataView rideDeleteView = new RideDataView() {
        @Override
        public void showDatas(Object o) {

            CommonUtils.showToast(mContext, "删除成功!");
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
    };

    private void updateMarkerState(int drawableId, String name) {
        markerPosView = LayoutInflater.from(mContext).inflate(R.layout.marker_pos_layout, null,
                false);
        TextView markerPosName = (TextView) markerPosView.findViewById(R.id.name);
        markerPosView.setBackgroundResource(drawableId);
        markerPosName.setText(name);

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

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(false);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        aMap.getUiSettings().setScrollGesturesEnabled(true);
    }

    /**
     * 初始化
     */
    private void init() {
        if (aMap == null) {
            aMap = aMapView.getMap();
            setUpMap();
        }

        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(mapWrapperListener);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isTitleBar = false;
        rideId = getIntent().getIntExtra(Constants.IntentParams.ID, -1);
        mPresenter = new RideDataPresenter();
        mPresenter.attachView(this);
        mDeletePresenter = new RideDataPresenter();
        mDeletePresenter.attachView(rideDeleteView);
        super.setContentView(R.layout.activity_ride_line_preview);
        aMapView.onCreate(savedInstanceState);
    }

    private RideLinePreviewModel mPrevModel;

    @Override
    public void showDatas(RideLinePreviewModel rideLinePreviewModel) {

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
            m.setIcon(
                    BitmapDescriptorFactory.fromBitmap(convertViewToBitmap(mContext, markerPosView)));
            index++;
        }

        driveRoute(items);
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

    private void initView() {
        aMapView = (MapView) findViewById(R.id.map);
        titleBarBack = (ImageButton) findViewById(R.id.title_bar_back);
        titleBarText = (TextView) findViewById(R.id.title_bar_text);
        editLineView = (ImageView) findViewById(R.id.editLineView);
        deleteLineView = (ImageView) findViewById(R.id.deleteLineView);
        backEvent(titleBarBack);
        titleBarText.setText("线路预览");
        editLineView.setOnClickListener(this);
        deleteLineView.setOnClickListener(this);
        init();

    }


    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {

            case R.id.deleteLineView:
                deleteLine();
                break;

            case R.id.editLineView:
                editLine();
                break;
        }


    }

    private void editLine() {
        if (mPrevModel == null) {

            CommonUtils.showToast(mContext, "正在加载线路，无法编辑，请稍候重试！");
            return;
        }

        Intent intent = new Intent();
        intent.setClass(mContext, CreateRideLineActivity.class);
        intent.putExtra(Constants.IntentParams.DATA, mPrevModel);
        startActivity(intent);
    }

    private void deleteLine() {

        if (mPrevModel == null) {

            CommonUtils.showToast(mContext, "正在加载线路，无法删除，请稍候重试！");
            return;
        }

        mDeletePresenter.deletePlanLine(rideId+"", IOUtils.getToken(mContext));


    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        aMapView.onResume();

        mPresenter.loadRidePlanDatas(rideId + "", IOUtils.getToken(mContext));
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        aMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        aMapView.onSaveInstanceState(outState);
    }

    // 起点终点列表
    private ArrayList<NaviLatLng> mStartPoints = new ArrayList<NaviLatLng>();
    private ArrayList<NaviLatLng> mEndPoints = new ArrayList<NaviLatLng>();
    private List<NaviLatLng> mWayPointList = new ArrayList<NaviLatLng>(); //途经点

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

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        aMapView.onDestroy();

    }

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

    private ArrayList<Polyline> mAllPolylines = new ArrayList<Polyline>();
}
