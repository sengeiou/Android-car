package com.tgf.kcwc.seek;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.CommonAdapter;
import com.tgf.kcwc.adapter.ViewHolder;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.coupon.CouponDetailActivity;
import com.tgf.kcwc.driving.driv.DrivingDetailsActivity;
import com.tgf.kcwc.driving.driv.ListviewHint;
import com.tgf.kcwc.driving.please.PleasePlayDetailsActivity;
import com.tgf.kcwc.mvp.model.SeekDetailsBean;
import com.tgf.kcwc.mvp.model.SeekTypeBean;
import com.tgf.kcwc.mvp.presenter.SeekDetailsPresenter;
import com.tgf.kcwc.mvp.view.SeekDetailsView;
import com.tgf.kcwc.posting.TopicDetailActivity;
import com.tgf.kcwc.see.SeriesDetailActivity;
import com.tgf.kcwc.see.sale.SaleDetailActivity;
import com.tgf.kcwc.tripbook.TripbookDetailActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SpannableUtil;
import com.tgf.kcwc.util.StringUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.SeekDetailsUpPopupWindow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by Administrator on 2017/5/22.
 */

public class SeekDetailsActivity extends BaseActivity implements SeekDetailsView {

    private SeekDetailsPresenter mSeekDetailsPresenter;
    private CommonAdapter<SeekTypeBean> mType;                                       //头部
    private List<SeekTypeBean> mTypeList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ListView mListView;
    private EditText etSearch;
    private TextView number;
    private ImageView pulldown;
    private int page = 1;
    boolean isRefresh = true;
    private ListviewHint mListviewHint;                         //尾部
    private String name;
    private  SeekDetailsBean mSeekBean = null;//数据源
    private  SeekTypeBean mSeekTypeBean = null; //选择
    private WrapAdapter<SeekDetailsBean.DataList> mAdapter;
    public List<SeekDetailsBean.DataList> dataList = new ArrayList<>();
    private   SeekDetailsUpPopupWindow mDropUpPopupWindow;
    private TextView back;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSeekDetailsPresenter!=null){
            mSeekDetailsPresenter.detachView();
        }
    }

    public void setTypeDate() {
        mTypeList.clear();
        setSeekTypeBean("找车", "car", "", true, 0);
        setSeekTypeBean("车友", "thread", "words", false, 1);
        setSeekTypeBean("开车去", "thread", "cycle", false, 2);
        setSeekTypeBean("请你玩", "thread", "play", false, 3);
        setSeekTypeBean("路书", "thread", "roadbook", false, 4);
        setSeekTypeBean("代金券", "coupon", "", false, 5);
        setSeekTypeBean("优惠", "privilege", "", false, 6);
        setSeekTypeBean("车主自售", "thread", "goods", false, 7);
        setSeekTypeBean("评测", "thread", "evaluate", false, 8);
        setSeekTypeBean("资讯", "article", "", false, 9);
        setSeekTypeBean("群组", "group", "", false, 10);
        setSeekTypeBean("用户", "user", "", false, 11);
    }

    public void setSeekTypeBean(String name, String type, String threadModel, boolean isClick, int num) {
        SeekTypeBean seekTypeBean = null;
        seekTypeBean = new SeekTypeBean();
        seekTypeBean.name = name;
        seekTypeBean.type = type;
        seekTypeBean.threadModel = threadModel;
        seekTypeBean.isClick = isClick;
        seekTypeBean.number = num;
        mTypeList.add(seekTypeBean);
        if (mSeekTypeBean == null) {
            mSeekTypeBean = seekTypeBean;
        }
    }

    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seekdetail);
        name = getIntent().getStringExtra("name");
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mListView = (ListView) findViewById(R.id.listview);
        etSearch = (EditText) findViewById(R.id.et_search);
        number = (TextView) findViewById(R.id.number);
        pulldown = (ImageView) findViewById(R.id.pulldown);
        back = (TextView) findViewById(R.id.back);
        initRefreshLayout(mBGDelegate);
        mListviewHint = new ListviewHint(mContext);

        mSeekDetailsPresenter = new SeekDetailsPresenter();
        mSeekDetailsPresenter.attachView(this);

        // 调整EditText右边边的搜索按钮的大小
        Drawable drawable = getResources().getDrawable(R.drawable.search_btn_selector);
        drawable.setBounds(0, 0, 60, 60);// 第一0是距左边距离，第二0是距上边距离，60分别是长宽
        etSearch.setCompoundDrawables(null, null, drawable, null);// 只放右边

        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        setTypeDate();
        setadapter();
        setListener();
        mSeekDetailsPresenter.getsDispatchList(IOUtils.getToken(mContext), mSeekTypeBean.type, mSeekTypeBean.threadModel, name, page);
        setLoadingIndicator(true);

        pulldown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDropUpPopupWindow = new SeekDetailsUpPopupWindow(mContext, mTypeList);
                mDropUpPopupWindow.showAsDropDownBelwBtnView(etSearch);
                mDropUpPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        SeekTypeBean selectDataItem = mDropUpPopupWindow.getSelectDataItem();
                        if (selectDataItem != null && mDropUpPopupWindow.getIsSelec()) {
                            mSeekTypeBean = selectDataItem;
                            gainData();
                        }
                    }
                });

            }
        });
    }

    public void setListener() {
        etSearch.setText(name);

        // 搜索框的键盘搜索键点击回调
        etSearch.setOnKeyListener(new View.OnKeyListener() {// 输入完后按键盘上的搜索键

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {// 修改回车键功能
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    String tempName = etSearch.getText().toString().trim();
                    if (StringUtils.checkNull(tempName)) {
                        gainData();
                    } else {
                        Toast.makeText(mContext, "请输入搜索关键字", Toast.LENGTH_SHORT).show();
                    }
                    // TODO 根据输入的内容模糊查询商品，并跳转到另一个界面，由你自己去实现
                    Toast.makeText(mContext, "clicked!" + tempName, Toast.LENGTH_SHORT).show();

                }
                return false;
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setadapter() {
        mType = new CommonAdapter<SeekTypeBean>(mContext, R.layout.typeselect_item, mTypeList) {
            @Override
            public void convert(ViewHolder helper, SeekTypeBean item) {
                final int position = helper.getPosition();
                LinearLayout selectlayout = helper.getView(R.id.selectlayout);
                TextView name = helper.getView(R.id.name);
                TextView select = helper.getView(R.id.select);
                name.setText(item.name);

                if (item.isClick) {
                    select.setVisibility(View.VISIBLE);
                } else {
                    select.setVisibility(View.GONE);
                }

                selectlayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mTypeList.get(position).isClick) {

                        } else {
                            for (SeekTypeBean bean : mTypeList) {
                                bean.isClick = false;
                            }
                            mTypeList.get(position).isClick = true;
                            mSeekTypeBean = mTypeList.get(position);
                            gainData();
                        }
                    }
                });

            }
        };
        mRecyclerView.setAdapter(mType);
        mType.notifyDataSetChanged();
    }

    public void gainData() {
        if (etSearch.getText().toString().trim() != null && !etSearch.getText().toString().trim().equals("")) {
            mType.notifyDataSetChanged();
            page = 1;
            isRefresh = true;
            mSeekDetailsPresenter.getsDispatchList(IOUtils.getToken(mContext), mSeekTypeBean.type, mSeekTypeBean.threadModel, etSearch.getText().toString().trim(), page);
            setLoadingIndicator(true);
        } else {
            CommonUtils.showToast(mContext, "请输入搜索的关键字");
        }
    }

    @Override
    public void dataListSucceed(SeekDetailsBean seekBean) {
        stopRefreshAll();
        setLoadingIndicator(false);
        mSeekBean = seekBean;
        if (page == 1) {
            dataList.clear();
            dataList.addAll(seekBean.data.list);
            setWordsAdapter();
        } else {
            dataList.addAll(seekBean.data.list);
            mAdapter.notifyDataSetChanged();
        }
        if (seekBean.data.list != null) {
            if (seekBean.data.list.size() != 0) {
                isRefresh = true;
            } else {
                isRefresh = false;
                if (page > 1) {
                    mListView.addFooterView(mListviewHint);
                }
                CommonUtils.showToast(mContext, "暂时没有数据");
            }
        } else {
            isRefresh = false;
            if (page > 1) {
                mListView.addFooterView(mListviewHint);
            }
            CommonUtils.showToast(mContext, "暂时没有数据");
        }

    }

    @Override
    public void dataListDefeated(String msg) {
        CommonUtils.showToast(mContext, msg);
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
        return mContext;
    }


    BGARefreshLayout.BGARefreshLayoutDelegate mBGDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {

        @Override
        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            // 在这里加载最新数据

            //            if (HttpUtils.isConnectNetwork(getApplicationContext())) {
            //                // 如果网络可用，则加载网络数据
            //
            //                loadMore();
            //            } else {
            //                // 网络不可用，结束下拉刷新
            //                HttpUtils.registerNWReceiver(getApplicationContext());
            //                mRefreshLayout.endRefreshing();
            //            }
            // mPageIndex = 1;
            page = 1;
            isRefresh = true;
            mListView.removeFooterView(mListviewHint);
            loadMore();
        }

        @Override
        public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
            // 在这里加载更多数据，或者更具产品需求实现上拉刷新也可以

            //            if (HttpUtils.isConnectNetwork(getApplicationContext())) {
            //                loadMore();
            //                return true;
            //            } else {
            //                // 网络不可用，返回false，不显示正在加载更多
            //                HttpUtils.registerNWReceiver(getApplicationContext());
            //                mRefreshLayout.endRefreshing();
            //                return false;
            //            }
            //loadMore();
            if (isRefresh) {
                page++;
                loadMore();
            }
            return false;
        }
    };

    public void loadMore() {
        mSeekDetailsPresenter.getsDispatchList(IOUtils.getToken(mContext), mSeekTypeBean.type, mSeekTypeBean.threadModel, name, page);
    }


    /**
     * 车友的adapter
     */
    public void setWordsAdapter() {
        number.setText(mSeekBean.data.pagination.count + "");
        mAdapter = null;
        switch (mSeekTypeBean.number) {
            case 0:
                //找车
                mAdapter = new WrapAdapter<SeekDetailsBean.DataList>(mContext, R.layout.seekdetails_car_item, dataList) {
                    @Override
                    public void convert(ViewHolder helper, final SeekDetailsBean.DataList item) {
                        RelativeLayout cartotal = helper.getView(R.id.cartotal);
                        RelativeLayout organization = helper.getView(R.id.organization);
                        RelativeLayout threadgoods = helper.getView(R.id.threadgoods);
                        TextView cartotalname = helper.getView(R.id.cartotalname);
                        TextView organizationname = helper.getView(R.id.organizationname);
                        TextView threadgoodsname = helper.getView(R.id.threadgoodsname);
                        TextView namic = helper.getView(R.id.namic);
                        TextView cartype = helper.getView(R.id.cartype);
                        TextView price = helper.getView(R.id.price);
                        SimpleDraweeView Thumbnail = helper.getView(R.id.thumbnail);
                        LinearLayout itemselect = helper.getView(R.id.itemselect);
                        namic.setText(item.name);
                        price.setText("￥" + item.referencePriceMin + " - " + "￥" + item.referencePriceMax);
                        cartype.setText(item.carLevel);
                        Thumbnail.setImageURI(URLUtil.builderImgUrl(item.coverOut, 144, 144));
                        itemselect.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Map<String, Serializable> args  = new HashMap<String, Serializable>();
                                args.put(Constants.IntentParams.ID, item.id);
                                CommonUtils.startNewActivity(mContext, args, SeriesDetailActivity.class);
                            }
                        });
                        if (item.carTotal != 0) {
                            cartotalname.setText(item.carTotal + "款标准车型");
                            cartotal.setVisibility(View.VISIBLE);
                            cartotal.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Map<String, Serializable> args  = new HashMap<String, Serializable>();
                                    args.put(Constants.IntentParams.ID, item.id);
                                    CommonUtils.startNewActivity(mContext, args, SeriesDetailActivity.class);
                                   // CommonUtils.showToast(mContext, "跳转车系详情");
                                }
                            });
                        } else {
                            cartotal.setVisibility(View.GONE);
                        }
                        if (item.organizationCarTotal != 0) {
                            organizationname.setText(item.organizationCarTotal + "款店内展车");
                            organization.setVisibility(View.VISIBLE);
                            organization.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Map<String, Serializable> args  = new HashMap<String, Serializable>();
                                    args.put(Constants.IntentParams.ID, item.id);
                                    CommonUtils.startNewActivity(mContext, args, SeriesDetailActivity.class);
                                  //  CommonUtils.showToast(mContext, "跳转店内看列表");
                                }
                            });
                        } else {
                            organization.setVisibility(View.GONE);
                        }
                        if (item.threadGoodsTotal != 0) {
                            threadgoodsname.setText(item.threadGoodsTotal + "条车主自售信息");
                            threadgoods.setVisibility(View.VISIBLE);
                            threadgoods.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Map<String, Serializable> args  = new HashMap<String, Serializable>();
                                    args.put(Constants.IntentParams.ID, item.id);
                                    CommonUtils.startNewActivity(mContext, args, SeriesDetailActivity.class);
                                  //  CommonUtils.showToast(mContext, "跳转车主自售列表");
                                }
                            });
                        } else {
                            threadgoods.setVisibility(View.GONE);
                        }

                    }
                };
                break;
            case 1:
                //车友的adapter
            case 2:
                //开车去
            case 3:
                //请你玩
            case 4:
                //路书
            case 6:
                //优惠
            case 9:
                //资讯
                mAdapter = new WrapAdapter<SeekDetailsBean.DataList>(mContext, R.layout.seekdetail_words_item, dataList) {
                    @Override
                    public void convert(ViewHolder helper, final SeekDetailsBean.DataList item) {
                        SimpleDraweeView thumbnail = helper.getView(R.id.thumbnail);
                        TextView mTitle = helper.getView(R.id.driving_list_view_dynamic);
                        TextView mLook = helper.getView(R.id.look);
                        TextView mLike = helper.getView(R.id.like);
                        TextView mInformation = helper.getView(R.id.information);
                        TextView time = helper.getView(R.id.time);
                        RelativeLayout thumbnaillayout = helper.getView(R.id.thumbnaillayout);
                        ImageView thumbnailjinh = helper.getView(R.id.thumbnailjinh);
                        LinearLayout itemselect = helper.getView(R.id.itemselect);

                        mTitle.setText(item.title);
                        mLook.setText(item.viewCount + "");
                        mLike.setText(item.likeCount + "");
                        mInformation.setText(item.replyCount + "");
                        //time.setText(DateFormatUtil.timeLogic(item.createTime));
                        time.setText(item.createTime);
                        thumbnail.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 360, 360)));//item图片

                        itemselect.setOnClickListener(new View.OnClickListener() {
                            Map<String, Serializable> args = null;

                            @Override
                            public void onClick(View v) {
                                if (item.model.equals("goods")) { //车主自售
                                    args = new HashMap<String, Serializable>();
                                    args.put(Constants.IntentParams.ID, item.id);
                                    CommonUtils.startNewActivity(mContext, args, SaleDetailActivity.class);
                                } else if (item.model.equals("play")) { //请你玩
                                    args = new HashMap<String, Serializable>();
                                    args.put("id", item.id + "");
                                    CommonUtils.startNewActivity(mContext, args, PleasePlayDetailsActivity.class);
                                } else if (item.model.equals("cycle")) { //开车去
                                    args = new HashMap<String, Serializable>();
                                    args.put("id", item.id + "");
                                    CommonUtils.startNewActivity(mContext, args, DrivingDetailsActivity.class);
                                } else if (item.model.equals("words")) {//普通帖子
                                    args = new HashMap<String, Serializable>();
                                    args.put(Constants.IntentParams.ID, item.id + "");
                                    CommonUtils.startNewActivity(mContext, args, TopicDetailActivity.class);
                                } else if (item.model.equals("evaluate")) { //达人评测
                                    args.put(Constants.IntentParams.ID, item.id + "");
                                    CommonUtils.startNewActivity(mContext, args, TopicDetailActivity.class);
                                } else if (item.model.equals("roadbook")) {//路书
                                    args = new HashMap<String, Serializable>();
                                    args.put(Constants.IntentParams.ID, item.id);
                                    CommonUtils.startNewActivity(mContext, args, TripbookDetailActivity.class);
                                } else {
                                    CommonUtils.showToast(mContext, "正在开发中");
                                }
                            }
                        });
                    }
                };
                break;
            case 5:
                //代金券
                mAdapter = new WrapAdapter<SeekDetailsBean.DataList>(mContext, R.layout.listview_item_recomment_voucherlist, dataList) {
                    @Override
                    public void convert(ViewHolder holder, final SeekDetailsBean.DataList voucher) {
                        LinearLayout select = holder.getView(R.id.select);
                        TextView titleTv = holder.getView(R.id.listitem_recoment_coupontitle);
                        titleTv.setText(voucher.title);
                        holder.setSimpleDraweeViewURL(R.id.couponlist_cover, URLUtil.builderImgUrl(voucher.cover, 270, 203));
                        holder.setText(R.id.recyleitem_near_nowprice, "￥ " + voucher.price);
                        SpannableString demoPrice = SpannableUtil.getDelLineString("￥ " + voucher.denomination);
                        holder.setText(R.id.listviewitem_recomment_oldprice, demoPrice);
                        holder.setText(R.id.listviewitem_recomment_salenum, "已售" + voucher.sales);
                        select.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent toIntent = new Intent(mContext, CouponDetailActivity.class);
                                toIntent.putExtra(Constants.IntentParams.ID, voucher.id);
                                startActivity(toIntent);
                            }
                        });
                    }
                };
                break;
            case 7:
                //车主自售
                mAdapter = new WrapAdapter<SeekDetailsBean.DataList>(mContext, R.layout.seekdetails_owner_item, dataList) {
                    @Override
                    public void convert(ViewHolder helper, final SeekDetailsBean.DataList item) {
                        RelativeLayout selse = helper.getView(R.id.selse);
                        TextView title1 = helper.getView(R.id.title1);
                        title1.setText(item.title);
                        SimpleDraweeView img = helper.getView(R.id.img);
                        img.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.cover, 270, 203)));
                        TextView content1 = helper.getView(R.id.content1);
                        content1.setText(CommonUtils.getMoneyType(item.price + ""));
                        TextView content2 = helper.getView(R.id.content3);
                        content2.setText(item.buyYear + "  |  " + item.roadHaul + "公里");
                        /*Date date = new Date();
                        date.setTime(DateFormatUtil.getTime(item.createTime));
                        content3.setText(DateFormatUtil.timeLogic(date));*/
                        TextView area = helper.getView(R.id.area);
                        area.setText(item.area);

                        selse.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Map<String, Serializable> args = new HashMap<String, Serializable>();
                                args.put(Constants.IntentParams.ID, item.id);
                                CommonUtils.startNewActivity(mContext, args, SaleDetailActivity.class);
                            }
                        });

                    }
                };
                break;
            case 8:
                //测评
                mAdapter = new WrapAdapter<SeekDetailsBean.DataList>(mContext, R.layout.listview_item_popmanes, dataList) {
                    @Override
                    public void convert(ViewHolder helper, final SeekDetailsBean.DataList item) {
                        helper.setText(R.id.popmanes_title, item.title);
                        helper.setText(R.id.listviewitem_visitors, item.viewCount + "");
                        helper.setText(R.id.listviewitem_focuson, item.likeCount + "");
                        helper.setText(R.id.listviewitem_comment, item.replyCount + "");
                        String dateAgo = DateFormatUtil.getBeforeDay(item.createTime);
                        helper.setText(R.id.listviewitem_date, dateAgo);
                    }
                };
                break;
            case 10:
                //群组

                mAdapter = new WrapAdapter<SeekDetailsBean.DataList>(mContext, R.layout.seek_groupitem, dataList) {
                    @Override
                    public void convert(ViewHolder helper, final SeekDetailsBean.DataList item) {

                    }
                };
                break;
            case 11:
                //用户
                mAdapter = new WrapAdapter<SeekDetailsBean.DataList>(mContext, R.layout.seekdetails_user_item, dataList) {
                    @Override
                    public void convert(ViewHolder helper, final SeekDetailsBean.DataList item) {
                        SimpleDraweeView mSimpleDraweeView = helper.getView(R.id.motodetail_avatar_iv);
                        TextView nametv = helper.getView(R.id.nametv);
                        TextView invitation = helper.getView(R.id.invitation); //帖子
                        TextView fans = helper.getView(R.id.fans); //粉丝
                        TextView attention = helper.getView(R.id.attention); //关注
                        ImageView modelImg = helper.getView(R.id.comment_model_tv);
                        ImageView popmanImg = helper.getView(R.id.comment_popman_tv);
                        SimpleDraweeView mbrandLogo = helper.getView(R.id.brandLogo);
                        mSimpleDraweeView
                                .setImageURI(URLUtil.builderImgUrl(item.avatar, 144, 144));
                        invitation.setText(item.threadNum + "");
                        fans.setText(item.fansNum + "");
                        attention.setText(item.followNum + "");
                        nametv.setText(item.nickname);

                    }
                };
                break;
        }

        mListView.setAdapter(mAdapter);
    }

}
