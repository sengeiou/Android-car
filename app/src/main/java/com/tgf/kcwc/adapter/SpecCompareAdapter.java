package com.tgf.kcwc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.mvp.model.ParamContrast;
import com.tgf.kcwc.mvp.model.ValueItem;
import com.tgf.kcwc.view.DrawableCenterTextView;
import com.tgf.kcwc.view.VSScrollView;
import com.tgf.kcwc.view.pinnedheader.SectionedBaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：Jenny
 * Date:2016/12/20 19:41
 * E-mail：fishloveqin@gmail.com
 */

public class SpecCompareAdapter extends SectionedBaseAdapter {

    private FrameLayout            mRlListHeader;
    private DrawableCenterTextView mListHeaderTitle;
    private TextView               mTvStandardTips;
    private TextView               mTvHollowTips;
    private ImageView              mHeadTipsDivider;
    private List<ParamContrast>    mEntitys;

    public SpecCompareAdapter(List<ParamContrast> entitys) {
        this.mEntitys = entitys;
    }

    @Override
    public Object getItem(int section, int position) {
        return null;
    }

    @Override
    public long getItemId(int section, int position) {
        return 0;
    }

    @Override
    public int getSectionCount() {
        return mEntitys.size();
    }

    @Override
    public int getCountForSection(int section) {
        int size = mEntitys.get(section).items.size();
        return size;
    }

    private View.OnTouchListener mOnTouchListener;

    public void setSpecItemTouchListener(View.OnTouchListener listener) {
        this.mOnTouchListener = listener;
    }

    public HorizontalScrollView mTouchView;

    public interface ScrollCallback {

        void callback(int l, int t, int oldl, int oldt);
    }

    public ScrollCallback     mScrollCallback;

    public List<VSScrollView> mVSViews = new ArrayList<VSScrollView>();

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {

        LayoutInflater inflator = (LayoutInflater) parent.getContext()
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder1 holder = null;
        if (convertView == null) {
            convertView = inflator.inflate(R.layout.owner_contrast_model_item, null);
            VSScrollView itemScrollView = (VSScrollView) convertView
                .findViewById(R.id.footer_content_scroll);
            holder = new ViewHolder1();
            holder.mVScrollView = itemScrollView;
            mVSViews.add(itemScrollView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder1) convertView.getTag();
        }
        holder.mVScrollView.mTouchView = mTouchView;
        holder.mVScrollView.setScrollChangedListener(new VSScrollView.ScrollChangedListener() {
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                if (mScrollCallback != null) {
                    mScrollCallback.callback(l, t, oldl, oldt);
                }

            }
        });

        TextView titleText = (TextView) convertView.findViewById(R.id.item_title);

        ParamContrast conditionEntity = mEntitys.get(section);
        List<ParamContrast.SpecItemEntity> specItemEntities = conditionEntity.items;
        ParamContrast.SpecItemEntity entity = specItemEntities.get(position);

        titleText.setText(entity.name);
        LinearLayout layout = (LinearLayout) convertView
            .findViewById(R.id.ll_contrast_model_footer_content);
        layout.removeAllViews();
        if (mAddItemViewListener != null) {

            mAddItemViewListener.addItemView(layout);
        }
        int size = entity.valueItems.size();
        for (int i = 0; i < size; i++) {
            ValueItem entity1 = entity.valueItems.get(i);
            View v = inflator.inflate(R.layout.owner_contrast_model_item_footer, layout, false);
            TextView nameText = (TextView) v.findViewById(R.id.tv_item_footer_name);
            TextView enquiryText = (TextView) v.findViewById(R.id.tv_item_footer_btn);
            enquiryText.setVisibility(View.GONE);
            nameText.setText(entity1.content);
            layout.addView(v);
        }
        if (size >= mMarginIndex) {
            View v = inflator.inflate(R.layout.owner_contrast_model_item_footer, layout, false);
            v.findViewById(R.id.tv_item_footer_btn).setVisibility(View.GONE);
            layout.addView(v);
        }

        return convertView;
    }

    private final int               mMarginIndex         = 2;   //填充空的item项，方便联动时可以同步
    private AddSpecItemViewListener mAddItemViewListener = null;

    private void addItemListener(AddSpecItemViewListener listener) {
        this.mAddItemViewListener = listener;
    }

    public interface AddSpecItemViewListener {

        public void addItemView(ViewGroup root);
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {

        ParamContrast c = mEntitys.get(section);
        WrapAdapter.ViewHolder holder = WrapAdapter.ViewHolder.get(parent.getContext(), convertView,
            parent, R.layout.vs_compare_header, section);

        holder.setText(R.id.list_header_title, c.title);
        return holder.getConvertView();
    }

    static class ViewHolder1 {
        VSScrollView mVScrollView;
        LinearLayout mLinearContent;
        TextView     txtName;
    }

}
