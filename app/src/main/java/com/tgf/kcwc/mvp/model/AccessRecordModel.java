package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author：Jenny
 * Date:2017/2/9 15:43
 * E-mail：fishloveqin@gmail.com
 */

public class AccessRecordModel {

    @JsonProperty("list")
    public List<AccessRecord> records;
}
