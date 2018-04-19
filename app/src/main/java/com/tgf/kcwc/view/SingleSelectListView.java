package com.tgf.kcwc.view;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.entity.DataItem;

import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/1/13 0013
 * E-mail:hekescott@qq.com
 */

public class SingleSelectListView {
    private Activity               context;
    private List<DataItem>        dataItems;
    private ListView              listView;
    private WrapAdapter<DataItem> singleSelectAdapter;
    private SelcetedLisenter mSelcetedLisenter;

    public SingleSelectListView(Activity context, List<DataItem> dataItems, ListView listView) {
        this.listView = listView;
        this.context = context;
        this.dataItems = dataItems;
        initAdapter();
        initView();
    }

    private void initAdapter() {
        singleSelectAdapter = new WrapAdapter<DataItem>(context, R.layout.item_listview_popwin,
            dataItems) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {
                TextView tv = helper.getView(R.id.listview_popwind_tv);
                tv.setText(item.name);
                ImageView imageView = helper.getView(R.id.select_status_img);
                if (item.isSelected) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.INVISIBLE);
                }
            }
        };
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataItem dataItem = dataItems.get(position);
                dataItem.isSelected = true;
                singleChecked(dataItems, dataItem);

                singleSelectAdapter.notifyDataSetChanged();
                mSelcetedLisenter.onSelected(position);
            }
        });

    }

    private void initView() {
        listView.setAdapter(singleSelectAdapter);
    }

    private void singleChecked(List<DataItem> items, DataItem item) {
        for (DataItem d : items) {
            if (d.id != item.id) {
                d.isSelected = false;
            }
        }
    }

    public  interface SelcetedLisenter{
        void onSelected(int selectPos);
    }
    public  void setSelcetedLisenter(SelcetedLisenter selcetedLisenter){
        this.mSelcetedLisenter =selcetedLisenter;

    }
}
