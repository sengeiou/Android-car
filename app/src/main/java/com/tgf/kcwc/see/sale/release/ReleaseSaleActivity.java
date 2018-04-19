package com.tgf.kcwc.see.sale.release;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Tip;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.squareup.picasso.Picasso;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.ExhibitionCollectAdapter;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.GoodsCategoryActivity;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.app.SelectAddressActivity;
import com.tgf.kcwc.app.SelectBrandActivity;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.callback.OnSingleClickListener;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.entity.PathItem;
import com.tgf.kcwc.entity.ReleaseGoodsEntity;
import com.tgf.kcwc.entity.RichEditorEntity;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.model.CarBean;
import com.tgf.kcwc.mvp.model.ExhibitionApplyModel;
import com.tgf.kcwc.mvp.model.FilterItem;
import com.tgf.kcwc.mvp.model.Form;
import com.tgf.kcwc.mvp.model.GoodsEditModel;
import com.tgf.kcwc.mvp.model.OwnerSaleEditModel;
import com.tgf.kcwc.mvp.model.OwnerSaleModel;
import com.tgf.kcwc.mvp.model.SelectExhibitionModel;
import com.tgf.kcwc.mvp.presenter.ExhibitionApplyPresenter;
import com.tgf.kcwc.mvp.presenter.ExhibitionPresenter;
import com.tgf.kcwc.mvp.presenter.FileUploadPresenter;
import com.tgf.kcwc.mvp.presenter.FilterDataPresenter;
import com.tgf.kcwc.mvp.presenter.OwnerSalePresenter;
import com.tgf.kcwc.mvp.view.ExhibitionApplyView;
import com.tgf.kcwc.mvp.view.ExhibitionView;
import com.tgf.kcwc.mvp.view.FileUploadView;
import com.tgf.kcwc.mvp.view.FilterDataView;
import com.tgf.kcwc.mvp.view.OwnerSaleDataView;
import com.tgf.kcwc.see.sale.AboutSaleActivity;
import com.tgf.kcwc.see.sale.GoodsDescActivity;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.PhoneFormatCheckUtils;
import com.tgf.kcwc.util.SharedPreferenceUtil;
import com.tgf.kcwc.util.StringUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.richeditor.SEImageLoader;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qqtheme.framework.picker.NumberPicker;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Author：Jenny
 * Date:2017/2/20 14:57
 * E-mail：fishloveqin@gmail.com
 * 发布出售页面
 */

public class ReleaseSaleActivity extends BaseActivity implements OwnerSaleDataView<OwnerSaleModel>, ExhibitionView, ExhibitionApplyView {
    protected ImageView mCover;
    protected RelativeLayout uploadCoverLayout;
    protected EditText mInputGoodsTitle;
    protected ListView mList;
    protected TextView mTitle;
    protected TextView mDesc2;
    protected TextView mAcceptBtn;
    protected TextView maboutSale;
    protected Button mReleaseBtn;
    //车主自售参展征集
    protected LinearLayout exhibitionCollectLl;

    //一个参展征集
    protected RelativeLayout exhibitionCollectRl;
    protected SimpleDraweeView itemExhibitionImg;
    protected TextView itemInterval;
    protected TextView itemExhibitionName;
    protected TextView itemExhibitionAddress;
    protected TextView itemRetainExhibitionPos;
    //我要参展
    protected TextView itemJoinExhibition;

    //征集列表
    protected RecyclerView exhibitionCollectRv;
    private ExhibitionCollectAdapter exhibitionCollectAdapter;
    private ArrayList<SelectExhibitionModel.List> exhibitionCollectList = new ArrayList<>();
    private ExhibitionCollectAdapter.OnItemsClickListener onItemsClickListener;

    private List<Form> mItems;
    private BaseAdapter mAdapter;
    private RelativeLayout mGoodsTypeLayout, mExpireLayout, mGoodsDescLayout;
    private TextView mGoodsTypeDesc;
    //宝贝发布
    private OwnerSalePresenter mPresenter;
    private OwnerSalePresenter mEditPresenter;
    //
    private OwnerSalePresenter mGetEditPresenter;
    private String mCoverUrl = "cover.png";
    //宝贝分类
    private FilterDataPresenter mGoodsCategoryPresenter;
    //封面图片上传
    private FileUploadPresenter mCoverUploadPresenter;
    //展会ID
    private int eventId;
    //展会名
    private String exhibitionName;
    //展会图
    private String exhibitionCover;
    //单个展位是否选中
    private boolean isSelect = true;
    //当前展会的展位数量
    private int exhibitionPosNum;
    //帖子id
    private int threadId;
    //展位id
    private int parkId;
    //申请
    private int applyId;
    private int status;
    private String inputs;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("发布宝贝");
    }

    @Override
    protected void setUpViews() {
        mExpireItems = mRes.getStringArray(R.array.expire_items);

        mPresenter = new OwnerSalePresenter();
        mPresenter.attachView(this);

        mEditPresenter = new OwnerSalePresenter();
        mEditPresenter.attachView(ownerSaleDataEditView);

        mGetEditPresenter = new OwnerSalePresenter();
        mGetEditPresenter.attachView(mGoodEditView);

        mCoverUploadPresenter = new FileUploadPresenter();
        mCoverUploadPresenter.attachView(mCoverView);

        mGoodsCategoryPresenter = new FilterDataPresenter();
        mGoodsCategoryPresenter.attachView(mGoodsCategoryView);
        initView();
    }

    //初始化展会征集
    public void initAdapter() {
        exhibitionCollectRv.setHasFixedSize(true);
        exhibitionCollectRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        exhibitionCollectAdapter = new ExhibitionCollectAdapter(this, exhibitionCollectList);
        onItemsClickListener = new ExhibitionCollectAdapter.OnItemsClickListener() {
            @Override
            public void onItemClick(int position, int eventIds, int exhibitionPosNums, String exhibitionNames, String exhibitionCovers) {
                eventId = eventIds;
                exhibitionPosNum = exhibitionPosNums;
                exhibitionName = exhibitionNames;
                exhibitionCover = exhibitionCovers;
            }
        };
        exhibitionCollectRv.setAdapter(exhibitionCollectAdapter);
        exhibitionCollectAdapter.setOnItemsClickListener(onItemsClickListener);
    }

    private FilterItem mFilterItem;
    //宝贝分类
    private FilterDataView<List<FilterItem>> mGoodsCategoryView = new FilterDataView<List<FilterItem>>() {
        @Override
        public void showData(List<FilterItem> filterItems) {
            if (filterItems
                    .size() > 0) {
                mFilterItem = filterItems
                        .get(0);
                goodsType = mFilterItem.id;
            }
        }
    };

    private FileUploadView<DataItem> mCoverView = new FileUploadView<DataItem>() {
        @Override
        public void resultData(DataItem dataItem) {
            PathItem item = dataItem.resp.data;
            mCoverUrl = item.path;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mId = getIntent().getIntExtra(Constants.IntentParams.ID, -1);
        carBean = getIntent().getParcelableExtra(Constants.IntentParams.DATA);
        super.setContentView(R.layout.activity_release_sale);

    }

    private CarBean carBean;
    private int mId;

    private void initView() {
        exhibitionCollectLl = (LinearLayout) findViewById(R.id.exhibition_collect_ll);

        exhibitionCollectRl = (RelativeLayout) findViewById(R.id.exhibition_collect_rl);
        itemExhibitionImg = (SimpleDraweeView) findViewById(R.id.item_exhibition_img);
        itemInterval = (TextView) findViewById(R.id.item_interval);
        itemExhibitionName = (TextView) findViewById(R.id.item_exhibition_name);
        itemExhibitionAddress = (TextView) findViewById(R.id.item_exhibition_address);
        itemRetainExhibitionPos = (TextView) findViewById(R.id.item_retain_exhibition_pos);
        itemJoinExhibition = (TextView) findViewById(R.id.item_join_exhibition);

        exhibitionCollectRv = (RecyclerView) findViewById(R.id.exhibition_collect_rv);

        mCover = (ImageView) findViewById(R.id.cover);
        uploadCoverLayout = (RelativeLayout) findViewById(R.id.uploadCoverLayout);
        uploadCoverLayout.setOnClickListener(this);
        mInputGoodsTitle = (EditText) findViewById(R.id.inputGoodsTitle);
        mInputGoodsTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                String str = editable.toString();
//                if (TextUtils.isEmpty(str.trim())) {
//
//                    mInputGoodsTitle.setGravity(Gravity.CENTER);
//                } else {
//                    mInputGoodsTitle.setGravity(Gravity.RIGHT);
//                }
            }
        });
        mGoodsTypeDesc = (TextView) findViewById(R.id.goodsTypeDesc);
        mList = (ListView) findViewById(R.id.list);
        mGoodsTypeLayout = (RelativeLayout) findViewById(R.id.goodsType);
        // mGoodsTypeLayout.setOnClickListener(this);
        mExpireLayout = (RelativeLayout) findViewById(R.id.expireLayout);
        mExpireLayout.setOnClickListener(this);
        mGoodsDescLayout = (RelativeLayout) findViewById(R.id.goodsDesc);
        mGoodsDescLayout.setOnClickListener(this);
        mTitle = (TextView) findViewById(R.id.title);
        mDesc2 = (TextView) findViewById(R.id.desc2);
        mAcceptBtn = (TextView) findViewById(R.id.accept);
        mAcceptBtn.setSelected(true);
        mAcceptBtn.setOnClickListener(this);
        maboutSale = (TextView) findViewById(R.id.aboutSale);
        maboutSale.setOnClickListener(this);
        exhibitionCollectRl.setOnClickListener(this);
        mReleaseBtn = (Button) findViewById(R.id.submit);
        //可点
        mReleaseBtn.setEnabled(true);
        mReleaseBtn.setBackgroundResource(R.color.style_bg6);
        mReleaseBtn.setOnClickListener(new OnSingleClickListener() {
            @Override
            protected void onSingleClick(View view) {
                String phone = mItems.get(5).desc;
                if (!PhoneFormatCheckUtils.isChinaPhoneLegal(phone)) {
                    CommonUtils.showToast(mContext, "手机号格式不对");
                    return;
                }
                Log.e("TAG", "mId: " + mId);
                if (mId == -1) {
                    //发布车主自售
                    mPresenter.publishOwnerGoods(buildPostData(), eventId);
                } else {
                    //帖子编辑或重发 publishEditGoods
                    mEditPresenter.publishOwnerSaleEditGoods(buildPostData(), eventId, mId);
                }
            }
        });
        if (mId == -1) {
            removeCache();
            initListData(1);
            mGoodsCategoryPresenter.loadFilterGoodsList("");
            initAdapter();
            //展会征集
            ExhibitionPresenter exhibitionPresenter = new ExhibitionPresenter();
            exhibitionPresenter.attachView(this);
            KPlayCarApp kPlayCarApp = (KPlayCarApp) getApplication();
            int cityId = kPlayCarApp.cityId;
            exhibitionPresenter.getSelectExhibition(IOUtils.getToken(this), cityId);
        } else {//编辑重发，显示前数据
            exhibitionCollectLl.setVisibility(View.GONE);
            mGetEditPresenter.getGoodsEditInfo(mId + "");
        }

    }

    private void removeCache() {
        //发布成功后删除缓存数据
        boolean flag = IOUtils.deleteFile(mContext, Constants.KeyParams.RICH_E_DATA);//富文本
        KPlayCarApp.removeValue(Constants.KeyParams.CAR_MODEL_KEY);//车型
    }

    private OwnerSaleDataView<GoodsEditModel> mGoodEditView = new OwnerSaleDataView<GoodsEditModel>() {
        @Override
        public void showData(GoodsEditModel goodsEditModel) {
            int id = 1;
            goodsType = id;
            mSelect = 2;
            mMoto = goodsEditModel.car;
            if (Constants.GoodsType.VEHICLE_TYPE == id) {
                initListData(1);
                if (goodsEditModel.car != null) {
                    mItems.get(0).desc = goodsEditModel.car.name;
                }
                mItems.get(1).desc = goodsEditModel.buyYear + "";
                mItems.get(2).desc = "" + (Double.parseDouble(goodsEditModel.roadHaul));
                mItems.get(3).desc = goodsEditModel.price;
                mItems.get(4).desc = goodsEditModel.localAddress;
                mItems.get(5).desc = goodsEditModel.phone;
            } else {
                initListData(2);
                mItems.get(0).desc = goodsEditModel.price;
                mItems.get(1).desc = goodsEditModel.localAddress;
                mItems.get(2).desc = goodsEditModel.phone;
            }
            RichEditorEntity richEditorEntity = goodsEditModel.describe;
            IOUtils.writeObject(mContext, richEditorEntity, Constants.KeyParams.RICH_E_DATA);
            mDesc2.setText(mExpireItems[mSelect]);
            mInputGoodsTitle.setText(goodsEditModel.title);
            mGoodsTypeDesc.setText(goodsEditModel.goodsType.title);
            mCoverUrl = goodsEditModel.cover;
            Picasso.with(mContext).load(URLUtil.builderImgUrl(goodsEditModel.cover, 540, 270))
                    .into(mCover);
            mAdapter.notifyDataSetChanged();
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

    /**
     * 构建需要上传的数据
     */
    private Map<String, String> buildPostData() {

        Map<String, String> params = new HashMap<String, String>();

        //car_id=&local_address=&area=
        // &latitude=&token=&cover=&buy_year=&price=&phone=&title=&failure_time=&goods_type=&describe=&longitude=
        String carId = "";
        String latitude = "";
        String longitude = "";
        String buyYear = "2002";
        String title = mInputGoodsTitle.getText().toString();
        String price = "";
        String failureTime = "";
        String describe = "";
        Gson gson = new Gson();
        RichEditorEntity entity = IOUtils.getObject(mContext, Constants.KeyParams.RICH_E_DATA);
        if (entity != null) {
            describe = gson.toJson(entity);
        }
        //
        String goodsType = "" + this.goodsType;

        String area = "";
        String phone = "";
        String localAddress = "";
        String roadHaul = "0";
        //        if (mPoiItem != null) {
        //            latitude = mPoiItem.getLatLonPoint().getLatitude() + "";
        //            longitude = mPoiItem.getLatLonPoint().getLongitude() + "";
        //            String adName = mPoiItem.getAdName();
        //            String address = "";
        //            if (!TextUtils.isEmpty(adName)) {
        //                localAddress = adName + "|";
        //            }
        //            localAddress += mPoiItem.getTitle();
        //        }

        if (tip != null) {
            latitude = tip.getPoint().getLatitude() + "";
            longitude = tip.getPoint().getLongitude() + "";
            //            String adName = mPoiItem.getAdName();
            //            String address = "";
            //            if (!TextUtils.isEmpty(adName)) {
            //                localAddress = adName + "|";
            //            }
            localAddress = tip.getName();
        }
        if (mMoto != null) {
            carId = mMoto.id + "";
        }

        if (mType == 1) {
            buyYear = mItems.get(1).desc;
            roadHaul = mItems.get(2).desc;
            price = mItems.get(3).desc;
            phone = mItems.get(5).desc;
        }
        if (mType == 2) {
            price = mItems.get(0).desc;
            phone = mItems.get(2).desc;
        }
        //        if (!TextUtils.isEmpty(roadHaul)) {
        //            roadHaul = (Double.parseDouble(roadHaul)) + "";
        //        }
        failureTime = mSelect + "";

        if (mCoverUrl.equals("cover.png")) {
            CommonUtils.showToast(getContext(), "请上传封面");
            return null;
        }
//        if (!StringUtils.checkNull(title)) {
//            CommonUtils.showToast(getContext(), "请输入宝贝标题");
//            return null;
//        }
//        if (!StringUtils.checkNull(goodsType)) {
//            CommonUtils.showToast(getContext(), "请选择出售车型");
//            return null;
//        }
        if (!StringUtils.checkNull(buyYear)) {
            CommonUtils.showToast(getContext(), "请选择购买年份");
            return null;
        }
        if (!StringUtils.checkNull(roadHaul)) {
            CommonUtils.showToast(getContext(), "请输入行驶里程");
            return null;
        }
        if (!StringUtils.checkNull(price)) {
            CommonUtils.showToast(getContext(), "请输入售价");
            return null;
        }
//        if (!StringUtils.checkNull(latitude) || !StringUtils.checkNull(longitude)) {
//            CommonUtils.showToast(getContext(), "请选择宝贝位置");
//            return null;
//        }
        if (!StringUtils.checkNull(phone)) {
            CommonUtils.showToast(getContext(), "请输入手机号");
            return null;
        }
        if (!StringUtils.checkNull(failureTime)) {
            CommonUtils.showToast(getContext(), "请选择失效时间");
            return null;
        }
//        if (!StringUtils.checkNull(describe)) {
//            CommonUtils.showToast(getContext(), "请输入宝贝描述");
//            return null;
//        }

        params.put("car_id", carId);
        params.put("local_address", localAddress);
        params.put("area", area);
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("cover", mCoverUrl);
        params.put("buy_year", buyYear);
        params.put("price", price);
        params.put("phone", phone);
        params.put("title", title);
        params.put("failure_time", failureTime);
        params.put("goods_type", goodsType);
        params.put("describe", describe);
        params.put("road_haul", roadHaul);
        params.put("token", IOUtils.getToken(mContext));

        return params;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.accept://发布协议选中
                boolean flag = true;
                Object tag = view.getTag();
                if (tag != null) {
                    flag = Boolean.parseBoolean(tag.toString());
                }
                if (flag) {
                    view.setSelected(false);
                    view.setTag(false);
                    //不可点击
                    mReleaseBtn.setEnabled(false);
                    mReleaseBtn.setBackgroundResource(R.color.text_nav_def_color);
                } else {
                    view.setSelected(true);
                    view.setTag(true);
                    //可点
                    mReleaseBtn.setEnabled(true);
                    mReleaseBtn.setBackgroundResource(R.color.style_bg6);
                }
                // TODO: 2017/11/6 需要判断当前展位数量是否大于0 
                break;
            case R.id.aboutSale://车主自售协议
                CommonUtils.startNewActivity(mContext, AboutSaleActivity.class);
                break;
            case R.id.goodsType://宝贝分类
                CommonUtils.startResultNewActivity(this, null, GoodsCategoryActivity.class,
                        GOODS_CATEGORY_REQUEST_CODE);
                break;
            case R.id.expireLayout://宝贝自动失效时间
                builderExpireDialog();
                break;
            case R.id.goodsDesc://宝贝描述
                CommonUtils.startResultNewActivity(this, null, GoodsDescActivity.class, GOODS_DESC_REQUEST_CODE);
                break;
            case R.id.uploadCoverLayout://上传封面
                ImagePicker.getInstance().setMultiMode(false);
                ImagePicker.getInstance().setFocusWidth(BitmapUtils.dp2px(mContext, 270));
                ImagePicker.getInstance().setFocusHeight(BitmapUtils.dp2px(mContext, 203));
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, IMAGE_PICKER);
                break;
            case R.id.exhibition_collect_rl://一个展会征集
                if (!isSelect) {
                    exhibitionName = exhibitionCollectList.get(0).name;
                    exhibitionCover = exhibitionCollectList.get(0).cover;
                    eventId = exhibitionCollectList.get(0).id;
                    exhibitionPosNum = exhibitionCollectList.get(0).remaining;
                    isSelect = true;
                    exhibitionCollectRl.setAlpha((float) 0.3);
                } else {
                    isSelect = false;
                    exhibitionName = "";
                    exhibitionCover = "";
                    exhibitionPosNum = -1;
                    eventId = 0;
                    exhibitionCollectRl.setAlpha((float) 1.0);
                }
                break;
        }

    }

    private static final int IMAGE_PICKER = 1001;
    private static final int GOODS_CATEGORY_REQUEST_CODE = 1002;

    private static final int GOODS_DESC_REQUEST_CODE = 1003;

    private static final int MARKER_POINT_REQUEST_CODE = 1004;

    private int mType = 1;

    /**
     * @param type 通过type加载不同的列表数据
     */
    private void initListData(int type) {
        mType = type;
        mItems = new ArrayList<Form>();
        String[] arrays = null;
        int index = 0;
        switch (type) {
            case 1:
                mList.setOnItemClickListener(mTypeItemClickListener1);
                arrays = mRes.getStringArray(R.array.sale_title_items);
                for (String s : arrays) {
                    Form item = new Form();
                    item.require = 1;
                    item.name = s;

                    switch (index) {

                        case 0: //条目的position,主要用于区分加载不同的item项布局
                        case 1:
                        case 4:
                            item.viewTypeId = 1;
                            break;
                        case 2:
                        case 3:
                        case 5:
                            item.viewTypeId = 0;
                            break;
                    }
                    mItems.add(item);
                    index++;
                }
                Account a = IOUtils.getAccount(mContext);
                mItems.get(4).desc = mGlobalContext.getAddressInfo() + "";
                mItems.get(5).desc = a.userInfo.tel;
                break;

            case 2:
                mList.setOnItemClickListener(mTypeItemClickListener2);
                arrays = mRes.getStringArray(R.array.sale_title_item2);
                for (String s : arrays) {
                    Form item = new Form();
                    item.require = 1;
                    item.name = s;

                    switch (index) {

                        case 0:
                        case 2:
                            item.viewTypeId = 0;
                            break;
                        case 1:
                            item.viewTypeId = 1;
                            break;
                    }
                    mItems.add(item);
                    index++;
                }
                break;
        }

        if (carBean != null) {
            ReleaseGoodsEntity entity = IOUtils.getObject(mContext, Constants.KEY_RELEASE_SALE_FILE);
            mMoto = carBean;
            mCoverUrl = entity.coverUrl;
            coverPath = entity.coverLocalPath;
            displayCover(coverPath);
            mItems.get(0).desc = carBean.seriesName + " " + carBean.name;
            if (entity != null) {
                mItems.get(1).desc = entity.year;
                mItems.get(2).desc = entity.mile;
                mItems.get(3).desc = entity.price;
                mItems.get(4).desc = entity.location;
                mItems.get(5).desc = entity.tel;
                mSelect = entity.expirePosition;
                System.out.println("title:" + entity.title);
                mInputGoodsTitle.setText(entity.title);
            }
        }

        mAdapter = new ReleaseSaleAdapter();
        mList.setAdapter(mAdapter);
        ViewUtil.setListViewHeightBasedOnChildren2(mList);

        mDesc2.setText(mExpireItems[mSelect]);
    }

    /**
     * 年选择菜单项
     *
     * @param position
     */
    private void onYearPicker(final int position) {
        NumberPicker picker = new NumberPicker(this);
        picker.setWidth(picker.getScreenWidthPixels() / 2);
        picker.setGravity(Gravity.CENTER);
        picker.setCycleDisable(false);
        picker.setLineVisible(false);
        picker.setOffset(2);//偏移量
        picker.setRange(2001, DateFormatUtil.getYear(), 1);//数字范围
        picker.setSelectedItem(2017);
        picker.setLabel("年");
        picker.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
            @Override
            public void onNumberPicked(int index, Number item) {
                mItems.get(position).desc = item.intValue() + "";

                mAdapter.notifyDataSetChanged();
            }
        });
        picker.show();
    }

    private CarBean mMoto;

    @Override
    protected void onResume() {
        super.onResume();

        Object tag = mCover.getTag();
        if (tag != null) {
            PathItem pathItem = (PathItem) tag;
            displayCover(pathItem.path);
        }
    }

    /**
     * 获取当前位置信息，跳转到地图标点页面带回数据
     */
    private void getLocationInfoByMarkerMap() {

        CommonUtils.startResultNewActivity(ReleaseSaleActivity.this, null,
                SelectAddressActivity.class, MARKER_POINT_REQUEST_CODE);
    }

    private static final int PERMISSION_LOCATION_REQUEST_CODE = 1000;
    private AdapterView.OnItemClickListener mTypeItemClickListener1 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent,View view,int position,long id) {
            switch (position) {
                case 0:
                    Map<String, Serializable> args = new HashMap<String, Serializable>();
                    args.put(Constants.IntentParams.MODULE_TYPE, Constants.ModuleTypes.RELEASE_SALE);
                    CommonUtils.startNewActivity(ReleaseSaleActivity.this, args, SelectBrandActivity.class);
                    //将已编辑的数据保存
                    ReleaseGoodsEntity entity = new ReleaseGoodsEntity();
                    entity.coverUrl = mCoverUrl;
                    entity.coverLocalPath = coverPath;
                    entity.title = mInputGoodsTitle.getText().toString();
                    entity.year = mItems.get(1).desc;
                    entity.mile = mItems.get(2).desc;
                    entity.price = mItems.get(3).desc;
                    entity.location = mItems.get(4).desc;
                    entity.tel = mItems.get(5).desc;
                    entity.expirePosition = mSelect;

                    IOUtils.writeObject(mContext, entity, Constants.KEY_RELEASE_SALE_FILE);
                    break;
                case 1:
                    onYearPicker(1);
                    break;
                case 4:
                    boolean flag = CommonUtils.checkSpPermission(ReleaseSaleActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            PERMISSION_LOCATION_REQUEST_CODE);
                    if (flag) {
                        getLocationInfoByMarkerMap();
                    }
                    break;
            }

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PERMISSION_LOCATION_REQUEST_CODE == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationInfoByMarkerMap();
            }
        }
    }

    private AdapterView.OnItemClickListener mTypeItemClickListener2 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent,
                                View view,
                                int position,
                                long id) {
            if (position == 1) {
                getLocationInfoByMarkerMap();
            }

        }
    };

    /**
     * 输入框小数的位数
     */
    private static final int DECIMAL_DIGITS = 1;
    private static final int DECIMAL_LENGTH = 4;
    /**
     * 设置小数位数控制
     */
    private InputFilter lengthfilter = new InputFilter() {
        public CharSequence filter(CharSequence source,
                                   int start,
                                   int end,
                                   Spanned dest,
                                   int dstart,
                                   int dend) {
            // 删除等特殊字符，直接返回
            if ("".equals(source
                    .toString())) {
                return null;
            }
            String dValue = dest.toString();
            String[] splitArray = dValue.split("\\.");
            if (splitArray.length > 1) {
                String dotValue = splitArray[1];
                int diff = dotValue.length() + 1 - DECIMAL_DIGITS;
                if (diff > 0) {
                    return source.subSequence(start, end - diff);
                }
            }
            return null;
        }
    };

    OwnerSaleDataView<OwnerSaleEditModel> ownerSaleDataEditView = new OwnerSaleDataView<OwnerSaleEditModel>() {
        @Override
        public void showData(OwnerSaleEditModel ownerSaleModel) {
            int code = ownerSaleModel.statusCode;
            if (code == Constants.NetworkStatusCode.SUCCESS) {
                CommonUtils.showToast(mContext, "发布成功");
                removeCache();
                IOUtils.deleteFile(mContext, Constants.KEY_RELEASE_SALE_FILE);
//                if (mId != -1) {
                finish();
//                } else {
//                    threadId = ownerSaleModel.data.threadId;
//                    inputs = ownerSaleModel.data.eventConfig.inputs;
//                    ExhibitionApplyPresenter exhibitionApplyPresenter = new ExhibitionApplyPresenter();
//                    exhibitionApplyPresenter.attachView(this);
//                    exhibitionApplyPresenter.commitApply(IOUtils.getToken(this), 0, "", status, "", "", "", eventId, "", "", 0, "", threadId, parkId);
//                }
            } else {
                CommonUtils.showToast(mContext, ownerSaleModel.statusMessage);
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
//            return ReleaseSaleActivity.this;
            return mContext;
        }
    };

    @Override
    public void showData(OwnerSaleModel repData) {
        int code = repData.statusCode;
        if (code == Constants.NetworkStatusCode.SUCCESS) {
            CommonUtils.showToast(mContext, "发布成功");
            removeCache();
            IOUtils.deleteFile(mContext, Constants.KEY_RELEASE_SALE_FILE);
            if (mId == -1) {
                finish();
            } else {
                threadId = repData.data.threadId;
                inputs = repData.data.eventConfig.inputs;
                ExhibitionApplyPresenter exhibitionApplyPresenter = new ExhibitionApplyPresenter();
                exhibitionApplyPresenter.attachView(this);
                exhibitionApplyPresenter.commitApply(IOUtils.getToken(this), 0, "", status, "", "", "", eventId, "", "", 0, "", threadId, parkId);
            }
        } else {
            CommonUtils.showToast(mContext, repData.statusMessage);
        }
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
    public void showExhibitionList(SelectExhibitionModel model) {
        ArrayList<SelectExhibitionModel.List> list = model.list;
        if (null != list && list.size() != 0) {
            exhibitionCollectList.addAll(list);
            if (exhibitionCollectList.size() == 1) {
                itemExhibitionImg.setImageURI(Uri.parse(URLUtil.builderImgUrl(list.get(0).cover, 144, 144)));
                itemExhibitionName.setText(list.get(0).name);
                itemExhibitionAddress.setText(list.get(0).address);
                itemInterval.setText(DateFormatUtil.formatTimePlus2(list.get(0).startTime) + "-" + DateFormatUtil.formatTimePlus2(list.get(0).endTime));
                itemRetainExhibitionPos.setText("剩余展位：" + list.get(0).remaining);
                exhibitionCollectRl.setVisibility(View.VISIBLE);
                exhibitionCollectRv.setVisibility(View.GONE);

                exhibitionName = exhibitionCollectList.get(0).name;
                exhibitionCover = exhibitionCollectList.get(0).cover;
                eventId = exhibitionCollectList.get(0).id;
                exhibitionPosNum = exhibitionCollectList.get(0).remaining;

                exhibitionCollectRl.setAlpha((float) 0.3);
            } else {
                exhibitionName = exhibitionCollectList.get(0).name;
                exhibitionCover = exhibitionCollectList.get(0).cover;
                eventId = exhibitionCollectList.get(0).id;
                exhibitionPosNum = exhibitionCollectList.get(0).remaining;

                exhibitionCollectRl.setVisibility(View.GONE);
                exhibitionCollectRv.setVisibility(View.VISIBLE);
                exhibitionCollectAdapter.notifyDataSetChanged();
            }
        } else {
            exhibitionCollectLl.setVisibility(View.GONE);
        }
    }

    @Override
    public void commitApplySuccess(ExhibitionApplyModel model) {
        applyId = model.data.applyId;
        if (eventId != 0) {//需要参展
            // TODO: 2017/11/6 判断当前展会的展位数量
            if (exhibitionPosNum > 0) {
                // TODO: 2017/9/15 提交审核资料
                Intent intent = new Intent(mContext, ExhibitionApplyActivity.class);
                intent.putExtra(Constants.IntentParams.ID3, applyId);
                intent.putExtra(Constants.IntentParams.ID, 1);
                startActivity(intent);
            } else {
                // TODO: 2017/11/6 跳转车主自售列表，弹出提示框
                CommonUtils.showToast(getContext(), "暂无剩余展位");
            }
        } else {
            finish();
        }
    }

    @Override
    public void commitApplyFail(ExhibitionApplyModel model) {
        CommonUtils.showToast(getContext(), model.statusMessage);
    }

    private class ReleaseSaleAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public int getItemViewType(int position) {
            return mItems.get(position).viewTypeId;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final Form form = mItems.get(position);
            int id = form.viewTypeId;
            WrapAdapter.ViewHolder holder = null;
            switch (id) {
                case 0:
                    holder = WrapAdapter.ViewHolder.get(mContext, convertView, parent, R.layout.base_info_item_1, position);
                    break;
                case 1:
                    holder = WrapAdapter.ViewHolder.get(mContext, convertView, parent, R.layout.base_info_item_3, position);
                    break;
            }

            switch (id) {
                case 0:
                    final EditText editText = holder.getView(R.id.content);
                    editText.clearFocus();
                    int filterType = InputType.TYPE_CLASS_PHONE;
                    if (mType == 1) {
                        if (position == 2) {
                            filterType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
                            //                            editText.setFilters(
                            //                                new InputFilter[] { lengthfilter,
                            //                                                    new InputFilter.LengthFilter(DECIMAL_LENGTH) });
                            editText.setHint("请输入行驶里程！");
                        } else if (position == 3) {
                            filterType = InputType.TYPE_CLASS_NUMBER;
                            editText.setHint("输入 0 表示面议");
                        } else if (position == 5) {
                            editText.setHint("请填写联系电话！");
                        }
                    }
                    if (mType == 2) {
                        if (position == 0) {
                            filterType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
                        } else if (position == 2) {
                            filterType = InputType.TYPE_CLASS_NUMBER;
                        }
                    }
                    editText.setInputType(filterType);
                    if (editText.getTag() instanceof TextWatcher) {
                        editText.removeTextChangedListener((TextWatcher) editText.getTag());
                    }
                    TextWatcher watcher = new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                      int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before,
                                                  int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            mItems.get(position).desc = s.toString();
                            //   setCommBtnStatus();
                        }
                    };
                    editText.addTextChangedListener(watcher);
                    editText.setTag(watcher);
                    editText.setText(mItems.get(position).desc);
                    break;
                case 1:
                    TextView textView = holder.getView(R.id.desc);
                    textView.setText(mItems.get(position).desc);
                    break;
            }
            TextView tvName = holder.getView(R.id.title);
            tvName.setText(mItems.get(position).name);
            ImageView imageView = holder.getView(R.id.img);

            if (Constants.CheckInFormKey.IS_REQUIRE == form.require) {
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }
            convertView = holder.getConvertView();
            return convertView;
        }
    }

    private int goodsType = 1;

    /**
     * 显示封面
     *
     * @param path
     */
    private void displayCover(String path) {

        PointF pointF = new PointF();
        pointF.x = mCover.getWidth();
        pointF.y = mCover.getHeight();
        //  System.out.println("x:" + pointF.x + ",y:" + pointF.y);
        PathItem pathItem = new PathItem();
        pathItem.path = path;
        mCover.setTag(pathItem);
        SEImageLoader.getInstance().loadImage(path, mCover, pointF);
    }

    private String coverPath = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode || resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            switch (requestCode) {
                case IMAGE_PICKER:
                    if (data != null && requestCode == IMAGE_PICKER) {
                        ArrayList<ImageItem> images = (ArrayList<ImageItem>) data
                                .getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        for (ImageItem item : images) {
                            //editor.addImage(item.path);
                            displayCover(item.path);
                            coverPath = item.path;
                            Luban.with(this).load(new File(item.path))//传人要压缩的图片
                                    //putGear(3) //设定压缩档次，默认三挡
                                    .setCompressListener(new OnCompressListener() { //设置回调

                                        @Override
                                        public void onStart() {
                                            // TODO 压缩开始前调用，可以在方法内启动 loading UI
                                        }

                                        @Override
                                        public void onSuccess(File file) {

                                            System.out.println("file:" + file.getAbsolutePath());
                                            mCoverUploadPresenter.uploadFile(
                                                    RequestBody.create(MediaType.parse("image/png"), file),
                                                    RequestBody.create(MediaType.parse("text/plain"),
                                                            "thread"),
                                                    RequestBody.create(MediaType.parse("text/plain"), ""),
                                                    RequestBody.create(MediaType.parse("text/plain"),
                                                            IOUtils.getToken(getContext())),
                                                    mCover);
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            // TODO 当压缩过去出现问题时调用
                                        }
                                    }).launch(); //启动压

                        }
                    } else {
                        CommonUtils.showToast(mContext, "没有数据");
                    }
                    break;
                case GOODS_CATEGORY_REQUEST_CODE:
                    FilterItem item = data.getParcelableExtra(Constants.IntentParams.DATA);
                    FilterItem item2 = data.getParcelableExtra(Constants.IntentParams.DATA2);
                    if (item != null) {
                        String title = item.title;
                        mGoodsTypeDesc.setText("" + title);
                        goodsType = item.id;
                        if ("整车".equals(title)) {
                            initListData(1);
                        } else {
                            initListData(2);
                        }
                    }
                    break;
                case GOODS_DESC_REQUEST_CODE:
                    break;
                case MARKER_POINT_REQUEST_CODE:

                    //                    PoiItem poiItem = data.getParcelableExtra(Constants.IntentParams.DATA);
                    //                    mPoiItem = poiItem;
                    //                    if (poiItem != null) {
                    //                        String adName = poiItem.getAdName();
                    //                        String address = "";
                    //                        if (!TextUtils.isEmpty(adName)) {
                    //                            address = adName + "|";
                    //                        }
                    //                        address += poiItem.getTitle();
                    //                        if (mType == 1) {
                    //
                    //                            mItems.get(4).desc = address;
                    //                        } else if (mType == 2) {
                    //                            mItems.get(1).desc = address;
                    //                        }
                    //                        mAdapter.notifyDataSetChanged();
                    //                    }

                    tip = data.getParcelableExtra(Constants.IntentParams.DATA);
                    String city = data.getStringExtra(Constants.IntentParams.DATA2);
                    mItems.get(4).desc = tip.getName();
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }

    }

    private Tip tip;

    private int mSelect = 0;

    private String[] mExpireItems = null;
    private PoiItem mPoiItem;

    //失效时间对话框
    private void builderExpireDialog() {

        AlertDialog alertDialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setSingleChoiceItems(mExpireItems, mSelect, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSelect = which;
                dialog.dismiss();
                mDesc2.setText(mExpireItems[mSelect]);

            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
