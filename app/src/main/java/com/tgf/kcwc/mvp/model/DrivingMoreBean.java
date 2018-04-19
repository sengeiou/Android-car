package com.tgf.kcwc.mvp.model;

import com.tgf.kcwc.entity.DataItem;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/10/16.
 */

public class DrivingMoreBean {

    public ArrayList<DataItem> onlyLookData=new ArrayList<>(); //只看数据
    public ArrayList<DataItem> stateData=new ArrayList<>();   //状态数据
    public ArrayList<DataItem> journeyData=new ArrayList<>(); //行程数据
    public ArrayList<DataItem> typekData=new ArrayList<>();     //类型数据
    public float distance=0;
    public boolean isDistance=false;
}
