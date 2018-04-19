package com.tgf.kcwc.see.dealer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hedgehog.ratingbar.RatingBar;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.tauth.Tencent;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.amap.LocationPreviewActivity;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.me.message.MessageActivity;
import com.tgf.kcwc.me.setting.FanKuiActivity;
import com.tgf.kcwc.mvp.model.MorePopupwindowBean;
import com.tgf.kcwc.mvp.model.StoreDetailData;
import com.tgf.kcwc.mvp.presenter.DealerDataPresenter;
import com.tgf.kcwc.mvp.presenter.UserDataPresenter;
import com.tgf.kcwc.mvp.view.DealerDataView;
import com.tgf.kcwc.mvp.view.UserDataView;
import com.tgf.kcwc.qrcode.ScannerCodeActivity;
import com.tgf.kcwc.share.OpenShareUtil;
import com.tgf.kcwc.share.SinaWBCallback;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DataUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SystemInvoker;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MorePopupWindow;
import com.tgf.kcwc.view.ObservableScrollView;
import com.tgf.kcwc.view.OpenShareWindow;

import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：Jenny
 * Date:2016/12/22 10:19
 * E-mail：fishloveqin@gmail.com
 */

public class DealerHomeActivity extends BaseActivity implements DealerDataView<StoreDetailData> {

    private SimpleDraweeView mBigHeaderImg;
    private TextView mImgCountText;
    private SimpleDraweeView mLogo;
    private TextView mName;
    private TextView mServiceRatingText;
    private TextView mAddressText;
    private ImageView mCall;
    private TextView mCallBtn, mNavBtn;
    private String mOrgId = "";
    private String mOrgTitle = "";
    private DealerDataPresenter mPresenter;
    private GridView mSubTabsGridView;
    private List<DataItem> mSubTabsDatas = new ArrayList<DataItem>();
    private WrapAdapter<DataItem> mSubTabsAdapter = null;
    private RatingBar mRatingBar;
    private List<BaseFragment> mSubTabs = new ArrayList<BaseFragment>();
    private ObservableScrollView mScrollView;
    private MorePopupWindow morePopupWindow;
    private ArrayList<MorePopupwindowBean> dataList = new ArrayList<>();
    private OpenShareWindow openShareWindow;
    private String cover;
    private WbShareHandler mWbHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mOrgId = intent.getStringExtra(Constants.IntentParams.ID);
        mOrgTitle = intent.getStringExtra(Constants.IntentParams.TITLE);
        index = intent.getIntExtra(Constants.IntentParams.INDEX, 0);
        initHideMoreMenuData();
        setContentView(R.layout.activity_dealer_home);
        openShareWindow = new OpenShareWindow(this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mWbHandler != null) {
            mWbHandler.doResultIntent(intent, new SinaWBCallback(mContext));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //加上这一句才能回调
        Tencent.onActivityResultData(requestCode, resultCode, data, mBaseUIListener);

    }

    private void initHideMoreMenuData() {

        if (dataList.size() == 0) {
            String[] navValues = null;

            navValues = mRes.getStringArray(R.array.global_nav_values6);

            Map<String, Integer> titleActionIconMap = DataUtil.getTitleActionIcon();
            int length = navValues.length;
            for (int i = 0; i < length; i++) {
                MorePopupwindowBean morePopupwindowBean = null;
                morePopupwindowBean = new MorePopupwindowBean();
                morePopupwindowBean.title = navValues[i];
                morePopupwindowBean.icon = titleActionIconMap.get(navValues[i]);
                morePopupwindowBean.id = i;
                dataList.add(morePopupwindowBean);
            }
        }

        morePopupWindow = new MorePopupWindow(DealerHomeActivity.this,
                dataList, mMoreOnClickListener);


    }

    @Override
    protected void titleBarCallback(ImageButton back, final FunctionView function, TextView text) {
        findViewById(R.id.titleBar).setBackgroundResource(R.drawable.shape_titlebar_bg);
        text.setText(mOrgTitle + "");
        function.setImageResource(R.drawable.btn_hide_more_selector);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                morePopupWindow.showPopupWindow(function);
            }
        });
        backEvent(back);
    }

    private int index;


    private MorePopupWindow.MoreOnClickListener mMoreOnClickListener = new MorePopupWindow.MoreOnClickListener() {
        @Override
        public void moreOnClickListener(int position, MorePopupwindowBean item) {
            switch (dataList.get(position).title) {
                case "首页":

//                    Intent intent = new Intent();
//                    intent.putExtra(Constants.IntentParams.INDEX, 0);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intent.setClass(mContext, MainActivity.class);
//                    startActivity(intent);
                    CommonUtils.gotoMainPage(mContext,Constants.Navigation.HOME_INDEX);
                    break;
                case "消息":
                    CommonUtils.startNewActivity(mContext, MessageActivity.class);
                    break;
                case "分享":
                    if (mModel != null) {
                        share();
                    } else {
                        CommonUtils.showToast(mContext, "正在加载数据，请稍候!");
                    }

                    break;
                case "扫一扫":
                    if (IOUtils.isLogin(mContext)) {
                        startActivity(new Intent(mContext, ScannerCodeActivity.class));
                    }
                    break;
                case "反馈":
                    CommonUtils.startNewActivity(mContext, FanKuiActivity.class);
                    break;
                case "收藏":

                    setFavorite();

                    break;

                case "取消收藏":
                    setFavorite();
                    break;
            }

        }
    };


    private void share() {

        openShareWindow.show(this);
        final String title = "看车玩车";
        final String description = mOrgTitle;

        cover = mModel.banner;
        cover = URLUtil.builderImgUrl(cover, 540, 270);

        openShareWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {

                    case 0:
                        OpenShareUtil.sendWXMoments(mContext, mGlobalContext.getMsgApi(), title, description);
                        break;
                    case 1:
                        OpenShareUtil.sendWX(mContext, mGlobalContext.getMsgApi(), title, description);
                        break;

                    case 2:
                        mWbHandler = OpenShareUtil.shareSina(DealerHomeActivity.this, title,
                                description);
                        break;
                    case 3:
                        ArrayList<String> urls = new ArrayList<String>();
                        urls.add(cover);
                        OpenShareUtil.shareToQzone(mGlobalContext.getTencent(), DealerHomeActivity.this,
                                mBaseUIListener, urls, title, description);
                        break;
                    case 4:
                        OpenShareUtil.shareToQQ(mGlobalContext.getTencent(), DealerHomeActivity.this,
                                mBaseUIListener, title, cover, description);
                        break;

                }
                openShareWindow.dismiss();
            }
        });
    }


    @Override
    protected void setUpViews() {

        initView();
        addSubFragments();
        buildSubTabs(index);

        mPresenter = new DealerDataPresenter();
        mPresenter.attachView(this);
        mPresenter.loadStoreInfo(mOrgId, IOUtils.getToken(mContext));

        mAddFavoritePresenter = new UserDataPresenter();
        mAddFavoritePresenter.attachView(mAddFavoriteView);
        mCancelFavoritePresenter = new UserDataPresenter();
        mCancelFavoritePresenter.attachView(mCancelFavoriteView);

    }


    private int isFav = -1;
    private UserDataView<Object> mCancelFavoriteView = new UserDataView<Object>() {
        @Override
        public Context getContext() {
            return mContext;
        }

        @Override
        public void setLoadingIndicator(boolean active) {

        }

        @Override
        public void showLoadingTasksError() {

        }

        @Override
        public void showDatas(Object o) {

            isFav = 0;
            mPresenter.loadStoreInfo(mOrgId, IOUtils.getToken(mContext));
            CommonUtils.showToast(mContext, "已取消收藏");

        }
    };

    private UserDataView<Object> mAddFavoriteView = new UserDataView<Object>() {
        @Override
        public Context getContext() {
            return mContext;
        }

        @Override
        public void setLoadingIndicator(boolean active) {

        }

        @Override
        public void showLoadingTasksError() {

        }

        @Override
        public void showDatas(Object o) {

            isFav = 1;
            mPresenter.loadStoreInfo(mOrgId, IOUtils.getToken(mContext));
            CommonUtils.showToast(mContext, "收藏成功");
        }
    };


    private void initView() {
        mScrollView = (ObservableScrollView) findViewById(R.id.scrollView);
        mScrollView.setScrollViewListener(mScrollListener);
        mBigHeaderImg = (SimpleDraweeView) findViewById(R.id.bigHeaderImg);
        mImgCountText = (TextView) findViewById(R.id.tv_picnum);
        mLogo = (SimpleDraweeView) findViewById(R.id.img);
        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        mName = (TextView) findViewById(R.id.name);
        mSubTabsGridView = (GridView) findViewById(R.id.subTabGridView);
        mSubTabsGridView.setOnItemClickListener(mSubTabsItemClickListener);
        mServiceRatingText = (TextView) findViewById(R.id.serviceRatingText);
        mAddressText = (TextView) findViewById(R.id.addressText);
        mCall = (ImageView) findViewById(R.id.telImg);
        mCallBtn = (TextView) findViewById(R.id.callBtn);
        mNavBtn = (TextView) findViewById(R.id.navBtn);
        mCallBtn.setOnClickListener(this);
        mNavBtn.setOnClickListener(this);

    }


    private ObservableScrollView.ScrollViewListener mScrollListener = new ObservableScrollView.ScrollViewListener() {
        @Override
        public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx,
                                    int oldy) {

            int topHeight = BitmapUtils.dp2px(mContext, 132);
            if (y >= topHeight) {
                findViewById(R.id.titleBar).setBackgroundResource(R.color.style_bg1);
            } else {
                findViewById(R.id.titleBar).setBackgroundResource(R.drawable.shape_titlebar_bg);

            }
            Logger.d("x:" + x + ",y:" + y + ",oldX:" + oldx + "oldy:" + oldy);
        }
    };


    private void setRatingScore(RatingBar ratingBar, int count, float star, int drawableEmptyId,
                                int drawableHalfId, int drawableFillId) {
        ratingBar.setStarEmptyDrawable(getResources().getDrawable(drawableEmptyId));
        ratingBar.setStarHalfDrawable(getResources().getDrawable(drawableHalfId));
        ratingBar.setStarFillDrawable(getResources().getDrawable(drawableFillId));
        ratingBar.setStarCount(count);
        ratingBar.setStarImageSize(star);
        ratingBar.setStar(star);
        ratingBar.halfStar(true);
        ratingBar.setmClickable(false);

    }

    private void addSubFragments() {

        mSubTabs.add(new HomeFragment());
        mSubTabs.add(new StoreExhibitionCarFragment());
        mSubTabs.add(new StoreLiveCarFragment());
        FragmentManager fragmentManager = getSupportFragmentManager();

        for (BaseFragment fragment : mSubTabs) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.IntentParams.ID, mOrgId);
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction().add(R.id.contentLayout, fragment).commit();
        }
    }

    /**
     * 根据index切换Fragment布局(已缓存Fragment， 调用show、hide方法显示、隐藏)
     *
     * @param index
     */
    private void switchSubFragment(int index) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        BaseFragment fragment = mSubTabs.get(index);

        for (BaseFragment f : mSubTabs) {
            if (f == fragment) {
                fragmentManager.beginTransaction().show(f).commit();
            } else {
                fragmentManager.beginTransaction().hide(f).commit();
            }
        }

    }

    private AdapterView.OnItemClickListener mSubTabsItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            DataItem item = (DataItem) parent.getAdapter().getItem(position);

            item.isSelected = true;
            singleChecked(mSubTabsDatas, item);
            mSubTabsAdapter.notifyDataSetChanged();

            switchSubFragment(position);
        }
    };

    /**
     * 创建子tab项
     */
    private void buildSubTabs(int position) {

        String[] arrays = mRes.getStringArray(R.array.dealer_sub_tabs);

        int id = 0;
        for (String s : arrays) {

            DataItem d = new DataItem();
            d.name = s;
            d.id = id;
            if (id == position) {
                d.isSelected = true;
                switchSubFragment(id);
            }
            mSubTabsDatas.add(d);
            id++;
        }

        mSubTabsAdapter = new WrapAdapter<DataItem>(mContext, mSubTabsDatas,
                R.layout.sub_tab_item) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {

                TextView titleTv = helper.getView(R.id.title);
                View statusView = helper.getView(R.id.tab_select_status);
                titleTv.setText(item.name);
                if (item.isSelected) {
                    titleTv.setSelected(true);
                    statusView.setVisibility(View.VISIBLE);
                } else {
                    titleTv.setSelected(false);
                    statusView.setVisibility(View.GONE);
                }

            }
        };

        mSubTabsGridView.setAdapter(mSubTabsAdapter);

    }

    private String lat = "";
    private String lng = "";
    private String mOrgName = "", mAddress = "";
    private String tel = "";


    private void setFavorite() {

        if (mModel == null) {
            CommonUtils.showToast(mContext, "数据正在加载.....");
            return;
        }

        HashMap<String, String> params = new HashMap<String, String>();
        // args.put("attach", "");
        String model = Constants.FavoriteTypes.ORGANIZATION;
        String sourceId = mOrgId;

        String title = mModel.name;
        params.put("img_path", mModel.banner);
        params.put("model", model);
        params.put("source_id", sourceId);
        params.put("title", title);
        params.put("type", "car");
        params.put("token", IOUtils.getToken(mContext));
        if (isFav == 1) {
            mCancelFavoritePresenter.cancelFavoriteData(params);
        } else {
            mAddFavoritePresenter.addFavoriteData(params);
        }

    }

    private UserDataPresenter mAddFavoritePresenter;
    private UserDataPresenter mCancelFavoritePresenter;

    private void showStoreInfo(final StoreDetailData data) {


        int isCollect = data.isCollect;
        if (isCollect == 0) {
            dataList.get(3).title = "收藏";
        } else {
            dataList.get(3).title = "取消收藏";
        }
        isFav = isCollect;
        morePopupWindow.getmCommonAdapter().notifyDataSetChanged();

        lat = data.latitude;
        lng = data.longitude;
        tel = data.tel;
        //显示店铺基本信息
        mName.setText(data.name);
        mImgCountText.setText(data.bannerNum + "张");
        setRatingScore(mRatingBar, 5, data.compositeScore, R.drawable.rating_bar_star_empty,
                R.drawable.icon_half_a_star_n, R.drawable.rating_bar_star_fill);
        String scoreText = String.format(mRes.getString(R.string.service_rating_text),
                data.serviceScore + "", data.envScore + "");
        // mServiceRatingText.setText(scoreText);
        CommonUtils.customDisplayText(mRes, scoreText, mServiceRatingText, R.color.text_color12);
        mBigHeaderImg
                .setImageURI(Uri.parse(URLUtil.builderImgUrl(data.banner, 540, 270)));

        mLogo.setImageURI(Uri.parse(URLUtil.builderImgUrl(data.logo, 144, 144)));
        mAddress = data.address;
        mOrgName = data.name;
        mAddressText.setText(data.address);
        mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SystemInvoker.launchDailPage(mContext, data.tel);

            }
        });

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.callBtn:
                SystemInvoker.launchDailPage(mContext, tel);
                break;
            case R.id.navBtn:
                //                MapNavPopWindow mapNavPopWindow = new MapNavPopWindow(mContext);
                //                mapNavPopWindow.setOnClickListener(this);
                //                mapNavPopWindow.show(this);

                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.LAT, lat);
                args.put(Constants.IntentParams.LNG, lng);
                args.put(Constants.IntentParams.DATA, mOrgName);
                args.put(Constants.IntentParams.DATA2, mAddress);
                CommonUtils.startNewActivity(mContext, args, LocationPreviewActivity.class);

                break;

            case R.id.evaluateHeaderLayout:
                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, mOrgId);
                args.put(Constants.IntentParams.TEL, tel);
                args.put(Constants.IntentParams.LAT, lat);
                args.put(Constants.IntentParams.LNG, lng);
                CommonUtils.startNewActivity(mContext, args, EvaluationListActivity.class);
                break;

            case R.id.salesHeaderLayout:

                args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, mOrgId);
                CommonUtils.startNewActivity(mContext, args, SalespersonListActivity.class);
                break;
            case R.id.playerHeaderLayout:
                CommonUtils.showToast(mContext, "玩家说");
                break;
            case R.id.aMap:

                try {
                    URLUtil.launcherInnerNavAMap(this, lat, lng);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.baiduMap:
                try {
                    URLUtil.launcherInnerNavBaidu(this, lat, lng);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.cancel:

                break;
        }

    }

    @Override
    public void showData(StoreDetailData storeDetailData) {

        mModel = storeDetailData;
        showStoreInfo(storeDetailData);
    }

    private StoreDetailData mModel;
}
