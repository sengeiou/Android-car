package com.tgf.kcwc.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;

import com.amap.api.maps.model.LatLng;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.logger.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Auther: Scott
 * Date: 2017/1/10 0010
 * E-mail:hekescott@qq.com
 */

public class URLUtil {
    public static String getHtml(String urlString) {
        try {
            StringBuffer html = new StringBuffer();
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String temp;
            while ((temp = br.readLine()) != null) {
                html.append(temp).append("\n");
            }
            br.close();
            isr.close();
            return html.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void loadWebData(WebView webView, String s) {
        webView.loadData(s, "text/html; charset=utf-8", "utf-8");
    }

    /**
     * 图片真实的url
     *
     * @param imgPath
     * @param width
     * @param height
     * @return
     */
    public static String builderImgUrl(String imgPath, int width, int height) {
        if (imgPath != null && imgPath.startsWith("http")) {
            return imgPath;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Constants.BaseApi.IMG_BASE_URL).append(imgPath).append("!")
                .append(width).append(height);
        Logger.d("imgeUrl ==" + stringBuilder.toString());
        return stringBuilder.toString();
    }

    public static String builderImgUrl750(String imgPath) {
        if (imgPath != null && imgPath.startsWith("http")) {
            return imgPath;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Constants.BaseApi.IMG_BASE_URL).append(imgPath).append("!")
                .append(750);
        Logger.d("imgeUrl ==" + stringBuilder.toString());
        return stringBuilder.toString();
    }

    public static String builderImgUrl(String imgPath) {
        if (imgPath != null && imgPath.startsWith("http")) {
            return imgPath;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Constants.BaseApi.IMG_BASE_URL).append(imgPath);
        return stringBuilder.toString();
    }

    public static void launcherInnerNavBaidu(Activity activity, String lat,
                                             String lng) throws URISyntaxException {

        //调用内置百度地图客户端

        StringBuilder action = new StringBuilder();
        action.append("baidumap://map/navi?");
        action.append("location=").append(lat).append(",").append(lng);
        //"intent://mMapView/direction?origin=latlng:34.264642646862,108.95108518068|name:我家&destination=大雁塔&mode=drivingion=西安&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end"
        Intent intent = new Intent();
        intent.setData(Uri.parse(action.toString()));
        if (isInstallByread("com.baidu.BaiduMap")) {
            activity.startActivity(intent); //启动调用
            Logger.e("GasStation: 百度地图客户端已经安装");
        } else {
            CommonUtils.showToast(activity, "未安装百度地图应用");
        }

    }

    public static void launcherInnerRouteBaidu(Activity activity, String destName, LatLng startLanLng, LatLng endLanLng) throws URISyntaxException {
        try {
            //调用内置百度地图客户端
//        "baidumap://mMapView/direction?origin=name:对外经贸大学|latlng:39.98871,116.43234&destination=name:西直门&mode=transit&sy=3&index=0&target=1"
            StringBuilder action = new StringBuilder();
            action.append("baidumap://mMapView/direction?");
            action.append("origin=name:").append("我的位置|").append(startLanLng.latitude).append(",").append(startLanLng.longitude).append("&destination=name:").append(destName).append("|").append(endLanLng.latitude).append(",").append(endLanLng.longitude);
            //"intent://mMapView/direction?origin=latlng:34.264642646862,108.95108518068|name:我家&destination=大雁塔&mode=drivingion=西安&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end"
            Intent intent = new Intent();
            intent.setData(Uri.parse(action.toString()));
            if (isInstallByread("com.baidu.BaiduMap")) {
                activity.startActivity(intent); //启动调用
                Logger.e("GasStation: 百度地图客户端已经安装");
            } else {
                CommonUtils.showToast(activity, "未安装百度地图应用");
            }
        } catch (Exception e) {
        }
    }

    public static void launcherInnerNavAMap(Activity activity, String lat,
                                            String lng) throws URISyntaxException {

        //调用内置高德地图客户端

        Intent intent = new Intent("android.intent.action.VIEW",
                android.net.Uri.parse("androidamap://navi?sourceApplication=税源地图&lat=" + lat + "&lon="
                        + lng + "&dev=0&style=2"));
        intent.setPackage("com.autonavi.minimap");
        if (isInstallByread("com.autonavi.minimap")) {
            activity.startActivity(intent); // 启动调用
        } else {
            CommonUtils.showToast(activity, "未安装高德地图应用");
        }

    }

    public static void launcherInnerRouteAMap(Activity activity, String destName, LatLng startLanLng, LatLng endLanLng) throws URISyntaxException {
        try {
            //调用内置高德地图客户端
//        slat=36.2&amp;slon=116.1&amp;sname=abc&amp;dlat=36.3&amp;dlon=116.2&amp;dname=def
            Intent intent = new Intent("android.intent.action.VIEW",
                    android.net.Uri.parse("androidamap://route?sourceApplication=税源地图&slat=" + startLanLng.latitude + "&slon="
                            + startLanLng.longitude + "&sname=我的位置" + "&dlat=" + endLanLng.latitude + "&dlon=" + endLanLng.longitude + "&dname=" + destName + "&dev=0&t=1"));
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setPackage("com.autonavi.minimap");
            if (isInstallByread("com.autonavi.minimap")) {
                activity.startActivity(intent); // 启动调用
            } else {
                CommonUtils.showToast(activity, "未安装高德地图应用");
            }
        } catch (Exception e) {
        }
    }

    /**
     * 判断是否安装目标应用
     *
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    private static boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }
}
