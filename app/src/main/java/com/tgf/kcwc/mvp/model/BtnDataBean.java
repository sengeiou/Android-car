package com.tgf.kcwc.mvp.model;

/**
 * @anthor noti
 * @time 2017/9/25
 * @describle
 */
public class BtnDataBean {
    public int imgUrl;
    public String nameBtn;

    public BtnDataBean(int imgUrl, String nameBtn) {
        this.imgUrl = imgUrl;
        this.nameBtn = nameBtn;
    }

    public int getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(int imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getNameBtn() {
        return nameBtn;
    }

    public void setNameBtn(String nameBtn) {
        this.nameBtn = nameBtn;
    }

    @Override
    public String toString() {
        return "BtnDataBean{" +
                "imgUrl=" + imgUrl +
                ", nameBtn='" + nameBtn + '\'' +
                '}';
    }
}
