package com.tgf.kcwc.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.util.TextUtil;

/**
 * Auther: Scott
 * Date: 2017/5/20 0020
 * E-mail:hekescott@qq.com
 */

public class SettingSelectedLayoutView extends RelativeLayout {

    private Context   mContext;
    private TextView  settingNameTv;
    private TextView  headingTv;
    private boolean mIsSelected;
    private ImageView statusIv;
    private OnChangerLisener mOnChangelistener;
    private String descStr;
    private boolean isDesc;

    public SettingSelectedLayoutView(Context context) {
        super(context);
        mContext = context;
    }

    public SettingSelectedLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView(context, attrs);
    }

    public SettingSelectedLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        View.inflate(mContext, R.layout.layout_setting_descselect, SettingSelectedLayoutView.this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
            R.styleable.SettingSelectedLayoutView);
        String title = typedArray.getString(R.styleable.SettingSelectedLayoutView_settingselecttitle);
        mIsSelected = typedArray.getBoolean(R.styleable.SettingSelectedLayoutView_is_selected, false);
        isDesc = typedArray.getBoolean(R.styleable.SettingSelectedLayoutView_isdesc, false);
        descStr = typedArray.getString(R.styleable.SettingSelectedLayoutView_desc);
        settingNameTv = (TextView) this.findViewById(R.id.setting_name);
        statusIv = (ImageView) this.findViewById(R.id.setting_statusiv);
        headingTv = (TextView) this.findViewById(R.id.setting_heading);
        settingNameTv.setText(title);
        setStatus();
        if(!TextUtil.isEmpty(descStr)){
            headingTv.setText(descStr);
        }
        if(isDesc){
            headingTv.setVisibility(VISIBLE);
        }
        statusIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                if(mOnChangelistener!=null){
                    //Jenny 修改，用于必须设置了OnChangeListener才改变状态
                    mIsSelected =!mIsSelected;
                    setStatus();
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

}
