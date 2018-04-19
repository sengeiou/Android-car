package com.tgf.kcwc.posting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.ExhibitPlace;
import com.tgf.kcwc.mvp.model.Exhibition;
import com.tgf.kcwc.mvp.model.ExhibitionDetailModel;
import com.tgf.kcwc.mvp.model.Image;
import com.tgf.kcwc.mvp.model.Topic;
import com.tgf.kcwc.mvp.model.TopicSearchModel;
import com.tgf.kcwc.mvp.presenter.ExhibitionDetailPrenter;
import com.tgf.kcwc.mvp.presenter.TopicDataPresenter;
import com.tgf.kcwc.mvp.view.ExhibitDetailView;
import com.tgf.kcwc.mvp.view.TopicDataView;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.ExRelativeLayout;
import com.tgf.kcwc.view.FunctionView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：Jenny
 * Date:2017/3/2 19:15
 * E-mail：fishloveqin@gmail.com
 */

public class TopicListActivity extends BaseActivity implements TopicDataView<TopicSearchModel> {
    protected EditText mSearch;
    protected TextView mCancel;
    protected ListView mList;
    private TopicDataPresenter mPresenter;

    private RelativeLayout mAddTagLayout;
    private TextView mTagTv;

    private TopicDataPresenter mCreatePresenter;
    private InputMethodManager imm;
    private int screenHeight;
    private int screenWidth;
    private int threshold;

    private boolean isShow;

    private String fromType;

    private String eventName;
    private String eventId;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    private String mSearchKey = "";
    private ExhibitionDetailPrenter exhibitionDetailPrenter;

    @Override
    protected void setUpViews() {
        initView();
        mPresenter = new TopicDataPresenter();
        mPresenter.attachView(this);
        mPresenter.loadTopicsListV2(mSearchKey, IOUtils.getToken(mContext));
        mCreatePresenter = new TopicDataPresenter();
        mCreatePresenter.attachView(mCreateDataView);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

    }

    private ExhibitDetailView exhibitDetailView = new ExhibitDetailView() {
        @Override
        public void showHead(Exhibition exhibition) {


        }

        @Override
        public void showMenu(List<ExhibitionDetailModel.MenuItem> menuItemList) {

        }

        @Override
        public void showBanner(ArrayList<Image> bannerList) {

        }

        @Override
        public void showPlaceList(ArrayList<ExhibitPlace> exhibitPlacelist) {

        }

        @Override
        public void showPlink(ArrayList<String> pinkImgs, int type) {

        }

        @Override
        public void showJump() {

        }

        @Override
        public void showExhibitionList(ArrayList<Exhibition> exhibitionList) {

            if (exhibitionList.size() > 0) {
                Exhibition exhibition = exhibitionList.get(0);
                eventName = exhibition.name;
                eventId = exhibition.id + "";
                Logger.d("eventName:" + eventName + ",eventId:" + eventId);
            }

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
    private TopicDataView<Topic> mCreateDataView = new TopicDataView<Topic>() {
        @Override
        public void showData(Topic topic) {

            CommonUtils.showToast(mContext, "创建成功！");
            if (TextUtils.isEmpty(topic.title)) {
                topic.title = mSearchKey;
            }
            setResult(topic);
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
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {

            case R.id.addTagLayout:

                mCreatePresenter.createTag(mSearchKey, IOUtils.getToken(mContext));
                break;

            case R.id.cancel:

                if (!isShow) {

                    finish();
                    return;
                }
                imm.hideSoftInputFromWindow(mSearch.getWindowToken(), 0);
                mSearchKey = "";
                mSearch.setText("");
                mPresenter.loadTopicsListV2(mSearchKey, IOUtils.getToken(mContext));
                break;
        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        fromType = intent.getStringExtra(Constants.IntentParams.FROM_TYPE);

        Logger.e("fromType:" + fromType);

        String topParam = KPlayCarApp.getValue(Constants.KeyParams.KEY_DATA);

        if (!TextUtils.isEmpty(fromType)) {
            eventName = mGlobalContext.mExhibitin.name;
            eventId = mGlobalContext.mExhibitin.id + "";
        } else if (Constants.TopicParams.EXHIBITION_PARAM.equals(topParam)) {
            eventName = mGlobalContext.mExhibitin.name;
            eventId = mGlobalContext.mExhibitin.id + "";
            KPlayCarApp.removeValue(Constants.KeyParams.KEY_DATA);
        } else {
            exhibitionDetailPrenter = new ExhibitionDetailPrenter();
            exhibitionDetailPrenter.attachView(exhibitDetailView);
            //exhibitionDetailPrenter.loadExhibitionDetail(0, mGlobalContext.locCityName, mGlobalContext.adcode);
            exhibitionDetailPrenter.getExhibitionList(builderExhibitListReqParams());
        }
        super.setContentView(R.layout.activity_topic_list);

        isTitleBar = false;

    }

    private Map<String, String> builderExhibitListReqParams() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("page", "1");
        params.put("pagecount", "9999");
        params.put("token", IOUtils.getToken(mContext));
        params.put("adcode", mGlobalContext.adcode);
        return params;
    }

    @Override
    public void showData(TopicSearchModel model) {

        List<Topic> topics = model.list;
        int size = topics.size();
        if (size == 0) {

            if (!isInput) {
                mAddTagLayout.setVisibility(View.GONE);
            } else {
                mAddTagLayout.setVisibility(View.VISIBLE);
                mTagTv.setText("添加新标签:\t" + mSearchKey);
                isInput = false;
            }


        } else {
            if (model.matchNum == 0) {
                if (!isInput) {
                    mAddTagLayout.setVisibility(View.GONE);
                } else {
                    mAddTagLayout.setVisibility(View.VISIBLE);
                    mTagTv.setText("添加新标签:\t" + mSearchKey);
                    isInput = false;
                }

            } else {
                mAddTagLayout.setVisibility(View.GONE);
            }

        }

        filterExistTag(topics);
        WrapAdapter<Topic> adapter = new WrapAdapter<Topic>(mContext, R.layout.select_topic_item,
                topics) {
            @Override
            public void convert(ViewHolder helper, Topic item) {

                SimpleDraweeView cover = helper.getView(R.id.image);
                cover.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 144, 144)));

                TextView titleTv = helper.getView(R.id.topicName);
                titleTv.setText(item.title);

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) titleTv.getLayoutParams();
                ImageView statusImage = helper.getView(R.id.statusImage);

                if (item.isSelected) {
                    statusImage.setVisibility(View.VISIBLE);
                    params.setMargins(BitmapUtils.dp2px(mContext, 10), 0, BitmapUtils.dp2px(mContext, 25), 0);
                } else {
                    params.setMargins(BitmapUtils.dp2px(mContext, 10), 0, BitmapUtils.dp2px(mContext, 10), 0);
                    statusImage.setVisibility(View.GONE);
                }
            }
        };
        mList.setAdapter(adapter);
    }

    private void filterExistTag(List<Topic> topics) {


        List<Topic> selTopics = KPlayCarApp.getValue(Constants.KeyParams.TAGS_DATA);
        if (selTopics != null && selTopics.size() > 0) {

            for (Topic t : selTopics) {

                for (Topic topic : topics) {

                    if (t.id == topic.id) {

                        topic.isSelected = true;
                    }
                }
            }
        }

    }

    private void initView() {
        mSearch = (EditText) findViewById(R.id.etSearch);
        mCancel = (TextView) findViewById(R.id.cancel);
        mCancel.setOnClickListener(this);
        mAddTagLayout = (RelativeLayout) findViewById(R.id.addTagLayout);
        mTagTv = (TextView) findViewById(R.id.addTagTv);
        mAddTagLayout.setOnClickListener(this);
        mSearch.addTextChangedListener(mWatcher);
        mList = (ListView) findViewById(R.id.list);
        mList.setOnItemClickListener(mTopicItemClickListener);
        mAddTagLayout.setVisibility(View.GONE);
        final ExRelativeLayout rootLayout = (ExRelativeLayout) findViewById(R.id.rootView);

        rootLayout.setSoftKeyboardListener(new ExRelativeLayout.OnSoftKeyboardListener() {
            @Override
            public void onSoftKeyboardChange() {
                isShow = isSoftKeyboardShow(rootLayout);
            }
        });

    }

    private boolean isSoftKeyboardShow(View rootView) {
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        threshold = screenHeight / 3;
        int rootViewBottom = rootView.getBottom();
        Rect rect = new Rect();
        rootView.getWindowVisibleDisplayFrame(rect);
        int visibleBottom = rect.bottom;
        int heightDiff = rootViewBottom - visibleBottom;
        //        System.out
        //            .println("----> rootViewBottom=" + rootViewBottom + ",visibleBottom=" + visibleBottom);
        //        System.out.println("----> heightDiff=" + heightDiff + ",threshold=" + threshold);
        return heightDiff > threshold;
    }

    /**
     * 设置返回值
     *
     * @param topic
     */
    private void setResult(Topic topic) {

        if (isShow) {
            imm.hideSoftInputFromWindow(mSearch.getWindowToken(), 0);
        }
        Intent intent = new Intent();
        intent.putExtra(Constants.IntentParams.DATA, topic);
        setResult(RESULT_OK, intent);
        finish();
    }

    private AdapterView.OnItemClickListener mTopicItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            Topic topic = (Topic) parent.getAdapter().getItem(position);

            int isSystem = topic.isSystem;
            if (isSystem == 1) {

                String model = topic.model;
                Map<String, Serializable> params = new HashMap<String, Serializable>();
                String type = "";
                switch (model) {

                    case Constants.TopicParams.SHOW_CAR:
                        type = Constants.TopicParams.SHOW_CAR;
                        break;
                    case Constants.TopicParams.NEW_CAR:
                        type = Constants.TopicParams.NEW_CAR;
                        break;
                    case Constants.TopicParams.MODEL:
                        type = Constants.TopicParams.MODEL;
                        break;
                }
                if (TextUtils.isEmpty(eventName)) {
                    CommonUtils.showToast(mContext, "正在获取展会信息，请稍候!");
                } else {
                    params.put(Constants.IntentParams.TAG_TYPE, type);
                    params.put(Constants.IntentParams.ID, eventId);
                    params.put(Constants.IntentParams.DATA, eventName);
                    CommonUtils.startNewActivity(mContext, params, CommonTagSettingActivity.class);
                }


            } else {
                setResult(topic);
            }


        }
    };

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

    private boolean isInput;
    TextWatcher mWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            isInput = true;
            mSearchKey = s.toString().trim();
            if (TextUtil.isEmpty(mSearchKey)) {
                isInput = false;
            } else {
                isInput = true;
            }
            mPresenter.loadTopicsListV2(mSearchKey, IOUtils.getToken(mContext));
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.detachView();
        }

        if (mCreatePresenter != null) {

            mCreatePresenter.detachView();

        }
    }
}
