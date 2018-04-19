package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @anthor noti
 * @time 2017/10/18
 * @describle
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarCertDetailModel {
    public Details details;
    @JsonProperty("car_cert_info")
    public CarCertInfo carCertInfo;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Details {
        @JsonProperty("event_id")
        public int eventId;
        @JsonProperty("times_type")
        public int timesType;
        @JsonProperty("times_check")
        public int timesCheck;
        @JsonProperty("is_can_print")
        public int isCanPrint;
        @JsonProperty("is_can_lose")
        public int isCanLose;
        @JsonProperty("is_print")
        public int isPrint;
        @JsonProperty("is_first")
        public int isFirst;
        @JsonProperty("check_status")
        public int checkStatus;
        @JsonProperty("need_brand")
        public int needBrand;
        @JsonProperty("is_bind")
        public int isBind;
        @JsonProperty("is_on")
        public int isOn;
        @JsonProperty("event_name")
        public String eventName;
        @JsonProperty("event_cover")
        public String eventCover;
        @JsonProperty("cert_name")
        public String certName;
        @JsonProperty("cert_start_time")
        public String certStartTime;
        @JsonProperty("cert_end_time")
        public String certEndTime;
        @JsonProperty("cert_sn")
        public String certSn;
        @JsonProperty("serial_sn")
        public String serialSn;
        @JsonProperty("check_fail_reason")
        public String checkFailReason;
        @JsonProperty("user_mobile")
        public String userMobile;
        @JsonProperty("print_sn")
        public String printSn;
        @JsonProperty("show_time")
        public String showTime;
        @JsonProperty("service_tel")
        public String serviceTel;
        public int cid;
        public int times;
        public int id;
        public int status;
        public int type;
        public String name;
        public String brand;
        public String company;
        public String profile;
        public String color;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CarCertInfo {
        @JsonProperty("car_apply_id")
        public int carApplyId;
        @JsonProperty("car_plate_number")
        public String carPlateNumber;
        @JsonProperty("car_hall")
        public String carHall;
        @JsonProperty("car_show_time")
        public String carShowTime;
        @JsonProperty("car_in_place_time")
        public String carInPlaceTime;
        @JsonProperty("car_out_place_time")
        public String carOutPlaceTime;
        @JsonProperty("car_overtime_time")
        public String carOvertimeTime;
        @JsonProperty("car_image_out")
        public String carImageOut;
    }
}
