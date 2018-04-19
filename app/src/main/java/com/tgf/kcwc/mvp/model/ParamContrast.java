package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author：Jenny
 * Date:2017/1/3 14:23
 * E-mail：fishloveqin@gmail.com
 * 车型参数对比
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ParamContrast {

    @JsonProperty("name")
    public String               title;
    @JsonProperty("items")
    public List<SpecItemEntity> items;

    public static class SpecItemEntity {
        @JsonProperty("name")
        public String          name;

        @JsonProperty("valueitems")
        public List<ValueItem> valueItems;

    }
    public  int have_radio;
    public boolean isSelected;
}
