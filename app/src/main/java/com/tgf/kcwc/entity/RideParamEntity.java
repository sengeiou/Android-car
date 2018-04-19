package com.tgf.kcwc.entity;

import java.io.Serializable;

/**
 * Author:Jenny
 * Date:2017/11/22
 * E-mail:fishloveqin@gmail.com
 */

public class RideParamEntity implements Serializable {

    public double totalMile;  //行驶的总里程
    public int rideTimer; //行驶的时间
    public double maxSpeed; //最大速度

    public double lat; //纬度
    public double lng; //精度

    @Override
    public String toString() {
        return "RideParamEntity{" +
                "totalMile=" + totalMile +
                ", rideTimer=" + rideTimer +
                ", maxSpeed=" + maxSpeed +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
