package com.tgf.kcwc.see;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.CommonAdapter;
import com.tgf.kcwc.adapter.OnItemClickListener;
import com.tgf.kcwc.adapter.ViewHolder;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.Brand;
import com.tgf.kcwc.mvp.presenter.BrandListPresenter;
import com.tgf.kcwc.mvp.view.BrandDataView;
import com.tgf.kcwc.mvp.view.DividerItemDecoration;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Author：Jenny
 * Date:2016/12/14 15:52
 * E-mail：fishloveqin@gmail.com
 * 处理品牌数据 如一级品牌、二级品牌
 */

public class BuilderBrands

        implements OnItemClickListener<Brand>, BrandDataView {
    private static final String TAG = BuilderBrands.class.getSimpleName();
    private RecyclerView mRv;
    private LinearLayoutManager mManager;

    public ArrayList<Brand> getDatas() {
        return mDatas;
    }

    private ArrayList<Brand> mDatas;

    private ArrayList<Brand> mHeaderDatas = new ArrayList<Brand>();
    private SuspensionDecoration mDecoration;

    /**
     * 右侧边栏导航区域
     */
    private IndexBar mIndexBar;

    /**
     * 显示指示器DialogText
     */
    private TextView mTvSideBarHint;
    CommonAdapter<Brand> adapter = null;

    private Context mContext;

    private View mRootView;

    public void setSingle(boolean single) {
        isSingle = single;
    }

    private boolean isSingle;

    public void setmExId(int mExId) {
        this.mExId = mExId;
    }

    private int mExId;

    public BuilderBrands(Context context, View rootView) {
        this.mContext = context;
        this.mRootView = rootView;
        mRv = (RecyclerView) mRootView.findViewById(R.id.rv);
        mManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRv.setLayoutManager(mManager);

        //使用indexBar
        mTvSideBarHint = (TextView) mRootView.findViewById(R.id.tvSideBarHint);//HintTextView
        mIndexBar = (IndexBar) mRootView.findViewById(R.id.indexBar);//IndexBar
        brandListPresenter.attachView(this);
    }

    private SweetAlertDialog mLoadingDialog;

    protected void showLoadingDialog(String text) {

        if (mLoadingDialog == null) {
            mLoadingDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
            mLoadingDialog.getProgressHelper()
                    .setBarColor(mContext.getResources().getColor(R.color.colorPrimary));
            mLoadingDialog.setCancelable(false);
            mLoadingDialog.setTitleText(text);
        }

        mLoadingDialog.show();

    }

    protected void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    private BrandListPresenter brandListPresenter = new BrandListPresenter();

    /**
     * @param type 加载品牌的类型， 1表示一级品牌 2表示厂商品牌,3表示展会品牌
     */
    public void loadData(int type, String isNeedAll,int exId) {

        switch (type) {

            case 1:
                brandListPresenter.getBrandsDatas(IOUtils.getToken(mContext), isNeedAll);
                break;

            case 2:
                brandListPresenter.loadBrandsDatas("car");
                break;
            case 3:
                brandListPresenter.loadSepBrandsDatas(exId + "", isNeedAll);
                break;
        }


    }

    private void singleChecked(List<Brand> items, Brand item) {
        for (Brand b : items) {
            if (b.brandId != item.brandId) {
                b.isSelected = false;
            }
        }

    }

    public void setDisplayChecked(boolean displayChecked) {
        isDisplayChecked = displayChecked;
    }

    private boolean isDisplayChecked = true;

    @Override
    public void onItemClick(ViewGroup parent, View view, Brand item, int position) {

        if (mCallback != null) {
            mCallback.refresh(parent, view, item, position);
        }


        if (isDisplayChecked) {
            if (isSingle) {

                item.isSelected = true;
            } else {
                if (item.isSelected) {
                    item.isSelected = false;
                } else {
                    item.isSelected = true;
                }
            }
            singleChecked(mDatas, item);

            notifyDataSetChanged();
        }

    }

    public void notifyDataSetChanged() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public interface Callback {

        void refresh(ViewGroup parent, View view, Brand item, int position);

    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    private Callback mCallback;

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, Brand cityBean, int position) {
        return false;
    }

    public void detachView() {

        brandListPresenter.detachView();
    }

    @Override
    public void showData(List<Brand> data) {
        mDatas = new ArrayList<Brand>();

        int size = data.size();
        final Brand allBrand = new Brand();
        allBrand.setBaseIndexTag("所有");
        allBrand.id = 0;
        allBrand.brandName = "不限";
        //allBrand.setBaseIndexTag("所有品牌");
        allBrand.brandLogo = "";

        mHeaderDatas.clear();
        mHeaderDatas.add(allBrand);
        //mDatas.addAll(mHeaderDatas);
        for (int i = 0; i < size; i++) {

            Brand b = data.get(i);
            b.setBaseIndexTag(b.letter);
            mDatas.add(b);
        }
        adapter = new CommonAdapter<Brand>(mContext, R.layout.brand_list_item, mDatas) {
            @Override
            public void convert(ViewHolder holder, Brand item) {
                Logger.d("DropDownCatSpinner brandName " + item.brandName);
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
        adapter.setOnItemClickListener(this);

        mRv.setAdapter(adapter);
        mDecoration = new SuspensionDecoration(mContext, mDatas);
        mDecoration.setColorTitleBg(

                getContext().getResources().getColor(R.color.style_bg4));
        mDecoration.setColorTitleFont(getContext().getResources().getColor(R.color.text_color15));
        //  mDecoration.setHeaderViewCount(1);
        mRv.addItemDecoration(mDecoration);
        //如果add两个，那么按照先后顺序，依次渲染。
        mRv.addItemDecoration(
                new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));

        mIndexBar.setVisibility(View.VISIBLE);
        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(mManager);//设置RecyclerView的LayoutManager
        mIndexBar.getDataHelper().sortSourceDatas(mDatas);
        mIndexBar.setmSourceDatas(mDatas);
        mIndexBar.invalidate();
    }

    @Override
    public void setLoadingIndicator(boolean active) {


        if (active) {
            showLoadingDialog("正在加载数据...");
        } else {
            dismissLoadingDialog();
        }
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
