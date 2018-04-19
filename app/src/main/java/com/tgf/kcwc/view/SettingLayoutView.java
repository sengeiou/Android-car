package com.tgf.kcwc.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;


/**
 * Auther: Scott
 * Date: 2017/5/20 0020
 * E-mail:hekescott@qq.com
 */

public class SettingLayoutView extends RelativeLayout {

    private Context mContext;
    private TextView settingNameTv;

    public SettingLayoutView(Context context) {
        super(context);
        mContext = context;
    }


    public SettingLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView(context, attrs);
    }

    public SettingLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        View.inflate(mContext, R.layout.layout_setting_item, SettingLayoutView.this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingLayoutView);
        String title =  typedArray.getString(R.styleable.SettingLayoutView_settingtitle);
        settingNameTv = (TextView) this.findViewById(R.id.setting_name);
        settingNameTv.setText(title);
    }
}
