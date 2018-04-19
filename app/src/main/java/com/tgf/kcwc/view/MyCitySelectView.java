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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;
import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.CommonAdapter;
import com.tgf.kcwc.adapter.HeaderRecyclerAndFooterWrapperAdapter;
import com.tgf.kcwc.adapter.ViewHolder;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.mvp.model.Brand;
import com.tgf.kcwc.mvp.model.CityBean;
import com.tgf.kcwc.mvp.model.CitySelectBean;
import com.tgf.kcwc.mvp.model.MeituanHeaderBean;
import com.tgf.kcwc.mvp.presenter.CityPresenter;
import com.tgf.kcwc.mvp.view.CityView;
import com.tgf.kcwc.mvp.view.DividerItemDecoration;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SharedPreferenceUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2017/5/3.
 */

public class MyCitySelectView extends FrameLayout implements CityView {

    CityPresenter mCityPresenter;

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
    List<Brand> hotCitys = new ArrayList<>();
    List<Brand> locationCitys = new ArrayList<>();

    OnCitySelect mOnCitySelect = null;                                                 //暴露的选择接口 名字
    public Context context;
    private KPlayCarApp mApp;
    private String locCityName = "";

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {            //此方法在ui线程运行
            switch (msg.what) {
                case 1:
                    updata();
                    break;
            }
        }
    };

    public MyCitySelectView(Context context) {
        super(context);
        setLayout(context);
    }

    public MyCitySelectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayout(context);
    }

    public MyCitySelectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayout(context);
    }

    public void setHeaderdata(String name, int id, int pid, String namecode, String adcode) {
        Brand brand = new Brand();
        brand.id = id;
        brand.brandName = name;
        brand.adcode = adcode;
        brand.pid = pid;
        brand.letter = namecode;
        hotCitys.add(brand);
    }

    public void setOnCitySelect(OnCitySelect monCitySelect) {
        this.mOnCitySelect = monCitySelect;
    }

    public void setLayout(final Context mcontext) {
        context = mcontext;
        mApp = (KPlayCarApp) context.getApplicationContext();
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.mycityselect, this);
        mRv = (RecyclerView) findViewById(R.id.rv);
        mTvSideBarHint = (TextView) findViewById(R.id.tvSideBarHint);
        mIndexBar = (IndexBar) findViewById(R.id.indexBar);
        mRv.setLayoutManager(mManager = new LinearLayoutManager(context));
        mCityPresenter = new CityPresenter();
        mCityPresenter.attachView(this);

        String data = SharedPreferenceUtil.getCitylist(context);
        if (TextUtils.isEmpty(data)) {
            mCityPresenter.gainAppLsis(IOUtils.getToken(mcontext));
            setLoadingIndicator(true);
        } else {
            if (comparisonTime()) {
                mCityPresenter.gainAppLsis(IOUtils.getToken(mcontext));
                setLoadingIndicator(true);
            } else {
                Gson gson = new Gson();
                CityBean cityBean = gson.fromJson(data, CityBean.class);
                dataListSucceed(cityBean);
            }
        }

    }

    @Override
    public void dataListSucceed(CityBean cityBean) {
        locationCitys.clear();
        MyCitySelectView.this.setLoadingIndicator(false);
        if (cityBean.data.hot != null && cityBean.data.hot.size() > 0) {
            for (CityBean.Select data : cityBean.data.hot) {
                setHeaderdata(data.name, data.id, 0, data.namecode, data.adcode);
            }
        }
        Brand brands = new Brand();
    //    if (cityBean.data.select == null) {
            if (cityBean.data.list != null && cityBean.data.list.size() > 0) {
                for (CityBean.Select item : cityBean.data.list) {
                    if (item.name.equals(mApp.locCityName)) {
                        item.IsSelect = true;
                        brands.id = item.id;
                        brands.pid = 0;
                        brands.adcode = item.adcode;
                        brands.brandName = item.name;
                        brands.letter = item.namecode;
                        brands.setBaseIndexTag(brands.letter);
                        locationCitys.add(brands);
                        break;
                    }
                }
            }
/*        } else {
            brands.id = cityBean.data.select.id;
            brands.pid = 0;
            brands.adcode = cityBean.data.select.adcode;
            brands.brandName = cityBean.data.select.name;
            brands.letter = cityBean.data.select.namecode;
            brands.setBaseIndexTag(brands.letter);
        }*/
        //locationCitys.add(brands);

        mHeaderDatas.add(new MeituanHeaderBean(locationCitys, "定位", "定"));
        mHeaderDatas.add(new MeituanHeaderBean(hotCitys, "热门城市", "热"));
        mSourceDatas.addAll(mHeaderDatas);
        List<CityBean.Select> datalist = cityBean.data.list;
        for (CityBean.Select data1 : datalist) {
            CitySelectBean citySelectBean = new CitySelectBean();
            if (data1.name.equals(mApp.locCityName)) {
                citySelectBean.IsSelect = true;
            } else {
                citySelectBean.IsSelect = false;
            }
            citySelectBean.id = data1.id;
            citySelectBean.pid = data1.pid;
            citySelectBean.name = data1.name;
            citySelectBean.adcode = data1.adcode;
            citySelectBean.pinyin = data1.namecode;
            citySelectBean.PY = data1.namecode;
            CitySelectList.add(citySelectBean);
        }
        Collections.sort(CitySelectList); //排序

        for (int i = 0; i < CitySelectList.size(); i++) {
            Brand brand = new Brand();
            brand.id = CitySelectList.get(i).id;
            brand.pid = CitySelectList.get(i).pid;
            brand.brandName = CitySelectList.get(i).name;
            brand.adcode = CitySelectList.get(i).adcode;
            brand.isSelected = CitySelectList.get(i).IsSelect;
            brand.letter = CitySelectList.get(i).PY;
            brand.setBaseIndexTag(brand.letter);
            brandList.add(brand);
        }

        mHandler.sendEmptyMessage(1);
    }

    @Override
    public void dataListDefeated(String msg) {
        CommonUtils.showToast(context, msg);
        MyCitySelectView.this.setLoadingIndicator(false);
    }

    public interface OnCitySelect {
        void citySelect(String name, Brand brand);
    }

    public void updata() {
        mAdapter = new CommonAdapter<Brand>(context, R.layout.mycityselect_list_item, brandList) {
            @Override
            public void convert(ViewHolder holder, final Brand brand) {
                holder.setText(R.id.brandName, brand.brandName);
                ImageView imageView = holder.getView(R.id.select_status_img);
                if (brand.isSelected) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.GONE);
                }
                holder.getConvertView().setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnCitySelect != null) {
                            mOnCitySelect.citySelect(brand.brandName, brand);
                        }
                    }
                });
            }
        };
        // mAdapter.setOnItemClickListener(this);
        mHeaderAdapter = new HeaderRecyclerAndFooterWrapperAdapter(mAdapter) {
            @Override
            protected void onBindHeaderHolder(ViewHolder holder, int headerPos, int layoutId,
                                              Object o) {
                switch (layoutId) {
                    case R.layout.city_item_header:
                        final MeituanHeaderBean meituanHeaderBean = (MeituanHeaderBean) o;
                        holder.setText(R.id.brandName, meituanHeaderBean.getSuspensionTag());
                        //网格
                        RecyclerView recyclerView = holder.getView(R.id.rvCity);
                        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
                        recyclerView.setAdapter(new CommonAdapter<Brand>(context,
                                R.layout.meituan_item_header_item, meituanHeaderBean.getCityList()) {
                            @Override
                            public void convert(ViewHolder holder, final Brand brand) {
                                holder.setText(R.id.tvName, brand.brandName);

                                holder.getConvertView()
                                        .setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (mOnCitySelect != null) {
                                                    mOnCitySelect.citySelect(brand.brandName, brand);
                                                }
                                            }
                                        });
                            }
                        });
                        break;
                    default:
                        holder.setText(R.id.brandName, (String) o);
                        break;
                }

            }
        };
        mHeaderAdapter.setHeaderView(0, R.layout.city_item_header, mHeaderDatas.get(0));
        mHeaderAdapter.setHeaderView(1, R.layout.city_item_header, mHeaderDatas.get(1));
        mRv.setAdapter(mHeaderAdapter);
        mDecoration = new SuspensionDecoration(context, brandList);
        mDecoration.setColorTitleFont(context.getResources().getColor(R.color.text_color15));
        mDecoration.setColorTitleBg(context.getResources().getColor(R.color.text_content_color));
        mDecoration.setHeaderViewCount(mHeaderAdapter.getHeaderViewCount());
        mRv.addItemDecoration(mDecoration);

        //如果add两个，那么按照先后顺序，依次渲染。
        mRv.addItemDecoration(
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));

        //使用indexBar
        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(mManager);//设置RecyclerView的LayoutManager
        //先排序
        mIndexBar.getDataHelper().sortSourceDatas(brandList);
        mSourceDatas.addAll(brandList);
        mIndexBar.setmSourceDatas(mSourceDatas)//设置数据
                .invalidate();
        mTvSideBarHint.setVisibility(GONE);
    }

    private SweetAlertDialog mLoadingDialog;

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
            mLoadingDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
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

    public boolean comparisonTime() {
        long cityTime = SharedPreferenceUtil.getCityTime(context);
        if (cityTime == 0) {
            return true;
        }
        long ltime = gaiPresentTime();
        long diff = ltime - cityTime;
        if (diff > 0) {
            if (diff <= (1 * 24 * 60 * 60 * 1000)) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    public static long gaiPresentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd    HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        long time = curDate.getTime();
        String str = formatter.format(curDate);
        return time;
    }

}
