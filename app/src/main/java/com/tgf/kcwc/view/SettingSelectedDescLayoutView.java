package com.tgf.kcwc.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;

/**
 * Auther: Scott
 * Date: 2017/5/20 0020
 * E-mail:hekescott@qq.com
 */

public class SettingSelectedDescLayoutView extends RelativeLayout {

    private Context   mContext;
    private TextView  settingNameTv;
    private TextView  headingTv;
    private boolean mIsSelected;
    private ImageView statusIv;
    private OnChangerLisener mOnChangelistener;

    public SettingSelectedDescLayoutView(Context context) {
        super(context);
        mContext = context;
    }

    public SettingSelectedDescLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView(context, attrs);
    }

    public SettingSelectedDescLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView(context, attrs);
    }
    private void initView(Context context, AttributeSet attrs) {
        View.inflate(mContext, R.layout.layout_setting_descselect, SettingSelectedDescLayoutView.this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
            R.styleable.SettingSelectedLayoutView);
        String title = typedArray.getString(R.styleable.SettingSelectedLayoutView_settingselecttitle);
        mIsSelected = typedArray.getBoolean(R.styleable.SettingSelectedLayoutView_is_selected, false);
        settingNameTv = (TextView) this.findViewById(R.id.setting_name);
        statusIv = (ImageView) this.findViewById(R.id.setting_statusiv);
        headingTv = (TextView) this.findViewById(R.id.setting_heading);
        settingNameTv.setText(title);
        setStatus();
        statusIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsSelected =!mIsSelected;
                setStatus();
                if(mOnChangelistener!=null){
                    mOnChangelistener.onChange();
                }
            }
        });
    }

    private void setStatus() {
        if (mIsSelected) {
            statusIv.setImageResource(R.drawable.btnset_selected);
        } else {
            statusIv.setImageResource(R.drawable.btnset_normal);
        }
    }

    public void setStatus(boolean isSelected) {
        this.mIsSelected = isSelected;
        if (mIsSelected) {
            statusIv.setImageResource(R.drawable.btnset_selected);
        } else {
            statusIv.setImageResource(R.drawable.btnset_normal);
        }
    }
    public boolean getStauts(){

        return mIsSelected;
    }
    public void setOnChangelistener(OnChangerLisener onChangelistener){
        mOnChangelistener = onChangelistener;
    }

    public interface OnChangerLisener{
        void onChange();
    }

    /**
     * 设置副标题显示
     */
    public void setHeadingVisible(){
        headingTv.setVisibility(VISIBLE);
    }
}
