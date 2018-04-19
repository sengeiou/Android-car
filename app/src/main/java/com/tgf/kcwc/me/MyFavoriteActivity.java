package com.tgf.kcwc.me;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.coupon.CouponDetailActivity;
import com.tgf.kcwc.driving.driv.DrivingDetailsActivity;
import com.tgf.kcwc.driving.please.PleasePlayDetailsActivity;
import com.tgf.kcwc.mvp.model.MorePopupwindowBean;
import com.tgf.kcwc.mvp.model.MyFavoriteDataModel;
import com.tgf.kcwc.mvp.presenter.UserDataPresenter;
import com.tgf.kcwc.mvp.view.UserDataView;
import com.tgf.kcwc.posting.TopicDetailActivity;
import com.tgf.kcwc.see.CarDetailActivity;
import com.tgf.kcwc.see.SeriesDetailActivity;
import com.tgf.kcwc.see.StoreShowCarDetailActivity;
import com.tgf.kcwc.see.dealer.DealerHomeActivity;
import com.tgf.kcwc.see.evaluate.PopmanESDetailActitivity;
import com.tgf.kcwc.see.exhibition.ExhibitNewsDetailActivity;
import com.tgf.kcwc.see.exhibition.ExhibitionEventDetailActivity;
import com.tgf.kcwc.see.sale.SaleDetailActivity;
import com.tgf.kcwc.tripbook.TripbookDetailActivity;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MorePopupWindow;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:Jenny
 * Date:2017/5/22
 * E-mail:fishloveqin@gmail.com
 * 我的收藏
 */

public class MyFavoriteActivity extends BaseActivity implements UserDataView<MyFavoriteDataModel> {

    protected TextView desc;
    protected SwipeMenuListView list;

    private UserDataPresenter mPresenter;
    private LinearLayout selectlayout;
    private TextView selecttext;
    private UserDataPresenter mDeleteFavoritePresenter;
    private WrapAdapter<MyFavoriteDataModel.FavoriteItem> adapter;
    private List<MyFavoriteDataModel.FavoriteItem> mDatas = new ArrayList<MyFavoriteDataModel.FavoriteItem>();
    private String token = "";
    MorePopupwindowBean selectMorePopupwindowBean = null;
    List<MorePopupwindowBean> dataList = new ArrayList<>();

    public void gainTypeDataList() {
        dataList.clear();
        MorePopupwindowBean morePopupwindowBean = null;
        morePopupwindowBean = new MorePopupwindowBean();
        morePopupwindowBean.title = "全部";
        morePopupwindowBean.type = "";
        dataList.add(morePopupwindowBean);
        morePopupwindowBean = new MorePopupwindowBean();
        morePopupwindowBean.title = "车产品";
        morePopupwindowBean.type = "car";
        dataList.add(morePopupwindowBean);
        morePopupwindowBean = new MorePopupwindowBean();
        morePopupwindowBean.title = "帖子";
        morePopupwindowBean.type = "words";
        dataList.add(morePopupwindowBean);
        morePopupwindowBean = new MorePopupwindowBean();
        morePopupwindowBean.title = "活动";
        morePopupwindowBean.type = "activity";
        dataList.add(morePopupwindowBean);
        morePopupwindowBean = new MorePopupwindowBean();
        morePopupwindowBean.title = "路书";
        morePopupwindowBean.type = "roadbook";
        dataList.add(morePopupwindowBean);
        morePopupwindowBean = new MorePopupwindowBean();
        morePopupwindowBean.title = "资讯";
        morePopupwindowBean.type = "article";
        dataList.add(morePopupwindowBean);
        morePopupwindowBean = new MorePopupwindowBean();
        morePopupwindowBean.title = "评测";
        morePopupwindowBean.type = "evaluate";
        dataList.add(morePopupwindowBean);
        morePopupwindowBean = new MorePopupwindowBean();
        morePopupwindowBean.title = "店铺";
        morePopupwindowBean.type = "organization";
        dataList.add(morePopupwindowBean);
        morePopupwindowBean = new MorePopupwindowBean();
        morePopupwindowBean.title = "代金券";
        morePopupwindowBean.type = "coupon";
        dataList.add(morePopupwindowBean);

        selectMorePopupwindowBean = dataList.get(0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.activity_my_favorite);
        gainTypeDataList();
    }

    @Override
    protected void setUpViews() {

        initView();
        mPresenter = new UserDataPresenter();
        mPresenter.attachView(this);

        mDeleteFavoritePresenter = new UserDataPresenter();
        mDeleteFavoritePresenter.attachView(mDeleteFavoriteView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        token = IOUtils.getToken(mContext);
        mPresenter.getFavoriteData(selectMorePopupwindowBean.type, "", "", token);
    }

    UserDataView<Object> mDeleteFavoriteView = new UserDataView<Object>() {
        @Override
        public Context getContext() {
            return mContext;
        }

        @Override
        public void setLoadingIndicator(boolean active) {

            // showLoadingIndicator(active);
        }

        @Override
        public void showLoadingTasksError() {
            // showLoadingIndicator(false);
        }

        @Override
        public void showDatas(Object o) {


            mPresenter.getFavoriteData("", "", "", token);
        }
    };

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {

        backEvent(back);
        text.setText("我的收藏");
    }

    private void initView() {
        desc = (TextView) findViewById(R.id.desc);
        selectlayout = (LinearLayout) findViewById(R.id.selectlayout);
        selecttext = (TextView) findViewById(R.id.selecttext);

        list = (SwipeMenuListView) findViewById(R.id.list);

        //设置侧滑菜项
        list.setMenuCreator(creator);
        list.setOnMenuItemClickListener(onMenuItemClickListener);
        adapter = new WrapAdapter<MyFavoriteDataModel.FavoriteItem>(mContext, mDatas,
                R.layout.favorite_list_item) {

            protected TextView time;
            protected TextView title;
            protected SimpleDraweeView img;

            @Override
            public void convert(ViewHolder helper, MyFavoriteDataModel.FavoriteItem item) {

                img = helper.getView(R.id.img);
                title = helper.getView(R.id.title);
                time = helper.getView(R.id.time);
                img.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.imgPath, 270, 203)));
                title.setText(item.title);

                long time = DateFormatUtil.getTime(item.addTime);
                Date d = new Date();
                d.setTime(DateFormatUtil.getTime(item.addTime));
                if (DateFormatUtil.comparisonTime(time)) {
                    this.time.setText(item.addTime);
                } else {
                    this.time.setText(DateFormatUtil.timeLogic(d));
                }

            }
        };
        list.setAdapter(adapter);
        list.setOnItemClickListener(mItemClickListener);
        selectlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MorePopupWindow morePopupWindow = new MorePopupWindow(MyFavoriteActivity.this, dataList, new MorePopupWindow.MoreOnClickListener() {
                    @Override
                    public void moreOnClickListener(int position, MorePopupwindowBean bean) {
                        selectMorePopupwindowBean = bean;
                        selecttext.setText(bean.title);
                        mPresenter.getFavoriteData(bean.type, "", "", token);
                    }
                });
                morePopupWindow.showPopupWindow(v);
            }
        });
    }

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            MyFavoriteDataModel.FavoriteItem item = (MyFavoriteDataModel.FavoriteItem) parent.getAdapter().getItem(position);

            String model = item.model;

            Intent intent = new Intent();
            intent.putExtra(Constants.IntentParams.ID, item.sourceId);
            Class classz = MyFavoriteActivity.class;
            switch (model) {

                case Constants.FavoriteTypes.CAR: //车辆详情
                    classz = CarDetailActivity.class;
                    break;
                case Constants.FavoriteTypes.SERIES://车系
                    classz = SeriesDetailActivity.class;
                    intent.putExtra(Constants.IntentParams.NAME, item.title);
                    intent.putExtra(Constants.IntentParams.DATA3,
                            item.title);
                    break;
                case Constants.FavoriteTypes.COUPON: //代金券
                    classz = CouponDetailActivity.class;
                    break;
                case Constants.FavoriteTypes.STORE_CAR://店内车辆
                    classz = StoreShowCarDetailActivity.class;
                    break;
                case Constants.FavoriteTypes.WORDS: //普通帖子
                    classz = TopicDetailActivity.class;
                    intent.putExtra(Constants.IntentParams.ID, item.sourceId + "");
                    break;
                case Constants.FavoriteTypes.EVALUATE: //评测
                    classz = PopmanESDetailActitivity.class;
                    intent.putExtra(Constants.IntentParams.ID, item.sourceId + "");
                    break;
                case Constants.FavoriteTypes.ACTIVITY: //活动
                    classz = ExhibitionEventDetailActivity.class;
                    intent.putExtra(Constants.IntentParams.ID, item.sourceId + "");
                    break;
                case Constants.FavoriteTypes.EVENT: //展会
                    //classz = ExhibitionEventDetailActivity.class;
                    //intent.putExtra(Constants.IntentParams.ID, item.sourceId+"");
                    break;
                case Constants.FavoriteTypes.ORGANIZATION://经销商
                    classz = DealerHomeActivity.class;
                    intent.putExtra(Constants.IntentParams.ID, item.sourceId + "");
                    intent.putExtra(Constants.IntentParams.TITLE, item.title);
                    break;
                case Constants.FavoriteTypes.ARTICLE: //资讯
                    classz = ExhibitNewsDetailActivity.class;
                    intent.putExtra(Constants.IntentParams.ID, item.sourceId + "");
                    break;
                case Constants.FavoriteTypes.ROADBOOK: //路书
                    classz = TripbookDetailActivity.class;
                    intent.putExtra(Constants.IntentParams.ID, item.sourceId);
                    break;
                case Constants.FavoriteTypes.GOODS: //车主自售
                    classz = SaleDetailActivity.class;
                    intent.putExtra(Constants.IntentParams.ID, item.sourceId);
                    break;
                case Constants.FavoriteTypes.PLAY: //请你玩
                    classz = PleasePlayDetailsActivity.class;
                    intent.putExtra(Constants.IntentParams.ID, item.sourceId + "");
                    break;
                case Constants.FavoriteTypes.CYCLE: //开车去
                    classz = DrivingDetailsActivity.class;
                    intent.putExtra(Constants.IntentParams.ID, item.sourceId + "");
                    break;
            }
            intent.setClass(mContext, classz);
            startActivity(intent);
        }
    };

    private SwipeMenuListView.OnMenuItemClickListener onMenuItemClickListener = new SwipeMenuListView.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

            MyFavoriteDataModel.FavoriteItem item = mDatas.get(position);

            Map<String, String> params = new HashMap<String, String>();
            params.put("id", item.id + "");
            params.put("token", token);
            mDeleteFavoritePresenter.deleteFavoriteData(params);

            return true;
        }
    };

    /**
     * 创建侧滑菜单项
     */
    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {

            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            deleteItem.setBackground(new ColorDrawable(
                    Color.rgb(0xF9, 0x3F, 0x25)));
            // set item width
            deleteItem.setWidth(
                    BitmapUtils.dp2px(mContext, 90));
            // set a icon
            deleteItem.setIcon(R.drawable.delete_icon);
            // add to menu
            menu.addMenuItem(deleteItem);
        }
    };


    @Override
    public void showDatas(MyFavoriteDataModel myFavoriteDataModel) {

        String totalNums = String.format(mRes.getString(R.string.total_fav_values),
                myFavoriteDataModel.list.size());

        CommonUtils.customDisplayText(mRes, totalNums, desc, R.color.text_color12);

        mDatas.clear();
        mDatas.addAll(myFavoriteDataModel.list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setLoadingIndicator(boolean active) {

        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

        showLoadingIndicator(false);
    }

    @Override
    public Context getContext() {
        return mContext;
    }


}
