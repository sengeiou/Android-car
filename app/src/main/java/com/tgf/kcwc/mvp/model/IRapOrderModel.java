package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tgf.kcwc.util.TextUtil;

/**
 * Auther: Scott
 * Date: 2017/3/10 0010
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IRapOrderModel {

    public String brand_name;
    public String car_name;
    public String tel;
    public String contact;
    public String description;
    public int status;
    public String create_time;
    public int offer_count;
    public int id;
    @JsonProperty("out_color_name")
    public String outColor;
    public String cover;
    @JsonProperty("in_color_name")
    public String inColor;

    public String getOutlooking(){

        StringBuilder outLookSB = new StringBuilder();
        if(!TextUtil.isEmpty(outColor)){
            outLookSB.append("外观: ").append(outColor);
        }
        if(!TextUtil.isEmpty(inColor)){
            outLookSB.append("  内饰: ").append(inColor);
        }
        return outLookSB.toString();
    }
}
