package com.tgf.kcwc.entity;

import android.view.View;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tgf.kcwc.mvp.model.ResponseMessage;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Author：Jenny
 * Date:2016/12/13 19:04
 * E-mail：fishloveqin@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataItem implements Serializable {
    @JsonProperty("id")
    public int id;
    @JsonProperty("name")
    public String name;

    public DataItem() {
    }

    public DataItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public boolean isSelected;
    public int type;

    public int nums = 0;

    public String title;
    public String content = "";
    public int code;
    public String msg;
    public ArrayList<DataItem> subDataItem;

    public ResponseMessage<PathItem> resp;
    public View v,v2;
    public String key;

    public int priceMin;
    public int priceMax;

    public int icon;

    public String hint="";

    public String url;
    public String oriUrl;
    public String time;

    public String nickname;

    public String colours;

    public int locIcon;

    public String ticketType;
    public String unitPrice;
    public String amount;
    public String total;

    public int textColorValue;

    public String refundState;

    public String refundPrice;

    public String tCode;

    public String priceMaxStr = "";
    public String priceMinStr = "";

    public String path;

    public  int count;

    public Object obj;
}
