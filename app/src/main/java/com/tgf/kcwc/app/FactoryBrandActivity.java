package com.tgf.kcwc.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;
import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.CommonAdapter;
import com.tgf.kcwc.adapter.HeaderRecyclerAndFooterWrapperAdapter;
import com.tgf.kcwc.adapter.OnItemClickListener;
import com.tgf.kcwc.adapter.ViewHolder;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.Brand;
import com.tgf.kcwc.mvp.presenter.BrandListPresenter;
import com.tgf.kcwc.mvp.view.BrandDataView;
import com.tgf.kcwc.mvp.view.DividerItemDecoration;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.LoadView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：Jenny
 * Date:2016/12/14 15:52
 * E-mail：fishloveqin@gmail.com
 * <p>
 * 二级品牌/厂商级品牌
 */

public class FactoryBrandActivity extends BaseActivity

        implements OnItemClickListener<Brand>, BrandDataView {
    private static final String TAG = FactoryBrandActivity.class
            .getSimpleName();
    private RecyclerView mRv;
    private HeaderRecyclerAndFooterWrapperAdapter mHeaderAdapter;
    private LinearLayoutManager mManager;
    private ArrayList<Brand> mDatas;

    private SuspensionDecoration mDecoration;

    /**
     * 右侧边栏导航区域
     */
    private IndexBar mIndexBar;
    //设置给InexBar、ItemDecoration的完整数据集
    private List<BaseIndexPinyinBean> mSourceDatas = new ArrayList<>();

    /**
     * 显示指示器DialogText
     */
    private TextView mTvSideBarHint;
    CommonAdapter<Brand> mAdapter = null;

    private ImageView mBackBtn;

    private String mSelectTextValue;
    private LoadView mLoadView;

    private TextView mConfirmBtn;
    private int mIntentType = -1;
    private boolean isJump;
    private boolean isSelectModel;
    private String mModuleType;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    private boolean isSepBrand = false;
    private int mSenseId = -1;

    @Override
    protected void setUpViews() {
        mRv = (RecyclerView) findViewById(R.id.rv);
        mRv.setLayoutManager(mManager = new LinearLayoutManager(this));
        mLoadView = (LoadView) findViewById(R.id.loadView);
        mBackBtn = (ImageView) findViewById(R.id.backBtn);
        mBackBtn.setOnClickListener(this);
        mConfirmBtn = (TextView) findViewById(R.id.title_function_btn);

        mSelectTextValue = getString(R.string.select_count_text);
        BrandListPresenter brandListPresenter = new BrandListPresenter();
        brandListPresenter.attachView(this);

        String isNeedAll = "1";
        if (Constants.ModuleTypes.PRE_REG_CERT.equals(mModuleType)) {
            isNeedAll = "0";
        }
        brandListPresenter.loadBrandsDatas("car", isNeedAll);

    }

    private void showSelectValue() {
        int size = mDatas.size();
        int count = 0;
        for (int i = 0; i < size; i++) {
            Brand cb = mDatas.get(i);
            if (cb.isSelected) {
                count++;
            }
        }
    }

    private int mIndex = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isTitleBar = false;
        Intent intent = getIntent();
        mModuleType = intent.getStringExtra(Constants.IntentParams.MODULE_TYPE);
        setContentView(R.layout.activity_factory_brands);

    }

    @Override
    public void onItemClick(ViewGroup parent, View view, Brand item, int position) {


        String modelType = KPlayCarApp.getValue(Constants.KeyParams.PRE_REG_SELECT_MODEL);
        if (!TextUtil.isEmpty(modelType) && Constants.CheckInFormKey.BRAND.equals(modelType)) {

            KPlayCarApp.putValue(Constants.KeyParams.PRE_REG_SELECT_MODEL_VALUE, item.brandName);
            finish();
            return;
        }


        Map<String, Serializable> args = new HashMap<String, Serializable>();
        args.put(Constants.IntentParams.ID, item.brandId);
        args.put(Constants.IntentParams.MODULE_TYPE, mModuleType);
        CommonUtils.startNewActivity(this, args, SelectSeriesByFactoryBrandActivity.class);

        mAdapter.notifyDataSetChanged();
        mHeaderAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, Brand cityBean, int position) {
        return false;
    }

    @Override
    public void showData(List<Brand> data) {

        mDatas = new ArrayList<Brand>();

        String sponsor = KPlayCarApp.getValue(Constants.IntentParams.SPONSORPLEASE);
        mDatas.clear();
        mDatas.addAll(data);
        int size = mDatas.size();
        for (int i = 0; i < size; i++) {

            Brand b = mDatas.get(i);

            b.setBaseIndexTag(b.letter);
            mDatas.add(b);
        }

        mAdapter = new CommonAdapter<Brand>(mContext, R.layout.brand_list_item, mDatas) {
            @Override
            public void convert(ViewHolder holder, Brand item) {
                holder.setText(R.id.brandName, item.brandName);
                SimpleDraweeView simpleDraweeView = holder.getView(R.id.img);
                simpleDraweeView
                        .setImageURI(Uri.parse(URLUtil.builderImgUrl(item.brandLogo, 144, 144)));
                ImageView imageView = holder.getView(R.id.select_status_img);
                if (item.isSelected) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.GONE);
                }
            }
        };
        mAdapter.setOnItemClickListener(this);
        mHeaderAdapter = new HeaderRecyclerAndFooterWrapperAdapter(mAdapter) {
            @Override
            protected void onBindHeaderHolder(ViewHolder holder, int headerPos, int layoutId,
                                              Object o) {
                holder.setText(R.id.brandName, (String) o);
            }
        };
        mRv.setAdapter(mHeaderAdapter);
        mDecoration = new SuspensionDecoration(this, mDatas);
        mDecoration.setColorTitleFont(mRes.getColor(R.color.text_color15));
        mDecoration.setColorTitleBg(mRes.getColor(R.color.text_content_color));
        mDecoration.setHeaderViewCount(mHeaderAdapter.getHeaderViewCount());
        mRv.addItemDecoration(mDecoration);

        //如果add两个，那么按照先后顺序，依次渲染。
        mRv.addItemDecoration(new DividerItemDecoration(FactoryBrandActivity.this,
                DividerItemDecoration.VERTICAL_LIST));

        //使用indexBar
        mTvSideBarHint = (TextView) findViewById(R.id.tvSideBarHint);//HintTextView
        mIndexBar = (IndexBar) findViewById(R.id.indexBar);//IndexBar

        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(mManager);//设置RecyclerView的LayoutManager
        //先排序
        mIndexBar.getDataHelper().sortSourceDatas(mDatas);
        mSourceDatas.addAll(mDatas);
        mIndexBar.setmSourceDatas(mSourceDatas)//设置数据
                .invalidate();

    }

    @Override
    public void setLoadingIndicator(boolean active) {

        showLoadingIndicator(active);

    }

    @Override
    public void showLoadingTasksError() {

        showLoadingIndicator(false);
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.title_function_btn:
                Intent intent = getIntent();
                intent.putParcelableArrayListExtra("data", mDatas);
                setResult(RESULT_OK, intent);
                finish();
                break;

            case R.id.backBtn:
                finish();
                break;
        }

    }
}
