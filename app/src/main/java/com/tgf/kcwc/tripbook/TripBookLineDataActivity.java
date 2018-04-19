package com.tgf.kcwc.tripbook;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.tgf.kcwc.R;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.MarkerPosItem;
import com.tgf.kcwc.mvp.model.LineDataModel;
import com.tgf.kcwc.mvp.model.TripBookMapModel;
import com.tgf.kcwc.mvp.presenter.LineDataPresenter;
import com.tgf.kcwc.mvp.presenter.TripBookMapPresenter;
import com.tgf.kcwc.mvp.view.LineDataView;
import com.tgf.kcwc.mvp.view.TripMapView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.chart.Line;
import com.tgf.kcwc.view.chart.TrendView;
import com.tgf.kcwc.view.chart.Util;
import com.tgf.kcwc.view.mapview.DrivingRouteSearchOverlay;

import java.util.ArrayList;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @anthor noti
 * @time 2017/10/16
 * @describle
 */
public class TripBookLineDataActivity extends BaseActivity implements TripMapView,RouteSearch.OnRouteSearchListener{
    private KPlayCarApp kPlayCarApp;
    private MapView mapView;
    private AMap aMap;
    //地图标记位置
    private View markerPosView;
    //时间+距离
    private TextView dateTv;
    //速度，海拔，压弯角度
    private TextView speedTv, elevationTv, angleTv;
    private TrendView elevationView, speedTrendView;
    private SeekBar seekBar;
    //标记点数据
    private ArrayList<TripBookMapModel.MapItem> mMapItemList;
    //标记点经纬度
    private ArrayList<LatLng> latLngList = new ArrayList<>();
    //标记点经纬度点
    private ArrayList<LatLonPoint> latLonPointList = new ArrayList<>();
    //分时图数据
    private ArrayList<TripBookMapModel.NodeItem> mNodeList;
    //分时图经纬度
    private ArrayList<LatLng> nodeLatLngList = new ArrayList<>();
    private TripBookMapModel.MapItem currentMapItem;
    private RouteSearch mRouteSearch;
    private DriveRouteResult mDriveRouteResult;
    //标记点下标
    private int index;
    private int mBookId;
    private String lng, lat;
    private Marker marker;

    @Override
    protected void setUpViews() {
        mBookId = getIntent().getIntExtra(Constants.IntentParams.ID, -1);
        index = getIntent().getIntExtra(Constants.IntentParams.INDEX, -1);
        lng = getIntent().getStringExtra(Constants.IntentParams.LNG);
        lat = getIntent().getStringExtra(Constants.IntentParams.LAT);

        dateTv = (TextView) findViewById(R.id.dateTv);
        speedTv = (TextView) findViewById(R.id.speedTv);
        elevationTv = (TextView) findViewById(R.id.elevationTv);
        angleTv = (TextView) findViewById(R.id.angleTv);
        speedTrendView = (TrendView) findViewById(R.id.speedTrendView);
        //获取线路数据
        TripBookMapPresenter mTripBookMapPresenter = new TripBookMapPresenter();
        mTripBookMapPresenter.attachView(this);
        kPlayCarApp = (KPlayCarApp) getApplication();
        mTripBookMapPresenter.getTripmapDetail(mBookId, kPlayCarApp.getLattitude(), kPlayCarApp.getLongitude());
//        lineDataPresenter.getLineData(IOUtils.getToken(this),kPlayCarApp.getLongitude(),kPlayCarApp.getLattitude(),mBookId,lat,lng);

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_book_line_data);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
//                CommonUtils.showToast(getContext(),marker.getSnippet()+"onMarkerClick");
                TripBookMapModel.MapItem mapItem = mMapItemList.get(Integer.parseInt(marker.getSnippet()));
                marker.showInfoWindow();
                setCurrentMap(mapItem);
                return true;
            }
        });// 设置点击marker事件监听器
        aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                int pos= (int) marker.getObject();
                TripBookMapModel.MapItem mapItem = mMapItemList.get(pos);
                Intent toThread = new Intent(getContext(),TripBookThreadActivity.class);
                toThread.putExtra(Constants.IntentParams.ID,mapItem.id);
                toThread.putExtra(Constants.IntentParams.TITLE,currentMapItem.adds);
                startActivity(toThread);
            }
        });// 设置点击infoWindow事件监听器
        aMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {

                View infoWindow =
                        View.inflate(getContext(),R.layout.tripmap_infowindow,null);
                TextView infotitleTv = (TextView) infoWindow.findViewById(R.id.inofowindow_titletv);
                infotitleTv.setText(marker.getTitle());
                return infoWindow;
            }

            @Override
            public View getInfoContents(Marker marker) {

                View infoWindow =
                        View.inflate(getContext(),R.layout.tripmap_infowindow,null);
                TextView infotitleTv = (TextView) infoWindow.findViewById(R.id.inofowindow_titletv);
                infotitleTv.setText(marker.getTitle());
                return null;
            }
        });// 设置自定义InfoWindow样式
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        text.setText("分时图");
        backEvent(back);
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
        return this;
    }
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            updateScrollIcon(nodeLatLngList,message.arg1);
            return false;
        }
    });
    //滑动图标
    private void updateScrollIcon(ArrayList<LatLng> latLngList, int index){
        MarkerOptions options = new MarkerOptions();
        options.position(latLngList.get(index));
        options.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(),R.drawable.icon_scroll_map)));
        //水平居中，垂直下对齐
        options.anchor(0.5f,0.5f);
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
//        options.setFlat(true);//设置marker平贴地图效果
        marker = aMap.addMarker(options);
    }
    private void updateMarkerState(int drawableId, int pos) {
        markerPosView = LayoutInflater.from(mContext).inflate(R.layout.marker_pos_tripmap, null,
                false);
        TextView markerPosName = (TextView) markerPosView.findViewById(R.id.name);
        RelativeLayout layoutRl = (RelativeLayout) markerPosView.findViewById(R.id.currentPosLayout);
        layoutRl.setBackgroundResource(drawableId);
        if (0 == pos) {
            markerPosName.setText("起");
        } else if (mMapItemList.size() - 1 == pos) {
            markerPosName.setText("终");
        } else {
            markerPosName.setText(pos + "");
        }
    }

    private Marker drawMarkerInAMap(LatLng locationLatLng, int pos) {
        MarkerOptions options = new MarkerOptions();
        options.position(locationLatLng).title("帖子" + mMapItemList.get(pos).totalCount).snippet(pos + "");
        options.icon(BitmapDescriptorFactory.fromView(markerPosView));
        Marker marker = aMap.addMarker(options);
        marker.setObject(pos);
        if (index != -1) {
            if (index == pos) {
                marker.showInfoWindow();
            }
        } else {
            marker.showInfoWindow();
        }

        return marker;
    }

    private void setCurrentMap(TripBookMapModel.MapItem endMap) {
        currentMapItem = endMap;
//        addressNameTv.setText(currentMapItem.adds);
//        addressDistanceTv.setText(currentMapItem.distance+"km");
    }

    @Override
    public void showTripMapdetail(ArrayList<TripBookMapModel.MapItem> mapItemList) {
        mMapItemList = mapItemList;
//        LatLngBounds.Builder b = LatLngBounds.builder();
        int i = 0;

        for (i = 0; i < mMapItemList.size(); i++) {
            //地图打marker点
            TripBookMapModel.MapItem mapItem = mMapItemList.get(i);
            MarkerPosItem markerPosItem = new MarkerPosItem();
            markerPosItem.address = mapItem.adds;
            LatLng latLng = new LatLng(Double.parseDouble(mapItem.lat),
                    Double.parseDouble(mapItem.lng));
            markerPosItem.latLng = latLng;
//            b.include(latLng);
            latLngList.add(latLng);
            updateMarkerState(R.drawable.amap_marker_away, i);
            drawMarkerInAMap(markerPosItem.latLng, i);
            latLonPointList.add(new LatLonPoint(Double.parseDouble(mapItem.lat), Double.parseDouble(mapItem.lng)));
        }
//        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(b.build(), 100));

        if (index == -1) {
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLngList.get(latLngList.size() - 1)));
            TripBookMapModel.MapItem endMap = mMapItemList.get(mapItemList.size() - 1);
            setCurrentMap(endMap);
        } else {
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLngList.get(index)));
            TripBookMapModel.MapItem endMap = mMapItemList.get(index);
            setCurrentMap(endMap);
        }
        //设置放大缩小级别
        aMap.moveCamera((CameraUpdateFactory.zoomTo(13)));
    }

    @Override
    public void showTripNodeList(ArrayList<TripBookMapModel.NodeItem> nodeList) {
        mNodeList = nodeList;
        if (mNodeList == null) {
            return;
        }
        int size = latLonPointList.size();

        if (size < 2) {
            return;
        }
        speedTrendView.withLine(new Line(mNodeList))
                .withPrevClose(40.2)
                .withDisplayFrom(0)
                .withDisplayNumber(mNodeList.size())
                .show();
        speedTrendView.setOnInvalidateCallBack(new TrendView.OnInvalidateCallBack() {
            @Override
            public void onInvalidateCall(final int index) {
                TripBookMapModel.NodeItem nodeItem = mNodeList.get(index);
                //时间+距离
//        dateTv.setText();
                //速度
                speedTv.setText(nodeItem.speed);
                //海拔
                elevationTv.setText(nodeItem.altitude+"");
                //压弯角度
//        angleTv.setText(nodeItem);
                if (null != marker)
                    marker.remove();
                updateScrollIcon(nodeLatLngList,index);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Message msg = new Message();
//                        msg.what = 0;
//                        msg.arg1 = index;
//                        Log.e("TAG", "run: "+msg.arg1 );
//                        handler.sendMessage(msg);
//                    }
//                });
            }
        });

        Observable.create(new Observable.OnSubscribe<ArrayList<TripBookMapModel.NodeItem>>() {

            @Override
            public void call(Subscriber<? super ArrayList<TripBookMapModel.NodeItem>> subscriber) {
                for (int j = 0; j < mNodeList.size(); j++) {
                    TripBookMapModel.NodeItem item = mNodeList.get(j);
                    nodeLatLngList.add(new LatLng(Double.parseDouble(item.lat),
                            Double.parseDouble(item.lng)));
                }

                updateScrollIcon(nodeLatLngList,0);

                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ArrayList<TripBookMapModel.NodeItem>>() {
            @Override
            public void onCompleted() {
                aMap.addPolyline(new PolylineOptions().addAll(nodeLatLngList).geodesic(true).color(
                        mRes.getColor(R.color.style_bg1)));
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ArrayList<TripBookMapModel.NodeItem> nodeItems) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
        if (i == AMapException.CODE_AMAP_SUCCESS) {
            if (driveRouteResult != null && driveRouteResult.getPaths() != null) {
                if (driveRouteResult.getPaths().size() > 0) {
                    mDriveRouteResult = driveRouteResult;
                    final DrivePath drivePath = mDriveRouteResult.getPaths().get(0);
                    DrivingRouteSearchOverlay drivingRouteOverlay = new DrivingRouteSearchOverlay(
                            mContext, aMap, drivePath, mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), null);
                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                }
            }
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
}
