package com.tgf.kcwc.tripbook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.view.RouteOverLay;
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
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.TripBookMapModel;
import com.tgf.kcwc.mvp.presenter.TripBookMapPresenter;
import com.tgf.kcwc.mvp.view.TripMapView;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MapNavPopWindow;
import com.tgf.kcwc.view.mapview.DrivingRouteSearchOverlay;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Auther: Scott
 * Date: 2017/5/17 0017
 * E-mail:hekescott@qq.com
 */

public class TripBookMapActivity extends BaseActivity
        implements TripMapView, RouteSearch.OnRouteSearchListener {

    private MapView                             mapView;
    private View                                markerPosView;
    private TripBookMapPresenter                mTripBookMapPresenter;
    private ArrayList<TripBookMapModel.MapItem> mMapItemList;
    private int                                 mBookId         = 51;
    private AMapNavi                            mAMapNavi;
    private AMap                                aMap;
    private RouteOverLay                        mRouteOverLay;
    private LinkedList<Marker>                  mDatas          = new LinkedList<Marker>();
    private RouteSearch                         mRouteSearch;
    private ArrayList<LatLonPoint>              latLonPointList = new ArrayList<>();
    private ArrayList<LatLng>                   latLngList      = new ArrayList<>();
    private ArrayList<LatLng>                   nodeLatLngList      = new ArrayList<>();
    private DriveRouteResult                    mDriveRouteResult;
    private ArrayList<TripBookMapModel.NodeItem> mNodeList;
    private TextView addressNameTv;
    private TextView addressDistanceTv;
    private KPlayCarApp kPlayCarApp;
    private TripBookMapModel.MapItem currentMapItem;
    private MapNavPopWindow mapNavPopWindow;
    private Intent fromIntent;
    private int index;
    @Override
    protected void setUpViews() {
        fromIntent = getIntent();
        kPlayCarApp = (KPlayCarApp) getApplication();
        mBookId = fromIntent.getIntExtra(Constants.IntentParams.ID,0);
        index = fromIntent.getIntExtra(Constants.IntentParams.INDEX,-1);
        mTripBookMapPresenter = new TripBookMapPresenter();
        mTripBookMapPresenter.attachView(this);
        mTripBookMapPresenter.getTripmapDetail(mBookId,kPlayCarApp.getLattitude(),kPlayCarApp.getLongitude());
        addressNameTv = (TextView) findViewById(R.id.tripmap_addressname);
        addressDistanceTv = (TextView) findViewById(R.id.tripbookmap_addresstv);
        findViewById(R.id.tripbook_around_searchrl).setOnClickListener(this);
        findViewById(R.id.tripbook_around_couponrl).setOnClickListener(this);
        findViewById(R.id.tripbook_around_tongxrl).setOnClickListener(this);
        findViewById(R.id.tripmap_navibtn).setOnClickListener(this);
        mapNavPopWindow = new MapNavPopWindow(mContext);
        mapNavPopWindow.setOnClickListener(this);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        text.setText("路书地图");
        backEvent(back);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripbookmap);
        mapView = (MapView) findViewById(R.id.tripbook_map);
        mapView.onCreate(savedInstanceState);
        initMap();
    }

    private void initMap() {
        aMap = mapView.getMap();
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setCompassEnabled(true);
        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mRouteOverLay = new RouteOverLay(aMap, null, getApplicationContext());
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
    }

    private void updateMarkerState(int drawableId, int pos) {
        markerPosView = LayoutInflater.from(mContext).inflate(R.layout.marker_pos_tripmap, null,
                false);
        TextView  markerPosName = (TextView) markerPosView.findViewById(R.id.name);
        RelativeLayout    layoutRl = (RelativeLayout) markerPosView.findViewById(R.id.currentPosLayout);
        layoutRl.setBackgroundResource(drawableId);
        if (0 == pos) {
            markerPosName.setText("起");
        } else if (mMapItemList.size()-1 == pos) {
            markerPosName.setText("终");
        } else {
            markerPosName.setText(pos + "");
        }
    }
    private Marker drawMarkerInAMap(LatLng locationLatLng,int pos) {
        MarkerOptions options = new MarkerOptions();
        options.position(locationLatLng).title("帖子"+mMapItemList.get(pos).totalCount).snippet(pos+"");
        options.icon(BitmapDescriptorFactory.fromView(markerPosView));
        Marker marker = aMap.addMarker(options);
        marker.setObject(pos);
        if(index!=-1){
            if(index==pos){
                marker.showInfoWindow();
            }
        }else {
            marker.showInfoWindow();
        }

        return marker;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tripbook_around_searchrl:
                Intent toTripBookSearchAct = new Intent(getContext(), TripBookAroudActivity.class);
                toTripBookSearchAct.putExtra(Constants.IntentParams.ID, currentMapItem.id);
                toTripBookSearchAct.putExtra(Constants.IntentParams.INTENT_TYPE,1);
                toTripBookSearchAct.putExtra(Constants.IntentParams.TITLE,currentMapItem.adds);
                startActivity(toTripBookSearchAct);
                break;
            case R.id.tripbook_around_couponrl:
                Intent toTripBookCuponAct = new Intent(getContext(), TripBookAroudActivity.class);
                toTripBookCuponAct.putExtra(Constants.IntentParams.ID,  currentMapItem.id);
                toTripBookCuponAct.putExtra(Constants.IntentParams.INTENT_TYPE,2);
                toTripBookCuponAct.putExtra(Constants.IntentParams.TITLE,currentMapItem.adds);
                startActivity(toTripBookCuponAct);
                break;
            case R.id.tripbook_around_tongxrl:
                Intent toTripBookTongAct = new Intent(getContext(), TripBookAroudActivity.class);
                toTripBookTongAct.putExtra(Constants.IntentParams.ID, currentMapItem.id);
                toTripBookTongAct.putExtra(Constants.IntentParams.INTENT_TYPE,3);
                toTripBookTongAct.putExtra(Constants.IntentParams.TITLE,currentMapItem.adds);

                startActivity(toTripBookTongAct);
                break;
            case R.id.tripmap_navibtn:
                //导航
                mapNavPopWindow.show(TripBookMapActivity.this);
                break;
            case R.id.cancel:
                mapNavPopWindow.dismiss();
                break;
            case R.id.aMap:
                try {
                    LatLng startGLat = new LatLng(Double.parseDouble(kPlayCarApp.latitude),Double.parseDouble(kPlayCarApp.longitude));
                    LatLng endGLat = new LatLng(Double.parseDouble(currentMapItem.lat),Double.parseDouble(currentMapItem.lng));
                    kPlayCarApp.getLattitude();

                    URLUtil.launcherInnerRouteAMap(TripBookMapActivity.this, currentMapItem.adds, startGLat,
                            endGLat);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.baiduMap:
                try {
                    LatLng startBLat = new LatLng(Double.parseDouble(kPlayCarApp.latitude),Double.parseDouble(kPlayCarApp.longitude));
                    LatLng endBLat = new LatLng(Double.parseDouble(currentMapItem.lat),Double.parseDouble(currentMapItem.lng));
                    URLUtil.launcherInnerRouteBaidu(TripBookMapActivity.this, currentMapItem.adds, startBLat,
                            endBLat);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
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
    public void showTripMapdetail(ArrayList<TripBookMapModel.MapItem> mapItemList) {
        mMapItemList = mapItemList;
        LatLngBounds.Builder b = LatLngBounds.builder();
        int i =0;

        for ( i = 0; i < mMapItemList.size(); i++) {
            //地图打marker点
            TripBookMapModel.MapItem mapItem = mMapItemList.get(i);
            MarkerPosItem markerPosItem = new MarkerPosItem();
            markerPosItem.address = mapItem.adds;
            LatLng latLng = new LatLng(Double.parseDouble(mapItem.lat),
                    Double.parseDouble(mapItem.lng));
            markerPosItem.latLng = latLng;
            b.include(latLng);
            latLngList.add(latLng);
            updateMarkerState(R.drawable.amap_marker_away, i);
            drawMarkerInAMap(markerPosItem.latLng,i);
            latLonPointList.add(new LatLonPoint(Double.parseDouble(mapItem.lat), Double.parseDouble(mapItem.lng)));
        }

        if(index==-1){
            TripBookMapModel.MapItem endMap= mMapItemList.get(mapItemList.size()-1);
            setCurrentMap(endMap);
        }else{
            TripBookMapModel.MapItem endMap= mMapItemList.get(index);
            setCurrentMap(endMap);
        }

        if(mapItemList.size()==1){
            if(index==-1){
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLngList.get(latLngList.size()-1)));
            }else{
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLngList.get(index)));
            }
        }else {
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(b.build(),100));

        }

    }

    private void setCurrentMap(TripBookMapModel.MapItem endMap) {
        currentMapItem = endMap;

        addressNameTv.setText(currentMapItem.adds);
        addressDistanceTv.setText(currentMapItem.distance+"km");
    }

    @Override
    public void showTripNodeList(ArrayList<TripBookMapModel.NodeItem> nodeList) {
        mNodeList = nodeList;

        if(mNodeList==null){
            return;
        }
        int size = latLonPointList.size();

        if (size < 2) {
            return;
        }

        Observable.create(new Observable.OnSubscribe<ArrayList<TripBookMapModel.NodeItem>>(){

            @Override
            public void call(Subscriber<? super ArrayList<TripBookMapModel.NodeItem>> subscriber) {
                for(int j=0;j<mNodeList.size();j++){
                    TripBookMapModel.NodeItem item = mNodeList.get(j);
                    nodeLatLngList.add( new LatLng(Double.parseDouble(item.lat),
                            Double.parseDouble(item.lng)));
                }
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

    private void driveRoute() {
        int size = latLonPointList.size();

        if (size < 2) {
            return;
        }
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(latLonPointList.get(0),
                latLonPointList.get(size - 1));
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo,
                RouteSearch.DrivingDefault, latLonPointList.subList(1, size - 1), null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
        mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
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



    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mDriveRouteResult = result;
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
    public void onRideRouteSearched(RideRouteResult result, int errorCode) {

    }

    private LatLngBounds getLatLngBounds(List<LatLng> latLngList) {
        LatLngBounds.Builder b = LatLngBounds.builder();
        for (int i = 0; i < latLngList.size(); i++) {
            b.include(latLngList.get(i));
        }
        return b.build();
    }


}
