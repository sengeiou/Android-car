package com.tgf.kcwc.mvp.model;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tgf.kcwc.app.KPlayCarApp;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/5/17 0017
 * E-mail:hekescott@qq.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class TripBookMapModel {

    @JsonProperty("line_list")
    public ArrayList<MapItem> mapItemList;
    @JsonProperty("node_list")
    public ArrayList<NodeItem> nodeList;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MapItem {
        public int    id;
        public String lng;
        public String lat;
        public String adds;
        public String distance;
        @JsonProperty("total_count")
        public int    totalCount;
        public void getLatLng(){

        }
        public String getDistance(KPlayCarApp kPlayCarApp){
            LatLng latLng1 = new LatLng(Double.parseDouble(kPlayCarApp.getLattitude()),Double.parseDouble(kPlayCarApp.getLongitude()));
            LatLng latLng2 = new LatLng(Double.parseDouble(this.lat),Double.parseDouble(this.lng));
            float meter= AMapUtils.calculateLineDistance(latLng1,latLng2);
            meter =  new BigDecimal(meter).divide(new BigDecimal(1000),1,BigDecimal.ROUND_HALF_UP).floatValue();
            return  meter+"km";
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NodeItem {
        public int    id;
        public String lng;
        public String lat;
        public int type;
        public int altitude;
        public String speed;
        public String speed_max;
    }
}
