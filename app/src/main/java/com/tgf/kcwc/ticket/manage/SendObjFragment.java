package com.tgf.kcwc.ticket.manage;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.CommonAdapter;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.mvp.model.TicketSendObjModel;
import com.tgf.kcwc.mvp.presenter.TicketSendObjPresenter;
import com.tgf.kcwc.mvp.view.TicketSendObjView;
import com.tgf.kcwc.ticket.widget.SendObjDialog;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.URLUtil;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/2/7 0007
 * E-mail:hekescott@qq.com
 */

public class SendObjFragment extends BaseFragment implements TicketSendObjView {
    private final String KEY_INTENT_TICKETID = "ticketid";
    private final String KEY_INTENT_MOBILE = "mobile";
    private final String KEY_INTENT_USERTYPE = "user_type";
    private ListView mSendObjLv;
    private TicketSendObjPresenter ticketSendObjPresenter;
    private SendObjDialog mSendObjDialog;
    private int dialogNums;
    private ArrayList<TicketSendObjModel.User> mTickeUserlist;
    private WrapAdapter<TicketSendObjModel.User> mSendObjAdapter;
    private TicketQueryActivity ticketQueryAct;
    private View emptyLayout;

    @Override
    protected void updateData() {
        ticketSendObjPresenter.getTicketSendObj(IOUtils.getToken(getContext()), ticketQueryAct.mTicketId);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ticketsendobj;
    }

    @Override
    protected void initView() {
        mSendObjLv = findView(R.id.fragment_sendobj_lv);
        emptyLayout = findView(R.id.empty_layout);
        ticketSendObjPresenter = new TicketSendObjPresenter();
        ticketSendObjPresenter.attachView(this);
        ticketQueryAct = (TicketQueryActivity) getActivity();

        View footerView = View.inflate(getContext(), R.layout.footer_sendobj_listview, null);
        mSendObjLv.addFooterView(footerView);
        mSendObjDialog = new SendObjDialog(getContext());
        mSendObjLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mTickeUserlist!=null){
                    if(position>=0&&position<mTickeUserlist.size()){
                        TicketSendObjModel.User user = mTickeUserlist.get(position);
                        Intent intent = new Intent(getContext(), TicketFellowActivity.class);
                        intent.putExtra(KEY_INTENT_TICKETID, user.tfhId);
                        intent.putExtra(KEY_INTENT_MOBILE, user.mobile);
                        intent.putExtra(KEY_INTENT_USERTYPE, user.userType);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        ticketSendObjPresenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void showSendUserList(ArrayList<TicketSendObjModel.User> tickeUserlist) {
        mTickeUserlist = tickeUserlist;

        if (mTickeUserlist == null || mTickeUserlist.size() == 0) {
            emptyLayout.setVisibility(View.VISIBLE);
            mSendObjLv.setVisibility(View.GONE);
        } else {
            emptyLayout.setVisibility(View.GONE);
            mSendObjLv.setVisibility(View.VISIBLE);
        }

        mSendObjAdapter = new WrapAdapter<TicketSendObjModel.User>(getContext(), mTickeUserlist,
                R.layout.listitem_ticket_sendobj) {
            @Override
            public void convert(final ViewHolder helper, final TicketSendObjModel.User item) {
                TicketSendObjModel.Nums nums = item.nums;
                helper.setImageByUrl(R.id.sendobj_avatar, URLUtil.builderImgUrl(item.userAvatar, 40, 40));
                helper.setText(R.id.sendobj_username, item.userName);
                helper.setText(R.id.sendobj_all, nums.all + "");
                helper.setText(R.id.sendobj_expire, nums.expire + "");
                helper.setText(R.id.sendobj_lose, nums.lose + "");
                helper.setText(R.id.sendobj_use, nums.use + "");
                helper.setText(R.id.sendobj_receive, nums.receive + "");
                helper.getView(R.id.sendobj_sendticket).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mSendObjDialog.setTitle("补发");
                        mSendObjDialog.setMessage(item.userName);
                        mSendObjDialog.numsEd.setText("");
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
                                ticketSendObjPresenter.sendTicket(IOUtils.getToken(getContext()), ticketQueryAct.mTicketId, item.mobile, item.userName, dialogNums, item.userType,helper.getPosition());
                                mSendObjDialog.dismiss();

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
    public void showSendTicketSuccess(int position) {
        mTickeUserlist.get(position).nums.all+=dialogNums;
        mSendObjAdapter.notifyDataSetChanged();
        CommonUtils.showToast(getContext(), "补发成功");
    }

    @Override
    public void faildeMessage(String statusMessage) {
        CommonUtils.showToast(getContext(), statusMessage);
    }


    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }
}
