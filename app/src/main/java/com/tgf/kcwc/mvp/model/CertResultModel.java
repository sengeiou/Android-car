package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author：Jenny
 * Date:2017/2/9 11:09
 * E-mail：fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class CertResultModel {

    /**
     * details : {"code":"1111","remarks":"每张证件只能打印1次纸质证件。纸质证件一旦打印成功，电子证件即时失效。请至打印点输入手机号和打印验证码打印纸质证件。"}
     */

    @JsonProperty("details")
    public DetailsBean detail;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DetailsBean {
        /**
         * code : 1111
         * remarks : 每张证件只能打印1次纸质证件。纸质证件一旦打印成功，电子证件即时失效。请至打印点输入手机号和打印验证码打印纸质证件。
         */

        public String code;
        public String remarks;
        public String rules;
    }
}
