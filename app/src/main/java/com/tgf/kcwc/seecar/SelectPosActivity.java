package com.tgf.kcwc.seecar;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.LocationUtil;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/2/23 0023
 * E-mail:hekescott@qq.com
 */

public class SelectPosActivity extends BaseActivity
                               implements LocationSource, TextWatcher, Inputtips.InputtipsListener,
                               TextView.OnEditorActionListener, PoiSearch.OnPoiSearchListener, AMap.OnCameraChangeListener {

    private static  final String KEY_INTENT_SELECTPOS ="slectpos";
    private MapView              mapView;
    private AMap                 aMap;
    private AMapLocationClient   mlocationClient;
    private AutoCompleteTextView etSearch;
    private String               mCity = "重庆";
    private String               mKeyWord;
    private Marker               selectMarker;
    String selecttitle = "我的位置";
    private Intent fromIntent;
    private PlacePoint selectPoint;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    protected void setUpViews() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectpos);

        mapView = (MapView) findViewById(R.id.selectpos_map);
        findViewById(R.id.selectpos_cancl).setOnClickListener(this);
        etSearch = (AutoCompleteTextView) findViewById(R.id.etsearch);
        etSearch.addTextChangedListener(this);
        etSearch.setOnEditorActionListener(this);
        mapView.onCreate(savedInstanceState);
        setMap();
        fromIntent = getIntent();
       selectPoint =  fromIntent.getParcelableExtra(KEY_INTENT_SELECTPOS);
        mCity = selectPoint.city;
//        aMap.moveCamera(CameraUpdateFactory
//                .newCameraPosition(new CameraPosition(latLng, 18, 30, 30)));

    }

    LocationSource.OnLocationChangedListener mListener;

    private void setMap() {
        aMap = mapView.getMap();
        UiSettings uiSetiting = aMap.getUiSettings();
        uiSetiting.setMyLocationButtonEnabled(true);
        // 设置定位监听
        aMap.setLocationSource(this);
        aMap.setMyLocationEnabled(true);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        aMap.setOnCameraChangeListener(this);
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
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        deactivate();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.selectpos_cancl:

                selectPoint.address = selecttitle;
                selectPoint.myLalng = selctLatLng;
                fromIntent.putExtra(KEY_INTENT_SELECTPOS, selectPoint);
                setResult(RESULT_OK,fromIntent);
                finish();
                break;
            default:
                break;
        }
    }
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            //初始化定位
            mlocationClient = new AMapLocationClient(SelectPosActivity.this);
            //初始化定位参数
            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mlocationClient.setLocationListener(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation amapLocation) {
                    if (mListener != null && amapLocation != null) {
                        if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                            mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                            Logger.d("显示系统小蓝点");
                        } else {
                            String errText = "定位失败," + amapLocation.getErrorCode() + ": "
                                             + amapLocation.getErrorInfo();
                            Log.e("AmapErr", errText);
                        }
                    }
                }
            });
            //设置为高精度定位模式
            mLocationOption
                .setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setOnceLocation(true);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除

        }
        mlocationClient.startLocation();//启动定位
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable newText) {
        if (!TextUtils.isEmpty(newText)) {
            InputtipsQuery inputquery = new InputtipsQuery(newText.toString(), mCity);
            Inputtips inputTips = new Inputtips(this, inputquery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        }

    }

    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
            List<String> listString = new ArrayList<String>();
            for (int i = 0; i < tipList.size(); i++) {
                listString.add(tipList.get(i).getName());
            }
            ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.listitem_search_text, listString);
            etSearch.setAdapter(aAdapter);
            aAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE
            || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode()
                && KeyEvent.ACTION_DOWN == event.getAction())) {
            //处理事件

            if (!TextUtils.isEmpty(etSearch.getText())) {
                mKeyWord = etSearch.getText() + "";
                doSearchQuery(mKeyWord);
            } else {
                CommonUtils.showToast(this, "请输入关键子");
            }
        }
        return false;
    }

    private void doSearchQuery(@Nullable String keyWord) {
        isOnpoiSearch = true;
        int currentPage = 0;
        PoiSearch.Query query = new PoiSearch.Query(keyWord, "", mCity);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();

    }

private  boolean isOnpoiSearch = false;

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {
                ArrayList<PoiItem> poiItems = result.getPois();
                if (poiItems != null && poiItems.size() != 0) {
                    PoiItem poiItem = poiItems.get(0);
                    LatLonPoint latLonPoint = poiItem.getLatLonPoint();
                    LatLng latLng = new LatLng(latLonPoint.getLatitude(),
                        latLonPoint.getLongitude());
                    selecttitle = poiItem.getTitle();
                    aMap.clear();
                    addMarkersToMap();
                    aMap.moveCamera(CameraUpdateFactory
                        .newCameraPosition(new CameraPosition(latLng, 18, 30, 30)));
                }
            }

        }
        isOnpoiSearch = false;

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (selectMarker == null) {
            addMarkersToMap();
        }

    }

    private void addMarkersToMap() {
        selectMarker = aMap.addMarker(new MarkerOptions().title(selecttitle)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            .draggable(true));
        selectMarker.setPositionByPixels(mapView.getWidth() / 2, mapView.getHeight() / 2);
        selectMarker.showInfoWindow();// 设置默认显示一个infowinfow
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }
private LatLng selctLatLng;
    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        if(isOnpoiSearch){
            return;
        }

        Point point = new Point(mapView.getWidth() / 2, mapView.getHeight() / 2);
        Projection projection = aMap.getProjection();
        LatLng latLng = projection.fromScreenLocation(point);
        selctLatLng = latLng;
        LocationUtil.reverseGeo(SelectPosActivity.this, new double[]{latLng.latitude, latLng.longitude}, new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int rCode) {
                if(regeocodeResult!=null&&rCode == AMapException.CODE_AMAP_SUCCESS){
                    RegeocodeAddress regeocodeAddress= regeocodeResult.getRegeocodeAddress();
                    selecttitle =regeocodeAddress.getFormatAddress();
                    aMap.clear();
                    addMarkersToMap();
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
    }
}
