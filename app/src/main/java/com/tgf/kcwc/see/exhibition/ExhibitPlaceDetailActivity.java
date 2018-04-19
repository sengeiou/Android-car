package com.tgf.kcwc.see.exhibition;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.OnItemClickListener;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
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
import com.tgf.kcwc.see.NewCarExhibitPlaceItemProvider;
import com.tgf.kcwc.see.NewCarGalleryActivity;
import com.tgf.kcwc.see.NewCarLaunchActivity;
import com.tgf.kcwc.see.model.ModelAlbumActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.NestLinearLayoutManager;
import com.tgf.kcwc.view.nestlistview.NestFullListView;
import com.tgf.kcwc.view.nestlistview.NestFullListViewAdapter;
import com.tgf.kcwc.view.nestlistview.NestFullViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Auther: Scott
 * Date: 2017/1/3 0003
 * E-mail:hekescott@qq.com
 */

public class ExhibitPlaceDetailActivity extends BaseActivity implements ExhibitPlaceDetailView, AttentionView {

    private GridView brandsGv;
    private WrapAdapter<Brand> brandWrapAdapter;
    private Items mItems = new Items();
    private MultiTypeAdapter mAdapter;
    private RecyclerView mRecyleViewNewcar;
    private GridView beautysGv;
    private WrapAdapter beautyAdapter;
    private NestFullListView eventLv;
    private NestFullListViewAdapter eventAdapter;
    private TextView exhibitPlaceTv;
    private TextView exhibitPlaceDescTv;
    private SimpleDraweeView headImg;
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
        findViewById(R.id.placedetail_gotonewcarrl).setOnClickListener(this);
        exhibitionPlaceDetailPresenter.getExhibitionPalaceDetail(exhibitId, hallId);
        brandsGv = (GridView) findViewById(R.id.palcedeatail_brands_gv);
        beautysGv = (GridView) findViewById(R.id.palcedetail_beauty_gv);
        mRecyleViewNewcar = (RecyclerView) findViewById(R.id.exhibitplacedetail_newcar_rc);
        eventLv = (NestFullListView) findViewById(R.id.palcedeatail_envent_lv);
        exhibitPlaceTv = (TextView) findViewById(R.id.exhibitplace_name);
        exhibitPlaceDescTv = (TextView) findViewById(R.id.exhibitplace_desc);
        headImg = (SimpleDraweeView) findViewById(R.id.exhibitplace_headiv);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        fromIntent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibit_palcedetail);

    }

//    private OnItemClickListener mOnItemListener =;

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

    }

    @Override
    public void showNewMotolist(final ArrayList<NewCarBean> newMotoArrayList) {
        for (int i = 0; i < newMotoArrayList.size(); i++) {
            NewCarBean moto = newMotoArrayList.get(i);
            mItems.add(moto);
        }
        mAdapter = new MultiTypeAdapter(mItems);
        NewCarExhibitPlaceItemProvider contentItemProvider = new NewCarExhibitPlaceItemProvider();
        contentItemProvider.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                Intent toNecarAct = new Intent(mContext, NewCarGalleryActivity.class);
                toNecarAct.putExtra(Constants.IntentParams.ID, newMotoArrayList.get(position).id);
                startActivity(toNecarAct);
            }

            ;

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
        mAdapter.register(NewCarBean.class, contentItemProvider);
        mRecyleViewNewcar.setLayoutManager(new NestLinearLayoutManager(mContext));
        mRecyleViewNewcar.setAdapter(mAdapter);
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

                    Intent toBranAce = new Intent(getContext(), BrandModelsActivity.class);
                    toBranAce.putExtra(Constants.IntentParams.ID, includeBrandlist.get(position).brandId);
                    startActivity(toBranAce);
                }
            });

        }
    }


    @Override
    public void showModellist(ArrayList<Beauty> beautylist) {
        if (beautylist != null && beautylist.size() != 0) {
            beautyAdapter = new WrapAdapter<Beauty>(mContext, beautylist,
                    R.layout.gridviewitem_exhibitplacedetail_beauty) {
                @Override
                public void convert(ViewHolder helper, final Beauty item) {
                    helper.setText(R.id.beauty_name, item.name);
                    SimpleDraweeView avatarCoverIv = helper.getView(R.id.recyleitme_beauty_cover);
                    String coverUrl = URLUtil.builderImgUrl(item.cover, 360, 360);
                    avatarCoverIv.setImageURI(Uri.parse(coverUrl));
                    avatarCoverIv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), ModelAlbumActivity.class);
                            intent.putExtra(Constants.IntentParams.ID, item.modelId);
                            getContext().startActivity(intent);
                        }
                    });
                    String avatarUrl = URLUtil.builderImgUrl(item.avatar, 144, 144);

                    helper.setSimpleDraweeViewURL(R.id.avatarbadge_avatar, avatarUrl);
                    final ImageView genderIv = helper.getView(R.id.avatarbadge_gender);
                    if (item.sex == 1) {
                        genderIv.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_men));
                    } else {
                        genderIv.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_women));
                    }
                    final TextView addattentionTv = helper.getView(R.id.add_attentiontv);
                    addattentionTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            item.isFollow++;
                            if (IOUtils.isLogin(getContext())) {
                                mAttentionPresenter.execAttention(item.modelId + "",
                                        IOUtils.getToken(getContext()));
                            }
                            addattentionTv.setText("粉丝 " + item.isFollow);
                            addattentionTv.setTextColor(getContext().getResources().getColor(R.color.text_color3));
                        }
                    });

                }
            };

            beautysGv.setAdapter(beautyAdapter);
            ViewUtil.setListViewHeightBasedOnChildren(beautysGv, 3);
        }

    }

    @Override
    public void showEventlist(ArrayList<ExhibitEvent> eventList) {
        if (eventList != null && eventList.size() != 0) {
            eventAdapter = new NestFullListViewAdapter<ExhibitEvent>(R.layout.listviewitem_exhibitplace_event,
                    eventList) {
                @Override
                public void onBind(int pos, ExhibitEvent exhibitEvent, NestFullViewHolder holder) {
                    holder.setText(R.id.event_title, exhibitEvent.title);
                    if (DateFormatUtil.getTime(exhibitEvent.endTime) < System.currentTimeMillis()) {
                        holder.setText(R.id.listviewitem_visitors, "已结束");
                    } else {
                        holder.setText(R.id.listviewitem_visitors, "开始时间:  " + exhibitEvent.startTime);
                    }
                    String activtyCoverUrl = URLUtil.builderImgUrl(exhibitEvent.cover, 360, 360);
                    holder.setSimpleDraweeViewURL(R.id.listitemt_activtyiv, activtyCoverUrl);
                }
            };
            eventLv.setAdapter(eventAdapter);
        }

    }

    @Override
    public void showList(ArrayList<NewCarBean> newCarList, ArrayList<Beauty> modelList, ArrayList<ExhibitEvent> eventList) {

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
        switch (view.getId()) {
            case R.id.placedetail_gotonewcarrl:
                Map<String, Serializable> args = new HashMap<String, Serializable>();
                args.put(Constants.IntentParams.ID, exhibitId);
                CommonUtils.startNewActivity(getContext(), args, NewCarLaunchActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    public void showAddAttention(Object o) {
        CommonUtils.showToast(getContext(), "加关注成功");
    }

    @Override
    public void showCancelAttention(Object o) {

    }
}
