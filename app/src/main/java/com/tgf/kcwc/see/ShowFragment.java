package com.tgf.kcwc.see;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.bumptech.glide.Glide;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.app.MainActivity;
import com.tgf.kcwc.app.WebViewActivity;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.certificate.CheckinActivity;
import com.tgf.kcwc.comment.CommentEditorActivity;
import com.tgf.kcwc.comment.CommentMoreActivity;
import com.tgf.kcwc.comment.ReplyMoreActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.fragments.TabSeeFragment;
import com.tgf.kcwc.me.UserPageActivity;
import com.tgf.kcwc.mvp.model.Account;
import com.tgf.kcwc.mvp.model.Comment;
import com.tgf.kcwc.mvp.model.CommentModel;
import com.tgf.kcwc.mvp.model.ExhibitPlace;
import com.tgf.kcwc.mvp.model.Exhibition;
import com.tgf.kcwc.mvp.model.ExhibitionDetailModel;
import com.tgf.kcwc.mvp.model.Image;
import com.tgf.kcwc.mvp.presenter.CommentListPresenter;
import com.tgf.kcwc.mvp.presenter.ExhibitionDetailPrenter;
import com.tgf.kcwc.mvp.view.CommentListView;
import com.tgf.kcwc.mvp.view.ExhibitDetailView;
import com.tgf.kcwc.see.exhibition.ExhibitPalaceListActivity;
import com.tgf.kcwc.see.exhibition.ExhibitPlaceDetailActivity;
import com.tgf.kcwc.see.exhibition.ExhibitionAlbumActivity;
import com.tgf.kcwc.see.exhibition.ExhibitionEventActivity;
import com.tgf.kcwc.see.exhibition.ExhibitionNewsActivity;
import com.tgf.kcwc.see.model.ModelListActivity;
import com.tgf.kcwc.ticket.PreRegistrationActivity;
import com.tgf.kcwc.ticket.PurchaseTicketActivity;
import com.tgf.kcwc.ticket.TicketActivity;
import com.tgf.kcwc.util.ArrayUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.SharedPreferenceUtil;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.MapNavPopWindow;
import com.tgf.kcwc.view.MultiImageView;
import com.tgf.kcwc.view.bannerview.Banner;
import com.tgf.kcwc.view.bannerview.FrescoImageLoader;
import com.tgf.kcwc.view.bannerview.OnBannerClickListener;
import com.tgf.kcwc.view.nestlistview.NestFullListView;
import com.tgf.kcwc.view.nestlistview.NestFullListViewAdapter;
import com.tgf.kcwc.view.nestlistview.NestFullViewHolder;

import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:Jenny
 * Date:16/12/9 11:16
 * E-mail:fishloveqin@gmail.com
 */
public class ShowFragment extends BaseFragment
        implements OnBannerClickListener, ExhibitDetailView, CommentListView {
    private Banner banner;
    private GridView menuGridView;
    private MainActivity activity;
    private ArrayList<ExhibitionDetailModel.MenuItem> menu = new ArrayList<>();
    private WrapAdapter menuGridAdapter;
    private NestFullListView showPlaceLv;
    private SimpleDraweeView lastPhotoIv;
    private FragmentManager fragmentManager;
    private Fragment mComentsFragment;
    private NestFullListViewAdapter replyadapter;
    private NestFullListViewAdapter commentsadapter;
    private NestFullListView motodetailCommentLv;
    ListView mexhibitListView;
    LinearLayout mexhibitListVielayout;
    //    private ArrayList                                 loopHead                    = new ArrayList();
    private List<ExhibitionDetailModel.MenuItem> mMenuItemList;
    private ExhibitionDetailPrenter exhibitionDetailPrenter;
    private MapNavPopWindow mapNavPopWindow;
    private final int KEY_REQUEST_EXHIBITONDETAIL = 100;
    private TextView titleTv;
    private TextView dateTv;
    private TextView addressTv;
    private Button getBtn;
    private Button buyBtn;
    private int exhibitId;
    private TextView showPre;
    private TextView showNext;
    private View showSelectll;
    private Exhibition exhibition;
    private KPlayCarApp rideMotoApp;
    private CommentListPresenter mCommentPresenter;
    private GridView threeLastGV;
    private SimpleDraweeView avatarIv;
    private TextView mlatestTitleTv;
    private RelativeLayout mlatestRl;
    private FrameLayout lastestPicsFl;
    private String mModules = "event_detail";
    private ScrollView showScrollView;
    private CommentListPresenter mLikePresenter;
    private TextView moreTv;
    private TextView commentNumTv;
    private View selectExhibit;
    private Button exhibitOverBtn;
    public DataItem mShowDataItem;
    private  String oldAdcode ="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rideMotoApp = (KPlayCarApp) getActivity().getApplicationContext();
        oldAdcode = rideMotoApp.adcode;
    }

    @Override
    protected void updateData() {
        exhibitionDetailPrenter = new ExhibitionDetailPrenter();
        exhibitionDetailPrenter.attachView(this);

        String tmpId = KPlayCarApp.getValue(Constants.IntentParams.EVENT_ID);

        exhibitId = SharedPreferenceUtil.getExhibitId(mContext);
        if (!TextUtil.isEmpty(tmpId)) {
            exhibitId = Integer.parseInt(tmpId);
            KPlayCarApp.removeValue(Constants.IntentParams.EVENT_ID);
        }
        if(oldAdcode != rideMotoApp.adcode){
            oldAdcode = rideMotoApp.adcode;
            exhibitId =0;
        }

        Boolean   isJump= KPlayCarApp.getValue(Constants.IntentParams.IS_JUMP);
        Boolean   isShowlist= KPlayCarApp.getValue(Constants.IntentParams.IS_SHOWLIST);
        if(isShowlist!=null&&isShowlist){
            isJump =true;
        }
        if (isJump!=null&&isJump) {
            showJump();
            KPlayCarApp.removeValue(Constants.IntentParams.IS_JUMP);
        } else {
            exhibitionDetailPrenter.getExhibitionDetail(exhibitId, rideMotoApp.locCityName, rideMotoApp.adcode);
        }
        menu.clear();
        mCommentPresenter = new CommentListPresenter();
        mCommentPresenter.attachView(this);
        threeLastGV = findView(R.id.last_showphoto_threeGv);
        mLikePresenter = new CommentListPresenter();
        mLikePresenter.attachView(mExecLikeView);
    }

    private void gotoMenu(int id) {
        Map<String, Serializable> args = new HashMap<String, Serializable>();
        args.put(Constants.IntentParams.ID, exhibitId);
        if (id == 1) {
            Integer tmeExhibitId = exhibitId;
            KPlayCarApp.putValue(Constants.IntentParams.EXHIBIT_ID, tmeExhibitId + "");
            TabSeeFragment tabSeeFragment = (TabSeeFragment) getParentFragment();
            tabSeeFragment.setCurrentItem(Constants.IntentParams.SALE_DISCOUNTS);
        } else if (id == 2) {
            CommonUtils.startNewActivity(mContext, args, ExhibitionEventActivity.class);
        } else if (id == 3) {
            CommonUtils.startNewActivity(mContext, args, ExhibitionNewsActivity.class);
        } else if (id == 4) {
            CommonUtils.startNewActivity(mContext, args, ExhibitPalaceListActivity.class);
        } else if (id == 5) {
            Intent toWebIntent = new Intent(mContext, WebViewActivity.class);
            toWebIntent.putExtra(Constants.IntentParams.TITLE, "观展攻略");
//            toWebIntent.putExtra(Constants.IntentParams.DATA,
//                    "http://car.i.cacf.cn/#/app/event/taste/" + exhibitId);
            toWebIntent.putExtra(Constants.IntentParams.DATA,
                    Constants.H5.WAP_LINK + "/#/app/event/taste/" + exhibitId);
            startActivity(toWebIntent);
        } else if (id == 6) {
            CommonUtils.startNewActivity(mContext, args, NewCarLaunchActivity.class);
        } else if (id == 7) {
            CommonUtils.startNewActivity(mContext, args, ModelListActivity.class);
            //            CommonUtils.startNewActivity(mContext, com.tgf.kcwc.see.model.ModelListActivity.class);
        } else if (id == 8) {
            if (IOUtils.isLogin(mContext)) {
                args.put(Constants.IntentParams.DATA, exhibition.name);
                args.put(Constants.IntentParams.DATA2, exhibition.coverImageurl);
                CommonUtils.startNewActivity(mContext, args, PreRegistrationActivity.class);
            }

        } else if (id == 9) {
            args.put(Constants.IntentParams.DATA, exhibition.name);
            args.put(Constants.IntentParams.DATA2, exhibition.coverImageurl);
            args.put(Constants.IntentParams.ID, exhibitId + "");
            args.put(Constants.IntentParams.ID2, "");
            CommonUtils.startNewActivity(mContext, args, CheckinActivity.class);
        } else if (id == 10) {

            CommonUtils.startNewActivity(mContext, args, BrandModelsActivity.class);
        } else if (id == 11) {
            TabSeeFragment tabSeeFragment = (TabSeeFragment) getParentFragment();
            tabSeeFragment.setCurrentItem(Constants.IntentParams.SALE_INDEX);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_show;
    }

    @Override
    protected void initView() {
        activity = (MainActivity) getActivity();
        banner = findView(R.id.show_banner);
        selectExhibit = findView(R.id.exhibitdetail_selectll);
        showPre = (TextView) findView(R.id.show_pre);
        showPre.setOnClickListener(this);
        showNext = (TextView) findView(R.id.show_next);
        showNext.setOnClickListener(this);
        menuGridView = findView(R.id.show_menu_gv);

        mexhibitListView = findView(R.id.exhibitionlist_lv);

        mexhibitListVielayout = findView(R.id.exhibitlist_layout);
        showPlaceLv = findView(R.id.show_place_lv);
        lastPhotoIv = findView(R.id.last_showphoto_iv);
        motodetailCommentLv = findView(R.id.fragshow_comments_lv);
        titleTv = findView(R.id.exhibitionlist_title_tv);
        commentNumTv = findView(R.id.cmtContent);
        avatarIv = findView(R.id.exhibitdetail_avatar_iv);
        dateTv = findView(R.id.exhibitionlist_date_tv);
        addressTv = findView(R.id.exhibitionlist_address_tv);
        addressTv.setOnClickListener(this);
        getBtn = findView(R.id.exhibitionlist_getticket_btn);
        buyBtn = findView(R.id.exhibitionlist_buy_btn);
        exhibitOverBtn = findView(R.id.exhibitdetail_over_btn);
        showSelectll = findView(R.id.show_selectll);
        mlatestTitleTv = findView(R.id.lastest_exhibit_tiltetv);
        lastestPicsFl = findView(R.id.lastest_exhibit_picsfl);
        findView(R.id.show_gonebtn).setOnClickListener(this);
        mlatestRl = findView(R.id.latest_exhibition_rl);
        showScrollView = findView(R.id.showframment_scrollv);
        mlatestRl.setOnClickListener(this);
        findView(R.id.show_alltv).setOnClickListener(this);
        findView(R.id.repayLayout).setOnClickListener(this);
        moreTv = findView(R.id.commment_moretv);
        moreTv.setOnClickListener(this);
        getBtn.setOnClickListener(this);
        buyBtn.setOnClickListener(this);
        Account account = IOUtils.getAccount(getContext());
        if (account != null) {
            avatarIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(account.userInfo.getAvatar())));
            SimpleDraweeView genderIv = findView(R.id.genderImg);
            GenericDraweeHierarchy hierarchy = genderIv.getHierarchy();
            avatarIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(account.userInfo.avatar)));
            if (account.userInfo.gender == 1) {
                hierarchy.setPlaceholderImage(R.drawable.icon_men);
            } else {
                hierarchy.setPlaceholderImage(R.drawable.icon_women);
            }
        }
        mapNavPopWindow = new MapNavPopWindow(mContext);
        mapNavPopWindow.setOnClickListener(this);
        mShowDataItem = new DataItem();
        mShowDataItem.title = "看车玩车";
        mShowDataItem.content = "欢迎关注看车玩车";
        mShowDataItem.url = Constants.ImageUrls.IMG_DEF_URL;
    }

    @Override
    public void OnBannerClick(int position) {
//        CommonUtils.showToast(mContext, "position" + position);
    }

    @Override
    public void onClick(View v) {
        Map<String, Serializable> args = new HashMap<String, Serializable>();
        args.put(Constants.IntentParams.ID, exhibitId);
        switch (v.getId()) {
            case R.id.latest_exhibition_rl:
                CommonUtils.startNewActivity(mContext, args, ExhibitionAlbumActivity.class);
                break;
            case R.id.show_pre:
                exhibitId = exhibition.pid;
                exhibitionDetailPrenter.getExhibitionDetail(exhibitId, rideMotoApp.locCityName, rideMotoApp.adcode);
                exhibitionDetailPrenter.getExhibitBanner(exhibitId);
                break;
            case R.id.repayLayout:
                if (IOUtils.isLogin(mContext)) {
                    Map<String, Serializable> argscoment = new HashMap<String, Serializable>();
                    argscoment.put(Constants.IntentParams.ID, exhibitId + "");
                    argscoment.put(Constants.IntentParams.INTENT_TYPE, mModules);
                    CommonUtils.startNewActivity(mContext, argscoment, CommentEditorActivity.class);
                }
                break;
            case R.id.show_next:
                exhibitId = exhibition.nextid;
                exhibitionDetailPrenter.getExhibitionDetail(exhibitId, rideMotoApp.locCityName, rideMotoApp.adcode);
                exhibitionDetailPrenter.getExhibitBanner(exhibitId);
                break;
            case R.id.show_alltv:
//                Intent intent = new Intent(mContext, ExhibitionListActivity.class);
//                getActivity().startActivity(intent);
                mexhibitListView.scrollTo(0, 0);
                KPlayCarApp.putValue(Constants.IntentParams.IS_SHOWLIST,true);
                exhibitionDetailPrenter.getExhibitionList(rideMotoApp.adcode, "1", "999", IOUtils.getToken(getContext()), rideMotoApp.adcode);
                mexhibitListVielayout.setVisibility(View.VISIBLE);
                break;
            case R.id.show_gonebtn:
                showScrollView.smoothScrollTo(0, 0);
                break;

            case R.id.exhibitionlist_getticket_btn:
                if (IOUtils.isLogin(mContext)) {
                    Intent toTicketIntent = new Intent(mContext, TicketActivity.class);
                    toTicketIntent.putExtra(Constants.IntentParams.INDEX, 0);
                    startActivity(toTicketIntent);
                }
                break;
            case R.id.exhibitionlist_buy_btn:
                if (IOUtils.isLogin(mContext)) {
                    args = new HashMap<String, Serializable>();
                    args.put(Constants.IntentParams.ID, exhibitId);
                    CommonUtils.startNewActivity(mContext, args, PurchaseTicketActivity.class);
                }
                break;
            case R.id.commment_moretv:
                Intent toComentMore = new Intent(mContext, CommentMoreActivity.class);
                toComentMore.putExtra(Constants.IntentParams.TITLE, mModules);
                toComentMore.putExtra(Constants.IntentParams.ID, exhibitId);
                startActivity(toComentMore);
                break;
            case R.id.exhibitionlist_address_tv:
                mapNavPopWindow.show(getActivity());
                break;
            case R.id.cancel:
                mapNavPopWindow.dismiss();
                break;
            case R.id.aMap:
                try {
                    LatLng startGLat = new LatLng(Double.parseDouble(rideMotoApp.latitude), Double.parseDouble(rideMotoApp.longitude));
                    LatLng endGLat = new LatLng(Double.parseDouble(exhibition.latitude), Double.parseDouble(exhibition.longitude));
                    rideMotoApp.getLattitude();

                    URLUtil.launcherInnerRouteAMap(getActivity(), exhibition.address, startGLat,
                            endGLat);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.baiduMap:
                try {
                    LatLng startBLat = new LatLng(Double.parseDouble(rideMotoApp.latitude), Double.parseDouble(rideMotoApp.longitude));
                    LatLng endBLat = new LatLng(Double.parseDouble(exhibition.latitude), Double.parseDouble(exhibition.longitude));
                    URLUtil.launcherInnerRouteBaidu(getActivity(), exhibition.address, startBLat,
                            endBLat);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void showHead(Exhibition exhibition) {
        mexhibitListVielayout.setVisibility(View.GONE);
        mShowDataItem = new DataItem();
        selectExhibit.setVisibility(View.VISIBLE);
        mShowDataItem.title = exhibition.name;
        mShowDataItem.content = exhibition.desc;
        mShowDataItem.url = URLUtil.builderImgUrl(exhibition.coverImageurl, 360, 360);
        this.exhibition = exhibition;
        titleTv.setText(exhibition.name);
        addressTv.setText(exhibition.address);
        dateTv.setText(exhibition.time);
        exhibitId = exhibition.id;
        SharedPreferenceUtil.putExhibitId(getContext(), exhibitId);
        mCommentPresenter.loadCommentList(mModules, exhibitId + "", "car");
        exhibitionDetailPrenter.getExhibitBanner(exhibitId);
        if (exhibition.nextid == 0) {
            showNext.setVisibility(View.GONE);
        } else {
            showNext.setVisibility(View.VISIBLE);
        }
        if (exhibition.pid == 0) {
            showPre.setVisibility(View.GONE);
        } else {
            showPre.setVisibility(View.VISIBLE);
        }
        if (exhibition.isTicketg == 1) {
            buyBtn.setVisibility(View.VISIBLE);
        } else {
            buyBtn.setVisibility(View.GONE);
        }
        if (exhibition.isTicketl == 1) {
            getBtn.setVisibility(View.VISIBLE);
        } else {
            getBtn.setVisibility(View.GONE);
        }
        if (exhibition.isClose == 1) {
            exhibitOverBtn.setVisibility(View.VISIBLE);
        } else {
            exhibitOverBtn.setVisibility(View.GONE);
        }
        findView(R.id.getlayout).setVisibility(View.VISIBLE);
        if (exhibition.isTicketg != 1 && exhibition.isTicketl != 1 && exhibition.isClose != 1) {
            findView(R.id.getlayout).setVisibility(View.GONE);
        }

    }

    @Override
    public void showMenu(List<ExhibitionDetailModel.MenuItem> menuItemList) {
        this.mMenuItemList = menuItemList;
        menuGridAdapter = new WrapAdapter<ExhibitionDetailModel.MenuItem>(mContext, mMenuItemList,
                R.layout.gridview_item_showmenu) {

            @Override
            public void convert(ViewHolder helper, ExhibitionDetailModel.MenuItem item) {
                TextView titletv = helper.getView(R.id.gridview_item_menutv);
                titletv.setText(item.title);
                setMenuIcon(item.id, helper);
            }
        };
        menuGridView.setAdapter(menuGridAdapter);
        menuGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gotoMenu(mMenuItemList.get(position).id);
            }
        });
        ViewUtil.setListViewHeightBasedOnChildren(menuGridView, 3);
    }

    private void setMenuIcon(int id, WrapAdapter.ViewHolder helper) {
        ImageView iv = helper.getView(R.id.gridview_item_menuiv);
        switch (id) {
            case 1:
                iv.setImageBitmap(BitmapFactory.decodeResource(mRes, R.drawable.show_saleoff));
                break;
            case 2:
                iv.setImageBitmap(BitmapFactory.decodeResource(mRes, R.drawable.show_event));
                break;
            case 3:
                iv.setImageBitmap(BitmapFactory.decodeResource(mRes, R.drawable.show_news));
                break;
            case 4:
                iv.setImageBitmap(BitmapFactory.decodeResource(mRes, R.drawable.show_place));
                break;
            case 5:
                iv.setImageBitmap(BitmapFactory.decodeResource(mRes, R.drawable.show_guide));
                break;
            case 6:
                iv.setImageBitmap(BitmapFactory.decodeResource(mRes, R.drawable.show_newcar));
                break;
            case 7:
                iv.setImageBitmap(BitmapFactory.decodeResource(mRes, R.drawable.show_beauty));
                break;
            case 8:
                iv.setImageBitmap(BitmapFactory.decodeResource(mRes, R.drawable.exhibit_piao));
                break;
            case 9:
                iv.setImageBitmap(BitmapFactory.decodeResource(mRes, R.drawable.exhibit_dengji));
                break;
            case 10:
                iv.setImageBitmap(BitmapFactory.decodeResource(mRes, R.drawable.show_brandcar));
                break;
            case 11:
                iv.setImageBitmap(BitmapFactory.decodeResource(mRes, R.drawable.show_saleself));
                break;
            default:
                break;
        }
    }

    @Override
    public void showBanner(ArrayList<Image> bannerList) {

        if (bannerList != null && bannerList.size() != 0) {
            findView(R.id.show_headlayout).setVisibility(View.VISIBLE);
            ArrayList<String> imgUrl = new ArrayList<>();
            for (int i = 0; i < bannerList.size() && i < 4; i++) {
                imgUrl.add(URLUtil.builderImgUrl(bannerList.get(i).link, 540, 270));
            }
            banner.setImages(imgUrl).setImageLoader(new FrescoImageLoader())
                    .setOnBannerClickListener(this).start();
        } else {
            findView(R.id.show_headlayout).setVisibility(View.GONE);
        }

    }

    @Override
    public void showPlaceList(final ArrayList<ExhibitPlace> exhibitPlacelist) {
        showPlaceLv.setAdapter(new NestFullListViewAdapter<ExhibitPlace>(
                R.layout.listview_item_exhibitpalce, exhibitPlacelist) {
            @Override
            public void onBind(final int pos, final ExhibitPlace exhibitPlace, NestFullViewHolder holder) {
                ImageView imageview = holder.getView(R.id.palce_img);
                TextView moreTv = holder.getView(R.id.listitem_exhibitpalce_tile);
                moreTv.setText(exhibitPlace.name);
                String url = URLUtil.builderImgUrl750(exhibitPlace.imageurl);
                holder.setText(R.id.palce_desctv,exhibitPlace.description);
                Glide.with(getActivity())
                        .load(url)
                        .into(imageview);
                imageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, BigPhotoPageActivity.class);
                        intent.putExtra(Constants.KEY_IMG,
                                URLUtil.builderImgUrl(exhibitPlace.imageurl));
                        startActivity(intent);
                    }
                });
                holder.getView(R.id.exhibit_more_rv).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (pos == 0) {
                            Intent toPlaceListAct = new Intent(getContext(), ExhibitPalaceListActivity.class);
                            toPlaceListAct.putExtra(Constants.IntentParams.ID, exhibitId);
                            startActivity(toPlaceListAct);
                        } else {
                            HashMap arg = new HashMap();
                            arg.put(Constants.IntentParams.ID, exhibitId);
                            arg.put(Constants.IntentParams.ID2, exhibitPlace.id);
                            CommonUtils.startNewActivity(mContext, arg, ExhibitPlaceDetailActivity.class);
//                            CommonUtils.startNewActivity(mContext, arg, com.tgf.kcwc.see.exhibition.plus.ExhibitPlaceDetailActivity.class);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void showPlink(ArrayList<String> pinkImgs, int type) {
        //        1 本届 2上届 0不显示
        if (type == 1) {
            mlatestTitleTv.setText("本届展会照片");
            mlatestRl.setVisibility(View.VISIBLE);
            lastestPicsFl.setVisibility(View.VISIBLE);
        }
        if (type == 2) {
            mlatestRl.setVisibility(View.VISIBLE);
            lastestPicsFl.setVisibility(View.VISIBLE);
            mlatestTitleTv.setText("上届展会照片");
        }
        if (type == 0) {
            mlatestRl.setVisibility(View.GONE);
            lastestPicsFl.setVisibility(View.GONE);
        }
        if (pinkImgs != null) {
            if (pinkImgs.size() == 1) {
                lastPhotoIv.setVisibility(View.VISIBLE);
                threeLastGV.setVisibility(View.GONE);
                lastPhotoIv
                        .setImageURI(Uri.parse(URLUtil.builderImgUrl(pinkImgs.get(0), 540, 270)));
            } else {
                lastPhotoIv.setVisibility(View.GONE);
                threeLastGV.setVisibility(View.VISIBLE);
                threeLastGV.setAdapter(
                        new WrapAdapter<String>(mContext, R.layout.gridview_item_plinks, pinkImgs) {
                            @Override
                            public void convert(ViewHolder helper, String item) {
                                helper.setSimpleDraweeViewURL(R.id.griditem_plink_iv,
                                        URLUtil.builderImgUrl(item, 144, 144));
                            }
                        });
            }
        }
    }

    @Override
    public void showJump() {

        mexhibitListVielayout.setVisibility(View.VISIBLE);

        exhibitionDetailPrenter.getExhibitionList(rideMotoApp.adcode, "1", "999", IOUtils.getToken(getContext()), rideMotoApp.adcode);
//        }

    }

    @Override
    public void showExhibitionList(final ArrayList<Exhibition> exhibitionList) {
        WrapAdapter mExhibitionListAdapter = new WrapAdapter<Exhibition>(mContext, exhibitionList,
                R.layout.listview_item_exhibitionlist) {
            @Override
            public void convert(ViewHolder helper, final Exhibition item) {
                SimpleDraweeView cover = helper.getView(R.id.exhibitionlist_cover_iv);

                cover.setImageURI(Uri.parse(URLUtil.builderImgUrl(item.coverImg, 540, 270)));

                Button buyBtn = helper.getView(R.id.exhibitionlist_buy_btn);
                Button overBtn = helper.getView(R.id.exhibitionlist_over_btn);
                helper.setText(R.id.exhibitionlist_title_tv, item.name);
                helper.setText(R.id.exhibitionlist_date_tv, item.time);
                helper.setText(R.id.exhibitionlist_address_tv, item.address);
                Button getBtn = helper.getView(R.id.exhibitionlist_getticket_btn);
                if (item.isTicketl == 1) {
                    getBtn.setVisibility(View.VISIBLE);
                } else {
                    getBtn.setVisibility(View.GONE);
                }
                if (item.isTicketg == 1) {
                    buyBtn.setVisibility(View.VISIBLE);
                } else {
                    buyBtn.setVisibility(View.GONE);
                }
                if (item.isClose == 1) {
                    overBtn.setVisibility(View.VISIBLE);
                } else {
                    overBtn.setVisibility(View.GONE);
                }
                getBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (IOUtils.isLogin(mContext)) {
                            Intent toTicketIntent = new Intent(mContext, TicketActivity.class);
                            toTicketIntent.putExtra(Constants.IntentParams.INDEX, 0);
                            startActivity(toTicketIntent);
                        }
                    }
                });
                buyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (IOUtils.isLogin(mContext)) {
                            HashMap args = new HashMap<String, Serializable>();
                            args.put(Constants.IntentParams.ID, item.id);
                            CommonUtils.startNewActivity(mContext, args, PurchaseTicketActivity.class);
                        }
                    }
                });
            }
        };
        mexhibitListView.setAdapter(mExhibitionListAdapter);
        mexhibitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//               SharedPreferenceUtil.setIsBack(getContext(),true);
//                SharedPreferenceUtil.putExhibitId(mContext, exhibitionList.get(position).id);
                mexhibitListVielayout.setVisibility(View.GONE);
                exhibitId = exhibitionList.get(position).id;
                exhibitionDetailPrenter.getExhibitionDetail(exhibitId, rideMotoApp.locCityName, rideMotoApp.adcode);
                exhibitionDetailPrenter.getExhibitBanner(exhibitId);
                KPlayCarApp.putValue(Constants.IntentParams.IS_SHOWLIST,false);
            }
        });
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showDatas(Object o) {
        CommentModel commentModel = (CommentModel) o;
        commentNumTv.setText("(" + (commentModel.count) + ")");
        //评论列表
        if (commentModel.count > 10) {
            moreTv.setVisibility(View.VISIBLE);
        }
        commentsadapter = new NestFullListViewAdapter<Comment>(R.layout.listview_item_comment,
                commentModel.comments) {

            @Override
            public void onBind(final int pos, final Comment comment, NestFullViewHolder holder) {
                final Account.UserInfo sendinfo = comment.senderInfo;
                SimpleDraweeView avatarCommentIv = holder.getView(R.id.motodetail_avatar_iv);
                avatarCommentIv.setImageURI(Uri.parse(URLUtil.builderImgUrl(sendinfo.avatar, 144, 144)));
                avatarCommentIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent toUserIntent = new Intent(getContext(), UserPageActivity.class);
                        toUserIntent.putExtra(Constants.IntentParams.ID, sendinfo.id);
                        startActivity(toUserIntent);
                    }
                });

                TextView goodTv = holder.getView(R.id.commnt_good);
                goodTv.setText(comment.fabNum + "");
                if (comment.isFab == 1) {
                    Drawable drawable = getResources().getDrawable(R.drawable.btn_heart2);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    goodTv.setCompoundDrawables(drawable, null, null, null);
                } else {
                    Drawable drawable = getResources().getDrawable(R.drawable.icon_like);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    goodTv.setCompoundDrawables(drawable, null, null, null);

                }
                holder.setText(R.id.nametv, sendinfo.nickName);
                holder.setText(R.id.comment_time, comment.time);
                holder.setEmojiText(R.id.contentTv, comment.content);
                ImageView modelImg = holder.getView(R.id.comment_model_tv);
                ImageView popmanImg = holder.getView(R.id.comment_popman_tv);
                SimpleDraweeView genderIv = holder.getView(R.id.genderImg);
                GenericDraweeHierarchy hierarchy = genderIv.getHierarchy();
                if (comment.senderInfo.sex == 1) {
                    hierarchy.setPlaceholderImage(R.drawable.icon_men);
                } else {
                    hierarchy.setPlaceholderImage(R.drawable.icon_women);
                }

                //达人
                if (comment.senderInfo.isDaren == 1) {
                    popmanImg.setVisibility(View.VISIBLE);
                } else {
                    popmanImg.setVisibility(View.GONE);
                }
                //模特
                if (comment.senderInfo.is_model == 1) {
                    modelImg.setVisibility(View.VISIBLE);
                } else {
                    modelImg.setVisibility(View.GONE);
                }
                String brandLogo = URLUtil.builderImgUrl(comment.senderInfo.brandLogo, 144, 144);
                holder.setSimpleDraweeViewURL(R.id.brandLogo, brandLogo);
                MultiImageView multiImageView = holder.getView(R.id.multiImagView);
                if (comment.imgs != null) {
                    List<String> listImgs = new ArrayList<>();
                    for (String img : comment.imgs) {
                        listImgs.add(URLUtil.builderImgUrl(img, 360, 360));
                    }
                    multiImageView.setList(listImgs);
                    multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            ArrayList<String> imageUrlList = ArrayUtils
                                    .getImageUrlList(comment.imgs);
                            Intent toInent = new Intent(getContext(), BigPhotoPageActivity.class);
                            toInent.putStringArrayListExtra(Constants.KEY_IMGS, imageUrlList);
                            toInent.putExtra(Constants.IntentParams.INDEX, position);
                            startActivity(toInent);
                        }
                    });
                }
                goodTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mLikePresenter.executePraise(comment.id + "", "comment", v,
                                IOUtils.getToken(mContext));

                    }
                });
                TextView replyNum = holder.getView(R.id.reply_comments_tv);
                replyNum.setText(comment.repliesCount + "");
                replyNum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (IOUtils.isLogin(mContext)) {
                            Intent toIntent = new Intent(mContext, CommentEditorActivity.class);
                            toIntent.putExtra(Constants.IntentParams.ID, exhibitId + "");
                            toIntent.putExtra(Constants.IntentParams.INTENT_TYPE, mModules);
                            toIntent.putExtra(Constants.IntentParams.ID2, comment.id + "");
                            getActivity().startActivityForResult(toIntent,
                                    getActivity().RESULT_FIRST_USER);
                        }
                    }
                });
                NestFullListView replyListview = holder.getView(R.id.listview_item_reply_lv);
                if (comment.repliesCount >= 3) {
                    comment.replies.add(new Comment());
                }
                List<Comment> replyList = null;
                if (comment.replies != null && comment.replies.size() >= 3) {
                    replyList = comment.replies.subList(0, 3);
                } else {
                    replyList = comment.replies;
                }
                replyadapter = new NestFullListViewAdapter<Comment>(R.layout.listview_item_reply,
                        replyList) {
                    @Override
                    public void onBind(int pos2, Comment cmt, NestFullViewHolder holder) {
                        TextView replyTv = holder.getView(R.id.replytv);


                        if (pos2 == 2) {
                            replyTv.setText("查看更多回复");
                        } else {
                            if (cmt.senderInfo != null) {
                                String msg = String.format(mRes.getString(R.string.reply_msg),
                                        cmt.senderInfo.nickName, cmt.receiverInfo.nickName, cmt.content);
                                replyTv.setText(Html.fromHtml(msg));
//                                CommonUtils.customDisplayReplyText(msg, replyTv, mRes);
                            }
                        }
                    }
                };
                replyListview.setAdapter(replyadapter);
                replyListview.setOnItemClickListener(new NestFullListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(NestFullListView parent, View view, int position) {
                        if (position >= 2) {
                            Intent toReplyMore = new Intent(getContext(), ReplyMoreActivity.class);
                            toReplyMore.putExtra(Constants.IntentParams.ID, exhibitId + "");
                            toReplyMore.putExtra(Constants.IntentParams.ID2, comment.id + "");
                            toReplyMore.putExtra(Constants.IntentParams.INTENT_TYPE, mModules);
                            startActivity(toReplyMore);
                        }
                    }
                });
            }

        };
        motodetailCommentLv.setAdapter(commentsadapter);

    }

    //点赞
    private CommentListView<View> mExecLikeView = new CommentListView<View>() {
        @Override
        public void showDatas(View v) {

            mCommentPresenter.loadCommentList(mModules, exhibitId + "", "car");

        }

        @Override
        public void setLoadingIndicator(boolean active) {

            if (isLoading) {
                showLoadingIndicator(active);
            }

        }

        @Override
        public void showLoadingTasksError() {

            dismissLoadingDialog();
        }

        @Override
        public Context getContext() {
            return mContext;
        }


    };


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        if (isVisibleToUser) {

            isLoading = true;
        } else {
            isLoading = false;
        }

    }


}
