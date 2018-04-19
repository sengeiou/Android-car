package com.tgf.kcwc.util;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;

import android.content.Context;

/**
 * Auther: Scott
 * Date: 2016/12/28 0028
 * E-mail:hekescott@qq.com
 */

public class LocationUtil {


//    public static LocationClient getLocationClient(Activity context) {
//        LocationClient mLocationClient = new LocationClient(context.getApplicationContext());
//        LocationClientOption option = new LocationClientOption();
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
//        option.setIsNeedLocationPoiList(true);
//        option.setIsNeedAddress(true);
//        option.setOpenGps(true);
//        option.setEnableSimulateGps(false);
//        option.SetIgnoreCacheException(false);
//        mLocationClient.setLocOption(option);

//        return mLocationClient;
//    }

//    public static LocationClient getLocationClient(Application context) {
//        LocationClient mLocationClient = new LocationClient(context);
//        LocationClientOption option = new LocationClientOption();
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
//        option.setIsNeedLocationPoiList(true);
//        option.setIsNeedAddress(true);
//        option.setOpenGps(true);
//        option.setEnableSimulateGps(false);
//        option.SetIgnoreCacheException(false);
//        mLocationClient.setLocOption(option);

//        return mLocationClient;
//    }

//    public static void getLocation(Activity context, BDLocationListener mBDLocationListener) {
//        LocationClient loaction = getLocationClient(context);
//        loaction.registerLocationListener(mBDLocationListener);
//        loaction.start();
//    }

    //    LocationClientOption option = new LocationClientOption();
    //    option.setLocationMode(LocationMode.Hight_Accuracy
    //    );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
    //    option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
    //    int span=1000;
    //    option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
    //    option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
    //    option.setOpenGps(true);//可选，默认false,设置是否使用gps
    //    option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
    //    option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
    //    option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
    //    option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死  
    //    option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
    //    option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
    //    mLocationClient.setLocOption(option);

    /**
     *
     * 根据经纬度查询地址
     * @param appContext 此处必须传当前应用Context
     */
    public static void reverseGeo(Context appContext, double[] locations,
                                  GeocodeSearch.OnGeocodeSearchListener listener) {

        GeocodeSearch   geocoderSearch = new GeocodeSearch(appContext);
        geocoderSearch.setOnGeocodeSearchListener(listener);
        LatLonPoint latLonPoint = new LatLonPoint(locations[0], locations[1]);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }

    /**
     * 默认的定位参数
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    public static AMapLocationClient getGaodeLocationClient(Context context) {

        //初始化client
        AMapLocationClient locationClient = new AMapLocationClient(context.getApplicationContext());
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption
            .setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        locationClient.setLocationOption(mOption);
        return locationClient;
    }
}
