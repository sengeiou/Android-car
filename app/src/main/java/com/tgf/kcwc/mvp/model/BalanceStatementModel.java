package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author:Jenny
 * Date:2017/10/19
 * E-mail:fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class BalanceStatementModel {


    /**
     * pagination : {"count":4,"page":1}
     * list : [{"id":26,"money":"0.01","flag":1,"act_type":"门票退款","create_time":"2017-07-27 11:05:55","remarks":""},{"id":47,"money":"125.00","flag":2,"act_type":"提现","create_time":"2017-10-17 13:56:19","remarks":""},{"id":49,"money":"125.00","flag":2,"act_type":"提现","create_time":"2017-10-18 11:11:55","remarks":""},{"id":50,"money":"100.00","flag":1,"act_type":"充值","create_time":"2017-10-18 14:39:53","remarks":""}]
     */

    public Pagination pagination;
    public List<StatementItem> list;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StatementItem {
        /**
         * id : 26
         * money : 0.01
         * flag : 1
         * act_type : 门票退款
         * create_time : 2017-07-27 11:05:55
         * remarks :
         */

        public int id;
        public String money;
        public int flag;
        @JsonProperty("act_type")
        public String actType;
        @JsonProperty("create_time")
        public String createTime;
        public String remarks;
    }
}
