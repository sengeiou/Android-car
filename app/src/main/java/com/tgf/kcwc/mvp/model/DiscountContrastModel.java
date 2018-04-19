package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author:Jenny
 * Date:2017/8/9
 * E-mail:fishloveqin@gmail.com
 * <p>
 * 优惠对比
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscountContrastModel {


    @JsonProperty("price_discount")
    public PriceDiscountBean priceDiscount;
    @JsonProperty("org_list")
    public List<OrgBean> orgs;
    public List<ParamBean> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PriceDiscountBean {
        /**
         * name : 最低优惠价
         * value : ["￥10.00万","￥10.00万","￥10.00万","￥10.00万","￥10.00万","￥10.00万","￥10.00万","￥1.4万","￥10.00万","￥10.00万","￥10.00万","￥10.00万","￥10.00万","￥10.00万","￥10.00万","￥10.00万"]
         */

        public String name;
        public List<String> value;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrgBean {
        /**
         * id : 2
         * name : lol
         */

        public int id;
        public String name;

        public  int index;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ParamBean {
        /**
         * name : 限时优惠
         * value : [{"name":"指导价","value":["￥10.00万","￥10.00万","￥10.00万","￥10.00万","￥10.00万","￥10.00万","￥10.00万","￥10.00万","￥10.00万","￥10.00万","￥10.00万","￥10.00万","￥10.00万","￥10.00万","￥10.00万","￥10.00万"]},{"name":"降价优惠","value":["-","-","-","-","-","-","-","8.6万","-","-","-","-","-","-","-","-"]},{"name":"到期时间","value":["-","-","-","-","-","-","-","06-18 00:00","-","-","-","-","-","-","-","-"]}]
         */

        public String name;
        public List<ValueBean> value;
        public  boolean isSelected;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ValueBean {
        /**
         * name : 指导价
         * value : ["￥10.00万","￥10.00万","￥10.00万","￥10.00万","￥10.00万","￥10.00万","￥10.00万","￥10.00万","￥10.00万","￥10.00万","￥10.00万","￥10.00万","￥10.00万","￥10.00万","￥10.00万","￥10.00万"]
         */

        public String name;
        public List<String> value;
    }
}
