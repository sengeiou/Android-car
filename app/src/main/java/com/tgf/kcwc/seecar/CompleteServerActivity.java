package com.tgf.kcwc.seecar;

import java.util.List;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hedgehog.ratingbar.RatingBar;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.YuyueBuyModel;
import com.tgf.kcwc.mvp.presenter.CompleteServerPresenter;
import com.tgf.kcwc.util.BitmapUtils;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.NumFormatUtil;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.view.BadgeView;
import com.tgf.kcwc.view.CompleteServerView;
import com.tgf.kcwc.view.FunctionView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Auther: Scott
 * Date: 2017/2/24 0024
 * E-mail:hekescott@qq.com
 */

public class CompleteServerActivity extends BaseActivity implements CompleteServerView {

    private SimpleDraweeView orgCover;
    private TextView offerNum;
    private TextView carModel;
    private ListView orglistListView;
    private CompleteServerPresenter completeServerPresenter;
    private String mToken;
    private Intent fromIntent;
    private int mOrderId;
    private ImageView outImgIv;
    private ImageView inImgIv;

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText("完成服务");
    }

    @Override
    protected void setUpViews() {
        completeServerPresenter = new CompleteServerPresenter();
        completeServerPresenter.attachView(this);
        mToken = IOUtils.getToken(this);
        fromIntent = getIntent();
        mOrderId = fromIntent.getIntExtra(Constants.IntentParams.ID,0);

        orgCover = (SimpleDraweeView) findViewById(R.id.completeserver_org_cover);
        offerNum = (TextView) findViewById(R.id.completeserver_tickettitle);
        carModel = (TextView) findViewById(R.id.completeserver_carmodel);
       View boottomView = View.inflate(getContext(),R.layout.listfooter_complete_server,null);
        orglistListView = (ListView) findViewById(R.id.completeserver_orglistlv);
        orglistListView.addFooterView(boottomView);
        outImgIv = (ImageView) findViewById(R.id.completeserver_outimg);
        inImgIv = (ImageView) findViewById(R.id.completeserver_inimg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        completeServerPresenter.getCompeleteServerModel(mToken, mOrderId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completeserver);
    }

    @Override
    public void showHead(YuyueBuyModel completeServerModel) {

        String url = URLUtil.builderImgUrl(completeServerModel.car_cover,270, 203);
        orgCover.setImageURI(Uri.parse(url));
        offerNum.setText(completeServerModel.offer_count+"名业务员已经报价");
        carModel.setText(completeServerModel.brand_name+" "+completeServerModel.seriesName+" "+completeServerModel.car_name);
         int size = BitmapUtils.dp2px(this,15);
        if(!TextUtil.isEmpty(completeServerModel.outColor))
        outImgIv.setImageBitmap(BitmapUtils.getRectColors(completeServerModel.outColor.split(","),size,size, R.color.style_bg4,2));
        if(!TextUtil.isEmpty(completeServerModel.inColor))
        inImgIv.setImageBitmap(BitmapUtils.getRectColors(completeServerModel.inColor.split(","),size,size, R.color.style_bg4,2));
        showOrglist(completeServerModel.offer_list);
    }

    @Override
    public void showOrglist(List<YuyueBuyModel.OrgItem> orgItemList){
        orglistListView.setAdapter(new WrapAdapter<YuyueBuyModel.OrgItem>(getContext(),R.layout.listitem_buymoto_completeserver,orgItemList) {
            @Override
            public void convert(ViewHolder helper, final YuyueBuyModel.OrgItem item) {
                helper.setText(R.id.listitem_complete_nickname,item.nickname);
               SimpleDraweeView avatarIv=  helper.getView(R.id.listitem_complete_avatar);
                String avatarUrl = URLUtil.builderImgUrl(item.avatar,144,144);
                avatarIv.setImageURI(Uri.parse(avatarUrl));
                BadgeView badgeView = new BadgeView(getContext());
                badgeView.setTargetView(avatarIv);
                badgeView.setBackground(10,mRes.getColor(R.color.red));
                badgeView.setGravity(Gravity.TOP|Gravity.RIGHT);
                badgeView.setBadgeCount(item.msg_num);
                badgeView.setBadgeMarginRight(10);
                badgeView.setTextColor(mRes.getColor(R.color.white));
                RatingBar   listitemCompleteRating = (RatingBar) helper.getView(R.id.listitem_complete_rating);

                listitemCompleteRating.setStar(NumFormatUtil.getFmtString(item.star));

                helper.setText(R.id.listitem_complete_leveal,item.star);
                helper.setText(R.id.listitem_complete_title,item.org_name);
                helper.setText(R.id.listitem_complete_adderss,item.org_address);

//                helper.setText(R.id.listitem_complete_price, Html.fromHtml("提车底价\t <font color=\"#e12c0e\">￥ " + item.offer + "</font>"));
                TextView isEvalateTv =  helper.getView(R.id.seecar_waitevaltv);
             View hasVallayout =   helper.getView(R.id.seecar_hasevallayout);
                if(0== item.commentStar){
                    isEvalateTv.setVisibility(View.VISIBLE);
                    hasVallayout.setVisibility(View.GONE);
                    isEvalateTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent toIntent = new Intent(getContext(),ServerEvaluationActivity.class);
                            toIntent.putExtra(Constants.IntentParams.DATA,item);
                            toIntent.putExtra(Constants.IntentParams.ID,mOrderId);
                            startActivity(toIntent);
                        }
                    });
                }else {
                    hasVallayout.setVisibility(View.VISIBLE);
                    helper.setText(R.id.seecar_hasevalscoreTv,item.commentStar+"分");
                    isEvalateTv.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        completeServerPresenter.detachView();
    }
}
