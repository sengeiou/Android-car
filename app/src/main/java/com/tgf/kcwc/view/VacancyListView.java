package com.tgf.kcwc.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.util.StringUtils;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.link.Link;

/**
 * Created by Administrator on 2017/6/6.
 */

public class VacancyListView extends LinearLayout {

    private Context mContext;

    private TextView mHintText; //提示文本

    private TextView mOperation; //操作

    private ImageView mImager; //提示图片

    protected Resources mRes;

    public VacancyListView(Context context) {
        super(context);
        setLayout(context);
    }

    public VacancyListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayout(context);
        setAttr(context, attrs);
    }

    public VacancyListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayout(context);
        setAttr(context, attrs);
    }


    public void setLayout(Context context) {
        mContext = context;
        mRes = mContext.getResources();
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.vacancyview, this);
        mHintText = (TextView) findViewById(R.id.hinttext);
        mOperation = (TextView) findViewById(R.id.operation);
    }

    /**
     * 获取xml信息
     */
    private void setAttr(Context context, AttributeSet attrs) {
        /*获取在xml中配置的属性值*/
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VacancyListView);

        String string = array.getString(R.styleable.VacancyListView_hintText);
        if (StringUtils.checkNull(string)) {
            mHintText.setText(string);
        } else {
            mHintText.setText("暂时没有数据");
        }

    }

    public void setOperation(String text, String select,int color, Link.OnClickListener onClickListener) {
        if (StringUtils.checkNull(text)) {
            mOperation.setVisibility(VISIBLE);
            mOperation.setText(text);
            ViewUtil.link(select, mOperation, onClickListener, mRes.getColor(R.color.text_color6), true);
        } else {
            mOperation.setVisibility(GONE);
        }

    }

    public void setmImager(int img){
        mImager.setImageResource(img);
    }

    public void setmHintText(String text){
        mHintText.setText(text);
        mOperation.setVisibility(GONE);
    }

}
