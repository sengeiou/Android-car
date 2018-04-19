package com.tgf.kcwc.tripbook;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Tip;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.app.SelectAddressActivity;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.entity.DataItem;
import com.tgf.kcwc.me.UserPageActivity;
import com.tgf.kcwc.mvp.model.TripBookModel;
import com.tgf.kcwc.mvp.presenter.TripBookFindPresenter;
import com.tgf.kcwc.mvp.view.TripBookFindView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.FilterPopwinUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.DropDownSingleSpinner;

import java.util.ArrayList;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

import static android.app.Activity.RESULT_OK;

/**
 * Auther: Scott
 * Date: 2017/5/2 0002
 * E-mail:hekescott@qq.com
 */

public class FindFrag extends BaseFragment implements TripBookFindView {

    private LinearLayout mFilterLayout;
    private LinearLayout mStartLayout;
    private LinearLayout mEndLayout;
    private LinearLayout mSortLayout;
    private DropDownSingleSpinner mDropDownSortSpinner;
    private ArrayList mSortItems;
    private Integer orderId;
    private KPlayCarApp kPlayCarApp;
    private Integer dayId;
    private TripBookFindPresenter mTripFindPresneter;
    private ListView mTripBookLv;
    private ArrayList<TripBookModel.TripBookModelItem> modelItemslist = new ArrayList<TripBookModel.TripBookModelItem>();
    private  int page =1;
    private boolean isRefresh;


    @Override
    protected void updateData() {
        kPlayCarApp = (KPlayCarApp) getActivity().getApplication();
        mTripFindPresneter = new TripBookFindPresenter();
        mTripFindPresneter.attachView(this);
        isRefresh = true;
        mTripFindPresneter.getTripBookList(kPlayCarApp.getLattitude(), kPlayCarApp.getLongitude(), dayId, startAddress, endAddress,page);
    }

    private void initFilter() {
        mFilterLayout = findView(R.id.playfind_filterlayout);
        mStartLayout = findView(R.id.playfind_startpos);
        mEndLayout = findView(R.id.playfind_endpos);
        mSortLayout = findView(R.id.playfind_distance);
        FilterPopwinUtil.commonFilterTile(mStartLayout, "出发点");
        FilterPopwinUtil.commonFilterTile(mEndLayout, "目的地");
        FilterPopwinUtil.commonFilterTile(mSortLayout, "行程");
        mStartLayout.setOnClickListener(this);
        mEndLayout.setOnClickListener(this);
        mSortLayout.setOnClickListener(this);
        mSortItems = new ArrayList<DataItem>();
        String[] arrays = mRes.getStringArray(R.array.filter_playfind_distance);
        int id = 1;
        for (String s : arrays) {
            DataItem dataItem = new DataItem();
            dataItem.name = s;
            dataItem.id = id;
            mSortItems.add(dataItem);
            id++;
        }

        mDropDownSortSpinner = new DropDownSingleSpinner(getContext(), mSortItems);
        mDropDownSortSpinner.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                FilterPopwinUtil.commonFilterTitleColor(mRes, mSortLayout,
                        R.color.text_content_color);
                FilterPopwinUtil.commonFilterImage(mSortLayout, R.drawable.fitler_drop_down_n);
                DataItem dataItem = mDropDownSortSpinner.getSelectDataItem();
                if (dataItem != null) {
                    FilterPopwinUtil.commonFilterTile(mSortLayout, "行程");
                    if (orderId == null || dataItem.id != orderId) {
//                        orderId = dataItem.id;
//                        mDiscountGiftPresenter.getLimitDiscountList(cityid, endPrice, brandId,
//                                orderId, special, startPrice);
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_playfind;
    }

    @Override
    protected void initView() {
//        setUserVisibleHint(true);
        initRefreshLayout(mBGDelegate);
        mTripBookLv = findView(R.id.playfind_lv);
        initFilter();
//        if (mTripBookAdapter != null) {
//            mTripBookLv.setAdapter(mTripBookAdapter);
//            mTripBookLv.setOnItemClickListener(mlistItemLisenter);
//        }

    }


    private final int KEY_REQUEST_ENDPOS = 100;
    private final int KEY_REQUEST_STARTPOS = 101;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playfind_distance:
                FilterPopwinUtil.commonFilterTitleColor(mRes, mSortLayout, R.color.text_color1);
                FilterPopwinUtil.commonFilterImage(mSortLayout, R.drawable.filter_drop_down_r);
                mDropDownSortSpinner.showAsDropDownBelwBtnView(mFilterLayout);
                break;
            case R.id.playfind_endpos:
                Intent toSelecAddress = new Intent(getContext(), SelectAddressActivity.class);
                toSelecAddress.putExtra(Constants.IntentParams.ISSEE,1);
                startActivityForResult(toSelecAddress, KEY_REQUEST_ENDPOS);
                break;
            case R.id.playfind_startpos:
                Intent toSelecAddress2 = new Intent(getContext(), SelectAddressActivity.class);
                toSelecAddress2.putExtra(Constants.IntentParams.ISSEE,1);
                startActivityForResult(toSelecAddress2, KEY_REQUEST_STARTPOS);
                break;
            default:
                break;
        }
    }
    private String endAddress;
    private String startAddress;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case KEY_REQUEST_ENDPOS:
                    Tip endTip = data.getParcelableExtra(Constants.IntentParams.DATA);
                    endAddress = endTip.getName();
                    mTripFindPresneter.getTripBookList(kPlayCarApp.getLattitude(), kPlayCarApp.getLongitude(), dayId, startAddress, endAddress,page);

                    FilterPopwinUtil.commonFilterTile(mEndLayout,endTip.getName());
                    break;
                case KEY_REQUEST_STARTPOS:
                    Tip startTip = data.getParcelableExtra(Constants.IntentParams.DATA);
                    startAddress = startTip.getName();
                    mTripFindPresneter.getTripBookList(kPlayCarApp.getLattitude(), kPlayCarApp.getLongitude(), dayId, startAddress, endAddress,page);
                    FilterPopwinUtil.commonFilterTile(mStartLayout,startTip.getName());
                    break;
                default:
                    break;
            }
        }
    }

    private WrapAdapter mTripBookAdapter;
    private AdapterView.OnItemClickListener mlistItemLisenter;

    @Override
    public void showTripBookList(ArrayList<TripBookModel.TripBookModelItem> list) {
        if(isRefresh){
            modelItemslist.clear();
            isRefresh =false;
        }
        modelItemslist.addAll(list);
        if(mTripBookAdapter==null){
            initApater();
            mTripBookLv.setAdapter(mTripBookAdapter);
            mTripBookLv.setOnItemClickListener(mlistItemLisenter);
        }else{
            mTripBookAdapter.notifyDataSetChanged();
        }
        if(list==null||list.size()==0){
            CommonUtils.showToast(getContext(),"没有数据了");
            return;
        }
    }

    private void initApater() {
            mTripBookAdapter = new WrapAdapter<TripBookModel.TripBookModelItem>(getContext(), modelItemslist,
                    R.layout.listitem_tripbook_my) {
                @Override
                public void convert(ViewHolder helper, final TripBookModel.TripBookModelItem item) {
                    String coverUrl = URLUtil.builderImgUrl750(item.cover);
                    helper.setSimpleDraweeViewURL(R.id.tripbook_cover, coverUrl);
                    String avatarUrl = URLUtil.builderImgUrl(item.avatar, 144, 144);
                    SimpleDraweeView tripAvatarIv = helper.getView(R.id.tripbook_avatar);
                    tripAvatarIv.setImageURI(Uri.parse(avatarUrl));
                    tripAvatarIv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent toUserAct = new Intent(getContext(), UserPageActivity.class);
                            toUserAct.putExtra(Constants.IntentParams.ID,item.user_id);
                            startActivity(toUserAct);
                        }
                    });
                  View recomLayout =  helper.getView(R.id.layou_item_recom);
                    if(item.is_recommend==0){
                        recomLayout.setVisibility(View.GONE);
                    }else {
                        recomLayout.setVisibility(View.VISIBLE);
                    }
                    helper.setText(R.id.mybook_title, item.title);
                    helper.setText(R.id.listviewitem_xiazai,item.downloadcount+"");
                    helper.setText(R.id.listviewitem_comment,item.reply_count+"");
                    helper.setText(R.id.listviewitem_like,item.digg_count+"");
                    helper.setText(R.id.tripbook_nicknametv,item.nickname);
                    helper.setText(R.id.mybook_title,item.title);
                    helper.setText(R.id.mybook_distance,item.distance+"km");
                    helper.setText(R.id.tripbook_altitudetv,item.altitude_average+"");
                    helper.setText(R.id.tripbook_actualtimetv,item.actual_time);
                    helper.setText(R.id.tripbook_mileage,item.mileage);
                    helper.setText(R.id.tripbook_speedmaxtv,item.getSpeedMax());
                    helper.setText(R.id.tripbook_speedavgtv,item.getSpeedAverage());
                    helper.setText(R.id.tripbook_numtv,item.quotient+"");
                    helper.setText(R.id.tripbook_startendtv,item.start_adds+"-"+item.end_adds);
                }
            };
            mlistItemLisenter = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent toIntent = new Intent(getContext(),TripbookDetailActivity.class);
                    toIntent.putExtra(Constants.IntentParams.ID,modelItemslist.get(position).id);
                    startActivity(toIntent);
                }
            };


    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            showLoadingIndicator(true);
        } else {
            stopRefreshAll();
            showLoadingIndicator(false);
        }
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTripFindPresneter.detachView();
    }
    BGARefreshLayout.BGARefreshLayoutDelegate mBGDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {


        @Override
        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            page = 1;
            isRefresh = true;
            loadMore();
        }

        @Override
        public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
            isRefresh = false;
            page++;
            loadMore();
            return false;
        }
    };
    public void loadMore() {
        mTripFindPresneter.getTripBookList(kPlayCarApp.getLattitude(), kPlayCarApp.getLongitude(), dayId, startAddress, endAddress,page);
    }
}
