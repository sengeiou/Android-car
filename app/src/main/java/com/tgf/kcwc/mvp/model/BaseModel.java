package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author：Jenny
 * Date:2016/12/19 20:01
 * E-mail：fishloveqin@gmail.com
 */

public class BaseModel {

    public boolean isSelected;

    @JsonProperty("event_id")
    public int     eventId;

    @JsonProperty("hall_id")
    public int     hallId;


}
