package com.tgf.kcwc.see.sale.release;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.tgf.kcwc.R;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.mvp.model.ExhibitionApplyModel;
import com.tgf.kcwc.mvp.model.ExhibitionIntervalModel;
import com.tgf.kcwc.mvp.model.ExhibitionIntervalPresenter;
import com.tgf.kcwc.mvp.model.ExhibitionIntervalView;
import com.tgf.kcwc.mvp.model.GetApplyModel;
import com.tgf.kcwc.mvp.model.GetRemainModel;
import com.tgf.kcwc.mvp.model.MorePopupwindowBean;
import com.tgf.kcwc.mvp.model.MyExhibitionInfoModel;
import com.tgf.kcwc.mvp.model.SelectExhibitionModel;
import com.tgf.kcwc.mvp.presenter.ExhibitionApplyPresenter;
import com.tgf.kcwc.mvp.presenter.FileUploadPresenter;
import com.tgf.kcwc.mvp.presenter.GetApplyPresenter;
import com.tgf.kcwc.mvp.presenter.GetRemainPresenter;
import com.tgf.kcwc.mvp.presenter.MyExhibitionInfoPresenter;
import com.tgf.kcwc.mvp.view.ExhibitionApplyView;
import com.tgf.kcwc.mvp.view.FileUploadView;
import com.tgf.kcwc.mvp.view.GetApplyView;
import com.tgf.kcwc.mvp.view.GetRemainView;
import com.tgf.kcwc.mvp.view.MyExhibitionInfoView;
import com.tgf.kcwc.util.CarNumUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SharedPreferenceUtil;
import com.tgf.kcwc.util.StringUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.DropUpPhonePopupWindow;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.NewPopupWindow;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @anthor noti
 * @time 2017/9/13
 * @describle 二手车主参展申请
 */
public class ExhibitionApplyActivity extends BaseActivity implements FileUploadView<DataItem>, ExhibitionApplyView, GetApplyView, GetRemainView, ExhibitionIntervalView {
    //相册、拍照
    private static final int IMAGE_PICKER_OUT = 1001;//外观图片
    private static final int IMAGE_PICKER_IN = 1002;//内饰图片
    private static final int IMAGE_PICKER_DRIVING_LICENSE = 1003;//行驶证图片
    private static final int IMAGE_PICKER_ID_CARD_TRUE = 1004;//身份证正面图片
    private static final int IMAGE_PICKER_ID_CARD_FALSE = 1005;//身份证反面图片
    private static final int REQUEST_EXHIBITION_SELECT = 1006;//展会选择
    //状态 提交审核材料+展位申请+等待审核
    ImageView commitIv, applyIv, waitIv;
    //展会 展会图+展会名
    RelativeLayout exhibitionRl;
    SimpleDraweeView imgSdv;
    TextView exhibitionNameTv;
    //姓名
    RelativeLayout applyNameRl;
    EditText nicknameEt;
    View applyNameView;

    LinearLayout idCardLl;
    //身份证正面
    RelativeLayout idCardTrueRl;
    ImageView idCardTrueHint;
    SimpleDraweeView idCardTrueIv;
    //身份证反面
    RelativeLayout idCardFalseRl;
    ImageView idCardFalseHint;
    SimpleDraweeView idCardFalseIv;

    LinearLayout carLicenseLl;
    //车牌
    RelativeLayout carNumRl;
    LinearLayout carNameLl;
    TextView filterTitle;
    EditText licenseEt;
    View carNumView;
    //行驶证
    LinearLayout drivingLicenseLl;
    RelativeLayout drivingLicenseRl;
    TextView drivingLicenseHint;
    SimpleDraweeView drivingLicenseIv;

    LinearLayout carLl;
    //外观
    RelativeLayout outRl;
    TextView outHint;
    SimpleDraweeView outIv;
    //内饰
    RelativeLayout inRl;
    TextView inHint;
    SimpleDraweeView inIv;
    //参展申请须知
    TextView applyHintTv;
    //下一步
    Button nextStep;

    //popwindow车牌数据
    List<MorePopupwindowBean> popList = new ArrayList<>();
    private List<DataItem> mDataList = new ArrayList<>();
    //上传图片dialog
    private ProgressDialog mProgressDialog = null;
    private DropUpPhonePopupWindow mDropUpPhonePopupWindow;
    private FileUploadPresenter mFileUploadPresenter;
    //身份证正0反1，行驶证2，外观3，内饰4
    private int index;
    //路径
    private String idCardTruePath, idCardFalsePath, drivingLicensePath, outPath, inPath;
    //展会ID
    private int eventId;
    //展会名
    private String exhibitionName;
    //展会图
    private String exhibitionCover;
    //申请ID
    private int applyId = -1;
    private int type = -1;
    //状态
    private int status;
    //车牌号
    private String carNumStr;
    private int parkTimeId = -1;
    //上个界面返回的数据
    private String inputStr;
    private int threadId;
    //展位id
    private int parkId;
    private String nickStr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibition_apply);
    }

    @Override
    protected void setUpViews() {
        type = getIntent().getIntExtra(Constants.IntentParams.ID, -1);
//        eventId = getIntent().getIntExtra(Constants.IntentParams.ID2, -1);
        applyId = getIntent().getIntExtra(Constants.IntentParams.ID3, -1);
//        inputStr = getIntent().getStringExtra(Constants.IntentParams.DATA);
//        exhibitionName = getIntent().getStringExtra(Constants.IntentParams.NAME);
//        exhibitionCover = getIntent().getStringExtra(Constants.IntentParams.INDEX);

        gainTypeDataList();
        initProgressDialog();
        commitIv = (ImageView) findViewById(R.id.commit_iv);
        applyIv = (ImageView) findViewById(R.id.apply_iv);
        waitIv = (ImageView) findViewById(R.id.wait_iv);

        exhibitionRl = (RelativeLayout) findViewById(R.id.exhibition_rl);
        imgSdv = (SimpleDraweeView) findViewById(R.id.img_sdv);
        exhibitionNameTv = (TextView) findViewById(R.id.exhibition_name_tv);

        applyNameRl = findViewById(R.id.applyNameRl);
        nicknameEt = (EditText) findViewById(R.id.nickname_et);
        applyNameView = findViewById(R.id.applyNameView);

        idCardLl = findViewById(R.id.idCardLl);
        idCardTrueRl = (RelativeLayout) findViewById(R.id.id_card_true_rl);
        idCardTrueHint = (ImageView) findViewById(R.id.id_card_true_hint);
        idCardTrueIv = (SimpleDraweeView) findViewById(R.id.id_card_true_iv);

        idCardFalseRl = (RelativeLayout) findViewById(R.id.id_card_false_rl);
        idCardFalseHint = (ImageView) findViewById(R.id.id_card_false_hint);
        idCardFalseIv = (SimpleDraweeView) findViewById(R.id.id_card_false_iv);

        carLicenseLl = findViewById(R.id.carLicenseLl);
        carNumRl = findViewById(R.id.carNumRl);
        carNameLl = (LinearLayout) findViewById(R.id.car_name);
        filterTitle = (TextView) findViewById(R.id.filter_title);
        licenseEt = (EditText) findViewById(R.id.license_et);
        carNumView = findViewById(R.id.carNumView);

        drivingLicenseLl = findViewById(R.id.drivingLicenseLl);
        drivingLicenseRl = (RelativeLayout) findViewById(R.id.driving_license_rl);
        drivingLicenseHint = (TextView) findViewById(R.id.driving_license_hint);
        drivingLicenseIv = (SimpleDraweeView) findViewById(R.id.driving_license_iv);

        carLl = findViewById(R.id.carLl);
        outRl = (RelativeLayout) findViewById(R.id.out_rl);
        outHint = (TextView) findViewById(R.id.out_hint);
        outIv = (SimpleDraweeView) findViewById(R.id.out_iv);

        inRl = (RelativeLayout) findViewById(R.id.in_rl);
        inHint = (TextView) findViewById(R.id.in_hint);
        inIv = (SimpleDraweeView) findViewById(R.id.in_iv);

        applyHintTv = (TextView) findViewById(R.id.apply_hint_tv);

        nextStep = (Button) findViewById(R.id.next_step);

        exhibitionRl.setOnClickListener(this);
        idCardTrueRl.setOnClickListener(this);
        idCardFalseRl.setOnClickListener(this);
        carNameLl.setOnClickListener(this);
        drivingLicenseRl.setOnClickListener(this);
        outRl.setOnClickListener(this);
        inRl.setOnClickListener(this);
        applyHintTv.setOnClickListener(this);
        nextStep.setOnClickListener(this);

        //初始化车牌数据
        initPop();

        //获取我的展会信息
//        MyExhibitionInfoPresenter infoPresenter = new MyExhibitionInfoPresenter();
//        infoPresenter.attachView(this);
//        infoPresenter.getInfo(IOUtils.getToken(getContext()), applyId);

        GetApplyPresenter getApplyPresenter = new GetApplyPresenter();
        getApplyPresenter.attachView(this);
        getApplyPresenter.getApply(IOUtils.getToken(getContext()), applyId);
    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("二手车主参展申请");
        function.setImageResource(R.drawable.cover_default);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/9/13 参展申请须知
                Intent intent = new Intent(getContext(), ApplyHintActivity.class);
                intent.putExtra(Constants.IntentParams.ID, eventId);
                intent.putExtra(Constants.IntentParams.INDEX, 1);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            //展会
            case R.id.exhibition_rl:
                // TODO: 2017/9/13 展会列表
                Map<String, Serializable> map = new HashMap<>();
                CommonUtils.startResultNewActivity(this, map, SelectExhibitionActivity.class, REQUEST_EXHIBITION_SELECT);
                break;
            //身份证证明
            case R.id.id_card_true_rl:
                mDataList.get(0).name = "选择身份证正面";
                mDropUpPhonePopupWindow = new DropUpPhonePopupWindow(mContext, mDataList);
                mDropUpPhonePopupWindow.show(this);
                mDropUpPhonePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        DataItem selectDataItem = mDropUpPhonePopupWindow.getSelectDataItem();
                        if (selectDataItem != null && mDropUpPhonePopupWindow.getIsSelec()) {
                            Intent intent = null;
                            switch (selectDataItem.id) {
                                case 2:
                                    ImagePicker.getInstance().setMultiMode(false);
                                    intent = new Intent(ExhibitionApplyActivity.this, ImageGridActivity.class);
                                    startActivityForResult(intent, IMAGE_PICKER_ID_CARD_TRUE);
                                    break;
                                case 3:
                                    ImagePicker.getInstance().setMultiMode(false);
                                    intent = new Intent(ExhibitionApplyActivity.this, ImageGridActivity.class);
                                    intent.putExtra(Constants.IntentParams.ID, true);
                                    startActivityForResult(intent, IMAGE_PICKER_ID_CARD_TRUE);
                                    break;
                            }
                        }
                    }
                });
                break;
            //身份证反面
            case R.id.id_card_false_rl:
                mDataList.get(0).name = "选择身份证反面";
                mDropUpPhonePopupWindow = new DropUpPhonePopupWindow(mContext, mDataList);
                mDropUpPhonePopupWindow.show(this);
                mDropUpPhonePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        DataItem selectDataItem = mDropUpPhonePopupWindow.getSelectDataItem();
                        if (selectDataItem != null && mDropUpPhonePopupWindow.getIsSelec()) {
                            Intent intent = null;
                            switch (selectDataItem.id) {
                                case 2:
                                    ImagePicker.getInstance().setMultiMode(false);
                                    intent = new Intent(ExhibitionApplyActivity.this, ImageGridActivity.class);
                                    startActivityForResult(intent, IMAGE_PICKER_ID_CARD_FALSE);
                                    break;
                                case 3:
                                    ImagePicker.getInstance().setMultiMode(false);
                                    intent = new Intent(ExhibitionApplyActivity.this, ImageGridActivity.class);
                                    intent.putExtra(Constants.IntentParams.ID, true);
                                    startActivityForResult(intent, IMAGE_PICKER_ID_CARD_FALSE);
                                    break;
                            }
                        }
                    }
                });
                break;
            //车牌头部选择
            case R.id.car_name:
                NewPopupWindow morePopupWindow = new NewPopupWindow(this, popList, new NewPopupWindow.MoreOnClickListener() {
                    @Override
                    public void moreOnClickListener(int position, MorePopupwindowBean bean) {
                        //设置车牌
                        filterTitle.setText(popList.get(position).title);
                    }
                });
                morePopupWindow.showPopupWindow(carNameLl);
                break;
            //行驶证图片选择
            case R.id.driving_license_rl:
                mDataList.get(0).name = "选择行驶证";
                mDropUpPhonePopupWindow = new DropUpPhonePopupWindow(mContext, mDataList);
                mDropUpPhonePopupWindow.show(this);
                mDropUpPhonePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        DataItem selectDataItem = mDropUpPhonePopupWindow.getSelectDataItem();
                        if (selectDataItem != null && mDropUpPhonePopupWindow.getIsSelec()) {
                            Intent intent = null;
                            switch (selectDataItem.id) {
                                case 2:
                                    ImagePicker.getInstance().setMultiMode(false);
                                    intent = new Intent(ExhibitionApplyActivity.this, ImageGridActivity.class);
                                    startActivityForResult(intent, IMAGE_PICKER_DRIVING_LICENSE);
                                    break;
                                case 3:
                                    ImagePicker.getInstance().setMultiMode(false);
                                    intent = new Intent(ExhibitionApplyActivity.this, ImageGridActivity.class);
                                    intent.putExtra(Constants.IntentParams.ID, true);
                                    startActivityForResult(intent, IMAGE_PICKER_DRIVING_LICENSE);
                                    break;
                            }
                        }
                    }
                });
                break;
            //外观
            case R.id.out_rl:
                mDataList.get(0).name = "选择外观";
                mDropUpPhonePopupWindow = new DropUpPhonePopupWindow(mContext, mDataList);
                mDropUpPhonePopupWindow.show(this);
                mDropUpPhonePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        DataItem selectDataItem = mDropUpPhonePopupWindow.getSelectDataItem();
                        if (selectDataItem != null && mDropUpPhonePopupWindow.getIsSelec()) {
                            Intent intent = null;
                            switch (selectDataItem.id) {
                                case 2:
                                    ImagePicker.getInstance().setMultiMode(false);
                                    intent = new Intent(ExhibitionApplyActivity.this, ImageGridActivity.class);
                                    startActivityForResult(intent, IMAGE_PICKER_OUT);
                                    break;
                                case 3:
                                    ImagePicker.getInstance().setMultiMode(false);
                                    intent = new Intent(ExhibitionApplyActivity.this, ImageGridActivity.class);
                                    intent.putExtra(Constants.IntentParams.ID, true);
                                    startActivityForResult(intent, IMAGE_PICKER_OUT);
                                    break;
                            }
                        }
                    }
                });
                break;
            //内饰
            case R.id.in_rl:
                mDataList.get(0).name = "选择内饰";
                mDropUpPhonePopupWindow = new DropUpPhonePopupWindow(mContext, mDataList);
                mDropUpPhonePopupWindow.show(this);
                mDropUpPhonePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        DataItem selectDataItem = mDropUpPhonePopupWindow.getSelectDataItem();
                        if (selectDataItem != null && mDropUpPhonePopupWindow.getIsSelec()) {
                            Intent intent = null;
                            switch (selectDataItem.id) {
                                case 2:
                                    ImagePicker.getInstance().setMultiMode(false);
                                    intent = new Intent(ExhibitionApplyActivity.this, ImageGridActivity.class);
                                    startActivityForResult(intent, IMAGE_PICKER_IN);
                                    break;
                                case 3:
                                    ImagePicker.getInstance().setMultiMode(false);
                                    intent = new Intent(ExhibitionApplyActivity.this, ImageGridActivity.class);
                                    intent.putExtra(Constants.IntentParams.ID, true);
                                    startActivityForResult(intent, IMAGE_PICKER_IN);
                                    break;
                            }
                        }
                    }
                });
                break;
            //参展申请须知
            case R.id.apply_hint_tv:
                // TODO: 2017/9/13 参展申请须知
                Intent intent = new Intent(getContext(), ApplyHintActivity.class);
                intent.putExtra(Constants.IntentParams.ID, eventId);
                intent.putExtra(Constants.IntentParams.INDEX, 1);
                startActivity(intent);
                break;
            //下一步
            case R.id.next_step:
                if (type == 1){
                    GetRemainPresenter getRemainPresenter = new GetRemainPresenter();
                    getRemainPresenter.attachView(this);
                    getRemainPresenter.getRemain(IOUtils.getToken(getContext()), eventId);
                }else if (type == 2){
                    carNumStr = filterTitle.getText().toString() + licenseEt.getText().toString();
                    nickStr = nicknameEt.getText().toString();
                    //申请者姓名
                    if (inputStr.contains("apply_name")) {
                        if (!StringUtils.checkNull(nickStr)) {
                            CommonUtils.showToast(mContext, "申请者姓名不能为空");
                            return;
                        }
                    }
                    //手机号
                    //身份证正面照
                    if (inputStr.contains("idcard_front")) {
                        if (!StringUtils.checkNull(idCardTruePath)) {
                            CommonUtils.showToast(mContext, "请上传身份证正面照");
                            return;
                        }
                        if (!StringUtils.checkNull(idCardFalsePath)) {
                            CommonUtils.showToast(mContext, "请上传身份证反面照");
                            return;
                        }
                    }
                    //车牌号
                    if (inputStr.contains("plate_number")) {
                        if (!CarNumUtils.isCarNo(this, licenseEt.getText().toString())) {
                            return;
                        }
                    }
                    //行驶证
                    if (inputStr.contains("driving_license")) {
                        if (!StringUtils.checkNull(drivingLicensePath)) {
                            CommonUtils.showToast(mContext, "请上传行驶证");
                            return;
                        }
                    }
                    //车辆外观图
                    if (inputStr.contains("car_image_out")) {
                        if (!StringUtils.checkNull(outPath)) {
                            CommonUtils.showToast(mContext, "请上传车辆外观图");
                            return;
                        }
                        if (!StringUtils.checkNull(inPath)) {
                            CommonUtils.showToast(mContext, "请上传车辆内饰图");
                            return;
                        }
                    }
                    Log.e("TAG", "eventId: " + eventId);
                    ExhibitionApplyPresenter exhibitionApplyPresenter = new ExhibitionApplyPresenter();
                    exhibitionApplyPresenter.attachView(this);
                    if (type == 1){
                        parkTimeId = -1;
                    }
                    exhibitionApplyPresenter.commitApply(IOUtils.getToken(getContext()), applyId, nickStr, status, inPath, outPath, drivingLicensePath, eventId, idCardFalsePath, idCardTruePath, parkTimeId, carNumStr, threadId, parkId);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //外观3
            if (data != null && requestCode == IMAGE_PICKER_OUT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data
                        .getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                for (ImageItem item : images) {
                    index = 3;
                    uploadImage(item);
                }
            }
            //内饰4
            if (data != null && requestCode == IMAGE_PICKER_IN) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data
                        .getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                for (ImageItem item : images) {
                    index = 4;
                    uploadImage(item);
                }
            }
            //行驶证2
            if (data != null && requestCode == IMAGE_PICKER_DRIVING_LICENSE) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data
                        .getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                for (ImageItem item : images) {
                    index = 2;
                    uploadImage(item);
                }
            }
            //身份证正面0
            if (data != null && requestCode == IMAGE_PICKER_ID_CARD_TRUE) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data
                        .getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                for (ImageItem item : images) {
                    index = 0;
                    uploadImage(item);
                }
            }
            //身份证反面1
            if (data != null && requestCode == IMAGE_PICKER_ID_CARD_FALSE) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data
                        .getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                for (ImageItem item : images) {
                    index = 1;
                    uploadImage(item);
                }
            }
        }
        //选择展会
        if (resultCode == RESULT_OK) {
            if (data != null && requestCode == REQUEST_EXHIBITION_SELECT) {
                Bundle bundle = data.getBundleExtra(Constants.IntentParams.DATA);
                SelectExhibitionModel.List model = (SelectExhibitionModel.List) bundle.getSerializable(Constants.IntentParams.DATA2);
                //展会ID
                eventId = model.id;
                exhibitionNameTv.setText(model.name);
                imgSdv.setImageURI(Uri.parse(URLUtil.builderImgUrl(model.cover, 144, 144)));
            }
        }
    }

    /**
     * @param item
     */
    private void uploadImage(ImageItem item) {
        File f = new File(item.path);
        //上传图片
        mFileUploadPresenter = new FileUploadPresenter();
        mFileUploadPresenter.attachView(this);
        mFileUploadPresenter.uploadFile(RequestBody.create(MediaType.parse("image/png"), f),
                RequestBody.create(MediaType.parse("text/plain"), "event"),
                RequestBody.create(MediaType.parse("text/plain"), "2"),
                RequestBody.create(MediaType.parse("text/plain"), IOUtils.getToken(mContext)), item);
    }

    /**
     * 初始化popwindow选项
     */
    public void initPop() {
        popList.clear();
        String[] popValues = mRes.getStringArray(R.array.car_num_arr);
        for (String value : popValues) {
            MorePopupwindowBean morePopupwindowBean = null;
            morePopupwindowBean = new MorePopupwindowBean();
            morePopupwindowBean.title = value;
            popList.add(morePopupwindowBean);
        }
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
    public void resultData(DataItem dataItem) {
        switch (index) {
            //身份证正面
            case 0:
                idCardTruePath = dataItem.resp.data.path;
                idCardTrueIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(idCardTruePath, 144, 144)));
                idCardTrueHint.setVisibility(View.GONE);
                break;
            //身份证反面
            case 1:
                idCardFalsePath = dataItem.resp.data.path;
                idCardFalseIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(idCardFalsePath, 144, 144)));
                idCardFalseHint.setVisibility(View.GONE);
                break;
            //行驶证
            case 2:
                drivingLicensePath = dataItem.resp.data.path;
                drivingLicenseIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(drivingLicensePath, 144, 144)));
                drivingLicenseHint.setVisibility(View.GONE);
                break;
            //外观
            case 3:
                outPath = dataItem.resp.data.path;
                outIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(outPath, 144, 144)));
                outHint.setVisibility(View.GONE);
                break;
            //内饰
            case 4:
                inPath = dataItem.resp.data.path;
                inIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(inPath, 144, 144)));
                inHint.setVisibility(View.GONE);
                break;
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

    @Override
    public void commitApplySuccess(ExhibitionApplyModel model) {
        if (type == 1) {
            // TODO: 2017/9/13 选择展位时段
            Intent intent = new Intent(mContext, SelectExhibitionPosActivity.class);
            //存入申请id
            SharedPreferenceUtil.putApplyId(this, model.data.applyId);
            //展会ID
            intent.putExtra(Constants.IntentParams.ID, model.data.applyId);
//        intent.putExtra(Constants.IntentParams.ID, model.data.orderSn);
            startActivity(intent);
        } else if (type == 2){
            finish();
        }
    }

    @Override
    public void commitApplyFail(ExhibitionApplyModel model) {
        CommonUtils.showToast(this, model.statusMessage);
    }

    @Override
    public void showGetApply(GetApplyModel model) {
        if (model != null) {
            if (model.parkTimeId > 0) {
                parkTimeId = model.parkTimeId;
            }
            eventId = model.eventId;
            applyId = model.applyId;
            threadId = model.threadId;
            parkId = model.parkId;
            status = model.status;
            //昵称
            nickStr = model.applyName;
            if (StringUtils.checkNull(nickStr)) {
                nicknameEt.setText(nickStr);
            }
            //车牌号
            carNumStr = model.plateNumber;
            if (StringUtils.checkNull(carNumStr)) {
                filterTitle.setText(carNumStr.substring(1));
                licenseEt.setText(carNumStr.substring(1, carNumStr.length()));
            }
            //身份证
            idCardTruePath = model.idcardFront;
            if (StringUtils.checkNull(idCardTruePath)) {
                idCardTrueIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(idCardTruePath, 144, 144)));
                idCardTrueHint.setVisibility(View.GONE);
            }
            idCardFalsePath = model.idcardBack;
            if (StringUtils.checkNull(idCardFalsePath)) {
                idCardFalseIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(idCardFalsePath, 144, 144)));
                idCardFalseHint.setVisibility(View.GONE);
            }
            //驾驶证
            drivingLicensePath = model.drivingLicense;
            if (StringUtils.checkNull(drivingLicensePath)) {
                drivingLicenseIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(drivingLicensePath, 144, 144)));
                drivingLicenseHint.setVisibility(View.GONE);
            }
            //外观内饰
            outPath = model.carImageOut;
            if (StringUtils.checkNull(outPath)) {
                outIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(outPath, 144, 144)));
                outHint.setVisibility(View.GONE);
            }
            inPath = model.carImageIn;
            if (StringUtils.checkNull(inPath)) {
                inIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(inPath, 144, 144)));
                inHint.setVisibility(View.GONE);
            }
            ExhibitionIntervalPresenter exhibitionIntervalPresenter = new ExhibitionIntervalPresenter();
            exhibitionIntervalPresenter.attachView(this);
            exhibitionIntervalPresenter.getExhibitionInterval(IOUtils.getToken(getContext()), eventId);
        }
    }

    @Override
    public void showGetRemain(GetRemainModel model) {
        if (model.data > 0) {
            carNumStr = filterTitle.getText().toString() + licenseEt.getText().toString();
            nickStr = nicknameEt.getText().toString();
            //申请者姓名
            if (inputStr.contains("apply_name")) {
                if (!StringUtils.checkNull(nickStr)) {
                    CommonUtils.showToast(mContext, "申请者姓名不能为空");
                    return;
                }
            }
            //手机号
            //身份证正面照
            if (inputStr.contains("idcard_front")) {
                if (!StringUtils.checkNull(idCardTruePath)) {
                    CommonUtils.showToast(mContext, "请上传身份证正面照");
                    return;
                }
                if (!StringUtils.checkNull(idCardFalsePath)) {
                    CommonUtils.showToast(mContext, "请上传身份证反面照");
                    return;
                }
            }
            //车牌号
            if (inputStr.contains("plate_number")) {
                if (!CarNumUtils.isCarNo(this, licenseEt.getText().toString())) {
                    return;
                }
            }
            //行驶证
            if (inputStr.contains("driving_license")) {
                if (!StringUtils.checkNull(drivingLicensePath)) {
                    CommonUtils.showToast(mContext, "请上传行驶证");
                    return;
                }
            }
            //车辆外观图
            if (inputStr.contains("car_image_out")) {
                if (!StringUtils.checkNull(outPath)) {
                    CommonUtils.showToast(mContext, "请上传车辆外观图");
                    return;
                }
                if (!StringUtils.checkNull(inPath)) {
                    CommonUtils.showToast(mContext, "请上传车辆内饰图");
                    return;
                }
            }
            Log.e("TAG", "eventId: " + eventId);
            ExhibitionApplyPresenter exhibitionApplyPresenter = new ExhibitionApplyPresenter();
            exhibitionApplyPresenter.attachView(this);
            if (type == 1){
                parkTimeId = -1;
            }
            exhibitionApplyPresenter.commitApply(IOUtils.getToken(getContext()), applyId, nickStr, status, inPath, outPath, drivingLicensePath, eventId, idCardFalsePath, idCardTruePath, parkTimeId, carNumStr, threadId, parkId);
        } else {
            CommonUtils.showToast(getContext(), "暂无剩余展位");
        }
    }

    @Override
    public void showExhibitionInterval(ExhibitionIntervalModel model) {
        inputStr = model.config.inputs;
        exhibitionName = model.name;
        exhibitionCover = model.cover;
        exhibitionNameTv.setText(exhibitionName);
        imgSdv.setImageURI(Uri.parse(URLUtil.builderImgUrl(exhibitionCover, 144, 144)));
        //申请者姓名
        if (!inputStr.contains("apply_name")) {
            applyNameRl.setVisibility(View.GONE);
            applyNameView.setVisibility(View.GONE);
        }
        //手机号
        //身份证
        if (!inputStr.contains("idcard_front")) {
            idCardLl.setVisibility(View.GONE);
        }
        //车牌号
        if (!inputStr.contains("plate_number")) {
            carNumRl.setVisibility(View.GONE);
            carNumView.setVisibility(View.GONE);
        }
        //行驶证
        if (!inputStr.contains("driving_license")) {
            drivingLicenseLl.setVisibility(View.GONE);
        }
        if (!inputStr.contains("driving_license") && !inputStr.contains("plate_number")) {
            carLicenseLl.setVisibility(View.GONE);
        }
        //车辆外观内饰图
        if (!inputStr.contains("car_image_out")) {
            carLl.setVisibility(View.GONE);
        }
    }
}
