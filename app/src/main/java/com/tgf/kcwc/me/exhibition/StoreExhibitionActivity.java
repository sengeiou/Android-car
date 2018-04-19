package com.tgf.kcwc.me.exhibition;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.driving.driv.ListviewHint;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.ExhibitionArryBean;
import com.tgf.kcwc.mvp.model.StoreExhibitionBean;
import com.tgf.kcwc.mvp.model.StreBelowAmendBean;
import com.tgf.kcwc.mvp.presenter.StreExhibitionPresenter;
import com.tgf.kcwc.mvp.view.StoreExhibitionView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.DropUpPhonePopupWindow;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MyListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by Administrator on 2017/7/18.
 */

public class StoreExhibitionActivity extends BaseActivity implements StoreExhibitionView {

    private StreExhibitionPresenter mStreExhibitionPresenter;
    private ListView mListView;
    private WrapAdapter<StoreExhibitionBean.DataList> mAdapter;
    private List<StoreExhibitionBean.DataList> dataList = new ArrayList<>();
    private RelativeLayout mTitleBar;
    private RelativeLayout releaseLayout;                           //发布
    private SimpleDraweeView brandimage;                              //品牌图片
    private TextView brandname;                               //品牌名字
    private TextView recordnum;                               //记录条数
    private LinearLayout selectlayout;                            //点击选择
    private TextView selectname;                              //选择的类型
    private DropUpPhonePopupWindow mDropUpPhonePopupWindow;
    private List<DataItem> mDataList = new ArrayList<>(); //type数据
    private DropUpPhonePopupWindow mDropUpExhibitionPopupWindow;            //选择展会
    private List<DataItem> mExhibitionDataList = new ArrayList<>(); //选择展会数据
    private StoreExhibitionBean mStoreBelowBean = null;
    private int page = 1;
    private boolean isRefresh = true;
    private String type = "";
    private String exhibitionname = "";
    private String eventId = "";
    private ListviewHint mListviewHint;                           //尾部
    private LinearLayout mSeleexhibition;                         //选择展会

    public void gainTypeDataList() {
        mDataList.clear();
        DataItem dataItem = null;
        dataItem = new DataItem();
        dataItem.id = -1;
        dataItem.name = "筛选";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = -1;
        dataItem.name = "全部";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 0;
        dataItem.name = "已撤销";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 1;
        dataItem.name = "未发布";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 2;
        dataItem.name = "已发布";
        mDataList.add(dataItem);
    }

    @Override
    protected void setUpViews() {
        mStreExhibitionPresenter = new StreExhibitionPresenter();
        mStreExhibitionPresenter.attachView(this);
        gainTypeDataList();
        initRefreshLayout(mBGDelegate);
        mListView = (ListView) findViewById(R.id.list);
        mTitleBar = (RelativeLayout) findViewById(R.id.titleBar);
        brandimage = (SimpleDraweeView) findViewById(R.id.brandimage);
        brandname = (TextView) findViewById(R.id.brandname);
        recordnum = (TextView) findViewById(R.id.recordnum);
        selectlayout = (LinearLayout) findViewById(R.id.selectlayout);
        selectname = (TextView) findViewById(R.id.selectname);
        releaseLayout = (RelativeLayout) findViewById(R.id.releaseLayout);
        mSeleexhibition = (LinearLayout) findViewById(R.id.seleexhibition);

        mAdapter = new WrapAdapter<StoreExhibitionBean.DataList>(mContext, dataList,
                R.layout.activity_storebelow_item) {
            @Override
            public void convert(ViewHolder helper, final StoreExhibitionBean.DataList item) {
                final int position = helper.getPosition();
                WrapAdapter<DataItem> KeyAdapter;
                MyListView mMyListView = helper.getView(R.id.keylistview);
                TextView drivdetailsTitle = helper.getView(R.id.drivdetails_title);//车系名字
                TextView facade = helper.getView(R.id.facade); //外观名字
                TextView upholstery = helper.getView(R.id.upholstery);//内饰名字
                SimpleDraweeView facadeimag = helper.getView(R.id.facadeimag); //外观图片
                SimpleDraweeView upholsteryimage = helper.getView(R.id.upholsteryimage); //内饰图片
                LinearLayout compile = helper.getView(R.id.compile); //编辑
                LinearLayout revocation = helper.getView(R.id.revocation); //撤销
                LinearLayout release = helper.getView(R.id.release); //发布
                List<DataItem> keyList = new ArrayList<>();

                facadeimag
                        .setImageURI(Uri.parse(URLUtil.builderImgUrl(item.appearanceImg, 270, 203)));
                upholsteryimage
                        .setImageURI(Uri.parse(URLUtil.builderImgUrl(item.interiorImg, 270, 203)));
                if (item.productId != 0) {
                   // drivdetailsTitle.setText(item.factoryName + " " + item.seriesName + " " + item.productName);
                    drivdetailsTitle.setText( item.seriesName + " " + item.productName);
                } else {
                    //drivdetailsTitle.setText(item.factoryName + " " + item.seriesName);
                    drivdetailsTitle.setText( item.seriesName);
                }
                facade.setText(item.appearanceColorName);
                upholstery.setText(item.interiorColorName);

                if (item.status == 1) {
                    DataItem dataItem = new DataItem();
                    dataItem.id = item.postUserId;
                    dataItem.name = item.postRealName;
                    dataItem.nickname = item.postNickname;
                    dataItem.time = item.postTime;
                    dataItem.title = "上传人：";
                    keyList.add(dataItem);
                } else {
                    DataItem dataItem = null;
                    dataItem = new DataItem();
                    dataItem.id = item.postUserId;
                    dataItem.name = item.postRealName;
                    dataItem.nickname = item.postNickname;
                    dataItem.time = item.postTime;
                    dataItem.title = "上传人：";
                    keyList.add(dataItem);
                    dataItem = new DataItem();
                    dataItem.id = item.reviewUserId;
                    dataItem.name = item.reviewRealName;
                    dataItem.nickname = item.reviewNickname;
                    dataItem.time = item.reviewTime;
                    dataItem.title = "发布人：";
                    keyList.add(dataItem);
                }
                //发布状态（0已撤销，1仅上传，2已发布）
                if (item.status != 2) {
                    //是否是本人发布
                    if (item.isPostUser == 1||mStoreBelowBean.data.auth == 2 ) {//编辑
                        compile.setVisibility(View.VISIBLE);
                        compile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Map<String, Serializable> args = new HashMap<>();
                                args.put(Constants.IntentParams.ID, mStoreBelowBean.data.auth);
                                args.put(Constants.IntentParams.ID2, item.id);
                                CommonUtils.startNewActivity(mContext, args,
                                        CompileStoreExhibitionActivity.class);
                            }
                        });
                    } else {
                        compile.setVisibility(View.GONE);
                    }

                }else {
                    compile.setVisibility(View.GONE);
                }

                //auth他的权限  1是上传 2发布
                if (mStoreBelowBean.data.auth == 2 && item.status != 2) {//发布
                    release.setVisibility(View.VISIBLE);
                    release.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mStreExhibitionPresenter.getRelease(IOUtils.getToken(mContext),
                                    item.id + "", position);
                        }
                    });
                } else {
                    release.setVisibility(View.GONE);
                }

                if (mStoreBelowBean.data.auth == 2 && item.status == 2) {//撤销
                    revocation.setVisibility(View.VISIBLE);
                    revocation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mStreExhibitionPresenter.getCancel(IOUtils.getToken(mContext),
                                    item.id + "", position);
                        }
                    });
                } else {
                    revocation.setVisibility(View.GONE);
                }

                KeyAdapter = new WrapAdapter<DataItem>(mContext, keyList,
                        R.layout.storebelow_list_item) {
                    @Override
                    public void convert(ViewHolder helper, DataItem item) {
                        TextView title = helper.getView(R.id.title);
                        TextView name = helper.getView(R.id.name);
                        TextView time = helper.getView(R.id.time);
                        title.setText(item.title);
                        name.setText(item.name + "（昵称是" + item.nickname + "）");
                        time.setText(item.time);
                    }
                };
                mMyListView.setAdapter(KeyAdapter);
            }
        };
        mListView.setAdapter(mAdapter);
        mTitleBar.setBackgroundColor(getResources().getColor(R.color.app_layout_bg_color));
        mStreExhibitionPresenter.getDataLists(IOUtils.getToken(mContext), page, type, eventId);

        selectlayout.setOnClickListener(this);
        releaseLayout.setOnClickListener(this);
        mSeleexhibition.setOnClickListener(this);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        back.setImageResource(R.drawable.nav_back_selector2);
        backEvent(back);
        text.setText(R.string.storeexhibition);
        text.setTextSize(16);
        text.setTextColor(getResources().getColor(R.color.text_color14));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storeexhibition);
    }

    @Override
    public void dataSucceed(StoreExhibitionBean storeBelowBean) {
        mStoreBelowBean = storeBelowBean;
        stopRefreshAll();
        mListView.removeFooterView(mListviewHint);
        if (page == 1) {
            dataList.clear();
        } else {
            if (storeBelowBean.data.list == null || storeBelowBean.data.list.size() == 0) {
                isRefresh = false;
                mListviewHint = new ListviewHint(mContext);
                mListView.addFooterView(mListviewHint);
            }
        }

        if (storeBelowBean.data.list == null || storeBelowBean.data.list.size() == 0) {
            isRefresh = false;
        }

        dataList.addAll(storeBelowBean.data.list);
        mAdapter.notifyDataSetChanged();
        brandimage.setImageURI(
                Uri.parse(URLUtil.builderImgUrl(storeBelowBean.data.event.cover, 144, 144)));
        exhibitionname=storeBelowBean.data.event.name;
        eventId=storeBelowBean.data.event.id+"";
        brandname.setText(storeBelowBean.data.event.name);
        String count = storeBelowBean.data.pagination.count + "";
        setTextColors(recordnum, "共 " + count + " 条记录", 2, count.length() + 2,
                R.color.text_color14);
    }

    @Override
    public void eventListSucceed(ExhibitionArryBean exhibitionArryBean) {
        List<ExhibitionArryBean.Data> data = exhibitionArryBean.data;
        mExhibitionDataList.clear();
        DataItem dataItem = null;
        dataItem = new DataItem();
        dataItem.id = -1;
        dataItem.name = "请选择展会";
        mExhibitionDataList.add(dataItem);
        for (ExhibitionArryBean.Data item : data) {
            dataItem = new DataItem();
            dataItem.id = item.id;
            dataItem.name = item.name;
            mExhibitionDataList.add(dataItem);
        }
        mDropUpExhibitionPopupWindow = new DropUpPhonePopupWindow(mContext, mExhibitionDataList);
        //mDropUpExhibitionPopupWindow.showAsDropDownBelwBtnView(mTitleBar);
        mDropUpExhibitionPopupWindow.show(StoreExhibitionActivity.this);
        mDropUpExhibitionPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                DataItem selectDataItem = mDropUpExhibitionPopupWindow.getSelectDataItem();
                if (selectDataItem != null && mDropUpExhibitionPopupWindow.getIsSelec()) {
                    if (selectDataItem.id >= 0) {
                        eventId = selectDataItem.id + "";
                        exhibitionname=selectDataItem.name;
                        type = "";
                        selectname.setText("全部");
                        brandname.setText(selectDataItem.name);
                        page = 1;
                        isRefresh = true;
                        loadMore();
                    }
                }
            }
        });

    }

    @Override
    public void revocationSucceed(StreBelowAmendBean baseBean, int num) {
        CommonUtils.showToast(mContext, "撤销成功");
        dataList.get(num).status = 0;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void releaseSucceed(StreBelowAmendBean baseBean, int num) {
        CommonUtils.showToast(mContext, "发布成功");
        dataList.get(num).status = 2;
        dataList.get(num).reviewRealName = baseBean.data.realName;
        dataList.get(num).reviewNickname = baseBean.data.nickname;
        dataList.get(num).reviewTime = baseBean.data.reviewTime;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void dataListDefeated(String msg) {
        CommonUtils.showToast(mContext, msg);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {
        showLoadingIndicator(false);
        finish();
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.selectlayout:
                mDropUpPhonePopupWindow = new DropUpPhonePopupWindow(mContext, mDataList);
                // mDropUpPhonePopupWindow.showAsDropDownBelwBtnView(mTitleBar);
                mDropUpPhonePopupWindow.show(StoreExhibitionActivity.this);
                mDropUpPhonePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        DataItem selectDataItem = mDropUpPhonePopupWindow.getSelectDataItem();
                        if (selectDataItem != null && mDropUpPhonePopupWindow.getIsSelec()) {
                            if (selectDataItem.id >= 0) {
                                type = selectDataItem.id + "";
                            } else {
                                type = "";
                            }
                            selectname.setText(selectDataItem.name);
                            page = 1;
                            isRefresh = true;
                            loadMore();
                        }
                    }
                });
                break;
            case R.id.releaseLayout:
                if (mStoreBelowBean != null) {
                    Map<String, Serializable> args = new HashMap<>();
                    args.put(Constants.IntentParams.ID, mStoreBelowBean.data.auth);
                    args.put(Constants.IntentParams.DATA, eventId);
                    args.put(Constants.IntentParams.DATA2,exhibitionname);
                    CommonUtils.startNewActivity(mContext, args, SponsorStoreExhibitionActivity.class);
                } else {
                    CommonUtils.showToast(mContext, "系统异常");
                }
                break;
            case R.id.seleexhibition:
                mStreExhibitionPresenter.getEventList(IOUtils.getToken(mContext));
                break;

        }
    }

    BGARefreshLayout.BGARefreshLayoutDelegate mBGDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {

        @Override
        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            // 在这里加载最新数据

            //            if (HttpUtils.isConnectNetwork(getApplicationContext())) {
            //                // 如果网络可用，则加载网络数据
            //
            //                loadMore();
            //            } else {
            //                // 网络不可用，结束下拉刷新
            //                HttpUtils.registerNWReceiver(getApplicationContext());
            //                mRefreshLayout.endRefreshing();
            //            }
            // mPageIndex = 1;
            page = 1;
            isRefresh = true;
            loadMore();
        }

        @Override
        public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
            // 在这里加载更多数据，或者更具产品需求实现上拉刷新也可以

            //            if (HttpUtils.isConnectNetwork(getApplicationContext())) {
            //                loadMore();
            //                return true;
            //            } else {
            //                // 网络不可用，返回false，不显示正在加载更多
            //                HttpUtils.registerNWReceiver(getApplicationContext());
            //                mRefreshLayout.endRefreshing();
            //                return false;
            //            }
            //loadMore();
            if (isRefresh) {
                page++;
                loadMore();
            }
            return false;
        }
    };

    public void loadMore() {
        mStreExhibitionPresenter.getDataLists(IOUtils.getToken(mContext), page, type, eventId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mStreExhibitionPresenter != null) {
            mStreExhibitionPresenter.detachView();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        page = 1;
        isRefresh = true;
        loadMore();
    }
}
