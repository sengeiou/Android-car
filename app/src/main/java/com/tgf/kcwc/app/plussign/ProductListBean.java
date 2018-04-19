package com.tgf.kcwc.app.plussign;

import java.io.Serializable;

/**
 * 实体类
 */
public class ProductListBean implements Serializable {
    private String proName;
    private int imgUrl;
    public int id;

    public ProductListBean(String proName, int imgUrl,int id) {
        this.proName = proName;
        this.imgUrl = imgUrl;
        this.id = id;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public int getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(int imgUrl) {
        this.imgUrl = imgUrl;
    }
}
