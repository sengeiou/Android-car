package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * @anthor noti
 * @time 2017/9/18
 * @describle
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SelectExhibitionPosModel {
    @JsonProperty("code")
    public int statusCode;
    @JsonProperty("msg")
    public String statusMessage;
    @JsonProperty("data")
    public ArrayList<Data> datas;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        @JsonProperty("park_time_id")
        public int parkTimeId;
        @JsonProperty("park_id")
        public int parkId;
        public int status;
        @JsonProperty("park_name")
        public String parkName;
        @JsonProperty("service_charge")
        public String serviceCharge;
        public String deposit;
        @JsonProperty("park_type_name")
        public String parkTypeName;

        public boolean isSelect;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }
    }
}
