package com.tgf.kcwc.play.topic;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.FoundTypeBean;
import com.tgf.kcwc.mvp.presenter.FileUploadPresenter;
import com.tgf.kcwc.mvp.presenter.FoundTopicPresenter;
import com.tgf.kcwc.mvp.view.FileUploadView;
import com.tgf.kcwc.mvp.view.FoundTopicView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.StringUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;

/**
 * Created by Administrator on 2017/5/16.
 */

public class FoundTopicActivity extends BaseActivity implements FoundTopicView {

    private FileUploadPresenter mFileUploadPresenter;                           //上传图片
    private static final int IMAGE_PICKER = 1001;                   //图片
    private static final int TYPE = 1002;                   //类型
    private FoundTopicPresenter mFoundTopicPresenter;
    private LinearLayout hintpicture; //点击选照片
    private LinearLayout type; //点击选类别
    private ImageView picture; //点击选照片
    private SimpleDraweeView cover;//封面
    private EditText mlabeltext;//标签
    private EditText mEdtext;//简介
    private TextView mConfirm;//创建
    private TextView mTypeName;

    private String Path;//封面
    private String categoryId;//分类id
    private String categoryType;//分类categoryType
    private String intro;//导语
    private  String title;//标题

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFileUploadPresenter!=null){
            mFileUploadPresenter.detachView();
        }
        if (mFoundTopicPresenter!=null){
            mFoundTopicPresenter.detachView();
        }
    }

    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText(R.string.topicfound);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foundtopic);
        mFoundTopicPresenter = new FoundTopicPresenter();
        mFoundTopicPresenter.attachView(this);
        hintpicture = (LinearLayout) findViewById(R.id.hintpicture);
        type = (LinearLayout) findViewById(R.id.type);
        picture = (ImageView) findViewById(R.id.picture);
        cover = (SimpleDraweeView) findViewById(R.id.cover);
        mlabeltext = (EditText) findViewById(R.id.labeltext);
        mEdtext = (EditText) findViewById(R.id.edtext);
        mConfirm = (TextView) findViewById(R.id.confirm);
        mTypeName = (TextView) findViewById(R.id.typename);

        initProgressDialog();
        mFileUploadPresenter = new FileUploadPresenter();
        mFileUploadPresenter.attachView(uploadView);

        picture.setOnClickListener(this);
        hintpicture.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        type.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.hintpicture:
            case R.id.picture:
                ImagePicker.getInstance().setMultiMode(true);
                ImagePicker.getInstance().setSelectLimit(1);
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, IMAGE_PICKER);
                break;
            case R.id.type:
                CommonUtils.startResultNewActivity(this, null, FoundTopicTypeActivity.class, TYPE);
                break;
            case R.id.confirm:
                confirm();
                break;

        }
    }

    public void confirm() {

        if (StringUtils.checkNull(Path)) {

        } else {
            CommonUtils.showToast(mContext, "请选择封面图");
            return;
        }

        if (StringUtils.checkNull(categoryId)) {

        } else {
            CommonUtils.showToast(mContext, "请选择分类ID");
            return;
        }

        if (StringUtils.checkNull(mlabeltext.getText().toString().trim())) {
            intro = mlabeltext.getText().toString().trim();
        } else {
            CommonUtils.showToast(mContext, "请填写话题标签");
            return;
        }
        if (StringUtils.checkNull(mEdtext.getText().toString().trim())) {
            title = mEdtext.getText().toString().trim();
        } else {
            CommonUtils.showToast(mContext, "请填写话题简介");
            return;
        }
        mFoundTopicPresenter.GetCreate(IOUtils.getToken(mContext), categoryId, Path, title, intro,categoryType);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data
                        .getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                for (ImageItem item : images) {
                    /* mHintpicture.setVisibility(View.GONE);
                    mDrivdetailsCover.setVisibility(View.VISIBLE);
                    Bitmap bm = BitmapFactory.decodeFile(item.path);
                    mDrivdetailsCover.setImageBitmap(bm);*/
                    uploadImage(item);
                }
            }
        }
        if (resultCode == RESULT_OK) {
            if (data != null && requestCode == TYPE) {
                DataItem selectItem = (DataItem) data.getSerializableExtra(Constants.IntentParams.DATA);
                categoryId = selectItem.id + "";
                categoryType = selectItem.title;
                mTypeName.setText(selectItem.name);
            }
        }
    }

    private void uploadImage(ImageItem item) {
        File f = new File(item.path);
        compressWithRx(f,item);
    }

    /**
     * 图片压缩，RX链式处理
     *
     * @param file
     */
    private void compressWithRx(File file, final ImageItem item) {
        Observable.just(file).observeOn(Schedulers.io()).map(new Func1<File, File>() {
            @Override
            public File call(File file) {
                try {
                    return Luban.with(mContext).load(file).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<File>() {
            @Override
            public void call(File file) {

                if (file != null) {
                    Logger.d("图片压缩文件路径:" + file.getAbsolutePath());
                    mFileUploadPresenter.uploadFile(RequestBody.create(MediaType.parse("image/png"), file),
                            RequestBody.create(MediaType.parse("text/plain"), "thread"),
                            RequestBody.create(MediaType.parse("text/plain"), ""),
                            RequestBody.create(MediaType.parse("text/plain"), IOUtils.getToken(getContext())),
                            item);
                }

            }
        });
    }

    private ProgressDialog mProgressDialog = null;

    private void initProgressDialog() {

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("正在上传，请稍候...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

    }


    private FileUploadView<DataItem> uploadView = new FileUploadView<DataItem>() {
        @Override
        public void resultData(DataItem dataItem) {
            if (dataItem.code == 0) {
                hintpicture.setVisibility(View.GONE);
                cover.setVisibility(View.VISIBLE);
                picture.setVisibility(View.VISIBLE);
                Path = dataItem.resp.data.path;
                cover
                        .setImageURI(Uri.parse(URLUtil.builderImgUrl(dataItem.resp.data.path, 540, 270)));
                CommonUtils.showToast(mContext, dataItem.msg + "");
            } else {
                CommonUtils.showToast(mContext, "系统异常");
            }
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

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void dataSucceed(BaseBean baseArryBean) {
        CommonUtils.showToast(mContext, "创建成功");
        finish();
    }

    @Override
    public void typeListSucceed(FoundTypeBean topicBean) {

    }

    @Override
    public void typerListSucceed(FoundTypeBean topicBean) {

    }

    @Override
    public void dataListDefeated(String msg) {
        CommonUtils.showToast(mContext, msg);
    }

    @Override
    public Context getContext() {
        return mContext;
    }
}
