package com.tgf.kcwc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.mvp.model.DiscountContrastModel;
import com.tgf.kcwc.view.VSScrollView;
import com.tgf.kcwc.view.pinnedheader.SectionedBaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：Jenny
 * Date:2016/8/9 11:41
 * E-mail：fishloveqin@gmail.com
 * <p>
 * 优惠对比Adapter
 */

public class DiscountCompareAdapter extends SectionedBaseAdapter {

    private TextView mTvStandardTips;
    private TextView mTvHollowTips;
    private ImageView mHeadTipsDivider;
    private List<DiscountContrastModel.ParamBean> mEntitys;

    public DiscountCompareAdapter(List<DiscountContrastModel.ParamBean> entitys) {
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
        int size = mEntitys.get(section).value.size();
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

    public ScrollCallback mScrollCallback;

    public List<VSScrollView> mVSViews = new ArrayList<VSScrollView>();

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {

        WrapAdapter.ViewHolder viewHolder = WrapAdapter.ViewHolder.get(parent.getContext(), convertView, parent, R.layout.owner_contrast_model_item2, position);


        DiscountContrastModel.ParamBean conditionEntity = mEntitys.get(section);
        List<DiscountContrastModel.ValueBean> specItemEntities = conditionEntity.value;
        DiscountContrastModel.ValueBean entity = specItemEntities.get(position);
        String name = conditionEntity.name;
        int type = 0;
        switch (name) {
            case "代金券":
                type = 1;
                break;
            case "限时优惠":
                type = 2;
                break;

        }
        setItemView(viewHolder, entity, type, position);
        return viewHolder.getConvertView();
    }


    private void setItemView(WrapAdapter.ViewHolder holder, DiscountContrastModel.ValueBean entity, int type, int position) {


        VSScrollView itemScrollView = holder.getView(R.id.footer_content_scroll);
        mVSViews.add(itemScrollView);
        Context context = holder.getConvertView().getContext();
        itemScrollView.mTouchView = mTouchView;
        itemScrollView.setScrollChangedListener(new VSScrollView.ScrollChangedListener() {
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                if (mScrollCallback != null) {
                    mScrollCallback.callback(l, t, oldl, oldt);
                }

            }
        });

        TextView titleText = holder.getView(R.id.item_title);
        titleText.setText(entity.name);
        LinearLayout layout = holder.getView
                (R.id.ll_contrast_model_footer_content);


        layout.removeAllViews();
        if (mAddItemViewListener != null) {

            mAddItemViewListener.addItemView(layout);
        }
        int size = entity.value.size();

        LayoutInflater inflater = LayoutInflater.from(holder.getConvertView().getContext());
        for (int i = 0; i < size; i++) {


            String entity1 = entity.value.get(i);
            View v = inflater.inflate(R.layout.owner_contrast_model_item_footer2, layout, false);
            TextView nameText = (TextView) v.findViewById(R.id.tv_item_footer_name);
            TextView enquiryText = (TextView) v.findViewById(R.id.tv_item_footer_btn);
            enquiryText.setVisibility(View.GONE);
            nameText.setText(entity1);
            if (type == 1) {
                if (position % 3 == 0) {
                    nameText.setTextColor(context.getResources().getColor(R.color.red));
                } else {
                    nameText.setTextColor(context.getResources().getColor(R.color.text_color17));
                }
            } else if (type == 2) {
                if (position % 2 != 0) {
                    if (!"-".equals(entity1.trim())) {
                        nameText.setText("\u2193 ￥" + entity1);
                    }
                    nameText.setTextColor(context.getResources().getColor(R.color.red));
                } else {
                    nameText.setTextColor(context.getResources().getColor(R.color.text_color17));
                }
            }
            layout.addView(v);
        }


    }

    private final int mMarginIndex = 3;   //填充空的item项，方便联动时可以同步
    private AddSpecItemViewListener mAddItemViewListener = null;

    private void addItemListener(AddSpecItemViewListener listener) {
        this.mAddItemViewListener = listener;
    }

    public interface AddSpecItemViewListener {

        public void addItemView(ViewGroup root);
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {

        DiscountContrastModel.ParamBean c = mEntitys.get(section);
        WrapAdapter.ViewHolder holder = WrapAdapter.ViewHolder.get(parent.getContext(), convertView,
                parent, R.layout.vs_compare_header2, section);

        holder.setText(R.id.list_header_title, c.name);
        return holder.getConvertView();
    }


}
