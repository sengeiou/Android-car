package com.tgf.kcwc.me;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.SaleMgrModel;
import com.tgf.kcwc.mvp.presenter.OwnerSalePresenter;
import com.tgf.kcwc.mvp.view.OwnerSaleDataView;
import com.tgf.kcwc.see.sale.MsgRecordsActivity;
import com.tgf.kcwc.see.sale.release.ReleaseSaleActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.StringUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：Jenny
 * Date:2017/2/27 15:12
 * E-mail：fishloveqin@gmail.com
 */

public class SaleMgrListActivity extends BaseActivity implements OwnerSaleDataView<SaleMgrModel> {
    protected TextView mTotalRecords;
    protected TextView mFilterTitle;
    protected ImageView mFilterImg;
    protected ListView mList;
    private RelativeLayout mFilterLayout;
    private ListView mFilterItemList;
    private List<DataItem> mDataItems;
    private WrapAdapter<DataItem> mFilterAdapter;
    private OwnerSalePresenter mPresenter;
    private OwnerSalePresenter mUnShelvePresenter;
    private WrapAdapter<SaleMgrModel.ListBean> mAdapter;
    private String mStatus = "";
    private Button mSaleBtn;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("车主自售");
    }

    @Override
    protected void setUpViews() {

        initView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.activity_sale_mgr_list);

    }

    private void initView() {
        mPresenter = new OwnerSalePresenter();
        mPresenter.attachView(this);
        mUnShelvePresenter = new OwnerSalePresenter();
        mUnShelvePresenter.attachView(mUnShelveView);
        mFilterLayout = (RelativeLayout) findViewById(R.id.filterLayout);
        mFilterLayout.setOnClickListener(this);
        mTotalRecords = (TextView) findViewById(R.id.totalRecords);
        mSaleBtn = (Button) findViewById(R.id.saleBtn);
        mSaleBtn.setOnClickListener(this);
        mFilterTitle = (TextView) findViewById(R.id.filterTitle);
        mFilterImg = (ImageView) findViewById(R.id.filterImg);
        mList = (ListView) findViewById(R.id.list);
        mFilterItemList = (ListView) findViewById(R.id.filterItemList);
        mFilterItemList.setOnItemClickListener(mFilterItemListener);
        initFilterData();

    }

    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.saleMgrList(mStatus, IOUtils.getToken(mContext));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    private int mSelect = 0;

    private OwnerSaleDataView<List<String>> mUnShelveView = new OwnerSaleDataView<List<String>>() {
        @Override
        public void showData(List<String> s) {

            mPresenter.saleMgrList("",
                    IOUtils.getToken(mContext));

            CommonUtils.showToast(mContext,
                    "下架成功");

        }

        @Override
        public void setLoadingIndicator(boolean active) {

        }

        @Override
        public void showLoadingTasksError() {

        }

        @Override
        public Context getContext() {
            return mContext;
        }
    };

    private void initFilterData() {
        mDataItems = new ArrayList<DataItem>();
        String[] arrays = mRes.getStringArray(R.array.sale_mgr_filter_items);

        int id = 0;
        for (String s : arrays) {
            DataItem dataItem = new DataItem();
            dataItem.name = s;
            dataItem.id = id;
            mDataItems.add(dataItem);
            id++;
        }
        mFilterAdapter = new WrapAdapter<DataItem>(mContext, R.layout.common_list_item,
                mDataItems) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {

                TextView tv = helper.getView(R.id.title);
                ImageView imageView = helper.getView(R.id.select_status_img);
                if (item.isSelected) {
                    imageView.setVisibility(View.VISIBLE);
                    tv.setTextColor(mRes.getColor(R.color.text_color6));
                } else {
                    imageView.setVisibility(View.GONE);
                    tv.setTextColor(mRes.getColor(R.color.text_color12));
                }
                tv.setText(item.name);

            }
        };
        mFilterItemList.setAdapter(mFilterAdapter);

    }

    private AdapterView.OnItemClickListener mFilterItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            DataItem dataItem = (DataItem) parent.getAdapter().getItem(position);
            dataItem.isSelected = true;
            singleChecked(mDataItems, dataItem);
            mFilterTitle.setText(dataItem.name);
            mFilterLayout.setTag(false);
            mFilterImg.setSelected(false);
            mFilterItemList.setVisibility(View.GONE);
            mFilterAdapter.notifyDataSetChanged();
            switch (position) {
                case 0:
                    mStatus = "";
                    break;
                case 1:
                    mStatus = "1";
                    break;
                case 2:
                    mStatus = "0";
                    break;
            }
            mPresenter.saleMgrList(mStatus, IOUtils.getToken(mContext));
        }
    };

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {
            case R.id.filterLayout:

                Object tag = view.getTag();
                boolean flag = false;
                if (tag != null) {
                    flag = Boolean.parseBoolean(tag.toString());
                }
                if (flag) {
                    view.setTag(false);
                    mFilterItemList.setVisibility(View.GONE);
                    mFilterImg.setSelected(false);
                } else {
                    view.setTag(true);
                    mFilterItemList.setVisibility(View.VISIBLE);
                    mFilterImg.setSelected(true);
                }
                break;

            case R.id.saleBtn:

                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, -1);
                CommonUtils.startNewActivity(mContext, args, ReleaseSaleActivity.class);
                break;
        }

    }

    @Override
    public void showData(SaleMgrModel saleMgrModel) {

        mTotalRecords.setText(String.format(mRes.getString(R.string.total_records),
                saleMgrModel.pagination.count + ""));
        mAdapter = new WrapAdapter<SaleMgrModel.ListBean>(mContext, R.layout.sale_mgr_list_item,
                saleMgrModel.list) {
            Button functionEditBtn;
            Button contactsBtn;
            TextView time;
            TextView commentsTv;
            TextView incentiveTv;
            TextView skimTv;
            TextView price;
            TextView moneyTag;
            TextView title;
            SimpleDraweeView img;
            ImageView goodsStatusView;

            @Override
            public void convert(ViewHolder helper, final SaleMgrModel.ListBean item) {

                img = helper.getView(R.id.img);
                title = helper.getView(R.id.title);
                moneyTag = helper.getView(R.id.moneyTag);
                price = helper.getView(R.id.price);
                skimTv = helper.getView(R.id.skimTv);
                incentiveTv = helper.getView(R.id.incentiveTv);
                commentsTv = helper.getView(R.id.commentsTv);
                time = helper.getView(R.id.time);
                contactsBtn = helper.getView(R.id.contactsBtn);
                contactsBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        args.put(Constants.IntentParams.ID, item.id);
                        args.put(Constants.IntentParams.TITLE, item.title);
                        CommonUtils.startNewActivity(mContext, args, MsgRecordsActivity.class);

                    }
                });
                functionEditBtn = helper.getView(R.id.functionEditBtn);
                goodsStatusView = helper.getView(R.id.goodsStatus);

                final int shelve = item.isGrounding;
                if (shelve == 0) {

                    goodsStatusView.setVisibility(View.VISIBLE);
                } else {
                    goodsStatusView.setVisibility(View.GONE);
                }

                if (Constants.GoodsStatus.SHELVE == shelve) {
                    functionEditBtn.setText("下架");
                    functionEditBtn.setBackgroundResource(R.drawable.button_bg_5);
                    functionEditBtn.setTextColor(mRes.getColor(R.color.text_color4));
                } else {
                    functionEditBtn.setText("编辑重发");
                    functionEditBtn.setBackgroundResource(R.drawable.button_bg_3);
                    functionEditBtn.setTextColor(mRes.getColor(R.color.white));
                }

                functionEditBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Constants.GoodsStatus.SHELVE == shelve) {

                            mUnShelvePresenter.unShelve(item.id + "", IOUtils.getToken(mContext));
                        } else {

                            Map<String, Serializable> args = new HashMap<String, Serializable>();
                            args.put(Constants.IntentParams.ID, item.id);
                            CommonUtils.startNewActivity(mContext, args, ReleaseSaleActivity.class);
                        }

                    }
                });
                img.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 270, 203)));
                title.setText(item.title + "");
                String priceStr = CommonUtils.getMoneyType(item.price + "");
                if (item.price.equals("0")) {
                    priceStr = "面议";
                    moneyTag.setText("");
                } else {
                    moneyTag.setText("￥");
                }
                price.setText(priceStr);
                skimTv.setText(item.viewCount + "");
                commentsTv.setText(item.replyCount + "");
                incentiveTv.setText(item.likeCount + "");
                time.setText(item.createTime + "");
            }
        };
        mList.setAdapter(mAdapter);
    }

    @Override
    public void setLoadingIndicator(boolean active) {

        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

        dismissLoadingDialog();
    }

    @Override
    public Context getContext() {
        return mContext;
    }
}
