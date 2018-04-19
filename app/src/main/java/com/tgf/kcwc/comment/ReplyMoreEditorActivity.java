package com.tgf.kcwc.comment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.ImagePickerAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.imageloader.PicassoImageLoader;
import com.tgf.kcwc.mvp.presenter.CommentListPresenter;
import com.tgf.kcwc.mvp.presenter.FileUploadPresenter;
import com.tgf.kcwc.mvp.view.CommentListView;
import com.tgf.kcwc.mvp.view.FileUploadView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.view.FunctionView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.tgf.kcwc.R.id.recyclerView;

/**
 * Author：Jenny
 * Date:2017/3/9 15:42
 * E-mail：fishloveqin@gmail.com
 */

public class ReplyMoreEditorActivity extends BaseActivity
        implements ImagePickerAdapter.OnRecyclerViewItemClickListener,
        CommentListView<List<String>> {

    public static final int IMAGE_ITEM_ADD = -1;
    protected TextView mCancel;
    protected EditText mTitle;
    protected RecyclerView mGrid;
    protected ImageView mPictureIv;
    protected ImageView mEmotionIv;
    private TextView mCmtBtn;                        //发送提交评论

    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;

    private ImagePickerAdapter adapter;
    private List<ImageItem> selImageList;                   //当前选择的所有图片
    private int maxImgCount = 8;       //允许选择图片最大数

    private CommentListPresenter mPresenter;
    private FileUploadPresenter mFileUploadPresenter;
    private String mId = "";
    private String mModule = "thread";

    private String mId2 = "";
    private String mReceiverUId = "";
    private String mType = "";

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    protected void setUpViews() {
        initView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent fromIntent = getIntent();
        String module = fromIntent.getStringExtra(Constants.IntentParams.INTENT_TYPE);
        if (!TextUtils.isEmpty(module)) {
            mModule = module;
        }
        Intent intent = getIntent();
        mId = intent.getStringExtra(Constants.IntentParams.ID);
        mId2 = intent.getStringExtra(Constants.IntentParams.ID2);
        mReceiverUId = intent.getStringExtra(Constants.IntentParams.ID3);
        mType = intent.getStringExtra(Constants.IntentParams.ID4);

        isTitleBar = false;
        //initImagePicker();
        setContentView(R.layout.activity_comment_edit_layout);


    }

    @Override
    protected void onResume() {
        super.onResume();
        mCmtBtn.setEnabled(true);
    }

    private void initView() {
        mCancel = (TextView) findViewById(R.id.cancel);
        backEvent(mCancel);
        mTitle = (EditText) findViewById(R.id.titleText);
        mGrid = (RecyclerView) findViewById(recyclerView);
        mPictureIv = (ImageView) findViewById(R.id.picture_iv);
        mEmotionIv = (ImageView) findViewById(R.id.emotion_iv);
        mCmtBtn = (TextView) findViewById(R.id.title_function_btn);
        mCmtBtn.setOnClickListener(this);
        mPictureIv.setOnClickListener(this);
        mEmotionIv.setOnClickListener(this);
        if (!TextUtils.isEmpty(mId2)) {
            mPictureIv.setVisibility(View.GONE);
        }
        initWidget();
        mPresenter = new CommentListPresenter();
        mPresenter.attachView(this);
        mFileUploadPresenter = new FileUploadPresenter();
        mFileUploadPresenter.attachView(uploadView);
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new PicassoImageLoader()); //设置图片加载器
        imagePicker.setShowCamera(true); //显示拍照按钮
        imagePicker.setCrop(false); //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount); //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE); //裁剪框的形状
        imagePicker.setFocusWidth(800); //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800); //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000); //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000); //保存文件的高度。单位像素
    }

    private void initWidget() {
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);

        mGrid.setLayoutManager(new GridLayoutManager(this, 3));
        mGrid.setHasFixedSize(true);
        mGrid.setAdapter(adapter);
        initProgressDialog();
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {
            case R.id.picture_iv:
                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT);
                break;
            case R.id.emotion_iv:
                break;
            case R.id.title_function_btn:
                mCmtBtn.setEnabled(false);
                mPresenter.publishCmt(buildParams());
                break;
        }
    }

    /* pid
    imgs
    receiver_uid
    text
    mModule
    vehicle_type
    resource_id */
    private Map buildParams() {

        Map<String, String> params = new HashMap<String, String>();
        String pid = "0";
        String receiverUId = "0";
        String type = "car";

        if (!TextUtils.isEmpty(mId2)) {
            pid = mId2;
        }
        if (!TextUtils.isEmpty(mReceiverUId)) {
            receiverUId = mReceiverUId;
        }
        if (!TextUtils.isEmpty(mType)) {
            type = mType;
        }


        String content = mTitle.getText().toString();
        params.put("pid", pid);//id2
        params.put("receiver_uid", receiverUId);//id3
        params.put("text", content);
        params.put("module", mModule);
        params.put("vehicle_type", type);
        params.put("reply_id", mId);//ID
        params.put("token", IOUtils.getToken(mContext));
        StringBuffer imgs = new StringBuffer();

        for (ImageItem item : selImageList) {
            imgs.append(item.url).append(",");
        }
        String urls = imgs.toString();
        int length = urls.length();
        if (length > 0) {
            urls = urls.substring(0, length - 1);
        }
        params.put("imgs", urls);
        return params;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data
                        .getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);

                //上传
                for (ImageItem item : images) {
                    uploadImage(item);
                }
                selImageList.addAll(images);
                adapter.setImages(selImageList);
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data
                        .getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                selImageList.clear();
                selImageList.addAll(images);
                adapter.setImages(selImageList);
            }
        }
    }

    private void uploadImage(ImageItem item) {
        File f = new File(item.path);
        mFileUploadPresenter.uploadFile(RequestBody.create(MediaType.parse("image/png"), f),
                RequestBody.create(MediaType.parse("text/plain"), "thread"),
                RequestBody.create(MediaType.parse("text/plain"), ""),
                RequestBody.create(MediaType.parse("text/plain"), IOUtils.getToken(getContext())),
                item);
    }

    @Override
    public void onItemClick(View view, int position) {

        Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
        intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS,
                (ArrayList<ImageItem>) adapter.getImages());
        intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
        startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);

        //        switch (position) {
        //            case IMAGE_ITEM_ADD:
        //                //打开选择,本次允许选择的数量
        //                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
        //                Intent intent = new Intent(this, ImageGridActivity.class);
        //                startActivityForResult(intent, REQUEST_CODE_SELECT);
        //                break;
        //            default:
        //                //打开预览
        //                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
        //                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
        //                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
        //                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
        //                break;
        //        }
    }

    @Override
    public void showDatas(List<String> strings) {
        mCmtBtn.setEnabled(true);
        CommonUtils.showToast(mContext, "发布成功!");
        Intent intentChange = new Intent();
        intentChange.putExtra(Constants.IntentParams.DATA, true);
        setResult(RESULT_OK, intentChange);
        finish();
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {
        mCmtBtn.setEnabled(true);
        // dismissLoadingDialog();
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    private ProgressDialog mProgressDialog = null;

    private void initProgressDialog() {

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage("正在上传，请稍候...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

    }

    private FileUploadView<DataItem> uploadView = new FileUploadView<DataItem>() {
        @Override
        public void resultData(DataItem dataItem) {

            CommonUtils.showToast(mContext, dataItem.msg + "");
        }

        @Override
        public void setLoadingIndicator(boolean active) {

            if (active) {
                mProgressDialog.show();
            } else {
                mProgressDialog.dismiss();
            }
        }

        @Override
        public void showLoadingTasksError() {

            mProgressDialog.dismiss();
        }

        @Override
        public Context getContext() {
            return mContext;
        }
    };
}
