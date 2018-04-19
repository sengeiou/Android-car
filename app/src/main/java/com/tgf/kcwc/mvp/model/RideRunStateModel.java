package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Author:Jenny
 * Date:2017/7/21
 * E-mail:fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class RideRunStateModel {

    /**
     * ride_id : 844
     * module : roadbook
     * book_id : 0
     * thread_id : 0
     * node_list : [{"id":12869,"ride_id":844,"lng":"106.493227","lat":"29.525627","add_time":"2017-07-21 10:16:57","altitude":335,"speed":"0.00","bending_angle":0,"mileage":"0.00","address":"重庆市九龙坡区渝州路街道重庆展览中心","city_name":"重庆市","ride_time":0}]
     */

    @JsonProperty("ride_id")
    public int                    rideId;
    public String                 module;
    @JsonProperty("book_id")
    public int                    bookId;
    @JsonProperty("thread_id")
    public int                    threadId;
    @JsonProperty("node_list")
    public ArrayList<RideDataNodeItem> nodeList;

}
