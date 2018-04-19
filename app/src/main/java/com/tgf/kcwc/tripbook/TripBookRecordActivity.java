package com.tgf.kcwc.tripbook;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.amap.LocationPreviewActivity;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.callback.OnSingleClickListener;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.MorePopupwindowBean;
import com.tgf.kcwc.mvp.model.TripBookRecordModel;
import com.tgf.kcwc.mvp.presenter.TripBookRecordPresenter;
import com.tgf.kcwc.mvp.view.TripBookRecordView;
import com.tgf.kcwc.qrcode.ScannerCodeActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DataUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.StringUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MorePopupWindow;
import com.tgf.kcwc.view.MyGridView;
import com.tgf.kcwc.view.MyListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;


/**
 * @anthor noti
 * @time 2017/9/29
 * @describle
 */
public class TripBookRecordActivity extends BaseActivity implements TripBookRecordView{
    private SimpleDraweeView mapIv;

    private MyListView listView;
    private WrapAdapter<TripBookRecordModel.Data> mAdapter;
    private ArrayList<TripBookRecordModel.Data> mList = new ArrayList<>();

    private MorePopupWindow morePopupWindow;
    private ArrayList<MorePopupwindowBean> dataList = new ArrayList<>();
    private TripBookRecordPresenter mPresenter;
    //路书ID
    private int mBookId;
    //首图地址
    private String mUrl;
    //刷新+加载
    private boolean isRefresh;

    @Override
    protected void setUpViews() {
//        initRefreshLayout(mBGDelegate);

        mBookId = getIntent().getIntExtra(Constants.IntentParams.ID,-1);
        mUrl = getIntent().getStringExtra(Constants.IntentParams.DATA);
//        mBookId = 1276;

        mapIv = (SimpleDraweeView) findViewById(R.id.mapIv);
        if (StringUtils.checkNull(mUrl)){
            mapIv.setImageURI(Uri.parse(mUrl));
        }
        listView = (MyListView) findViewById(R.id.listView);

//        initPopData();

        initAdapter();

        mPresenter = new TripBookRecordPresenter();
        mPresenter.attachView(this);
        mPresenter.getTripBookRecord(mBookId,mPageIndex,mPageSize);
    }

    private void initAdapter() {
        mAdapter = new WrapAdapter<TripBookRecordModel.Data>(this,mList,R.layout.trip_book_record_item) {
            @Override
            public void convert(ViewHolder helper, final TripBookRecordModel.Data item) {
                //大概地址
                helper.setText(R.id.addressTv,item.poi);
                ImageView navigationIv = helper.getView(R.id.navigationIv);
                navigationIv.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    protected void onSingleClick(View view) {
                        Map<String, Serializable> args = new HashMap<String, Serializable>();
                        args.put(Constants.IntentParams.LAT, item.lat);//经度
                        args.put(Constants.IntentParams.LNG, item.lng);//纬度
                        args.put(Constants.IntentParams.DATA, "");
                        args.put(Constants.IntentParams.DATA2, item.poi);
                        CommonUtils.startNewActivity(mContext, args, LocationPreviewActivity.class);
                    }
                });
                ListView bodyList = helper.getView(R.id.bodyList);
                WrapAdapter<TripBookRecordModel.Data.ThreadList> bodyAdapter = new WrapAdapter<TripBookRecordModel.Data.ThreadList>(getContext(),item.threadList,R.layout.body_item) {
                    @Override
                    public void convert(ViewHolder helper, TripBookRecordModel.Data.ThreadList item) {
                        //月日
                        helper.setText(R.id.dayTv,item.day);
                        helper.setText(R.id.monthTv,item.month);
                        //标题及图文
                        TextView titleTv = helper.getView(R.id.titleTv);
                        TextView descTv = helper.getView(R.id.descTv);
                        titleTv.setText(item.title);
                        descTv.setText(item.content);
                        //单图+视频+多图
                        RelativeLayout picVideoRl = helper.getView(R.id.picVideoRl);
                        SimpleDraweeView coverSingleIv = helper.getView(R.id.coverSingleIv);
                        ImageView videoIv = helper.getView(R.id.videoIv);
                        MyGridView gridView = helper.getView(R.id.gridView);
                        //帖子类型 words 帖子及动态 show 光影秀
                        String type = item.model;
                        ArrayList<String> cover = new ArrayList<>();
                        cover.addAll(item.cover);
                        if (type.equals("words")){
                            videoIv.setVisibility(View.GONE);
                            if (cover.size() == 1){
                                gridView.setVisibility(View.GONE);
                                coverSingleIv.setVisibility(View.VISIBLE);
                                //单图
                                coverSingleIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(cover.get(0),144,144)));
                            }else {
                                gridView.setVisibility(View.VISIBLE);
                                coverSingleIv.setVisibility(View.GONE);
                                //初始化适配器
                                initGridAdapter(gridView,cover);
                            }
                        }else if (type.equals("show")){
                            gridView.setVisibility(View.GONE);
                            coverSingleIv.setVisibility(View.VISIBLE);
                            videoIv.setVisibility(View.VISIBLE);
                            //单图
                            coverSingleIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(cover.get(0),144,144)));
                        }
                        //详细地址
                        helper.setText(R.id.detailAddressTv,item.localAddress);
                        helper.setText(R.id.numTv,item.viewCount+"");
                        helper.setText(R.id.zanTv,item.diggCount+"");
                        helper.setText(R.id.commentTv,item.replyCount+"");
                    }
                };
                bodyList.setAdapter(bodyAdapter);
            }
        };
        listView.setAdapter(mAdapter);
    }

    /**
     * 初始化图片适配器
     * @param gridView
     * @param cover
     */
    private void initGridAdapter(GridView gridView, ArrayList<String> cover) {
        WrapAdapter<String> gridAdapter = new WrapAdapter<String>(this,cover,R.layout.trip_book_grid_item) {
            @Override
            public void convert(ViewHolder helper, String item) {
                SimpleDraweeView imageView = helper.getView(R.id.gridImg);
                imageView.setImageURI(Uri.parse(URLUtil.builderImgUrl(item,144,144)));
            }
        };
        gridView.setAdapter(gridAdapter);
    }

    @Override
    protected void titleBarCallback(ImageButton back, final FunctionView function, TextView text) {
        backEvent(back);
        text.setText("我的记录");
//        function.setImageResource(R.drawable.global_nav_n);
//        function.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (dataList != null && dataList.size() != 0) {
//                    morePopupWindow = new MorePopupWindow(TripBookRecordActivity.this,
//                            dataList, mMoreOnClickListener);
//                    morePopupWindow.showPopupWindow(function);
//                }
//            }
//        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_book_record);
    }

    @Override
    public void showTripBookRecord(ArrayList<TripBookRecordModel.Data> model) {
        if (isRefresh){
            if (null != model){
                mList.clear();
                mList.addAll(model);
                mAdapter.notifyDataSetChanged();
            }
        }else {
            if (null != model){
                mList.clear();
                mList.addAll(model);
                mAdapter.notifyDataSetChanged();
            }else {
                CommonUtils.showToast(getContext(),"赞无更多数据");
            }
        }

    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return this;
    }
    //初始化弹窗数据
    private void initPopData(){
        Map<String, Integer> titleActionIconMap = DataUtil.getTitleActionIcon2();
        String[] data = mRes.getStringArray(R.array.global_nav_values);
        for (int i = 0; i < data.length; i++) {
            MorePopupwindowBean morePopupwindowBean = null;
            morePopupwindowBean = new MorePopupwindowBean();
            morePopupwindowBean.title = data[i];
            morePopupwindowBean.icon = titleActionIconMap.get(data[i]);
            morePopupwindowBean.id = i;
            dataList.add(morePopupwindowBean);
        }
    }

    private MorePopupWindow.MoreOnClickListener mMoreOnClickListener = new MorePopupWindow.MoreOnClickListener() {
        @Override
        public void moreOnClickListener(int position, MorePopupwindowBean item) {
            switch (dataList.get(position).title) {
                case "首页":
                    finish();
                    break;
                case "消息":
                    break;
                case "分享":
//                    share();
                    break;
                case "扫一扫":
                    if (IOUtils.isLogin(mContext)) {
                        startActivity(new Intent(mContext, ScannerCodeActivity.class));
                    }
                    break;
                case "反馈":
                    break;
//                case "编辑":
//                    if (unStopDialog == null) {
//                        Intent toCreateIntent = new Intent(getContext(), EditTripbookActivity.class);
//                        toCreateIntent.putExtra(Constants.IntentParams.ID, mTripBookDetailModel.ride_id);
//                        startActivity(toCreateIntent);
//                    } else {
//                        unStopDialog.show();
//                    }
//                    break;
//                case "删除":
//                    tripPresenter.deleteTripBook(mBookId, token);
//                    break;
                case "举报":
                    break;
                case "收藏":
//                    HashMap<String, String> params = new HashMap<String, String>();
//                    params.put("model", "roadbook");
//                    params.put("source_id", mTripBookDetailModel.thread_id + "");
//                    params.put("title", mTripBookDetailModel.title);
//                    params.put("type", "car");
//                    params.put("token", IOUtils.getToken(mContext));
//                    if (isFav == 1) {
//                        mFavorPresenter.cancelFavoriteData(params);
//                    } else {
//                        mFavorPresenter.addFavoriteData(params);
//                    }
                    break;
            }
        }
    };

    BGARefreshLayout.BGARefreshLayoutDelegate mBGDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {

        @Override
        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            mPageIndex = 1;
            isRefresh = true;
            loadMore();
        }

        @Override
        public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
            isRefresh = false;
            mPageIndex++;
            loadMore();
            return false;
        }
    };

    public void loadMore() {
        mPresenter.getTripBookRecord(mBookId,mPageIndex,mPageSize);
    }
}
