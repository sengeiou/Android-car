package com.tgf.kcwc.util;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.tgf.kcwc.app.KPlayCarApp;

import java.math.BigDecimal;

/**
 * Auther: Scott
 * Date: 2017/11/2 0002
 * E-mail:hekescott@qq.com
 */

public class DistanceUtil {

    /**
     * km不足.足千米时，保留一位小数，2.不足km，显示m
     *
     * @param distance 单位km
     * @return
     */
    public static String getDistance(double distance) {
        try {
            if (distance < 1) {
                return ((int) (distance * 1000)) + "m";
            } else {
                String tmp = distance + "";
                if (!TextUtil.isEmpty(tmp)) {
                    if (tmp.length() > 1) {
                        return NumFormatUtil.getFmtOneNum(distance) + "km";
                    }
                }
            }
            return 0 + "m";
        } catch (Exception e) {
            e.printStackTrace();
            return 0 + "m";
        }
    }

    /**
     * 足千米时，保留一位小数，2.不足km，显示m
     *
     * @param distanceStr 单位km
     * @return
     */
    public static String getDistance(String distanceStr) {
        try {
            double distance = Double.parseDouble(distanceStr);
            if (distance < 1) {
                return ((int) (distance * 1000)) + "m";
            } else {
                String tmp = distance + "";
                if (!TextUtil.isEmpty(tmp)) {
                    if (tmp.length() > 1) {
                        return NumFormatUtil.getFmtOneNum(distance) + "km";
                    }
                }
            }
            return 0 + "m";
        } catch (Exception e) {
            e.printStackTrace();
            return 0 + "m";
        }
    }
    /**
     * 足千米时，保留一位小数，2.不足km，显示m
     *
     * @param lat 目的纬度
     * @param lng 目的经度
     * @return
     */
    public static String getDistance(KPlayCarApp kPlayCarApp,String lat,String lng){
        LatLng latLng1 = new LatLng(Double.parseDouble(kPlayCarApp.getLattitude()),Double.parseDouble(kPlayCarApp.getLongitude()));
        LatLng latLng2 = new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
        float meter= AMapUtils.calculateLineDistance(latLng1,latLng2);
        meter =  new BigDecimal(meter).divide(new BigDecimal(1000),1,BigDecimal.ROUND_HALF_UP).floatValue();
        return  getDistance(meter);
    }

}
