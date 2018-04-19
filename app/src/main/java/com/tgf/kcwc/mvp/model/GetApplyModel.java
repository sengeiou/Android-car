package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @anthor noti
 * @time 2017/11/6
 * @describle
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetApplyModel {
    @JsonProperty("apply_id")
    public int applyId;
    @JsonProperty("event_id")
    public int eventId;
    @JsonProperty("park_id")
    public int parkId;
    @JsonProperty("park_time_id")
    public int parkTimeId;
    @JsonProperty("thread_id")
    public int threadId;
    public int status;
    @JsonProperty("apply_name")
    public String applyName;
    @JsonProperty("car_image_in")
    public String carImageIn;
    @JsonProperty("car_image_out")
    public String carImageOut;
    @JsonProperty("driving_license")
    public String drivingLicense;
    @JsonProperty("idcard_back")
    public String idcardBack;
    @JsonProperty("idcard_front")
    public String idcardFront;
    @JsonProperty("plate_number")
    public String plateNumber;
}
