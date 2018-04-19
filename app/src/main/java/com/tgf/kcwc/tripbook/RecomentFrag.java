package com.tgf.kcwc.tripbook;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.KPlayCarApp;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.logger.Logger;
import com.tgf.kcwc.me.UserPageActivity;
import com.tgf.kcwc.mvp.model.TripBookModel;
import com.tgf.kcwc.mvp.presenter.TripBookRecomPresneter;
import com.tgf.kcwc.mvp.view.TripBookFindView;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DataUtil;
import com.tgf.kcwc.util.URLUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Auther: Scott
 * Date: 2017/5/2 0002
 * E-mail:hekescott@qq.com
 */

public class RecomentFrag extends BaseFragment implements TripBookFindView {

    private TripBookRecomPresneter tripBookRecomPresneter;
    private KPlayCarApp           kPlayCarApp;
    private ListView              mTripBookLv;
    private WrapAdapter mTripBookAdapter;
    private AdapterView.OnItemClickListener mlistItemLisenter;
    private ArrayList<TripBookModel.TripBookModelItem> mlist = new ArrayList<>();
    private int page =1;

    private boolean isRefresh;
    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_playrecomend;
    }

    @Override
    protected void initView() {
//        setUserVisibleHint(true);
        Logger.d("RecomentFrag initView");
        mlist.clear();
        initRefreshLayout(mBGDelegate);

        mTripBookLv = findView(R.id.playrecoment_lv);

        initApater();
        mTripBookLv.setAdapter(mTripBookAdapter);
        mTripBookLv.setOnItemClickListener(mlistItemLisenter);
        kPlayCarApp = (KPlayCarApp) getActivity().getApplication();
        tripBookRecomPresneter = new TripBookRecomPresneter();
        tripBookRecomPresneter.attachView(this);
        tripBookRecomPresneter.getRecomTripBookList(kPlayCarApp.getLattitude(),
                kPlayCarApp.getLongitude(),page);

//        if(mTripBookAdapter!=null){
//            mTripBookLv.setAdapter(mTripBookAdapter);
//            mTripBookLv.setOnItemClickListener(mlistItemLisenter);
//        }
    }



    @Override
    public void showTripBookList(ArrayList<TripBookModel.TripBookModelItem> list) {
        if(isRefresh){
            mlist.clear();
        }
        mlist.addAll(list);
//        if(mTripBookAdapter==null){
//            mTripBookLv.setAdapter(mTripBookAdapter);
//
//        }else
//        {
            mTripBookAdapter.notifyDataSetChanged();
//        }
        if(list==null||list.size()==0){
            CommonUtils.showToast(getContext(),"没有数据了");
        }
    }

    private void initApater() {
        mTripBookAdapter = new WrapAdapter<TripBookModel.TripBookModelItem>(getContext(), mlist,
                R.layout.listitem_tripbook_my) {
            @Override
            public void convert(ViewHolder helper, final TripBookModel.TripBookModelItem item) {
                String coverUrl = URLUtil.builderImgUrl750(item.cover);;
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
                toIntent.putExtra(Constants.IntentParams.ID,mlist.get(position).id);
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
        tripBookRecomPresneter.getRecomTripBookList(kPlayCarApp.getLattitude(),
                kPlayCarApp.getLongitude(),page);
    }

    @Override
    public void onPause() {
        Logger.d("RecomentFrag onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Logger.d("RecomentFrag onStop");
        super.onStop();
        onDestroyView();
    }

    @Override
    public void onDestroyView() {
        Logger.d("RecomentFrag onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Logger.d("RecomentFrag onDestroy");
        super.onDestroy();
    }
}
