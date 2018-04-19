package com.tgf.kcwc.posting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.tgf.kcwc.mvp.model.TopicTagDataModel;
import com.tgf.kcwc.mvp.presenter.CarDataPresenter;
import com.tgf.kcwc.mvp.view.CarDataView;
import com.tgf.kcwc.mvp.view.DividerItemDecoration;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author:Jenny
 * Date:2017/9/4
 * E-mail:fishloveqin@gmail.com
 * 公共标签设置页面
 */

public class CommonTagSettingActivity extends BaseActivity implements OnItemClickListener<TopicTagDataModel> {
    protected TextView exhibitionName;


    private RecyclerView mRv;
    private HeaderRecyclerAndFooterWrapperAdapter mHeaderAdapter;
    private LinearLayoutManager mManager;
    private ArrayList<Brand> mDatas = new ArrayList<Brand>();
    ;

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

    protected ImageView moreImage;
    protected RelativeLayout headerLayout;
    private String eventId;
    private String fromType;
    private String mType = "";
    private String eventName = "";
    private CarDataPresenter mCarPresenter;

    @Override
    protected void setUpViews() {
        initView();
        mCarPresenter = new CarDataPresenter();
        mCarPresenter.attachView(carDataView);
    }

    private CarDataView<List<TopicTagDataModel>> carDataView = new CarDataView<List<TopicTagDataModel>>() {
        @Override
        public void showData(List<TopicTagDataModel> topicTagDataModels) {

            showBrand(topicTagDataModels);

        }

        @Override
        public void setLoadingIndicator(boolean active) {

        }

        @Override
        public void showLoadingTasksError() {

        }

        @Override
        public Context getContext() {
            return mContext;
        }
    };

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        showTitle(text);
        backEvent(back);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mType = intent.getStringExtra(Constants.IntentParams.TAG_TYPE);
        eventId = intent.getStringExtra(Constants.IntentParams.ID);
        eventName = intent.getStringExtra(Constants.IntentParams.DATA);
        super.setContentView(R.layout.activity_common_tag_setting);
        mCarPresenter.getTopicTagDatas(builderReqParams());
    }

    private void initView() {
        exhibitionName = (TextView) findViewById(R.id.exhibitionName);
        exhibitionName.setText("" + eventName);
        mRv = (RecyclerView) findViewById(R.id.rv);
        mRv.setLayoutManager(mManager = new LinearLayoutManager(this));
        mIndexBar = (IndexBar) findViewById(R.id.indexBar);
        mTvSideBarHint = (TextView) findViewById(R.id.tvSideBarHint);
        moreImage = (ImageView) findViewById(R.id.moreImage);
        headerLayout = (RelativeLayout) findViewById(R.id.headerLayout);
        headerLayout.setOnClickListener(this);

        initAdapter();
        mAdapter.setOnItemClickListener(this);
    }


    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {

            case R.id.headerLayout:

                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, eventId);
                CommonUtils.startResultNewActivity(this, args, TagExhibitionsActivity.class, Constants.InteractionCode.REQUEST_CODE);
                break;
        }


    }

    private void showTitle(TextView titleTv) {
        String title = "";

        String[] arrays = mRes.getStringArray(R.array.tag_title_values);
        switch (mType) {

            case Constants.TopicParams.SHOW_CAR:
                title = arrays[0];
                fromType = Constants.TopicParams.SHOW_CAR;
                break;
            case Constants.TopicParams.NEW_CAR:
                title = arrays[1];
                fromType = Constants.TopicParams.NEW_CAR;
                break;
            case Constants.TopicParams.MODEL:
                title = arrays[2];
                fromType = Constants.TopicParams.MODEL;
                break;
        }
        titleTv.setText(title);
    }


    private Map<String, String> builderReqParams() {

        Map<String, String> params = new HashMap<String, String>();

        params.put("parent_id", "0");
        params.put("vehicle_type", "car");
        params.put("event_id", eventId);
        params.put("from_type", fromType);
        params.put("token", IOUtils.getToken(mContext));

        return params;
    }

    private static final int MIN_CHARS = 6;

    public void showBrand(List<TopicTagDataModel> topicTagDataModels) {

        Set<String> sets = new HashSet<String>();
        for (TopicTagDataModel topicTagDataModel : topicTagDataModels) {

            topicTagDataModel.setBaseIndexTag(topicTagDataModel.letter);
            sets.add(topicTagDataModel.letter);
            mDatas.add(topicTagDataModel);
        }
        //先排序
        //mSourceDatas.addAll(mDatas);
        if (mDatas.size() == 0) {
            mIndexBar.setVisibility(View.GONE);
        } else {
            mIndexBar.setVisibility(View.VISIBLE);
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mIndexBar.getLayoutParams();
        int size = sets.size();
        if (size <= MIN_CHARS) {
            layoutParams.height = BitmapUtils.dp2px(mContext, 200);
        } else {
            layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
        }


        mIndexBar.setLayoutParams(layoutParams);
        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(mManager);//设置RecyclerView的LayoutManager
        mIndexBar.setmSourceDatas(mDatas)//设置数据
                .invalidate();
        mHeaderAdapter.notifyDataSetChanged();
    }


    private void initAdapter() {


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
        mRv.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));

        //使用indexBar
        mTvSideBarHint = (TextView) findViewById(R.id.tvSideBarHint);//HintTextView
        mIndexBar = (IndexBar) findViewById(R.id.indexBar);//IndexBar


    }

    @Override
    public void onItemClick(ViewGroup parent, View view, TopicTagDataModel topicTagDataModel, int position) {


        Map<String, Serializable> args = new HashMap<String, Serializable>();
        args.put(Constants.IntentParams.ID, eventId);
        args.put(Constants.IntentParams.TAG_BRAND_ID, topicTagDataModel.id + "");
        args.put(Constants.IntentParams.TAG_TYPE, mType);
        if (Constants.TopicParams.MODEL.equals(fromType)) {
            CommonUtils.startNewActivity(mContext, args, SelectModelByBrandActivity.class);
        } else {

            CommonUtils.startNewActivity(mContext, args, SelectCarByTagBrandActivity.class);
        }


    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, TopicTagDataModel topicTagDataModel, int position) {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (Constants.InteractionCode.REQUEST_CODE == requestCode && RESULT_OK == resultCode) {

            String name = data.getStringExtra(Constants.IntentParams.NAME);
            String id = data.getStringExtra(Constants.IntentParams.ID);
            exhibitionName.setText(name);
            eventId = id;
            mDatas.clear();
            mSourceDatas.clear();
            mCarPresenter.getTopicTagDatas(builderReqParams());

        }
    }
}
