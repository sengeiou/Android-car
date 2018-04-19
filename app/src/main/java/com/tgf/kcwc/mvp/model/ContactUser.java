package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;

import java.io.Serializable;

/**
 * Auther: Scott
 * Date: 2017/2/9 0009
 * E-mail:hekescott@qq.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactUser extends BaseIndexPinyinBean implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("user_id")
    public int userId;
    @JsonProperty("nickname")
    public String name;
    @JsonProperty("real_name")
    public String realName;
    @JsonProperty("tel")
    public String mobile;
    @JsonProperty("avatar")
    public String avatar;
    //    用户类型列表1站内好友2手机用户
    public int type =2;
    public boolean isSelected;
    //已有量
    @JsonProperty("num")
    public int num=1;
    //最大领取量
    public int maxNum=12;

    @Override
    public String getTarget() {
        return name;
    }
    boolean isTop;
    @Override
    public boolean isNeedToPinyin() {
        return !isTop;
    }


    @Override
    public boolean isShowSuspension() {
        return !isTop;
    }
}
