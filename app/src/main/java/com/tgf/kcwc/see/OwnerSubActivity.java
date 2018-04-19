package com.tgf.kcwc.see;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.app.SelectBrandActivity;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.CarBean;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.LoadView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：Jenny
 * Date:2016/12/16 14:11
 * E-mail：fishloveqin@gmail.com
 * 车型对比(PK)列表页面
 */

public class OwnerSubActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    protected LoadView           mLoadView;
    protected ListView           mPKList;
    protected Button             mStartPKBtn;
    protected TextView           mAddBtn;
    protected TextView           mEditBtn;
    private WrapAdapter<CarBean> mAdapter;
    private ArrayList<CarBean>   mDatas = new ArrayList<CarBean>();
    private TextView             mFunctionTextView;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText(R.string.moto_type_pk_text);
        text.setTextColor(mRes.getColor(R.color.white));
        mFunctionTextView = function.setTextResource(formatTitle(0), R.color.white, 14);
    }

    private String formatTitle(int current) {
        return String.format(getString(R.string.select_count_text), current, Constants.PK_MAX);
    }

    @Override
    protected void setUpViews() {

        initView();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isTitleBar = true;
        super.setContentView(R.layout.activity_onwer_sub);

        Intent intent = getIntent();
        // ArrayList<CarBean> datas = intent.getParcelableArrayListExtra(Constants.IntentParams.DATA);

    }

    private int mCurrentLength=0;
    @Override
    protected void onResume() {
        super.onResume();

        List<CarBean> datas = KPlayCarApp.getValue(Constants.KeyParams.PK_DATAS);
        if (datas != null) {

            int size = datas.size();
            mCurrentLength=size;
            for (int i = 0; i < size; i++) {
                CarBean m = datas.get(i);
                addData(datas.get(i));
            }
            mAdapter.notifyDataSetChanged();
            checkIsCanPK();
            mFunctionTextView.setText(formatTitle(datas.size()));
        }

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {
            case R.id.startPKBtn:
                StringBuilder builder = new StringBuilder();
                for (CarBean moto : mDatas) {
                    if (moto.isSelected) {
                        builder.append(moto.id).append(",");
                    }

                }
                String ids = builder.toString();
                ids = ids.substring(0, ids.length() - 1);
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.DATA, ids);
                CommonUtils.startNewActivity(mContext, args, OwnerContrastGoListActivity.class);
                break;
            case R.id.addBtn:

                if(mCurrentLength>=Constants.PK_MAX){

                    CommonUtils.showToast(mContext,"亲，最多只能添加"+Constants.PK_MAX+"辆车进行PK");
                    return;
                }
                KPlayCarApp.putValue(Constants.KeyParams.IS_CONTRAST,false);
                args = new HashMap<String, Serializable>();
                args.put(
                        Constants.IntentParams.MODULE_TYPE,
                        Constants.ModuleTypes.CONTRAST_PK);
                CommonUtils
                        .startNewActivity(
                                OwnerSubActivity.this,
                                args,
                                SelectBrandActivity.class);
                break;
            case R.id.editBtn:
                Intent intent = new Intent();
                //intent.putParcelableArrayListExtra(Constants.IntentParams.DATA, mDatas);
                intent.setClass(mContext, OwnerSubEditActivity.class);
                startActivityForResult(intent, Constants.InteractionCode.REQUEST_CODE);
                break;
        }
    }

    private void initView() {
        mLoadView = (LoadView) findViewById(R.id.loadView);
        mPKList = (ListView) findViewById(R.id.rv);
        mStartPKBtn = (Button) findViewById(R.id.startPKBtn);
        mAddBtn = (TextView) findViewById(R.id.addBtn);
        mEditBtn = (TextView) findViewById(R.id.editBtn);
        mStartPKBtn.setOnClickListener(this);
        mStartPKBtn.setSelected(false);
        mStartPKBtn.setEnabled(false);
        mAddBtn.setOnClickListener(this);
        mEditBtn.setOnClickListener(this);
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
        mPKList.setAdapter(mAdapter);
        mPKList.setOnItemClickListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        KPlayCarApp.removeValue(Constants.KeyParams.CAR_MODEL_KEY);
    }

    private void addData(CarBean moto) {

        //遍历一次，根据Id去重
        for (CarBean m : mDatas) {

            if (m.id == moto.id) {

                return;
            }
        }
        mDatas.add(moto);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CarBean moto = (CarBean) parent.getAdapter().getItem(position);
        if (moto.isSelected) {
            moto.isSelected = false;
        } else {
            moto.isSelected = true;
        }
        mAdapter.notifyDataSetChanged();

        checkIsCanPK();
    }

    private int selectNum() {

        int size = mDatas.size();
        int count = 0;
        for (int i = 0; i < size; i++) {
            CarBean moto = mDatas.get(i);
            if (moto.isSelected) {
                count++;
            }
        }
        return count;
    }

    private void checkIsCanPK() {
        int count = selectNum();
        //mFunctionTextView.setText(formatTitle(count));
        if (count < PK_MIN_VALUE) {
            mStartPKBtn.setSelected(false);
            mStartPKBtn.setEnabled(false);
        } else {
            mStartPKBtn.setSelected(true);
            mStartPKBtn.setEnabled(true);
        }
    }

    private static final int PK_MIN_VALUE = 2;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Constants.InteractionCode.REQUEST_CODE == requestCode && RESULT_OK == resultCode) {

            ArrayList<CarBean> unSelectItems = data
                .getParcelableArrayListExtra(Constants.IntentParams.DATA);

            int size = unSelectItems.size();

            int totalSize = mDatas.size();
            for (int i = 0; i < totalSize; i++) {

                CarBean m1 = mDatas.get(i);
                for (int j = 0; j < size; j++) {
                    CarBean m2 = unSelectItems.get(j);
                    if (m1.id == m2.id) {
                        m2.isSelected = m1.isSelected;
                    }
                }

            }

            mDatas.clear();
            mDatas.addAll(unSelectItems);
            // mFunctionTextView.setText(formatTitle(selectNum(), mDatas.size()));
            checkIsCanPK();
            mAdapter.notifyDataSetChanged();
        }

    }
}
