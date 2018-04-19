package com.tgf.kcwc.view;

import android.content.Context;
import android.view.View;

import com.tgf.kcwc.R;

/**
 * Author:Jenny
 * Date:16/9/11 14:16
 * E-mail:fishloveqin@gmail.com
 * 授权弹窗
 */
public class MapNavPopWindow extends BottomPushPopupWindow<Void> {

    public MapNavPopWindow(Context context) {
        super(context, null);
    }

    @Override
    protected View generateCustomView(Void data) {
        View root = View.inflate(context, R.layout.bottom_pop_window, null);
        View aMapView = root.findViewById(R.id.aMap);
        aMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
                if (mListener != null) {
                    mListener.onClick(v);
                }
            }
        });
        View baiduMapView = root.findViewById(R.id.baiduMap);
        baiduMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mListener != null) {
                    mListener.onClick(v);
                }
            }
        });
        View cancelView = root.findViewById(R.id.cancel);
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mListener != null) {
                    mListener.onClick(v);
                }
            }
        });
        return root;
    }

    private View.OnClickListener mListener;

    public void setOnClickListener(View.OnClickListener listener) {
        this.mListener = listener;
    }
}