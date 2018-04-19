package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Auther: Scott
 * Date: 2017/3/6 0006
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewPriceModel {
    @JsonProperty("id")
    public int       id;
    @JsonProperty("offer_count")
    public int       offerCount;
    @JsonProperty("offer_info")
    public OfferInfo offerInfo;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OfferInfo {
        @JsonProperty("avatar")
        public String avatar;
        @JsonProperty("nickname")
        public String nickname;
        @JsonProperty("offer")
        public String offer;
        @JsonProperty("offer_user_id")
        public String offer_user_id;
    }

}
