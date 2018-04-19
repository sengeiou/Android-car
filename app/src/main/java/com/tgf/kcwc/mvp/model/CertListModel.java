package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author：Jenny
 * Date:2017/2/7 21:17
 * E-mail：fishloveqin@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CertListModel {

    public int            count;

    @JsonProperty("list")
    public List<CertItem> items;
}
