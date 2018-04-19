package com.tgf.kcwc.me.sale;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.CommonAdapter;
import com.tgf.kcwc.adapter.ExhibitionPosAdapter;
import com.tgf.kcwc.adapter.OnItemClickListener;
import com.tgf.kcwc.adapter.ViewHolder;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.callback.OnSingleClickListener;
import com.tgf.kcwc.certificate.CarCertDetailActivity;
import com.tgf.kcwc.certificate.CertListActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.me.sale.MyExhibitionInfoActivity;
import com.tgf.kcwc.mvp.model.BtnDataBean;
import com.tgf.kcwc.mvp.model.MorePopupwindowBean;
import com.tgf.kcwc.mvp.model.SaleMgrModel;
import com.tgf.kcwc.mvp.presenter.CancelApplyPresenter;
import com.tgf.kcwc.mvp.presenter.OwnerSalePresenter;
import com.tgf.kcwc.mvp.view.CancelApplyView;
import com.tgf.kcwc.mvp.view.OwnerSaleDataView;
import com.tgf.kcwc.potentialcustomertrack.PotentialCustomerTrackActivity;
import com.tgf.kcwc.see.sale.MsgRecordsActivity;
import com.tgf.kcwc.see.sale.release.ExhibitionApplyActivity;
import com.tgf.kcwc.see.sale.release.OrderPayActivity;
import com.tgf.kcwc.see.sale.release.ReleaseSaleActivity;
import com.tgf.kcwc.see.sale.release.SelectExhibitionPosActivity;
import com.tgf.kcwc.signin.ExhibitionDepositActivity;
import com.tgf.kcwc.signin.ExhibitionPosSignActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.StringUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MorePopupWindow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：Jenny
 * Date:2017/2/27 15:12
 * E-mail：fishloveqin@gmail.com
 */

public class SaleMgrListActivity extends BaseActivity implements OwnerSaleDataView<SaleMgrModel>,CancelApplyView {
    protected TextView mTotalRecords;
    protected TextView mFilterTitle;
    protected ImageView mFilterImg;
    protected ListView mList;
    private RelativeLayout mFilterLayout;
    private ListView mFilterItemList;
    private List<DataItem> mDataItems;
    private WrapAdapter<DataItem> mFilterAdapter;
    private OwnerSalePresenter mPresenter;
    private OwnerSalePresenter mUnShelvePresenter;
    private WrapAdapter<SaleMgrModel.ListBean> mAdapter;
    private String mStatus = "";
    private Button mSaleBtn;

    //按钮
    private ArrayList<BtnDataBean> btnList = new ArrayList<>();
    //下拉按钮集合数据
    List<MorePopupwindowBean> btnRightList = new ArrayList<>();
    //申请id
    private int applyId;
    private LinearLayout linearLayout1;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("车主自售");
    }

    @Override
    protected void setUpViews() {
        initView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_sale_mgr_list_plus);

    }

    private void initView() {
        mPresenter = new OwnerSalePresenter();
        mPresenter.attachView(this);
        mUnShelvePresenter = new OwnerSalePresenter();
        mUnShelvePresenter.attachView(mUnShelveView);
        mFilterLayout = (RelativeLayout) findViewById(R.id.filterLayout);
        mFilterLayout.setOnClickListener(this);
        mTotalRecords = (TextView) findViewById(R.id.totalRecords);
        mSaleBtn = (Button) findViewById(R.id.saleBtn);
        mSaleBtn.setOnClickListener(this);
        mFilterTitle = (TextView) findViewById(R.id.filterTitle);
        mFilterImg = (ImageView) findViewById(R.id.filterImg);
        mList = (ListView) findViewById(R.id.list);
        mFilterItemList = (ListView) findViewById(R.id.filterItemList);
        mFilterItemList.setOnItemClickListener(mFilterItemListener);
        initFilterData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.saleMgrList(mStatus, IOUtils.getToken(mContext));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    private int mSelect = 0;

    private OwnerSaleDataView<List<String>> mUnShelveView = new OwnerSaleDataView<List<String>>() {
        @Override
        public void showData(List<String> s) {

            mPresenter.saleMgrList("",
                    IOUtils.getToken(mContext));

            CommonUtils.showToast(mContext,
                    "下架成功");
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

    private void initFilterData() {
        mDataItems = new ArrayList<DataItem>();
        String[] arrays = mRes.getStringArray(R.array.sale_filter_items);

        int id = 0;
        for (String s : arrays) {
            DataItem dataItem = new DataItem();
            if (s.equals("全部")) {
                dataItem.isSelected = true;
            }
            dataItem.name = s;
            dataItem.id = id;
            mDataItems.add(dataItem);
            id++;
        }
        mFilterAdapter = new WrapAdapter<DataItem>(mContext, R.layout.common_list_item,
                mDataItems) {
            @Override
            public void convert(ViewHolder helper, DataItem item) {

                TextView tv = helper.getView(R.id.title);
                ImageView imageView = helper.getView(R.id.select_status_img);
                if (item.isSelected) {
                    imageView.setVisibility(View.VISIBLE);
                    tv.setTextColor(mRes.getColor(R.color.text_color6));
                } else {
                    imageView.setVisibility(View.GONE);
                    tv.setTextColor(mRes.getColor(R.color.text_color12));
                }
                tv.setText(item.name);
            }
        };
        mFilterItemList.setAdapter(mFilterAdapter);

    }

    private AdapterView.OnItemClickListener mFilterItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            DataItem dataItem = (DataItem) parent.getAdapter().getItem(position);
            dataItem.isSelected = true;
            singleChecked(mDataItems, dataItem);
            mFilterTitle.setText(dataItem.name);
            mFilterLayout.setTag(false);
            mFilterImg.setSelected(false);
            mFilterItemList.setVisibility(View.GONE);
            mFilterAdapter.notifyDataSetChanged();
            switch (position) {
                case 0:
                    mStatus = "";
                    break;
                case 1:
                    mStatus = "1";
                    break;
                case 2:
                    mStatus = "0";
                    break;
                case 3:
                    mStatus = "3";
                    break;
                case 4:
                    mStatus = "4";
                    break;
            }
            mPresenter.saleMgrList(mStatus, IOUtils.getToken(mContext));
        }
    };

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {
            case R.id.filterLayout:

                Object tag = view.getTag();
                boolean flag = false;
                if (tag != null) {
                    flag = Boolean.parseBoolean(tag.toString());
                }
                if (flag) {
                    view.setTag(false);
                    mFilterItemList.setVisibility(View.GONE);
                    mFilterImg.setSelected(false);
                } else {
                    view.setTag(true);
                    mFilterItemList.setVisibility(View.VISIBLE);
                    mFilterImg.setSelected(true);
                }
                break;

            case R.id.saleBtn:
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, -1);
                CommonUtils.startNewActivity(mContext, args, ReleaseSaleActivity.class);
                break;
        }

    }

    @Override
    public void showData(SaleMgrModel saleMgrModel) {

        mTotalRecords.setText(String.format(mRes.getString(R.string.total_records),
                saleMgrModel.pagination.count + ""));
        if (saleMgrModel.list == null) {
            return;
        }
        mAdapter = new WrapAdapter<SaleMgrModel.ListBean>(mContext, R.layout.sale_mgr_list_item_plus,
                saleMgrModel.list) {
            TextView time;
            TextView commentsTv;
            TextView incentiveTv;
            TextView skimTv;
            TextView price;
            TextView moneyTag;
            TextView title;
            SimpleDraweeView img;

            @Override
            public void convert(final ViewHolder helper, final SaleMgrModel.ListBean items) {
                img = helper.getView(R.id.img);
                title = helper.getView(R.id.title);
                price = helper.getView(R.id.price);
                moneyTag = helper.getView(R.id.moneyTag);
                skimTv = helper.getView(R.id.skimTv);
                incentiveTv = helper.getView(R.id.incentiveTv);
                commentsTv = helper.getView(R.id.commentsTv);
                time = helper.getView(R.id.time);
                img.setImageURI(Uri.parse(URLUtil.builderImgUrl(items.cover, 270, 203)));
                title.setText(items.title + "");
                String priceStr = CommonUtils.getMoneyType(items.price + "");
                if (items.price.equals("0")) {
                    priceStr = "面议";
                    moneyTag.setText("");
                } else {
                    moneyTag.setText("￥");
                }
                price.setText(priceStr);
                skimTv.setText(items.viewCount + "");
                commentsTv.setText(items.replyCount + "");
                time.setText(items.createTime + "");
                //清空按钮集合
                btnList.clear();
                //帖子状态
                int shelve = items.isGrounding;
                addBtnList(R.drawable.ic_launcher, "查看联系人");
                if (shelve == 1) {//正常状态
                    addBtnList(R.drawable.ic_launcher, "帖子下架");
                    addBtnList(R.drawable.ic_launcher, "帖子编辑");
                } else {//下架
                    addBtnList(R.drawable.ic_launcher, "重新发布");
//                    addBtnList(R.drawable.ic_launcher, "帖子删除");
                }
                //参展
                final SaleMgrModel.ListBean.Apply applyList = items.apply;
                RelativeLayout exhibitionRl = helper.getView(R.id.exhibitionRl);
                if (null != applyList) {
                    //参展数据
                    exhibitionRl.setVisibility(View.VISIBLE);
                    exhibitionRl.setOnClickListener(new OnSingleClickListener() {
                        @Override
                        protected void onSingleClick(View view) {
                            //我的展会信息
                            Intent intent = new Intent(getContext(), MyExhibitionInfoActivity.class);
                            intent.putExtra(Constants.IntentParams.ID, applyList.id);
                            startActivity(intent);
                        }
                    });
                    TextView exhibitionStatus = helper.getView(R.id.exhibitionStatus);
                    TextView exhibitionName = helper.getView(R.id.exhibitionName);
                    TextView exhibitionPos = helper.getView(R.id.exhibitionPos);
                    TextView exhibitionInterval = helper.getView(R.id.exhibitionInterval);
//                    状态待支付0，待审核1，审核通过2，审核不通过3，打回修改 4，支付后取消5
                    String statusStr = "";
                    int statusDrawable = -1;
                    switch (applyList.status) {
                        case 0:
                            statusStr = "等待支付";
                            statusDrawable = R.drawable.shape_left_bg;
                            addBtnList(R.drawable.ic_launcher, "继续申请");
                            break;
                        case 1:
                            statusStr = "等待审核";
                            statusDrawable = R.drawable.shape_left_bg;
                            break;
                        case 2:
                            statusStr = "审核通过";
                            statusDrawable = R.drawable.shape_left_green_bg;
                            addBtnList(R.drawable.ic_launcher, "取消参展");
                            addBtnList(R.drawable.ic_launcher, "变更展位");
                            addBtnList(R.drawable.ic_launcher, "参展情况");
                            addBtnList(R.drawable.ic_launcher, "查看车证");
                            //0展会未开始，1进行中未进场，2进行中已进场，3进行中（已签到），4进行中已离场，5展会结束
//                            switch (applyList.eventStatus.status) {
//                                case 0:
//                                    addBtnList(R.drawable.ic_launcher, "取消参展");
//                                    //还需要根据 最晚变更时间 来判断
//                                    addBtnList(R.drawable.ic_launcher, "变更展位");
//                                    break;
//                                case 1:
//                                    addBtnList(R.drawable.ic_launcher, "展位签到");
//                                    addBtnList(R.drawable.ic_launcher, "现场投诉");
//                                    break;
//                                case 2:
//                                    addBtnList(R.drawable.ic_launcher, "展位签到");
//                                    addBtnList(R.drawable.ic_launcher, "现场投诉");
//                                    break;
//                                case 3:
//                                    addBtnList(R.drawable.ic_launcher, "现场投诉");
//                                    addBtnList(R.drawable.ic_launcher, "展位打卡");
//                                    break;
//                                case 4:
//                                    addBtnList(R.drawable.ic_launcher, "现场投诉");
//                                    break;
//                                case 5:
//                                    statusStr = "展会结束";
//                                    statusDrawable = R.drawable.shape_left_gray_bg;
//                                    break;
//                            }
                            break;
                        case 3:
                            statusStr = "审核不通过";
                            statusDrawable = R.drawable.shape_left_red_bg;
                            addBtnList(R.drawable.ic_launcher, "重新申请");
                            break;
                        case 4://修改申请 只修改身份证
                            statusStr = "打回修改";
                            statusDrawable = R.drawable.shape_left_red_bg;
                            addBtnList(R.drawable.ic_launcher, "修改申请");
                            break;
                        case 5:
                            break;
                        case 8:
                            statusStr = "申请未提交";
                            statusDrawable = R.drawable.shape_left_bg;
                            addBtnList(R.drawable.ic_launcher, "继续申请");
                            break;
                        case 9:
                            statusStr = "取消参展";
                            statusDrawable = R.drawable.shape_left_bg;
                            addBtnList(R.drawable.ic_launcher, "继续申请");
                            break;
                        case 10:
                            break;
                        case 11:
                            statusStr = "取消参展";
                            statusDrawable = R.drawable.shape_left_bg;
                            addBtnList(R.drawable.ic_launcher, "继续申请");
                            break;
                        default:
                            break;
                    }
                    exhibitionStatus.setText(statusStr);
                    exhibitionStatus.setBackgroundResource(statusDrawable);
                    helper.setText(R.id.exhibitionName, applyList.eventName);
                    helper.setText(R.id.exhibitionPos, applyList.hallName + "/" + applyList.parkName);
                    helper.setText(R.id.exhibitionInterval, applyList.eventStartTime + "/" + applyList.eventEndTime);
                } else {
                    //隐藏参展版块
                    exhibitionRl.setVisibility(View.GONE);
                }
                final ArrayList<BtnDataBean> btnLeftList = new ArrayList<>();
                btnLeftList.clear();
                LinearLayout btnLeft = helper.getView(R.id.btnLeft);
                //删除所有子控件
                btnLeft.removeAllViews();
                //按钮布局+适配器初始化
                if (btnList.size() > 0) {
                    if (btnList.size() < 5) {
                        btnLeftList.addAll(btnList);
                    } else {
                        for (int i = 0; i < 5; i++) {
                            btnLeftList.add(btnList.get(i));
                        }
                    }
                    for (int i = 0; i < btnLeftList.size(); i++) {
                        linearLayout1 = new LinearLayout(getContext());
                        linearLayout1.setOrientation(LinearLayout.VERTICAL);
                        linearLayout1.setGravity(Gravity.CENTER);
                        linearLayout1.setId(i);
                        linearLayout1.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

                        ViewGroup.LayoutParams imageViewLP = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        ViewGroup.LayoutParams textViewLP = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                        ImageView imageView = new ImageView(getContext());
                        imageView.setBackgroundResource(btnLeftList.get(i).imgUrl);
                        imageView.setLayoutParams(imageViewLP);

                        TextView textView = new TextView(getContext());
                        textView.setText(btnLeftList.get(i).nameBtn);
                        textView.setGravity(Gravity.CENTER);
                        textView.setPadding(10, 10, 10, 10);
                        textView.setLayoutParams(textViewLP);

                        linearLayout1.addView(imageView);
                        linearLayout1.addView(textView);

                        linearLayout1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //按钮名
                                String nameStr = btnLeftList.get(view.getId()).nameBtn;
                                Intent intent = null;
                                if (nameStr.equals("帖子下架")) {
                                    mUnShelvePresenter.unShelve(items.id + "", IOUtils.getToken(mContext));
                                } else if (nameStr.equals("帖子编辑")) {
                                    Map<String, Serializable> args = new HashMap<String, Serializable>();
                                    args.put(Constants.IntentParams.ID, items.id);
                                    CommonUtils.startNewActivity(mContext, args, ReleaseSaleActivity.class);
                                } else if (nameStr.equals("查看联系人")) {
                                    Map<String, Serializable> args = new HashMap<String, Serializable>();
                                    args.put(Constants.IntentParams.ID, items.id);
                                    args.put(Constants.IntentParams.TITLE, items.title);
                                    CommonUtils.startNewActivity(mContext, args, MsgRecordsActivity.class);
                                } else if (nameStr.equals("重新发布")) {
                                    Map<String, Serializable> args = new HashMap<String, Serializable>();
                                    args.put(Constants.IntentParams.ID, items.id);
                                    CommonUtils.startNewActivity(mContext, args, ReleaseSaleActivity.class);
                                } else if (nameStr.equals("帖子删除")){
                                    CommonUtils.showToast(getContext(),"正在开发中...");
                                }else if (nameStr.equals("取消参展")) {
                                    // TODO: 2017/11/16 需要弹窗 
                                    CancelApplyPresenter cancelApplyPresenter = new CancelApplyPresenter();
                                    cancelApplyPresenter.attachView(SaleMgrListActivity.this);
                                    cancelApplyPresenter.cancelApply(IOUtils.getToken(getContext()),items.apply.id);
                                } else if (nameStr.equals("变更展位")) {
                                    intent = new Intent(getContext(), SelectExhibitionPosActivity.class);
                                    intent.putExtra(Constants.IntentParams.ID,items.apply.id);
                                    intent.putExtra(Constants.IntentParams.NAME,"变更展位");
                                } else if (nameStr.equals("重新申请")) {
                                    intent = new Intent(getContext(), ExhibitionApplyActivity.class);
                                    intent.putExtra(Constants.IntentParams.ID3,items.apply.id);
                                    intent.putExtra(Constants.IntentParams.ID,1);
                                } else if (nameStr.equals("修改申请")) {
                                    intent = new Intent(getContext(), ExhibitionApplyActivity.class);
                                    intent.putExtra(Constants.IntentParams.ID3,items.apply.id);
                                    intent.putExtra(Constants.IntentParams.ID,2);
                                } else if (nameStr.equals("继续申请")) {
                                    if (items.apply.needPay == 1 ) {//需要支付
                                        //订单支付
                                        intent = new Intent(getContext(), OrderPayActivity.class);
                                        intent.putExtra(Constants.IntentParams.ID,items.apply.orderSn);
                                    }else {
                                        if (StringUtils.checkNull(items.apply.applyName)){
                                            //选择展位
                                            intent = new Intent(getContext(), SelectExhibitionPosActivity.class);
                                            intent.putExtra(Constants.IntentParams.ID,items.apply.id);
                                        }else {
                                            //审核资料
                                            intent = new Intent(getContext(), ExhibitionApplyActivity.class);
                                            //申请id
                                            intent.putExtra(Constants.IntentParams.ID3,items.apply.id);
                                        }
                                    }
                                } else if (nameStr.equals("参展情况")) {
                                    intent = new Intent(getContext(), MyExhibitionInfoActivity.class);
                                    //申请id
                                    intent.putExtra(Constants.IntentParams.ID, applyList.id);
                                } else if (nameStr.equals("查看车证")) {
                                    Map<String, Serializable> args = new HashMap<String, Serializable>();
                                    args.put(Constants.IntentParams.ID, Integer.parseInt(IOUtils.getUserId(mContext)));
                                    CommonUtils.startNewActivity(mContext, args, CertListActivity.class);
                                } else if (nameStr.equals("展位签到")) {
                                    intent = new Intent(getContext(), ExhibitionPosSignActivity.class);
                                    //申请id
                                    intent.putExtra(Constants.IntentParams.ID, applyList.id);
                                } else if (nameStr.equals("现场投诉")) {
                                    intent = new Intent(getContext(), ExhibitionDepositActivity.class);
                                } else if (nameStr.equals("展位打卡")) {
                                    intent = new Intent(getContext(), ExhibitionPosSignActivity.class);
                                    intent.putExtra(Constants.IntentParams.ID, applyId);
                                }
                                if (null != intent && !nameStr.equals("查看车证")) {
                                    startActivity(intent);
                                }
                            }
                        });

                        btnLeft.addView(linearLayout1);
                    }

                    ArrayList<BtnDataBean> btnRightPlusList = new ArrayList<>();
                    //更多按钮
                    final ImageView btnMore = helper.getView(R.id.btnMore);
                    //下拉按钮
                    if (btnList.size() > 5) {
                        for (int i = 5; i < btnList.size(); i++) {
                            btnRightPlusList.add(btnList.get(i));
                        }
                        initPop(btnRightPlusList);
                        btnMore.setVisibility(View.VISIBLE);
                        btnMore.setOnClickListener(new OnSingleClickListener() {
                            @Override
                            protected void onSingleClick(View view) {
                                MorePopupWindow morePopupWindow = new MorePopupWindow(SaleMgrListActivity.this,
                                        btnRightList, new MorePopupWindow.MoreOnClickListener() {
                                    @Override
                                    public void moreOnClickListener(int position, MorePopupwindowBean item) {
                                        //按钮名
                                        String nameStr = item.title;
                                        Intent intent = null;
                                        if (nameStr.equals("帖子下架")) {
                                            mUnShelvePresenter.unShelve(item.id + "", IOUtils.getToken(mContext));
                                        } else if (nameStr.equals("帖子编辑")) {
                                            Map<String, Serializable> args = new HashMap<String, Serializable>();
                                            args.put(Constants.IntentParams.ID, item.id);
                                            CommonUtils.startNewActivity(mContext, args, ReleaseSaleActivity.class);
                                        } else if (nameStr.equals("查看联系人")) {
                                            Map<String, Serializable> args = new HashMap<String, Serializable>();
                                            args.put(Constants.IntentParams.ID, item.id);
                                            args.put(Constants.IntentParams.TITLE, item.title);
                                            CommonUtils.startNewActivity(mContext, args, MsgRecordsActivity.class);
                                        } else if (nameStr.equals("重新发布")) {
                                            Map<String, Serializable> args = new HashMap<String, Serializable>();
                                            args.put(Constants.IntentParams.ID, item.id);
                                            CommonUtils.startNewActivity(mContext, args, ReleaseSaleActivity.class);
                                        } else if (nameStr.equals("帖子删除")){
                                            CommonUtils.showToast(getContext(),"正在开发中...");
                                        }else if (nameStr.equals("取消参展")) {
                                            // TODO: 2017/11/16 需要弹窗
                                            CancelApplyPresenter cancelApplyPresenter = new CancelApplyPresenter();
                                            cancelApplyPresenter.attachView(SaleMgrListActivity.this);
                                            cancelApplyPresenter.cancelApply(IOUtils.getToken(getContext()),items.apply.id);
                                        } else if (nameStr.equals("变更展位")) {
                                            intent = new Intent(getContext(), SelectExhibitionPosActivity.class);
                                            intent.putExtra(Constants.IntentParams.ID,items.apply.id);
                                            intent.putExtra(Constants.IntentParams.NAME,"变更展位");
                                        } else if (nameStr.equals("重新申请")) {
                                            intent = new Intent(getContext(), ExhibitionApplyActivity.class);
                                            intent.putExtra(Constants.IntentParams.ID3,items.apply.id);
                                            intent.putExtra(Constants.IntentParams.ID,1);
                                        } else if (nameStr.equals("修改申请")) {
                                            intent = new Intent(getContext(), ExhibitionApplyActivity.class);
                                            intent.putExtra(Constants.IntentParams.ID3,items.apply.id);
                                            intent.putExtra(Constants.IntentParams.ID,2);
                                        } else if (nameStr.equals("继续申请")) {
                                            if (items.apply.needPay == 1 ) {//需要支付
                                                //订单支付
                                                intent = new Intent(getContext(), OrderPayActivity.class);
                                                intent.putExtra(Constants.IntentParams.ID,items.apply.orderSn);
                                            }else {
                                                if (StringUtils.checkNull(items.apply.applyName)){
                                                    //选择展位
                                                    intent = new Intent(getContext(), SelectExhibitionPosActivity.class);
                                                    intent.putExtra(Constants.IntentParams.ID,items.apply.id);
                                                }else {
                                                    //审核资料
                                                    intent = new Intent(getContext(), ExhibitionApplyActivity.class);
                                                    //申请id
                                                    intent.putExtra(Constants.IntentParams.ID3,items.apply.id);
                                                }
                                            }
                                        } else if (nameStr.equals("参展情况")) {
                                            intent = new Intent(getContext(), MyExhibitionInfoActivity.class);
                                            //申请id
                                            intent.putExtra(Constants.IntentParams.ID, applyList.id);
                                        } else if (nameStr.equals("查看车证")) {
                                            Map<String, Serializable> args = new HashMap<String, Serializable>();
                                            args.put(Constants.IntentParams.ID, Integer.parseInt(IOUtils.getUserId(mContext)));
                                            CommonUtils.startNewActivity(mContext, args, CertListActivity.class);
                                        } else if (nameStr.equals("展位签到")) {
                                            intent = new Intent(getContext(), ExhibitionPosSignActivity.class);
                                            //申请id
                                            intent.putExtra(Constants.IntentParams.ID, applyList.id);
                                        } else if (nameStr.equals("现场投诉")) {
                                            intent = new Intent(getContext(), ExhibitionDepositActivity.class);
                                        } else if (nameStr.equals("展位打卡")) {
                                            intent = new Intent(getContext(), ExhibitionPosSignActivity.class);
                                            intent.putExtra(Constants.IntentParams.ID, applyId);
                                        }
                                        if (null != intent && !nameStr.equals("查看车证")) {
                                            startActivity(intent);
                                        }
                                    }
                                });
                                morePopupWindow.showPopupWindow(btnMore);
                            }
                        });
                    } else {
                        btnMore.setVisibility(View.GONE);
                    }
                }
            }
        };
        mList.setAdapter(mAdapter);
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

    /**
     * 初始化按钮名称及图片
     *
     * @param imgUrl
     * @param nameBtn
     */
    public void addBtnList(int imgUrl, String nameBtn) {
        BtnDataBean btnBean = new BtnDataBean(imgUrl, nameBtn);
        btnList.add(btnBean);
    }

    /**
     * 初始化右边按钮popwindow选项
     *
     * @param btnRightList
     */
    public void initPop(ArrayList<BtnDataBean> btnRightList) {
        //清空
        this.btnRightList.clear();
        for (BtnDataBean btnDataBean : btnRightList) {
            MorePopupwindowBean morePopupwindowBean = null;
            morePopupwindowBean = new MorePopupwindowBean();
            morePopupwindowBean.title = btnDataBean.nameBtn;
            morePopupwindowBean.icon = btnDataBean.imgUrl;
            this.btnRightList.add(morePopupwindowBean);
        }
    }

    @Override
    public void cancelSuccess(String str) {
        mPresenter.saleMgrList(mStatus, IOUtils.getToken(mContext));
        CommonUtils.showToast(getContext(),str);
    }

    @Override
    public void cancelFail(String str) {
        mPresenter.saleMgrList(mStatus, IOUtils.getToken(mContext));
        CommonUtils.showToast(getContext(),str);
    }
}
