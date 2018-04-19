package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
* Auther: Scott
 *  * Date: 2016/12/22 0022
* E-mail:hekescott@qq.com
 * 展位
*/

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExhibitPlace {

    @JsonProperty("area_name")
    public String                  name = "N1";
    @JsonProperty("id")
    public int                     id;
    @JsonProperty("area_img_link")
    public String                  imageurl;
    @JsonProperty("headimg")
    public String                  headimg;
    @JsonProperty("description")
    public String                  description;
    @JsonProperty("brandlist")
    public ArrayList<Brand>        includeBrandlist;
    @JsonProperty("modellist")
    public ArrayList<Beauty>       beautylist;
    @JsonProperty("newcarlist")
    public ArrayList<NewCarBean>      newMotolist;
    @JsonProperty("activitylist")
    public ArrayList<ExhibitEvent> eventlist;
}
