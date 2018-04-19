package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author:Jenny
 * Date:2017/11/16
 * E-mail:fishloveqin@gmail.com
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class WXAuthModel {


    /**
     * access_token : 3_5kNToB0JYJruPlNQUorva7Vu2vJKNvDCmrjDOM0QtH8Uvn_fTV21qeY_BAnF7HPt2aATYsJTVm4ERYlxisdNuw
     * expires_in : 7200
     * refresh_token : 3_tUjHNxxAFFtq8I3y7Aaubdjwz6dRjLUmALEJhv1EZpOzoCBp_oVTkFb7gsVtLgWk2I-yJhWQ90pMdXuvv7arEw
     * openid : o-F0H1bw0Lqt1yaJvmFKFn5gXFMI
     * scope : snsapi_userinfo
     * unionid : obPAbwli6YodSIyuHgR5JqnXwMYI
     */

    @JsonProperty("access_token")
    public String accessToken;
    @JsonProperty("expires_in")
    public int expiresIn;
    @JsonProperty("refresh_token")
    public String refreshToken;
    public String openid;
    public String scope;
    public String unionid;
}
