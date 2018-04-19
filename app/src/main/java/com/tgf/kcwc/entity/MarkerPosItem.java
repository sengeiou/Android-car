package com.tgf.kcwc.entity;

import com.amap.api.maps.model.LatLng;

/**
 * Author:Jenny
 * Date:2017/5/11
 * E-mail:fishloveqin@gmail.com
 */

public class MarkerPosItem {

    public String address="北京天安门"; //地址信息

    public int    type;   //1表示开始,2表示结束，其它表示途经点

    public LatLng latLng;//点坐标
}
