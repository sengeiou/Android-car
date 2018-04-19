package com.tgf.kcwc.callback;

import android.view.View;

/**
 * Author:Jenny
 * Date:2017/6/26
 * E-mail:fishloveqin@gmail.com
 * 
 *重写OnClickLisnter事件，解决快速点击重复提交问题
 */

public abstract class OnSingleClickListener implements View.OnClickListener {

    private long preTime;
    private int  delaySecond = 500; // 默认两次点击的间隔为 500 毫秒

    public OnSingleClickListener() {
        super();
    }

    /**
     * 可设置两次点击的间隔时间
     * @param delaySecond   两次点击的间隔时间，单位 毫秒
     */
    public OnSingleClickListener(int delaySecond) {
        this();
        this.delaySecond = delaySecond;
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        if (!isDoubleClick()) {
            onSingleClick(view);
        }
    }

    /**
     * 用于为外部提供的覆写方法，以实现点击事件
     */
    protected abstract void onSingleClick(View view);

    /**
     * 判断是否是连续点击了Button
     * @return
     *      true    连续点击了Button
     *      false   没有连续点击Button
     */
    private boolean isDoubleClick() {
        long lastTime = System.currentTimeMillis();
        boolean flag = lastTime - preTime < delaySecond ? true : false;
        preTime = lastTime;
        return flag;
    }

}
