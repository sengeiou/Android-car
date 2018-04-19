package com.tgf.kcwc.logger;

/**
 * Author:Jenny
 * Date:2017/11/28
 * E-mail:fishloveqin@gmail.com
 */

public class RideActionInfo implements IActionInfo {

    public String address;
    public String speed;

    public RideActionInfo(String address, String speed) {
        this.address = address;
        this.speed = speed;
    }

    public RideActionInfo(){

    }
}
