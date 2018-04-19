package com.tgf.kcwc.see.exhibition.plus;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.MyPagerAdapter;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.Beauty;
import com.tgf.kcwc.mvp.model.Brand;
import com.tgf.kcwc.mvp.model.ExhibitEvent;
import com.tgf.kcwc.mvp.model.ExhibitPlace;
import com.tgf.kcwc.mvp.model.NewCarBean;
import com.tgf.kcwc.mvp.presenter.AttentionDataPresenter;
import com.tgf.kcwc.mvp.presenter.ExhibitionPlaceDetailPresenter;
import com.tgf.kcwc.mvp.view.AttentionView;
import com.tgf.kcwc.mvp.view.ExhibitPlaceDetailView;
import com.tgf.kcwc.see.BigPhotoPageActivity;
import com.tgf.kcwc.see.BrandModelsActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Auther: Scott
 * Date: 2017/1/3 0003
 * E-mail:hekescott@qq.com
 * 展馆页
 */

public class ExhibitPlaceDetailActivity extends BaseActivity implements ExhibitPlaceDetailView, AttentionView {

    private GridView brandsGv;
    private WrapAdapter<Brand> brandWrapAdapter;
//    private Items mItems = new Items();
//    private WrapAdapter mAdapter;
//    private ListView mListViewNewcar;
//    private GridView beautysGv;
//    private WrapAdapter beautyAdapter;
//    private NestFullListView eventLv;
//    private NestFullListViewAdapter eventAdapter;
    private TextView exhibitPlaceTv;
    private TextView exhibitPlaceDescTv;
    private SimpleDraweeView headImg;

    private PagerSlidingTabStrip mTab;
    private ViewPager mPager;

    private ExhibitionPlaceDetailPresenter exhibitionPlaceDetailPresenter;
    private Intent fromIntent;
    private int hallId;
    private int exhibitId;
    private TextView mText;
    private AttentionDataPresenter mAttentionPresenter;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        this.mText = text;
        backEvent(back);
    }

    @Override
    protected void setUpViews() {
        mAttentionPresenter = new AttentionDataPresenter();
        mAttentionPresenter.attachView(this);
        exhibitId = fromIntent.getIntExtra(Constants.IntentParams.ID, 2);
        hallId = fromIntent.getIntExtra(Constants.IntentParams.ID2, 4);
        exhibitionPlaceDetailPresenter = new ExhibitionPlaceDetailPresenter();
        exhibitionPlaceDetailPresenter.attachView(this);
//        findViewById(R.id.placedetail_gotonewcarrl).setOnClickListener(this);
//        findViewById(R.id.palcedetail_activityTitle).setOnClickListener(this);
        exhibitionPlaceDetailPresenter.getExhibitionPalaceDetail(exhibitId, hallId);
        brandsGv = (GridView) findViewById(R.id.palcedeatail_brands_gv);
//        beautysGv = (GridView) findViewById(R.id.palcedetail_beauty_gv);
//        mListViewNewcar = (ListView) findViewById(R.id.exhibitplacedetail_newcar_rc);
//        eventLv = (NestFullListView) findViewById(R.id.palcedeatail_envent_lv);
        exhibitPlaceTv = (TextView) findViewById(R.id.exhibitplace_name);
        exhibitPlaceDescTv = (TextView) findViewById(R.id.exhibitplace_desc);
        headImg = (SimpleDraweeView) findViewById(R.id.exhibitplace_headiv);
//        findViewById(R.id.palcedetail_modeTitle).setOnClickListener(this);

        mTab = (PagerSlidingTabStrip) findViewById(R.id.pager_tab);
        mPager = (ViewPager) findViewById(R.id.pager);
//        List<BaseFragment> fragments = new ArrayList<BaseFragment>();
//        fragments.add(new NewCarFragment());
//        fragments.add(new SeeModelFragment());
//        fragments.add(new ExhibitionCarFragment());
//        fragments.add(new OwnerSaleFragment());
//        fragments.add(new ExhibitionEventFragment());
//        fragments.add(new ExhibitionSceneFragment());
//        MyPagerAdapter mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments,
//                getResources().getStringArray(R.array.car_tabs));
//        mPager.setAdapter(mPagerAdapter);
//        mTab.setViewPager(mPager);
//        mPager.setCurrentItem(0);
//        setTabsValue();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        fromIntent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibit_palcedetail_plus);

    }
    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        mTab.setShouldExpand(false);

        // 设置Tab的分割线的颜色
        // mTabs.setDividerColor(getResources().getColor(R.color.color_80cbc4));
        // 设置分割线的上下的间距,传入的是dp
        mTab.setDividerPaddingTopBottom(12);
        //左右间距
        mTab.setTabPaddingLeftRight(16);
        // 设置Tab底部线的高度,传入的是dp
        mTab.setUnderlineHeight(1);
        //设置Tab底部线的颜色
        mTab.setUnderlineColor(getResources().getColor(R.color.split_line_color));

        // 设置Tab 指示器Indicator的高度,传入的是dp
        mTab.setIndicatorHeight(2);
        // 设置Tab Indicator的颜色
        mTab.setIndicatorColorResource(R.color.customer_detail_bottom);
        // 设置Tab标题文字的大小,传入的是dp
        mTab.setTextSize(12);
        // 设置选中Tab文字的颜色
        mTab.setSelectedTextColorResource(R.color.customer_detail_bottom);
        //设置正常Tab文字的颜色
        mTab.setTextColorResource(R.color.text_color15);
    }
    @Override
    public void showHeadImg(final ExhibitPlace exhibitPlace) {
        exhibitPlaceTv.setText(exhibitPlace.name);
        mText.setText(exhibitPlace.name);
        exhibitPlaceDescTv.setText(exhibitPlace.description);
        if (!TextUtils.isEmpty(exhibitPlace.headimg)) {
            headImg.setImageURI(Uri.parse(URLUtil.builderImgUrl750(exhibitPlace.headimg)));
            headImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), BigPhotoPageActivity.class);
                    intent.putExtra(Constants.KEY_IMG, URLUtil.builderImgUrl750(exhibitPlace.headimg));
                    startActivity(intent);
                }
            });
        }
        showBrandlist(exhibitPlace.includeBrandlist);
        showNewMotolist(exhibitPlace.newMotolist);
        showModellist(exhibitPlace.beautylist);
        showEventlist(exhibitPlace.eventlist);
        showList(exhibitPlace.newMotolist,exhibitPlace.beautylist,exhibitPlace.eventlist);
    }

    @Override
    public void showNewMotolist(final ArrayList<NewCarBean> newMotoArrayList) {
//        for (int i = 0; i < newMotoArrayList.size(); i++) {
//            NewCarBean moto = newMotoArrayList.get(i);
//            mItems.add(moto);
//        }
//        mAdapter = new WrapAdapter<NewCarBean>(getContext(), newMotoArrayList, R.layout.new_car_exhibitplace_item) {
//            @Override
//            public void convert(final ViewHolder holder, NewCarBean moto) {
//                holder.setText(R.id.name, moto.brandName);
//                SimpleDraweeView draweeView = holder.getView(R.id.img);
//                draweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl(moto.brandLogo, 144, 144)));
//                holder.setText(R.id.releaseTime, "发布时间: " + DateFormatUtil.formatTime1(moto.releaseTime));
//
//                holder.setText(R.id.carname, moto.productName + "");
//                holder.setText(R.id.carprice, "指导价: " + moto.brandPrice);
//                GridView gridView = holder.getView(R.id.grid);
//                gridView.setAdapter(new WrapAdapter<Image>(holder.getConvertView().getContext(), moto.imgs,
//                        R.layout.new_car_grid_item) {
//                    @Override
//                    public void convert(ViewHolder helper, Image item) {
//
//                        SimpleDraweeView draweeView = helper.getView(R.id.img);
//                        draweeView.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.link, 270, 203)));
//
//                    }
//                });
//                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Intent toNecarAct = new Intent(mContext, NewCarGalleryActivity.class);
//                        toNecarAct.putExtra(Constants.IntentParams.ID, newMotoArrayList.get(holder.getPosition()).id);
//                        startActivity(toNecarAct);
//                    }
//                });
//                ViewUtil.setListViewHeightBasedOnChildren(gridView, 3);
//            }
//        };
//        mListViewNewcar.setAdapter(mAdapter);
//        ViewUtil.setListViewHeightBasedOnChildren(mListViewNewcar);

    }

    @Override
    public void showBrandlist(final ArrayList<Brand> includeBrandlist) {
        if (includeBrandlist != null && includeBrandlist.size() != 0) {
            brandWrapAdapter = new WrapAdapter<Brand>(mContext, includeBrandlist,
                    R.layout.griditem_exhibitplace_deatail) {
                @Override
                public void convert(ViewHolder helper, Brand item) {
                    helper.setSimpleDraweeViewURL(R.id.exhbitplace_brandlogo, URLUtil.builderImgUrl(item.brandLogo, 144, 144));
                }
            };
            brandsGv.setAdapter(brandWrapAdapter);
            ViewUtil.setListViewHeightBasedOnChildren(brandsGv, 3);
            brandsGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //参展品牌
                    Intent toBranAce = new Intent(getContext(), BrandExhibitionCarActivity.class);
                    toBranAce.putExtra(Constants.IntentParams.ID2, includeBrandlist.get(position).brandId);
                    toBranAce.putExtra(Constants.IntentParams.ID, exhibitId);
                    startActivity(toBranAce);
                }
            });

        }
    }


    @Override
    public void showModellist(ArrayList<Beauty> beautylist) {
//        if (beautylist != null && beautylist.size() != 0) {
//            beautyAdapter = new WrapAdapter<Beauty>(mContext, beautylist,
//                    R.layout.gridviewitem_exhibitplacedetail_beauty) {
//                @Override
//                public void convert(ViewHolder helper, final Beauty item) {
//                    helper.setText(R.id.beauty_name, item.name);
//                    SimpleDraweeView avatarCoverIv = helper.getView(R.id.recyleitme_beauty_cover);
//                    String coverUrl = URLUtil.builderImgUrl(item.cover, 360, 360);
//                    avatarCoverIv.setImageURI(Uri.parse(coverUrl));
//                    avatarCoverIv.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(getContext(), ModelAlbumActivity.class);
//                            intent.putExtra(Constants.IntentParams.ID, item.modelId);
//                            getContext().startActivity(intent);
//                        }
//                    });
//                    String avatarUrl = URLUtil.builderImgUrl(item.avatar, 144, 144);
//
//                    helper.setSimpleDraweeViewURL(R.id.avatarbadge_avatar, avatarUrl);
//                    final ImageView genderIv = helper.getView(R.id.avatarbadge_gender);
//                    if (item.sex == 1) {
//                        genderIv.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_men));
//                    } else {
//                        genderIv.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_women));
//                    }
//                    final TextView addattentionTv = helper.getView(R.id.add_attentiontv);
//                    addattentionTv.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            item.isFollow++;
//                            if (IOUtils.isLogin(getContext())) {
//                                mAttentionPresenter.execAttention(item.modelId + "",
//                                        IOUtils.getToken(getContext()));
//                            }
//                            addattentionTv.setText("粉丝 " + item.isFollow);
//                            addattentionTv.setTextColor(getContext().getResources().getColor(R.color.text_color3));
//                        }
//                    });
//
//                }
//            };
//
//            beautysGv.setAdapter(beautyAdapter);
//            ViewUtil.setListViewHeightBasedOnChildren(beautysGv, 3);
//        }

    }

    @Override
    public void showEventlist(final ArrayList<ExhibitEvent> eventList) {
//        if (eventList != null && eventList.size() != 0) {
//            eventAdapter = new NestFullListViewAdapter<ExhibitEvent>(R.layout.listviewitem_exhibitplace_event,
//                    eventList) {
//                @Override
//                public void onBind(int pos, ExhibitEvent exhibitEvent, NestFullViewHolder holder) {
//                    holder.setText(R.id.event_title, exhibitEvent.title);
//                    if (DateFormatUtil.getTime(exhibitEvent.endTime) < System.currentTimeMillis()) {
//                        holder.setText(R.id.listviewitem_visitors, "已结束");
//                    } else {
//                        holder.setText(R.id.listviewitem_visitors, "开始时间:  " + exhibitEvent.startTime);
//                    }
//                    String activtyCoverUrl = URLUtil.builderImgUrl(exhibitEvent.cover, 360, 360);
//                    holder.setSimpleDraweeViewURL(R.id.listitemt_activtyiv, activtyCoverUrl);
//                }
//            };
//            eventLv.setAdapter(eventAdapter);
//            eventLv.setOnItemClickListener(new NestFullListView.OnItemClickListener() {
//                @Override
//                public void onItemClick(NestFullListView parent, View view, int position) {
//                    Intent intent = new Intent(mContext, ExhibitionEventDetailActivity.class);
//                    intent.putExtra(Constants.IntentParams.ID,eventList.get(position).id);
//                    startActivity(intent);
//                }
//            });
//        }

    }

    @Override
    public void showList(ArrayList<NewCarBean> newCarList, ArrayList<Beauty> modelList, ArrayList<ExhibitEvent> eventList) {
        List<BaseFragment> fragments = new ArrayList<BaseFragment>();
        fragments.add(new NewCarFragment(newCarList));
        fragments.add(new SeeModelFragment(modelList));
        fragments.add(new ExhibitionCarFragment());
        fragments.add(new OwnerSaleFragment());
        fragments.add(new ExhibitionEventFragment(eventList));
        fragments.add(new ExhibitionSceneFragment());
        MyPagerAdapter mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments,
                getResources().getStringArray(R.array.car_tabs));
        mPager.setAdapter(mPagerAdapter);
        mTab.setViewPager(mPager);
        mPager.setCurrentItem(0);
        setTabsValue();
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

    @Override
    protected void onDestroy() {
        exhibitionPlaceDetailPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
//        switch (view.getId()) {
//            case R.id.placedetail_gotonewcarrl:
//                Map<String, Serializable> args = new HashMap<String, Serializable>();
//                args.put(Constants.IntentParams.ID, exhibitId);
//                CommonUtils.startNewActivity(getContext(), args, NewCarLaunchActivity.class);
//                break;
//            case R.id.palcedetail_modeTitle:
//                Intent toModelList = new Intent(getContext(), ModelListActivity.class);
//                toModelList.getIntExtra(Constants.IntentParams.ID,exhibitId);
//                startActivity(toModelList);
//                break;
//            case R.id.palcedetail_activityTitle:
//                Intent toEventAct = new Intent(getContext(), ExhibitionEventActivity.class);
//                toEventAct.putExtra(Constants.IntentParams.ID,exhibitId);
//                startActivity(toEventAct);
//                break;
//            default:
//                break;
//        }
    }

    @Override
    public void showAddAttention(Object o) {
        CommonUtils.showToast(getContext(), "加关注成功");
    }

    @Override
    public void showCancelAttention(Object o) {

    }
}
