package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author:Jenny
 * Date:2017/10/18
 * E-mail:fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class BankCardModel {


    /**
     * bank :
     */

    public String bank;


    @JsonProperty("bank_info")
    public BankInfo bankInfo;
    public int id;
    public String name;
    @JsonProperty("card_bank")
    public String cardBank;

    @JsonProperty("create_time")
    public String createTime;

    @JsonProperty("is_selected")
    public int isSelected;

    @JsonProperty("card_code")
    public String cardCode;

    public String money;
}
