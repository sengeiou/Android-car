package com.tgf.kcwc.coupon.manage;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.mvp.model.CouponSendObjModel;
import com.tgf.kcwc.mvp.presenter.CouponSendObjPresenter;
import com.tgf.kcwc.mvp.view.CouponSendObjView;
import com.tgf.kcwc.ticket.widget.SendObjDialog;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.StringUtils;
import com.tgf.kcwc.util.URLUtil;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/2/7 0007
 * E-mail:hekescott@qq.com
 */

public class CouponSendObjFragment extends BaseFragment implements CouponSendObjView {
    private final String KEY_INTENT_COUPONID = "couponid";
    private final String KEY_INTENT_MOBILE = "mobile";
    private ListView mSendObjLv;
    //    private TicketSendObjPresenter ticketSendObjPresenter;
    private SendObjDialog mSendObjDialog;
    private int dialogNums;
    private ArrayList<CouponSendObjModel> mTickeUserlist;
    private WrapAdapter<CouponSendObjModel> mSendObjAdapter;
    private CouponQueryActivity ticketQueryAct;
    private CouponSendObjPresenter couponSendObjPresenter;
    private int listId;
    private View emptyLayout;

    @Override
    protected void updateData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ticketsendobj;
    }

    @Override
    protected void initView() {
        mSendObjLv = findView(R.id.fragment_sendobj_lv);
        emptyLayout = findView(R.id.empty_layout);
//        ticketSendObjPresenter = new TicketSendObjPresenter();
//        ticketSendObjPresenter.attachView(this);
        couponSendObjPresenter = new CouponSendObjPresenter();

        couponSendObjPresenter.attachView(this);
        ticketQueryAct = (CouponQueryActivity) getActivity();
        mSendObjDialog = new SendObjDialog(getContext());
        couponSendObjPresenter.getSendObjUser(IOUtils.getToken(getContext()), ticketQueryAct.mTicketId);
//        ticketSendObjPresenter.getTicketSendObj(Constants.token, ticketQueryAct.mTicketId);
        View footerView = View.inflate(getContext(), R.layout.footer_sendobj_couponlistview, null);
        mSendObjLv.addFooterView(footerView);

        mSendObjLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mTickeUserlist!=null){
                    if(position>0&&position<mTickeUserlist.size()-1){

                        //Todo 在未开放
//                        CouponSendObjModel.CouponUser user = mTickeUserlist.get(position).user;
//                        Intent intent = new Intent(getContext(),CouponFellowActivity.class);
//                        intent.putExtra(KEY_INTENT_COUPONID,ticketQueryAct.mTicketId);
//                        intent.putExtra(KEY_INTENT_MOBILE,user.tel);
//                        startActivity(intent);
                    }
                }

            }
        });
    }

    @Override
    public void onDestroyView() {
        couponSendObjPresenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void errorMessage(String msg) {
        CommonUtils.showToast(getContext(), msg);
    }



    @Override
    public void showSendObjUser(ArrayList<CouponSendObjModel> userlist) {
        mTickeUserlist = userlist;
        if(mTickeUserlist==null||mTickeUserlist.size()==0){
            mSendObjLv.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
            return;
        }else {
            mSendObjLv.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        }

        mSendObjAdapter = new WrapAdapter<CouponSendObjModel>(getContext(),R.layout.listitem_ticket_sendobj, mTickeUserlist) {
            @Override
            public void convert(final ViewHolder helper, final CouponSendObjModel item) {
                final CouponSendObjModel.CouponUser couponUser = item.user;
//                TicketSendObjModel.Nums nums = item.user;
                helper.setSimpleDraweeViewURL(R.id.sendobj_avatar, URLUtil.builderImgUrl(couponUser.avatar, 40, 40));
                if (StringUtils.checkNull(couponUser.nickname)) {
                    helper.setText(R.id.sendobj_username, couponUser.nickname);
                }else {
                    helper.setText(R.id.sendobj_username,couponUser.tel);
                }
                helper.setText(R.id.sendobj_all, item.num + "");
                helper.setText(R.id.sendobj_expire, item.expirationNum + "");
                helper.setText(R.id.sendobj_lose, item.lose_num + "");
                helper.setText(R.id.sendobj_use, item.checkNum + "");
                helper.setText(R.id.sendobj_receive, item.getNum + "");
                helper.getView(R.id.sendobj_sendticket).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mSendObjDialog.setTitle("补发");
                        if(!TextUtil.isEmpty(couponUser.nickname)){
                            mSendObjDialog.setMessage(couponUser.nickname);
                        }else if(!TextUtil.isEmpty(couponUser.tel)){
                            mSendObjDialog.setMessage(couponUser.tel);
                        }

                        mSendObjDialog.setNoOnclickListener("取消", new SendObjDialog.onNoOnclickListener() {
                            @Override
                            public void onNoClick() {
                                mSendObjDialog.dismiss();
                            }
                        });
                        mSendObjDialog.setYesOnclickListener("确认", new SendObjDialog.onYesOnclickListener() {
                            @Override
                            public void onYesClick() {
                                dialogNums = mSendObjDialog.getNums();
                                if(dialogNums ==0){
                                    CommonUtils.showToast(getContext(),"请输入补发数量");
                                    return;
                                }
                                couponSendObjPresenter.reSendCoupon(IOUtils.getToken(getContext()), item.id, dialogNums,helper.getPosition());
                                mSendObjDialog.dismiss();
                                mSendObjDialog.numsEd.setText("");

                            }
                        });
                        mSendObjDialog.show();

                    }
                });
            }
        };

        mSendObjLv.setAdapter(mSendObjAdapter);
    }

    @Override
    public void showResendSucces(int pos) {
        CommonUtils.showToast(getContext(),"补发成功");
        mTickeUserlist.get(pos).num += dialogNums;
        mSendObjAdapter.notifyDataSetChanged();
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }
}
