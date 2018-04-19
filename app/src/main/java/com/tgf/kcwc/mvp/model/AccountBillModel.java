package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author:Jenny
 * Date:2017/10/24
 * E-mail:fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountBillModel {


    /**
     * income : 2650
     * expenditure : 50
     * income_info : [{"act_type":"充值","total_money":"2650.00"}]
     * expenditure_info : [{"act_type":"提现","total_money":"50.00"}]
     * month : 10
     */

    public float income;
    public float expenditure;
    public String month;
    @JsonProperty("income_info")
    public List<BillItemBean> incomeInfo;
    @JsonProperty("expenditure_info")
    public List<BillItemBean> expenditureInfo;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BillItemBean {
        /**
         * act_type : 充值
         * total_money : 2650.00
         */

        @JsonProperty("act_type")
        public String actType;
        @JsonProperty("total_money")
        public String totalMoney;

        @JsonProperty("icon_type")
        public int iconType;
        public String title;

        public String content;
    }


}
