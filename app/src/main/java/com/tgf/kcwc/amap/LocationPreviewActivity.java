package com.tgf.kcwc.amap;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.TranslateAnimation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.SearchResultAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MapNavPopWindow;
import com.tgf.kcwc.view.SegmentedGroup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Author：Jenny
 * Date:2017/2/23 11:19
 * E-mail：fishloveqin@gmail.com
 * 当前位置信息预览
 */

public class LocationPreviewActivity extends BaseActivity
                                     implements AMap.OnMarkerClickListener, AMap.InfoWindowAdapter { // Inputtips.InputtipsListener

    private AMap                     aMap;
    private MapView                  mapView;
    private AMapLocationClient       mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private Marker                   locationMarker;

    private ProgressDialog           progDialog = null;

    private String                   lat        = "";
    private String                   lng        = "";
    private String                   mOrgName   = "", mAddress = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        lat = intent.getStringExtra(Constants.IntentParams.LAT);
        lng = intent.getStringExtra(Constants.IntentParams.LNG);
        mOrgName = intent.getStringExtra(Constants.IntentParams.DATA);
        mAddress = intent.getStringExtra(Constants.IntentParams.DATA2);
        setContentView(R.layout.activity_location_preview);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        init();

    }

    /**
     * 初始化
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }

    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.getUiSettings().setZoomControlsEnabled(false);
        // aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示

        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
        //aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {

                double lat = 0;
                double lng = 0;

                if (!TextUtils.isEmpty(LocationPreviewActivity.this.lat)) {

                    lat = Double.parseDouble(LocationPreviewActivity.this.lat);
                }
                if (!TextUtils.isEmpty(LocationPreviewActivity.this.lng)) {
                    lng = Double.parseDouble(LocationPreviewActivity.this.lng);
                }
                LatLng latLng = new LatLng(lat, lng);
                addMarkerInScreenCenter(latLng);
            }
        });
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
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("位置预览");

    }

    @Override
    protected void setUpViews() {

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
    }

    public void showDialog() {
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在加载...");
        progDialog.show();
    }

    public void dismissDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    private void addMarkerInScreenCenter(LatLng locationLatLng) {
        MarkerOptions options = new MarkerOptions();
        options.position(locationLatLng);
        options.anchor(0.5f, 0.5f);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.amap_marker_away));
        options.title(mOrgName+"");
        options.snippet(mAddress+"");
        locationMarker = aMap.addMarker(options);
        locationMarker.showInfoWindow();
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, 16f));
    }

    /**
     * 屏幕中心marker 跳动
     */
    public void startJumpAnimation() {

        if (locationMarker != null) {
            //根据屏幕距离计算需要移动的目标点
            final LatLng latLng = locationMarker.getPosition();
            Point point = aMap.getProjection().toScreenLocation(latLng);
            point.y -= dip2px(this, 125);
            LatLng target = aMap.getProjection().fromScreenLocation(point);
            //使用TranslateAnimation,填写一个需要移动的目标点
            Animation animation = new TranslateAnimation(target);
            animation.setInterpolator(new Interpolator() {
                @Override
                public float getInterpolation(float input) {
                    // 模拟重加速度的interpolator
                    if (input <= 0.5) {
                        return (float) (0.5f - 2 * (0.5 - input) * (0.5 - input));
                    } else {
                        return (float) (0.5f - Math.sqrt((input - 0.5f) * (1.5f - input)));
                    }
                }
            });
            //整个移动所需要的时间
            animation.setDuration(600);
            //设置动画
            locationMarker.setAnimation(animation);
            //开始动画
            locationMarker.startAnimation();

        } else {
            Log.e("ama", "screenMarker is null");
        }
    }

    //dip和px转换
    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        View infoContent = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        render(marker, infoContent);
        return infoContent;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    /**
     * 自定义infowinfow窗口
     */
    public void render(Marker marker, View view) {
        String title = marker.getTitle();
        System.out.println("type" + title);
        TextView titleUi = ((TextView) view.findViewById(R.id.title));
        titleUi.setText(title + "");
        String snippet = marker.getSnippet();
        TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
        snippetUi.setText(snippet + "");
        view.findViewById(R.id.navLayout).setOnClickListener(this);
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
                    URLUtil.launcherInnerNavAMap(this, lat, lng);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.baiduMap:
                try {
                    URLUtil.launcherInnerNavBaidu(this, lat, lng);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                break;
        }

    }
}
