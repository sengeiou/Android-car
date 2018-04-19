package com.tgf.kcwc.play.topic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.BaseArryBean;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.FoundTypeBean;
import com.tgf.kcwc.mvp.presenter.FoundTopicPresenter;
import com.tgf.kcwc.mvp.view.FoundTopicView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.DropDownFoundTopicSpinner;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/16.
 */

public class FoundTopicTypeActivity extends BaseActivity implements FoundTopicView {

    private List<DataItem> mDataList = new ArrayList<>();
    private List<DataItem> mDataListRight = new ArrayList<>();
    private FoundTopicPresenter mFoundTopicPresenter;
    private ListView kilPopListViewLeft;
    private ListView kilPopListViewRight;

    private WrapAdapter<DataItem> mKilometerLeftAdapter;
    private WrapAdapter<DataItem> mKilometerRightAdapter;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFoundTopicPresenter!=null){
            mFoundTopicPresenter.detachView();
        }
    }

    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("分类");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foundtype_filternear_list);
        kilPopListViewLeft = (ListView) findViewById(R.id.popwin_supplier_listleft_lv);
        kilPopListViewRight = (ListView) findViewById(R.id.popwin_supplier_listright_lv);
        mFoundTopicPresenter = new FoundTopicPresenter();
        mFoundTopicPresenter.attachView(this);
        initAdapter();
        mFoundTopicPresenter.GetType(IOUtils.getToken(mContext), "");
    }

    private void initAdapter() {
        mKilometerLeftAdapter = new WrapAdapter<DataItem>(mContext, mDataList,
                R.layout.item_listview_popwin) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {
                TextView tv = helper.getView(R.id.listview_popwind_tv);
                tv.setText(item.name);
                LinearLayout ll = helper.getView(R.id.listitem_popwind_ll);
                if (item.isSelected) {
                    ll.setBackgroundColor(mRes.getColor(R.color.white));
                } else {
                    ll.setBackgroundColor(mRes.getColor(R.color.voucher_divide_line));
                }
            }
        };
        mKilometerRightAdapter = new WrapAdapter<DataItem>(mContext, mDataListRight,
                R.layout.item_listview_popwin) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {
                TextView tv = helper.getView(R.id.listview_popwind_tv);
                tv.setText(item.name);
                View view = helper.getView(R.id.select_status_img);
                if (item.isSelected) {
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.INVISIBLE);
                }
            }
        };

        kilPopListViewLeft.setAdapter(mKilometerLeftAdapter);
        kilPopListViewLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataItem selectItem = mDataList.get(position);
                for (DataItem data1 : mDataList) {
                    data1.isSelected=false;
                }
                selectItem.isSelected = true;
                mFoundTopicPresenter.GetTyper(IOUtils.getToken(mContext), selectItem.id + "");
                mKilometerLeftAdapter.notifyDataSetChanged();
            }
        });

        kilPopListViewRight.setAdapter(mKilometerRightAdapter);
        kilPopListViewRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataItem selectItem = mDataListRight.get(position);
                selectItem.isSelected = true;
                mKilometerLeftAdapter.notifyDataSetChanged();
                back(selectItem);
            }
        });
    }

    public void back( DataItem selectItem){
        Intent intent = new Intent();
        intent.putExtra(Constants.IntentParams.DATA, selectItem);
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void dataSucceed(BaseBean baseArryBean) {

    }

    @Override
    public void typeListSucceed(FoundTypeBean topicBean) {
        mDataList.clear();
        List<FoundTypeBean.Data> data = topicBean.data;
        for (FoundTypeBean.Data data1 : data) {
            DataItem dataItem = new DataItem();
            dataItem.name = data1.name;
            dataItem.id = data1.id;
            dataItem.title = data1.categoryType;
            mDataList.add(dataItem);
        }
        if (mDataList.size() != 0) {
            mDataList.get(0).isSelected = true;
            mFoundTopicPresenter.GetTyper(IOUtils.getToken(mContext), mDataList.get(0).id + "");
        }
        mKilometerLeftAdapter.notifyDataSetChanged();
    }

    @Override
    public void typerListSucceed(FoundTypeBean topicBean) {
        mDataListRight.clear();
        List<FoundTypeBean.Data> data = topicBean.data;
        for (FoundTypeBean.Data data1 : data) {
            DataItem dataItem = new DataItem();
            dataItem.name = data1.name;
            dataItem.id = data1.id;
            dataItem.title = data1.categoryType;
            mDataListRight.add(dataItem);
        }
        mKilometerRightAdapter.notifyDataSetChanged();
    }

    @Override
    public void dataListDefeated(String msg) {
        CommonUtils.showToast(mContext, msg);
    }

    @Override
    public Context getContext() {
        return mContext;
    }
}
