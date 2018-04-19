package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Administrator on 2017/5/3.
 */

public class CitySelectBean implements Comparable<CitySelectBean> {
    public String label;

    public String name;

    public String pinyin;

    public int pid;

    @JsonProperty("adcode")
    public String        adcode;

    public int id;

    public String PY;

    public String brandLogo;

    public boolean IsSelect;

    @Override
    public int compareTo(CitySelectBean t1) {

        if (this.pinyin.compareTo(t1.pinyin) > 0) {

            return 1;
        } else {
            return -1;
        }

    }
}
