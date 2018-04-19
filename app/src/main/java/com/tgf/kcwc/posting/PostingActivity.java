package com.tgf.kcwc.posting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.google.gson.Gson;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.tgf.kcwc.R;
import com.tgf.kcwc.amap.MarkerPointActivity;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.callback.OnSingleClickListener;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.RichEditorEntity;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.Topic;
import com.tgf.kcwc.mvp.model.TopicDetailModel;
import com.tgf.kcwc.mvp.model.TopicTagDataModel;
import com.tgf.kcwc.mvp.presenter.TopicDataPresenter;
import com.tgf.kcwc.mvp.view.TopicDataView;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.view.FlowLayout;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.richeditor.SEditorData;
import com.tgf.kcwc.view.richeditor.SortRichEditor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：Jenny
 * Date:2016/12/8 22:04
 * E-mail：fishloveqin@gmail.com
 * 发帖(普通帖子)
 */

public class PostingActivity extends BaseActivity implements TopicDataView<List<String>> {
    protected TextView mCancelBtn;
    protected TextView mTitleBarText;
    protected TextView mTitleFunctionBtn;
    private SortRichEditor editor;
    private static final int IMAGE_PICKER = 1001;
    private TextView mBtnGallery, mBtnTag;
    private FlowLayout mTagLayout;
    private RelativeLayout mTagRootLayout;
    private List<Topic> mTags = new ArrayList<Topic>(); //帖子标签集合，最多只存取5个
    private TextView mAddressTv;
    private static final int TAG_MAX = 5;
    private TextView mPositionName;
    private TextView mAddressNameTv;
    private LinearLayout mAddressLayout;
    private PoiItem mPoiItem;
    private String latitude = "", longitude = "";

    private TopicDataPresenter mPresenter, mCreateTagPresenter, mTopicDetailPreseneter;

    private String mLocationAddress = "";
    private String parentId = "";

    private int fromId;
    private String fromType;

    private int mRideId = -1;

    private String mEndAddress;

    private boolean isEdit;
    private String threadId = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isTitleBar = false;
        Intent intent = getIntent();
        mEndAddress = intent.getStringExtra(Constants.IntentParams.RIDE_ADDRESS);
        parentId = intent.getStringExtra(Constants.IntentParams.ID);
        fromId = intent.getIntExtra(Constants.IntentParams.FROM_ID, -1);
        fromType = intent.getStringExtra(Constants.IntentParams.FROM_TYPE);
        mRideId = intent.getIntExtra(Constants.IntentParams.RIDE_ID, -1);
        isEdit = intent.getBooleanExtra(Constants.IntentParams.EDIT_TOPIC, false);
        threadId = intent.getStringExtra(Constants.IntentParams.THREAD_ID);
        latitude = intent.getStringExtra(Constants.IntentParams.LAT);
        longitude = intent.getStringExtra(Constants.IntentParams.LNG);
        super.setContentView(R.layout.activity_publish_posting);

    }

    @Override
    protected void onResume() {
        super.onResume();

        TopicTagDataModel topicTagDataModel = KPlayCarApp.getValue(Constants.KeyParams.SYS_TAG_MODEL_VALUE);

        if (topicTagDataModel != null) {

            isSystemTag = true;
            mCreateTagPresenter.createTag(topicTagDataModel.id + "", topicTagDataModel.fromType, IOUtils.getToken(mContext));
            KPlayCarApp.removeValue(Constants.KeyParams.SYS_TAG_MODEL_VALUE);
        }

        int size = mTags.size();
        if (size > 0) {
            mTagRootLayout.setVisibility(View.VISIBLE);
        } else {
            mTagRootLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }

        boolean flag = IOUtils.deleteFile(mContext, Constants.KeyParams.RICH_E_DATA);
    }

    @Override
    protected void titleBarCallback(ImageButton back, final FunctionView function, TextView text) {

    }

    @Override
    protected void setUpViews() {

        initView();
    }

    /**
     * 添加标签，有去重
     *
     * @param topic
     */
    private void addTag(Topic topic) {

        for (Topic t : mTags) {
            if (t.id == topic.id) {
                return;
            }
        }
        mTags.add(topic);
    }

    private void settingTag(Topic topic) {

        int size = mTags.size();
        if (size >= TAG_MAX) {
            CommonUtils.showToast(mContext, "最多只能添加5个标签");
        } else {

            addTag(topic);
        }
        addTagItems(mTags);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data
                        .getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                for (ImageItem item : images) {
                    editor.addImage(item.path);
                }
            }

        }
        if (Constants.InteractionCode.REQUEST_CODE == requestCode && resultCode == RESULT_OK) {

            Topic topic = data.getParcelableExtra(Constants.IntentParams.DATA);
            settingTag(topic);
        }

        if (LOCATION_REQ_CODE == requestCode && resultCode == RESULT_OK) {

            String localAddress = "";
            mPoiItem = data.getParcelableExtra(Constants.IntentParams.DATA);
            if (mPoiItem != null) {
                latitude = mPoiItem.getLatLonPoint().getLatitude() + "";
                longitude = mPoiItem.getLatLonPoint().getLongitude() + "";
                String adName = mPoiItem.getAdName();
                String address = "";
                if (!TextUtils.isEmpty(adName)) {
                    localAddress = adName + "|";
                }
                localAddress += mPoiItem.getTitle();
                mLocationAddress = localAddress;
                mPositionName.setText(localAddress);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGallery:

                ImagePicker.getInstance().setMultiMode(true);
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, IMAGE_PICKER);

                break;
            case R.id.btnTag:

                Map<String, Serializable> args = new HashMap<String, Serializable>();
                if (!TextUtil.isEmpty(fromType)) {
                    if (!Constants.IntentParams.TOPIC_VALUE.equals(fromType)) {
                        args.put(Constants.IntentParams.FROM_TYPE, fromType);
                    }
                }

                KPlayCarApp.putValue(Constants.KeyParams.TAGS_DATA, mTags);
                CommonUtils.startResultNewActivity(this, args, TopicListActivity.class,
                        Constants.InteractionCode.REQUEST_CODE);
                break;

        }
    }

    private void initView() {
        mPresenter = new TopicDataPresenter();
        mPresenter.attachView(this);
        mCreateTagPresenter = new TopicDataPresenter();
        mCreateTagPresenter.attachView(mCreateTagView);
        mTopicDetailPreseneter = new TopicDataPresenter();
        mTopicDetailPreseneter.attachView(mTopicDetailView);
        mCancelBtn = (TextView) findViewById(R.id.cancel);
        mTitleBarText = (TextView) findViewById(R.id.title_bar_text);
        mTitleFunctionBtn = (TextView) findViewById(R.id.title_function_btn);
        mBtnGallery = (TextView) findViewById(R.id.btnGallery);
        mBtnTag = (TextView) findViewById(R.id.btnTag);
        mTagRootLayout = (RelativeLayout) findViewById(R.id.tagLayout);
        mTagLayout = (FlowLayout) findViewById(R.id.tagLists);
        mAddressNameTv = (TextView) findViewById(R.id.addressName);
        mAddressLayout = (LinearLayout) findViewById(R.id.addressLayout);
        mPositionName = (TextView) findViewById(R.id.name);
        mTagLayout.setVerticalSpacing(BitmapUtils.dp2px(mContext, 5));
        mTagLayout.setHorizontalSpacing(BitmapUtils.dp2px(mContext, 5));
        editor = (SortRichEditor) findViewById(R.id.richEditor);
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOperator()) {

                    showEditorDialog();
                } else {
                    finish();
                }
            }
        });

        mBtnGallery.setOnClickListener(this);
        mBtnTag.setOnClickListener(this);
        initTitleBarData();
        initAddressInfo();
        if (fromId != -1) {
            mCreateTagPresenter.createTag(fromId + "", fromType, IOUtils.getToken(mContext));
        }
        if (isEdit) {
            mTitleBarText.setText("编辑帖子");
            mTitleFunctionBtn.setText("重新发布");

            mTopicDetailPreseneter.getTopicDetails(threadId, longitude, latitude, IOUtils.getToken(mContext));
        }
        mTitleFunctionBtn.setOnClickListener(new OnSingleClickListener(1000) {
            @Override
            protected void onSingleClick(View view) {
                if (isEdit) {
                    mPresenter.editTopic(builderParams());
                } else {
                    mPresenter.createNewTopic(builderParams());
                }


            }
        });
    }

    private TopicDataView<Topic> mCreateTagView = new TopicDataView<Topic>() {
        @Override
        public void showData(Topic topic) {

            if (isSystemTag) {
                mTagRootLayout
                        .setVisibility(View.VISIBLE);
                settingTag(topic);
                isSystemTag = false;

            } else {
                mTags.clear();
                mTags.add(topic);
                mTagRootLayout
                        .setVisibility(View.VISIBLE);
                addTagItems(mTags);
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


    private TopicDataView<TopicDetailModel> mTopicDetailView = new TopicDataView<TopicDetailModel>() {
        @Override
        public void showData(TopicDetailModel model) {


            showTopicEditInfo(model);

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

    private void showTopicEditInfo(TopicDetailModel model) {

        //发帖标题和内容

        final List<SEditorData> datas = model.describe.mEditorDatas;
        String title = model.describe.title;
        if (TextUtil.isEmpty(title)) {
            title = "";
        }
        editor.getEtTitle().setText(title + "");
        editor.drawSaveData(datas);
        mLocationAddress = model.address;
        mPositionName.setText(mLocationAddress);


        //显示标签


        for (Topic t : model.topic) {
            settingTag(t);
        }

        int size = mTags.size();
        if (size > 0) {
            mTagRootLayout.setVisibility(View.VISIBLE);
        } else {
            mTagRootLayout.setVisibility(View.GONE);
        }
    }


    private static final int LOCATION_REQ_CODE = 1111;

    private void initAddressInfo() {

        if (!isEdit) {

            latitude = mGlobalContext.getLattitude();
            longitude = mGlobalContext.getLongitude();
            if (mEndAddress != null) {
                mLocationAddress = mEndAddress;

            } else {
                mLocationAddress = mGlobalContext.getAddressInfo() + "";
            }

            mPositionName.setText(mLocationAddress);
        }


        mPositionName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommonUtils.startResultNewActivity(PostingActivity.this, null,
                        MarkerPointActivity.class, LOCATION_REQ_CODE);

            }
        });
        ImageView imageView = (ImageView) findViewById(R.id.img);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAddressLayout.setVisibility(View.GONE);
                mAddressNameTv.setVisibility(View.VISIBLE);
            }
        });
        mAddressNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddressLayout.setVisibility(View.VISIBLE);
                mAddressNameTv.setVisibility(View.GONE);
            }
        });

    }

    private boolean isSystemTag = false;

    private void initTitleBarData() {
        final RichEditorEntity entity = IOUtils.getObject(mContext,
                Constants.KeyParams.RICH_E_DATA);

        if (!isEdit) {


            if (entity != null) {
                final List<SEditorData> datas = entity.mEditorDatas;
                String title = entity.title;
                if (TextUtil.isEmpty(title)) {
                    title = "";
                }
                editor.getEtTitle().setText(title + "");
                editor.drawSaveData(datas);
            } else {
                editor.addHeaderEditView();
            }
        }


    }

    private Map<String, String> builderParams() {

        Map<String, String> params = new HashMap<String, String>();

        String title = editor.getTitleData() + "";
        String content = "";
        String topicIds = "";
        RichEditorEntity entity = new RichEditorEntity();
        entity.mEditorDatas = editor.buildEditData();
        entity.title = title;
        Gson gson = new Gson();
        content = gson.toJson(entity);

        StringBuffer stringBuffer = new StringBuffer();
        for (Topic t : mTags) {
            stringBuffer.append(t.id).append(",");
        }
        topicIds = stringBuffer.toString();
        if (topicIds.length() > 0) {
            topicIds = topicIds.substring(0, topicIds.length() - 1);
        }

        Logger.d("发帖内容:" + content + ",latitude:" + latitude + ",longitude" + longitude);
        params.put("title", title);
        params.put("content", content);
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("local_address", mLocationAddress);
        params.put("topic", topicIds);
        params.put("token", IOUtils.getToken(mContext));
        if (isEdit) {
            params.put("id", threadId);
        }
        if (mRideId != -1) {
            params.put("ride_id", mRideId + "");

        }

        if (!TextUtils.isEmpty(parentId)) {
            params.put("parent_id", parentId);
        }

        return params;
    }

    /**
     * 添加标签
     *
     * @param datas
     */
    private void addTagItems(final List<Topic> datas) {
        mTagLayout.removeAllViews();
        mTagRootLayout.setVisibility(View.VISIBLE);
        int size = datas.size();
        for (int i = 0; i < size; i++) {
            Topic topic = datas.get(i);
            View v = LayoutInflater.from(mContext).inflate(R.layout.text_tag_item2, null,
                    false);
            v.setTag(topic.id);
            mTagLayout.addView(v);
            TextView tv = (TextView) v.findViewById(R.id.name);
            tv.setText(topic.title + "");
            tv.setTag(topic.id);
            ImageView imgView = (ImageView) v.findViewById(R.id.img);
            imgView.setTag(v);
            imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View parentView = (View) v.getTag();
                    mTagLayout.removeViewInLayout(parentView);
                    mTagLayout.invalidate();
                    removeTagById(Integer.parseInt(parentView.getTag().toString()));
                    if (datas.size() == 0) {
                       mTagRootLayout.setVisibility(View.GONE);
                    }
                }
            });


        }
    }

    /**
     * 根据id,移出已选择的话题Tag
     *
     * @param id
     */
    private void removeTagById(int id) {

        List<Topic> temps = new ArrayList<Topic>();
        for (Topic t : mTags) {
            temps.add(t);
        }

        for (Topic t : temps) {
            if (id == t.id) {
                mTags.remove(t);
                break;
            }
        }
    }

    @Override
    public void showData(List<String> strings) {

        finish();
        CommonUtils.showToast(mContext, "发布成功！");

        editor.clearData();
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


    private void showEditorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View v = LayoutInflater.from(mContext).inflate(R.layout.driving_dialog, null);
        builder.setView(v);
        final AlertDialog alertDialog = builder.create();
        ImageView img = (ImageView) v.findViewById(R.id.img);
        TextView title = (TextView) v.findViewById(R.id.title);
        title.setText("是否放弃发帖？");
        TextView mEnddiary = (TextView) v.findViewById(R.id.appltlist_item_enddiary); //我在想想
        TextView mendgroup = (TextView) v.findViewById(R.id.appltlist_item_endgroup); //确认取消
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        mEnddiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        mendgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        alertDialog.show();
        //改变对话框的宽度和高度
        alertDialog.getWindow().setLayout(BitmapUtils.dp2px(mContext, 300),
                BitmapUtils.dp2px(mContext, 300));
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {


            if (isOperator()) {

                showEditorDialog();

            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean isOperator() {
        String title = editor.getTitleData();
        if (!TextUtils.isEmpty(title)) {

            return true;
        }
        if (editor.buildEditData().size() == 1) {
            SEditorData data = editor.buildEditData().get(0);
            if (!TextUtils.isEmpty(data.getInputStr())) {

                return true;
            }
        }
        if (editor.buildEditData().size() > 1) {
            return true;
        }
        if (mTags.size() > 0) {

            return true;
        }
        return false;
    }
}
