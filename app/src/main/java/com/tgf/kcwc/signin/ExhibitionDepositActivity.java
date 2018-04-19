package com.tgf.kcwc.signin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.ImgAdapter;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.callback.OnSingleClickListener;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.ResponseMessage;
import com.tgf.kcwc.mvp.presenter.ExhibitionDepositPresenter;
import com.tgf.kcwc.mvp.presenter.FileUploadPresenter;
import com.tgf.kcwc.mvp.view.ExhibitionDepositView;
import com.tgf.kcwc.mvp.view.FileUploadView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.StringUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.DropUpPhonePopupWindow;
import com.tgf.kcwc.view.FunctionView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @anthor noti
 * @time 2017/9/14
 * @describle 展位投诉
 */
public class ExhibitionDepositActivity extends BaseActivity implements ExhibitionDepositView, FileUploadView<DataItem> {
    //图片
    private static final int IMAGE_PICKER_DEPOSIT = 12;
    private TextView exhibitionHallPos;
    private TextView exhibitionTime;
    private EditText depositDesc;
    //投诉占位图列表
    private GridView imgRv;
    private ArrayList<String> imgList = new ArrayList<>();
    private ImgAdapter imgAdapter;
//    private WrapAdapter<String> imgAdapter ;

    private Button depositBtn;

    ExhibitionDepositPresenter mPresenter;
    private DropUpPhonePopupWindow mDropUpPhonePopupWindow;
    private ProgressDialog mProgressDialog;
    private List<DataItem> mDataList = new ArrayList<>();
    private FileUploadPresenter mFileUploadPresenter;
    //五张图片
    private String complainImg1, complainImg2, complainImg3, complainImg4, complainImg5, complainContent;
    //申请ID
    private int applyId;
    public String timeStr;
    public String hallStr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibition_deposit);
    }

    @Override
    protected void setUpViews() {
        applyId = getIntent().getIntExtra(Constants.IntentParams.ID, -1);
        hallStr = getIntent().getStringExtra(Constants.IntentParams.TITLE);
        timeStr = getIntent().getStringExtra(Constants.IntentParams.NAME);

        gainTypeDataList();
        initProgressDialog();
        exhibitionHallPos = (TextView) findViewById(R.id.exhibitionHallPos);
        if (StringUtils.checkNull(hallStr)) {
            exhibitionHallPos.setText(hallStr);
        }
        exhibitionTime = (TextView) findViewById(R.id.exhibitionTime);
        if (StringUtils.checkNull(timeStr)) {
            exhibitionTime.setText(timeStr);
        }
        depositDesc = (EditText) findViewById(R.id.depositDesc);
        imgRv = (GridView) findViewById(R.id.imgRv);
        depositBtn = (Button) findViewById(R.id.depositBtn);
        initAdapter();
        depositBtn.setOnClickListener(new OnSingleClickListener() {
            @Override
            protected void onSingleClick(View view) {
                // TODO: 2017/9/14  立即投诉
                mPresenter = new ExhibitionDepositPresenter();
                mPresenter.attachView(ExhibitionDepositActivity.this);
                for (int i = 0; i < imgList.size(); i++) {
                    switch (i) {
                        case 0:
                            complainImg1 = imgList.get(i);
                            break;
                        case 1:
                            complainImg2 = imgList.get(i);
                            break;
                        case 2:
                            complainImg3 = imgList.get(i);
                            break;
                        case 3:
                            complainImg4 = imgList.get(i);
                            break;
                        case 4:
                            complainImg5 = imgList.get(i);
                            break;
                    }
                }
                complainContent = depositDesc.getText().toString();
                Log.e("TAG", "applyId: " + applyId + "complainContent: " + complainContent);
                Log.e("TAG", "complainImg1: " + complainImg1 + "complainImg2: " + complainImg2 + "complainImg3: " + complainImg3 + "complainImg4: " + complainImg4 + "complainImg5: " + complainImg5);
                mPresenter.exhibitionDeposit(IOUtils.getToken(getContext()), applyId, complainContent, complainImg1, complainImg2, complainImg3, complainImg4, complainImg5);
            }
        });
    }

    public void initAdapter() {
        imgList.add(R.drawable.ic_launcher+"");
        imgAdapter = new ImgAdapter(this,imgList);
        imgRv.setAdapter(imgAdapter);
//        imgAdapter = new WrapAdapter<String>(this, R.layout.img_item,imgList) {
//            @Override
//            public void convert(final ViewHolder helper, String item) {
//                ImageView itemX = helper.getView(R.id.itemX);
//                SimpleDraweeView imgSdv = helper.getView(R.id.imgSdv);
//                if (helper.getPosition() == 0) {
//                    //隐藏删除按钮
//                    itemX.setVisibility(View.GONE);
//                    imgSdv.setBackgroundResource(Integer.parseInt(item));
//                    //图片多选
//                    imgSdv.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                        }
//                    });
//                } else {
//                    imgSdv.setImageURI(Uri.parse(URLUtil.builderImgUrl(item)));
//                    //删除
//                    itemX.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                        }
//                    });
//                }
//            }
//        };
        imgRv.setAdapter(imgAdapter);

        imgAdapter.setOnDeleteClickListener(new ImgAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                imgList.remove(position);
                imgAdapter.notifyDataSetChanged();
            }
        });

        imgAdapter.setOnAddClickListener(new ImgAdapter.OnAddClickListener() {
            @Override
            public void onAddClick(int position) {
                if (imgList.size() > 5){
                    CommonUtils.showToast(getContext(),"最多可上传5张照片");
                    return;
                }
                mDataList.get(0).name = "选择图片";
                mDropUpPhonePopupWindow = new DropUpPhonePopupWindow(mContext, mDataList);
                mDropUpPhonePopupWindow.show(ExhibitionDepositActivity.this);
                mDropUpPhonePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        DataItem selectDataItem = mDropUpPhonePopupWindow.getSelectDataItem();
                        if (selectDataItem != null && mDropUpPhonePopupWindow.getIsSelec()) {
                            Intent intent = null;
                            switch (selectDataItem.id) {
                                case 2:
                                    ImagePicker.getInstance().setMultiMode(false);
                                    intent = new Intent(getContext(), ImageGridActivity.class);
                                    startActivityForResult(intent, IMAGE_PICKER_DEPOSIT);
                                    break;
                                case 3:
                                    ImagePicker.getInstance().setMultiMode(false);
                                    intent = new Intent(getContext(), ImageGridActivity.class);
                                    intent.putExtra(Constants.IntentParams.ID, true);
                                    startActivityForResult(intent, IMAGE_PICKER_DEPOSIT);
                                    break;
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER_DEPOSIT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data
                        .getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                for (ImageItem item : images) {
                    uploadImage(item);
                }
            }
        }
    }

    private void uploadImage(ImageItem item) {
        File f = new File(item.path);
        //上传图片
        mFileUploadPresenter = new FileUploadPresenter();
        mFileUploadPresenter.attachView(ExhibitionDepositActivity.this);
        mFileUploadPresenter.uploadFile(RequestBody.create(MediaType.parse("image/png"), f),
                RequestBody.create(MediaType.parse("text/plain"), "event"),
                RequestBody.create(MediaType.parse("text/plain"), "2"),
                RequestBody.create(MediaType.parse("text/plain"), IOUtils.getToken(mContext)), item);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("展位投诉");
//        function.setImageResource(R.drawable.cover_default);
//        function.setOnClickListener(new OnSingleClickListener() {
//            @Override
//            protected void onSingleClick(View view) {
//                // TODO: 2017/9/14
//
//            }
//        });
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("正在上传，请稍候...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    public void gainTypeDataList() {
        mDataList.clear();
        DataItem dataItem = null;
        dataItem = new DataItem();
        dataItem.id = 1;
        dataItem.name = "更换背景";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 2;
        dataItem.name = "本地上传";
        mDataList.add(dataItem);
        dataItem = new DataItem();
        dataItem.id = 3;
        dataItem.name = "拍照上传";
        mDataList.add(dataItem);
    }

    @Override
    public void depositSuccess(ResponseMessage message) {
        CommonUtils.startNewActivity(this, ExhibitionDepositSuccessActivity.class);
        finish();
    }

    @Override
    public void depositFail(ResponseMessage message) {
        CommonUtils.showToast(this, message.statusMessage);
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

    @Override
    public void resultData(DataItem dataItem) {
        String inPath = dataItem.resp.data.path;
        imgList.add(inPath);
        imgAdapter.notifyDataSetChanged();
    }
}
