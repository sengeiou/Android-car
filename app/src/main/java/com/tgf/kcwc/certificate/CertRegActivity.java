package com.tgf.kcwc.certificate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.FactoryBrandActivity;
import com.tgf.kcwc.app.MainActivity;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.Brand;
import com.tgf.kcwc.mvp.model.CertDetailModel;
import com.tgf.kcwc.mvp.model.Form;
import com.tgf.kcwc.mvp.presenter.CertDataPresenter;
import com.tgf.kcwc.mvp.view.CertDataView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：Jenny
 * Date:2017/3/27 19:12
 * E-mail：fishloveqin@gmail.com
 */

public class CertRegActivity extends BaseActivity implements CertDataView<CertDetailModel>,
        AdapterView.OnItemClickListener {

    protected ListView mList;
    private CertDataPresenter mPresenter;
    private CertDataPresenter mCmtPresenter;
    private String mCode;
    private TextView mTitleTv;
    private SimpleDraweeView mCover;
    private CertRegAdapter mAdapter;
    private List<Form> mForms = new ArrayList<Form>();
    private int brandIndex = -1;
    private int orgIndex = 5;
    private String mCId = "";                   //证件id

    private Button mCmtBtn;

    private RelativeLayout mBottomLayout, mErrorLayout;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        mTitleTv = text;
        backEvent(back);
    }

    @Override
    protected void setUpViews() {
        initView();
        mPresenter = new CertDataPresenter();
        mPresenter.attachView(this);
        mPresenter.loadCertDetailDatas("1", "", "1", "" + mCode, IOUtils.getToken(mContext));
        mCmtPresenter = new CertDataPresenter();
        mCmtPresenter.attachView(mCmtDataView);
    }

    private void initFormDatas(boolean isNeedBrand) {

        String[] arrays = null;
        if (isNeedBrand) {
            arrays = mRes.getStringArray(R.array.cert_reg_values);
        } else {
            arrays = mRes.getStringArray(R.array.cert_reg_values_2);
        }

        int length = arrays.length;
        for (int i = 0; i < length; i++) {

            Form f = new Form();
            f.desc = "";
            f.name = arrays[i];
            switch (i) {
                case 0:
                case 1:
                case 2:
                case 3:

                    if (i == 0 || i == 1) {
                        f.isEnabled = false;
                    }
                    if (i == 2) {
                        f.hintContent = "请输入手机号";
                    }
                    if (i == 3) {
                        f.hintContent = "请输入姓名";
                    }

                    f.layoutId = R.layout.base_info_item_1;
                    f.viewTypeId = 0;
                    break;
                case 4:
                case 5:
                    f.layoutId = R.layout.base_info_item_3;
                    f.viewTypeId = 1;
                    break;
            }
            mForms.add(f);
        }
        mAdapter = new CertRegAdapter();
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCode = getIntent().getStringExtra(Constants.IntentParams.DATA);
        super.setContentView(R.layout.activity_cert_reg);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (mCmtPresenter != null) {
            mCmtPresenter.detachView();
        }
    }

    @Override
    public void showData(CertDetailModel certDetailModel) {

        int needBrand = certDetailModel.detail.needBrand;
        if (needBrand == 1) {
            initFormDatas(true);
            brandIndex = 4;
            orgIndex = 5;
        } else {
            initFormDatas(false);
            orgIndex = 4;
            brandIndex = -1;
        }

        mCId = certDetailModel.detail.id + "";
        mCover.setImageURI(
                Uri.parse(URLUtil.builderImgUrl(certDetailModel.detail.eventCover, 540, 270)));

        mTitleTv.setText(certDetailModel.detail.eventName);
        mForms.get(0).desc = certDetailModel.detail.certName;
        mForms.get(1).desc = certDetailModel.detail.certSN;
        mForms.get(2).desc = certDetailModel.detail.mobile;
        mAdapter.notifyDataSetChanged();
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

    private void initView() {
        mCover = (SimpleDraweeView) findViewById(R.id.img);
        mList = (ListView) findViewById(R.id.list);
        mCmtBtn = (Button) findViewById(R.id.commitBtn);
        mCmtBtn.setOnClickListener(this);
        mBottomLayout = (RelativeLayout) findViewById(R.id.bottomLayout);
        mErrorLayout = (RelativeLayout) findViewById(R.id.errorLayout);
    }

    /**
     * 正常布局与异常布局切换
     *
     * @param display
     */
    private void displayNormalViews(boolean display) {

        if (display) {
            mList.setVisibility(View.VISIBLE);
            mBottomLayout.setVisibility(View.VISIBLE);
            mErrorLayout.setVisibility(View.GONE);
        } else {
            mBottomLayout.setVisibility(View.GONE);
            mErrorLayout.setVisibility(View.VISIBLE);
            mList.setVisibility(View.GONE);
            initErrorViewDatas();
        }
    }

    private void initErrorViewDatas() {

        ImageView imageView = (ImageView) findViewById(R.id.opStatus);
        //imageView.setImageResource(drawableId);
        TextView desc1 = (TextView) findViewById(R.id.desc1);
        desc1.setText("您的证件已成功注册!");
        Button btn = (Button) findViewById(R.id.functionBtn);
        btn.setText("去展会详情");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.INDEX, 1);
                CommonUtils.startNewActivity(mContext, args, MainActivity.class);
                finish();

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if ((position == brandIndex)) {
            Map<String, Serializable> args = new HashMap<String, Serializable>();
            args.put(Constants.IntentParams.MODULE_TYPE, Constants.ModuleTypes.PRE_REG_CERT);
            CommonUtils
                    .startNewActivity(
                            CertRegActivity.this,
                            args, FactoryBrandActivity.class
                    );
        }
        if (position == orgIndex) {

            CommonUtils.startResultNewActivity(this, null, OrgListActivity.class,
                    Constants.InteractionCode.REQUEST_CODE_2);
        }
    }

    private class CertRegAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mForms.size();
        }

        @Override
        public int getItemViewType(int position) {
            return mForms.get(position).viewTypeId;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public Object getItem(int position) {
            return mForms.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final Form form = mForms.get(position);
            int id = form.layoutId;
            WrapAdapter.ViewHolder holder = null;
            switch (id) {
                case R.layout.base_info_item_1:

                    holder = WrapAdapter.ViewHolder.get(mContext, convertView, parent,
                            R.layout.base_info_item_1, position);

                    break;

                case R.layout.base_info_item_3:
                    holder = WrapAdapter.ViewHolder.get(mContext, convertView, parent,
                            R.layout.base_info_item_3, position);
                    break;
            }

            switch (id) {
                case R.layout.base_info_item_1:

                    final EditText editText = holder.getView(R.id.content);
                    int filterType = InputType.TYPE_CLASS_TEXT;
                    if (position == 2) {
                        filterType = InputType.TYPE_CLASS_NUMBER;
                    }
                    editText.setInputType(filterType);
                    editText.clearFocus();
                    editText.setEnabled(form.isEnabled);
                    editText.setHint(form.hintContent);
                    editText.setHintTextColor(mRes.getColor(R.color.text_color2));
                    if (editText.getTag() instanceof TextWatcher) {

                        editText.removeTextChangedListener((TextWatcher) editText.getTag());
                    }
                    TextWatcher watcher = new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                      int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before,
                                                  int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            mForms.get(position).desc = s.toString();
                        }
                    };
                    editText.addTextChangedListener(watcher);
                    editText.setTag(watcher);
                    editText.setText(mForms.get(position).desc);
                    break;

                case R.layout.base_info_item_3:

                    TextView textView = holder.getView(R.id.desc);
                    textView.setText(mForms.get(position).desc);
                    break;
            }
            TextView tvName = holder.getView(R.id.title);
            tvName.setText(mForms.get(position).name);
            ImageView imageView = holder.getView(R.id.img);

            if (Constants.CheckInFormKey.IS_REQUIRE == form.require) {
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }

            return holder.getConvertView();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Constants.InteractionCode.REQUEST_CODE == requestCode && RESULT_OK == resultCode) {

            Brand b = data.getParcelableExtra(Constants.IntentParams.DATA);
            if (brandIndex != -1) {
                mForms.get(brandIndex).desc = b.brandName;
                mAdapter.notifyDataSetChanged();
            }
        }

        if (Constants.InteractionCode.REQUEST_CODE_2 == requestCode && RESULT_OK == resultCode) {

            String name = data.getStringExtra(Constants.IntentParams.DATA);
            mForms.get(orgIndex).desc = name;
            mAdapter.notifyDataSetChanged();
        }
    }

    private CertDataView<DataItem> mCmtDataView = new CertDataView<DataItem>() {
        @Override
        public void showData(DataItem dataItem) {

            int code = dataItem.code;

            switch (code) {
                case Constants.NetworkStatusCode.SUCCESS:
                case Constants.NetworkStatusCode.STATUS_CODE1:

                    displayNormalViews(false);
                    break;
                case Constants.NetworkStatusCode.STATUS_CODE2:
                    displayNormalViews(true);
                    break;
            }
            CommonUtils.showToast(mContext, dataItem.msg);
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
    };

    private Map<String, String> builderCmtParams() {

        Map<String, String> params = new HashMap<String, String>();

        String mobile = mForms.get(2).desc;
        String name = mForms.get(3).desc;
        String company = "";
        String brand = "";
        if (mForms.size() == 6) {
            brand = mForms.get(brandIndex).desc;
        }
        company = mForms.get(orgIndex).desc;
        params.put("id", mCId);
        params.put("brand", brand);
        params.put("company", company);
        params.put("name", name);
        params.put("mobile", mobile);
        return params;
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {
            case R.id.commitBtn:
                if (!isNullText()) {
                    mCmtPresenter.relationshipCertInfo(builderCmtParams());
                }

                break;
        }

    }

    private boolean isNullText() {

        boolean flag = false;
        for (Form f : mForms) {
            String desc = f.desc;
            if (TextUtils.isEmpty(desc)) {

                CommonUtils.showToast(mContext, f.name + "不能为空!");
                flag = true;

                return flag;
            }
        }

        return flag;
    }
}
