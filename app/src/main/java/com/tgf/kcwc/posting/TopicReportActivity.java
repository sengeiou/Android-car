package com.tgf.kcwc.posting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.squareup.picasso.Picasso;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.presenter.FileUploadPresenter;
import com.tgf.kcwc.mvp.presenter.TopicOperatorPresenter;
import com.tgf.kcwc.mvp.view.FileUploadView;
import com.tgf.kcwc.mvp.view.TopicOperatorView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Author:Jenny
 * Date:2017/9/25
 * E-mail:fishloveqin@gmail.com
 * <p>
 * 帖子举报
 */

public class TopicReportActivity extends BaseActivity implements TopicOperatorView {
    protected TextView reportDescTv;
    protected GridView reasonGridView;
    protected EditText reasonDetailET;
    protected TextView bockNoteTextSizeTv;
    protected GridView addImgGridView;
    private String mId;
    private String mTitle;
    private ArrayList<DataItem> mReasons;
    private WrapAdapter<DataItem> mReasonAdapter;
    private ArrayList<DataItem> addImages;
    private WrapAdapter<DataItem> addImageAdapter;
    private FileUploadPresenter mFilePresenter;
    private TopicOperatorPresenter mOperatorPresenter;
    private TextView mImageCountTv;

    @Override
    protected void setUpViews() {
        initView();
        showReportContent();
        showReportReason();
        showReportImage();
        mFilePresenter = new FileUploadPresenter();
        mFilePresenter.attachView(mFileView);
        mOperatorPresenter = new TopicOperatorPresenter();
        mOperatorPresenter.attachView(this);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);

        text.setText("举报");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        mId = intent.getStringExtra(Constants.IntentParams.ID);
        mTitle = intent.getStringExtra(Constants.IntentParams.TITLE);
        super.setContentView(R.layout.activity_topic_report);


    }

    private void showReportImage() {

        addImages = new ArrayList<DataItem>();

        DataItem dataItem = new DataItem();
        dataItem.id = -1;
        dataItem.icon = R.drawable.add_image_btn;
        addImages.add(dataItem);

        addImageAdapter = new WrapAdapter<DataItem>(mContext, addImages, R.layout.add_image_grid) {
            @Override
            public void convert(final ViewHolder helper, DataItem item) {

                ImageView imageView = helper.getView(R.id.item_grid_image);

                ImageView ivCloseView = helper.getView(R.id.iv_item_header_close);
                int id = item.id;

                if (id == -1) {
                    Picasso.with(mContext).load(item.icon).into(imageView);
                    ivCloseView.setVisibility(View.GONE);
                } else {
                    Picasso.with(mContext).load(item.url).into(imageView);
                    ivCloseView.setVisibility(View.VISIBLE);
                }

                ivCloseView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        addImages.remove(helper.getPosition());
                        addImageAdapter.notifyDataSetChanged();
                        showImageCount();
                    }
                });

            }
        };

        addImgGridView.setAdapter(addImageAdapter);
        addImgGridView.setOnItemClickListener(onAddImageItemClickListener);
    }

    private AdapterView.OnItemClickListener onAddImageItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            DataItem item = (DataItem) parent.getAdapter().getItem(position);

            if (item.id == -1) {
                ImagePicker.getInstance().setMultiMode(true);
                Intent intent = new Intent(TopicReportActivity.this, ImageGridActivity.class);
                startActivityForResult(intent, IMAGE_PICKER);
            }
        }
    };
    private static final int IMAGE_PICKER = 1001;

    private void showReportReason() {

        mReasons = new ArrayList<DataItem>();
        String[] arrays = mRes.getStringArray(R.array.report_reasons);
        int index = 0;
        for (String str : arrays) {

            DataItem dataItem = new DataItem();
            dataItem.title = str;
            dataItem.id = index;
            index++;
            mReasons.add(dataItem);
        }
        mReasonAdapter = new WrapAdapter<DataItem>(mContext, mReasons, R.layout.listitem_check) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {
                helper.setText(R.id.titleTv, item.title);
                if (item.isSelected) {
                    helper.setImageResource(R.id.statusIv, R.drawable.refound_s);
                } else {
                    helper.setImageResource(R.id.statusIv, R.drawable.refound_n);
                }
            }
        };

        reasonGridView.setAdapter(mReasonAdapter);

        reasonGridView.setOnItemClickListener(mReasonItemClickListener);
    }

    private AdapterView.OnItemClickListener mReasonItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            DataItem dataItem = (DataItem) parent.getAdapter().getItem(position);
            dataItem.isSelected = true;
            singleChecked(mReasons, dataItem);
            mReasonAdapter.notifyDataSetChanged();
        }
    };


    private void showReportContent() {

        int index = mTitle.lastIndexOf(" ");

        CommonUtils.customDisplayText(reportDescTv, mTitle, mRes.getColor(R.color.text_color12), index, mTitle.length());
    }

    private void initView() {
        reportDescTv = (TextView) findViewById(R.id.reportDescTv);
        reasonGridView = (GridView) findViewById(R.id.reasonGridView);
        reasonDetailET = (EditText) findViewById(R.id.reasonDetailET);
        reasonDetailET.addTextChangedListener(mWatcher);
        bockNoteTextSizeTv = (TextView) findViewById(R.id.bockNoteTextSizeTv);
        addImgGridView = (GridView) findViewById(R.id.addImgGridView);
        mImageCountTv = (TextView) findViewById(R.id.imageCountTv);
        findViewById(R.id.reportBtn).setOnClickListener(this);
    }


    private TextWatcher mWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            bockNoteTextSizeTv.setText(s.length() + "/500");
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data
                        .getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                for (ImageItem item : images) {


                    // addImage(item.path);
                    uploadFile(item.path);

                }


            }

        }
    }

    private Map<String, DataItem> caches = new HashMap<String, DataItem>();

    private void addImage(String path, String url) {


        DataItem dataItem = new DataItem();

        dataItem.id = index;
        dataItem.path = "file://" + path;
        dataItem.oriUrl = url;
        dataItem.url = URLUtil.builderImgUrl(url, 360, 360);
        if(addImages.size()<4){
            addImages.add(0, dataItem);
            index++;
            caches.put(path, dataItem);
            addImageAdapter.notifyDataSetChanged();
            showImageCount();
        }


    }

    private synchronized void uploadFile(String path) {

        File file = new File(path);
        mFilePresenter.uploadFile(
                RequestBody.create(MediaType.parse("image/png"), file),
                RequestBody.create(MediaType.parse("text/plain"),
                        "thread"),
                RequestBody.create(MediaType.parse("text/plain"), ""),
                RequestBody.create(MediaType.parse("text/plain"),
                        IOUtils.getToken(mContext)), path);


    }

    private int index;


    private void showImageCount() {

        int size = addImages.size() - 1;
        mImageCountTv.setText(size + "/3");
    }

    private FileUploadView<DataItem> mFileView = new FileUploadView<DataItem>() {
        @Override
        public void resultData(DataItem dataItem) {

            addImage(dataItem.path, dataItem.url);
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

            case R.id.reportBtn:

                reportData();
                break;
        }

    }

    private void reportData() {

        Map<String, String> params = new HashMap<String, String>();

        params.put("token", IOUtils.getToken(mContext));
        params.put("vehicle_type", Constants.TopicParams.VEHICLE_TYPE);
        params.put("resource_id", mId);

        int type = 0;
        for (DataItem item : mReasons) {
            if (item.isSelected) {

                break;
            } else {
                type++;
            }
        }
        params.put("type", type + "");
        String content = reasonDetailET.getText().toString();
        params.put("content", content);

        StringBuffer sb = new StringBuffer();
        for (DataItem item : addImages) {

            if (item.id != -1) {
                sb.append(item.oriUrl).append(",");
            }
        }

        String imgs = sb.toString();
        if (imgs.length() > 0) {
            imgs = imgs.substring(0, imgs.length() - 1);
        }
        params.put("uploadImg", imgs);

        mOperatorPresenter.addReport(params);

    }

    @Override
    public void showData(Object o) {

        CommonUtils.showToast(mContext, "举报成功");
        finish();
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

        if (mOperatorPresenter != null) {
            mOperatorPresenter.detachView();
        }

        if (mFilePresenter != null) {
            mFilePresenter.detachView();
        }
    }
}
