package com.tgf.kcwc.see;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.CarBean;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.view.FunctionView;

import java.util.ArrayList;

/**
 * Author：Jenny
 * Date:2016/12/16 14:11
 * E-mail：fishloveqin@gmail.com
 * 车型对比(PK)列表页面
 */

public class OwnerSubEditActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private TextView             mCancel;
    private TextView             mSelectAll;
    private ListView             mList;
    private TextView             mDeleteBtn;
    private TextView             mResetBtn;
    private ArrayList<CarBean>   mDatas;
    private WrapAdapter<CarBean> mAdapter;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    protected void setUpViews() {
        initView();
        mDatas = KPlayCarApp.getValue(Constants.KeyParams.PK_DATAS);

        if (mDatas == null) {
            mDatas = new ArrayList<CarBean>();
            KPlayCarApp.putValue(Constants.KeyParams.PK_DATAS, mDatas);
        }
        mAdapter = new WrapAdapter<CarBean>(mContext, mDatas, R.layout.spec_compare_list_item) {
            @Override
            public void convert(ViewHolder helper, CarBean item) {

                TextView nameTv = helper.getView(R.id.name);
                nameTv.setText(item.name);
                ImageView imageView = helper.getView(R.id.checkImg);
                if (item.isSelected) {
                    imageView.setImageResource(R.drawable.item_check_s);
                    nameTv.setSelected(true);
                } else {
                    imageView.setImageResource(R.drawable.item_check_n);
                    nameTv.setSelected(false);
                }
            }
        };
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isTitleBar = false;
        super.setContentView(R.layout.activity_onwer_sub_edit);

    }

    /**
     * 全选
     */
    private void selectAll(String title) {
        int size = mDatas.size();
        for (int i = 0; i < size; i++) {
            CarBean m = mDatas.get(i);
            m.isSelected = true;
        }
        mAdapter.notifyDataSetChanged();
        mSelectAll.setText(title);

    }

    /**
     * 取消全选
     */
    private void inverseAll(String title) {
        int size = mDatas.size();
        for (int i = 0; i < size; i++) {
            CarBean m = mDatas.get(i);
            m.isSelected = false;
        }
        mAdapter.notifyDataSetChanged();
        mSelectAll.setText(title);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {
            case R.id.selectAll:
                Object tag = view.getTag();
                boolean flag = false;
                if (tag != null) {
                    flag = Boolean.valueOf(tag.toString());
                }
                if (flag) {
                    view.setTag(false);
                    inverseAll(getString(R.string.select_all));
                } else {
                    view.setTag(true);
                    selectAll(getString(R.string.canecl_select_all));

                }
                break;
            case R.id.deleteBtn:
                deleteItems();
                break;
            case R.id.resetBtn:
                inverseAll("全选");
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CarBean moto = (CarBean) parent.getAdapter().getItem(position);
        if (moto.isSelected) {
            moto.isSelected = false;
        } else {
            moto.isSelected = true;
        }
        int size = mDatas.size();
        int count = 0;
        for (int i = 0; i < size; i++) {
            CarBean m = mDatas.get(i);
            if (!m.isSelected) {
                count++;
            }
        }
        if (count > 0) {
            mSelectAll.setText(R.string.select_all);
            mSelectAll.setTag(false);
        } else {
            mSelectAll.setText(R.string.canecl_select_all);
            mSelectAll.setTag(true);
        }

        mAdapter.notifyDataSetChanged();

    }

    private void initView() {
        mCancel = (TextView) findViewById(R.id.cancel);
        mSelectAll = (TextView) findViewById(R.id.selectAll);
        mList = (ListView) findViewById(R.id.list);
        mDeleteBtn = (TextView) findViewById(R.id.deleteBtn);
        mResetBtn = (TextView) findViewById(R.id.resetBtn);
        mCancel.setOnClickListener(this);
        mSelectAll.setOnClickListener(this);
        mDeleteBtn.setOnClickListener(this);
        mResetBtn.setOnClickListener(this);
    }

    private void deleteItems() {

        ArrayList<CarBean> unSelectItems = new ArrayList<CarBean>();

        int size = mDatas.size();
        for (int i = 0; i < size; i++) {
            CarBean m = mDatas.get(i);
            if (!m.isSelected) {
                unSelectItems.add(m);
            }
        }

        if (size == unSelectItems.size()) {
            CommonUtils.showToast(mContext, R.string.delete_prompt_msg);
        } else {

            mDatas.clear();
            mDatas.addAll(unSelectItems);
            Intent intent = getIntent();
            intent.putParcelableArrayListExtra(Constants.IntentParams.DATA, unSelectItems);
            setResult(RESULT_OK, intent);
            finish();
            mAdapter.notifyDataSetChanged();
        }
    }
}
