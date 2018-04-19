package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Author:Jenny
 * Date:2017/10/23
 * E-mail:fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class BankInfo {


    /**
     * name : 兴业银行
     * level : 兴业自然人生理财卡
     * type : 借记卡
     */

    public String name;
    public String level;
    public String type;
}
