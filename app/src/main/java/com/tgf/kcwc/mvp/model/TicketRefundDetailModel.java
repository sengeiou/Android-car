package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author:Jenny
 * Date:2017/8/25
 * E-mail:fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketRefundDetailModel {


    /**
     * details : {"event_name":"汽车展会-重庆-全国级","event_cover":"/project/1708/01/350c80fb82fe2f6b0ccca84e63b4f9b4.png","order_sn":"S00001384","order_id":1384,"ticket_name":"有代金券抵扣的门票","create_order_time":"2017-08-24 18:14:59","pay_time":"2017-08-24 18:15:39","pay_type":1,"price":"60.01","user_coupon_code":"FLEVEGDU","price_discount":"60.00","apply_refund_time":"2017-08-24 18:39:26","refund_price":"0.01","refund_type":1,"reason":"后悔了，不想要了","status":2,"serial_sn":"O01601000190","refund_time":"2017-08-24 18:39:27","event_id":160,"mobile":"18623655153","ticket_type":1,"media_type":1}
     * follow : [{"time":"2017-08-24 18:39:26","content":"发起退款申请（原路退回）;"},{"time":"2017-08-24 18:39:27","content":"看车玩车审核通过（原路退回）"},{"time":"2017-08-24 18:39:27","content":"退款入账（原路退回）"}]
     */

    public TicketDetailBean details;
    @JsonProperty("follow")
    public List<FollowBean> follows;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TicketDetailBean {
        /**
         * event_name : 汽车展会-重庆-全国级
         * event_cover : /project/1708/01/350c80fb82fe2f6b0ccca84e63b4f9b4.png
         * order_sn : S00001384
         * order_id : 1384
         * ticket_name : 有代金券抵扣的门票
         * create_order_time : 2017-08-24 18:14:59
         * pay_time : 2017-08-24 18:15:39
         * pay_type : 1
         * price : 60.01
         * user_coupon_code : FLEVEGDU
         * price_discount : 60.00
         * apply_refund_time : 2017-08-24 18:39:26
         * refund_price : 0.01
         * refund_type : 1
         * reason : 后悔了，不想要了
         * status : 2
         * serial_sn : O01601000190
         * refund_time : 2017-08-24 18:39:27
         * event_id : 160
         * mobile : 18623655153
         * ticket_type : 1
         * media_type : 1
         */

        @JsonProperty("event_name")
        public String eventName;
        @JsonProperty("event_cover")
        public String cover;
        @JsonProperty("order_sn")
        public String orderSN;
        @JsonProperty("order_id")
        public int orderId;
        @JsonProperty("ticket_name")
        public String ticketName;
        @JsonProperty("create_order_time")
        public String createOrderTime;
        @JsonProperty("pay_time")
        public String payTime;
        @JsonProperty("pay_type")
        public int payType;
        public String price;
        @JsonProperty("user_coupon_code")
        public String userCouponCode;
        @JsonProperty("price_discount")
        public String priceDiscount;
        @JsonProperty("apply_refund_time")
        public String applyRefundTime;
        @JsonProperty("refund_price")
        public String refundPrice;
        @JsonProperty("refund_type")
        public int refundType;
        public String reason;
        public int status;
        @JsonProperty("serial_sn")
        public String serialSN;
        @JsonProperty("refund_time")
        public String refundTime;
        @JsonProperty("event_id")
        public int eventId;
        public String mobile;
        @JsonProperty("ticket_type")
        public int ticketType;
        @JsonProperty("media_type")
        public int mediaType;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FollowBean {
        /**
         * time : 2017-08-24 18:39:26
         * content : 发起退款申请（原路退回）;
         */

        public String time;
        public String content;
    }
}
