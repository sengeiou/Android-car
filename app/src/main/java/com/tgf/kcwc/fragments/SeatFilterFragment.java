package com.tgf.kcwc.fragments;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.entity.DataItem;

import java.util.ArrayList;

/**
 * Author：Jenny
 * Date:2017/4/12 09:43
 * E-mail：fishloveqin@gmail.com
 */

public class SeatFilterFragment extends BaseFragment {
    private String[]              seatArrays;

    private ArrayList<DataItem>   mDatas                 = new ArrayList<DataItem>();
    private ListView              filterSeatList;
    private WrapAdapter<DataItem> mFilterSeatListAdapter = null;

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_filter_seat;
    }

    @Override
    protected void initView() {

        filterSeatList = findView(R.id.filterSeatList);
        filterSeatList.setOnItemClickListener(mFilterSeatItemListener);
        initFilterListsData();
        bindAdapter();

    }

    private void initFilterListsData() {

        seatArrays = mRes.getStringArray(R.array.seat_values);
        int size = 0;

        //先清空集合数据，防止重复增加
        mDatas.clear();
        size = seatArrays.length;
        for (int i = 0; i < size; i++) {
            DataItem dataItem = new DataItem();
            dataItem.name = seatArrays[i];
            dataItem.id = (i + 1);
            dataItem.key = i+"";
            dataItem.type = R.array.seat_values;
            mDatas.add(dataItem);
        }
    }

    private void bindAdapter() {

        mFilterSeatListAdapter = new WrapAdapter<DataItem>(mContext, mDatas,
            R.layout.common_list_item) {
            @Override
            public void convert(ViewHolder holder, DataItem item) {
                TextView titleText = holder.getView(R.id.title);
                titleText.setText(item.name);
                ImageView imageView = holder.getView(R.id.select_status_img);
                if (item.isSelected) {
                    imageView.setVisibility(View.VISIBLE);
                    titleText.setTextColor(mRes.getColor(R.color.text_color6));
                } else {
                    imageView.setVisibility(View.GONE);
                    titleText.setTextColor(mRes.getColor(R.color.text_color12));
                }
            }
        };
        filterSeatList.setAdapter(mFilterSeatListAdapter);
    }

    private AdapterView.OnItemClickListener mFilterSeatItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DataItem item = (DataItem) parent.getAdapter().getItem(position);

            if (mCallback != null) {
                mCallback.refresh(item);
            }

        }
    };
}
