package com.tgf.kcwc.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;
import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.CommonAdapter;
import com.tgf.kcwc.adapter.HeaderRecyclerAndFooterWrapperAdapter;
import com.tgf.kcwc.adapter.OnItemClickListener;
import com.tgf.kcwc.adapter.ViewHolder;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.Brand;
import com.tgf.kcwc.mvp.model.CityBean;
import com.tgf.kcwc.mvp.model.CitySelectBean;
import com.tgf.kcwc.mvp.model.MeituanHeaderBean;
import com.tgf.kcwc.mvp.presenter.BrandListPresenter;
import com.tgf.kcwc.mvp.presenter.CityPresenter;
import com.tgf.kcwc.mvp.view.BrandDataView;
import com.tgf.kcwc.mvp.view.CityView;
import com.tgf.kcwc.mvp.view.DividerItemDecoration;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SharedPreferenceUtil;
import com.tgf.kcwc.util.URLUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2017/5/3.
 */

public class MyBrandSelectView extends FrameLayout implements BrandDataView {

    BrandListPresenter brandListPresenter;

    /**
     * 右侧边栏导航区域
     */
    private IndexBar mIndexBar;

    /**
     * 显示指示器DialogText
     */
    private TextView mTvSideBarHint;
    /**
     * 列表
     */
    private RecyclerView mRv;
    private LinearLayoutManager mManager;

    /**
     * adapter
     */
    CommonAdapter<Brand> mAdapter = null;                                                 //普通adapter
    private HeaderRecyclerAndFooterWrapperAdapter mHeaderAdapter;                                                        //排序adapter
    List<Brand> brandList = new ArrayList<>();

    private SuspensionDecoration mDecoration;

    List<CitySelectBean> CitySelectList = new ArrayList<>();
    //设置给InexBar、ItemDecoration的完整数据集
    private List<BaseIndexPinyinBean> mSourceDatas = new ArrayList<>();
    //头部数据源
    private List<MeituanHeaderBean> mHeaderDatas = new ArrayList<>();
    List<String> hotCitys = new ArrayList<>();

    OnCitySelect mOnCitySelect = null;                                                 //暴露的选择接口
    private Context mContext;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    showData((ArrayList<Brand>) msg.obj);

                    break;

            }
        }
    };
    public MyBrandSelectView(Context context) {
        super(context);
        setLayout(context);
    }

    public MyBrandSelectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayout(context);
    }

    public MyBrandSelectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayout(context);
    }

    public void setOnCitySelect(OnCitySelect monCitySelect) {
        this.mOnCitySelect = monCitySelect;
    }



    public void setLayout(final Context mcontext) {
        mContext = mcontext;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.mycityselect, this);
        mRv = (RecyclerView) findViewById(R.id.rv);
        mTvSideBarHint = (TextView) findViewById(R.id.tvSideBarHint);
        mIndexBar = (IndexBar) findViewById(R.id.indexBar);
        mRv.setLayoutManager(mManager = new LinearLayoutManager(mContext));
        brandListPresenter = new BrandListPresenter();
        brandListPresenter.attachView(this);
        final String data = SharedPreferenceUtil.getBrandlist(mContext);
        if (TextUtils.isEmpty(data)) {
            brandListPresenter.loadBrandsDatas("car");
        } else {
            setLoadingIndicator(true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Gson gson = new Gson();
                    JsonParser parser = new JsonParser();
                    JsonArray Jarray = parser.parse(data).getAsJsonArray();
                    ArrayList<Brand> lcs = new ArrayList<Brand>();
                    for (JsonElement obj : Jarray) {
                        Brand cse = gson.fromJson(obj, Brand.class);
                        lcs.add(cse);
                    }
                    Message msg = mHandler.obtainMessage();
                    msg.obj = lcs;
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                }
            }).start();
        }

    }

    @Override
    public void showData(List<Brand> data) {
        brandList = new ArrayList<Brand>();
        setLoadingIndicator(false);
        int size = data.size();
        Brand allBrand = new Brand();
        allBrand.setBaseIndexTag("所有");
        allBrand.id = 0;
        allBrand.brandName = "不限";
        allBrand.setBaseIndexTag("所有品牌");
        allBrand.brandLogo = "";

        mSourceDatas.clear();
        mSourceDatas.add(allBrand);
        //mDatas.addAll(mHeaderDatas);
        for (int i = 1; i < size; i++) {
            Brand b = data.get(i);
            b.setBaseIndexTag(b.letter);
            brandList.add(b);
        }
        mAdapter = new CommonAdapter<Brand>(mContext, R.layout.brand_list_item, brandList) {
            @Override
            public void convert(ViewHolder holder, Brand item) {
                Logger.i("TT" + item.brandName);
                TextView brandNameText = holder.getView(R.id.brandName);
                brandNameText.setText(item.brandName);
                SimpleDraweeView simpleDraweeView = holder.getView(R.id.img);
                simpleDraweeView.setImageURI(URLUtil.builderImgUrl(item.brandLogo, 144, 144));
                ImageView imageView = holder.getView(R.id.select_status_img);
                if (item.isSelected) {
                    imageView.setVisibility(View.VISIBLE);
                    brandNameText
                            .setTextColor(mContext.getResources().getColor(R.color.text_color6));
                } else {
                    imageView.setVisibility(View.GONE);
                    brandNameText
                            .setTextColor(mContext.getResources().getColor(R.color.text_color12));
                }
            }
        };
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                mOnCitySelect.citySelect(brandList.get(position));
                singleChecked(brandList, brandList.get(position));
                notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });

        mHeaderAdapter = new HeaderRecyclerAndFooterWrapperAdapter(mAdapter) {
            @Override
            protected void onBindHeaderHolder(ViewHolder holder, int headerPos, int layoutId,
                                              Object o) {
                holder.setText(R.id.brandName, (String) o);
            }
        };
        mRv.setAdapter(mHeaderAdapter);
        mDecoration = new SuspensionDecoration(mContext, brandList);
        mDecoration.setColorTitleFont(mContext.getResources().getColor(R.color.text_color15));
        mDecoration.setColorTitleBg(mContext.getResources().getColor(R.color.text_content_color));
        mDecoration.setHeaderViewCount(mHeaderAdapter.getHeaderViewCount());
        mRv.addItemDecoration(mDecoration);

        //如果add两个，那么按照先后顺序，依次渲染。
        mRv.addItemDecoration(
                new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));

        //使用indexBar
        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(mManager);//设置RecyclerView的LayoutManager
        //先排序
        mIndexBar.getDataHelper().sortSourceDatas(brandList);
        mSourceDatas.addAll(brandList);
        mIndexBar.setmSourceDatas(mSourceDatas)//设置数据
                .invalidate();
        mTvSideBarHint.setVisibility(View.GONE);
    }

    public interface OnCitySelect {
        void citySelect(Brand brand);
    }


    private SweetAlertDialog mLoadingDialog;


    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            showLoadingDialog();
        } else {
            dismissLoadingDialog();
        }
    }

    @Override
    public void showLoadingTasksError() {

    }


    protected void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    protected void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
            mLoadingDialog.getProgressHelper()
                    .setBarColor(getResources().getColor(R.color.colorPrimary));
            mLoadingDialog.setCancelable(false);
            mLoadingDialog.setTitleText("数据加载中...");
        }
        new CountDownTimer(800 * 7, 800) {
            int i;

            public void onTick(long millisUntilFinished) {
                // you can change the progress bar color by ProgressHelper every 800 millis
                i++;
                switch (i) {
                    case 0:
                        mLoadingDialog.getProgressHelper()
                                .setBarColor(getResources().getColor(R.color.blue_btn_bg_color));
                        break;
                    case 1:
                        mLoadingDialog.getProgressHelper()
                                .setBarColor(getResources().getColor(R.color.material_deep_teal_50));
                        break;
                    case 2:
                        mLoadingDialog.getProgressHelper()
                                .setBarColor(getResources().getColor(R.color.success_stroke_color));
                        break;
                    case 3:
                        mLoadingDialog.getProgressHelper()
                                .setBarColor(getResources().getColor(R.color.material_deep_teal_20));
                        break;
                    case 4:
                        mLoadingDialog.getProgressHelper()
                                .setBarColor(getResources().getColor(R.color.material_blue_grey_80));
                        break;
                    case 5:
                        mLoadingDialog.getProgressHelper()
                                .setBarColor(getResources().getColor(R.color.warning_stroke_color));
                        break;
                    case 6:
                        mLoadingDialog.getProgressHelper()
                                .setBarColor(getResources().getColor(R.color.success_stroke_color));
                        break;
                }
            }

            public void onFinish() {
                i = -1;
                //                mLoadingDialog//setTitleText("完成")
                //                        //.setConfirmText("OK")
                //                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                mLoadingDialog.dismiss();
            }
        }.start();
        mLoadingDialog.show();
    }

    private void singleChecked(List<Brand> items, Brand item) {
        for (Brand b : items) {
            if (b.brandId != item.brandId) {
                b.isSelected = false;
            }
        }
    }

    public void notifyDataSetChanged() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }
}
