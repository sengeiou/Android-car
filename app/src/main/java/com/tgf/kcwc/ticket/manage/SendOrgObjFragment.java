package com.tgf.kcwc.ticket.manage;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.mvp.model.TicketSendObjModel;
import com.tgf.kcwc.mvp.model.TicketSendOrgObjModel;
import com.tgf.kcwc.mvp.presenter.TicketSendObjPresenter;
import com.tgf.kcwc.mvp.presenter.TicketSendOrgObjPresenter;
import com.tgf.kcwc.mvp.view.TicketSendObjView;
import com.tgf.kcwc.mvp.view.TicketSendOrgObjView;
import com.tgf.kcwc.ticket.widget.SendObjDialog;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;

import java.util.ArrayList;

/**
 * Auther: Scott
 * Date: 2017/2/7 0007
 * E-mail:hekescott@qq.com
 */

public class SendOrgObjFragment extends BaseFragment implements TicketSendObjView, TicketSendOrgObjView {
    private final String KEY_INTENT_TICKETID = "ticketid";
    private final String KEY_INTENT_MOBILE = "mobile";
    private final String KEY_INTENT_USERTYPE = "user_type";
    private ListView mSendObjLv;
    private TicketSendOrgObjPresenter ticketSendObjPresenter;
    private SendObjDialog mSendObjDialog;
    private SendObjDialog mSendObjReduceDialog;
    private int dialogNums;
    private ArrayList<TicketSendOrgObjModel.SendOrgObj> mTickeUserlist;
    private WrapAdapter<TicketSendOrgObjModel.SendOrgObj> mSendObjAdapter;
    private TicketOrgQueryActivity ticketQueryAct;
    private View emptyLayout;

    @Override
    protected void updateData() {
        ticketSendObjPresenter.getTicketSendOrgObj(IOUtils.getToken(getContext()), ticketQueryAct.mTicketId);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ticketorgsendobj;
    }

    @Override
    protected void initView() {
        mSendObjLv = findView(R.id.fragment_sendobj_lv);
        emptyLayout = findView(R.id.empty_layout);
        ticketSendObjPresenter = new TicketSendOrgObjPresenter();
        ticketSendObjPresenter.attachView(this);
        ticketQueryAct = (TicketOrgQueryActivity) getActivity();
        View footerView = View.inflate(getContext(), R.layout.footer_sendobj_listview, null);
        mSendObjLv.addFooterView(footerView);
        mSendObjDialog = new SendObjDialog(getContext());
        mSendObjReduceDialog = new SendObjDialog(getContext());
        mSendObjLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mTickeUserlist != null) {
                    if (position >= 0 && position < mTickeUserlist.size()) {
                        TicketSendOrgObjModel.SendOrgObj user = mTickeUserlist.get(position);
                        Intent intent = new Intent(getContext(), TicketFellowActivity.class);
//                        intent.putExtra(KEY_INTENT_TICKETID, user.tfhId);
//                        intent.putExtra(KEY_INTENT_MOBILE, user.mobile);
//                        intent.putExtra(KEY_INTENT_USERTYPE, user.userType);
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

    }

    @Override
    public void showSendTicketSuccess(int position) {
        mTickeUserlist.get(position).nums += dialogNums;
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

    @Override
    public void showSendObjData(TicketSendOrgObjModel sendOrgObjModelData) {

        mTickeUserlist = sendOrgObjModelData.list;
        if (mTickeUserlist == null || mTickeUserlist.size() == 0) {
            emptyLayout.setVisibility(View.VISIBLE);
            mSendObjLv.setVisibility(View.GONE);
        } else {
            emptyLayout.setVisibility(View.GONE);
            mSendObjLv.setVisibility(View.VISIBLE);
        }

        mSendObjAdapter = new WrapAdapter<TicketSendOrgObjModel.SendOrgObj>(getContext(), mTickeUserlist,
                R.layout.listitem_ticket_sendorgobj) {

            @Override
            public void convert(final ViewHolder helper, final TicketSendOrgObjModel.SendOrgObj item) {
//                TicketSendObjModel.Nums nums = item.nums;
                helper.setImageByUrl(R.id.sendobj_avatar, URLUtil.builderImgUrl(item.avatar, 40, 40));
                helper.setText(R.id.sendobj_username, item.real_name);
                helper.setText(R.id.sendobj_all, item.nums + "");
                helper.setText(R.id.sendobj_htnum, item.ht_user_nums + "");
                helper.setText(R.id.sendobj_transnum, item.transfer_nums + "");
                helper.setText(R.id.sendobj_havenums, item.have_nums + "");
                helper.setText(R.id.sendobj_recnums, item.receive_nums + "");
                helper.setText(R.id.sendobj_usenum, item.use_nums + "");


                helper.getView(R.id.sendorgobj_addticket).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mSendObjDialog.setTitle("请输入补发数量");
                        String showName;
                        if (!TextUtil.isEmpty(item.tel)) {
                            StringBuilder nameSb = new StringBuilder(item.real_name).append("(").append(item.tel).append(")");
                            showName = nameSb.toString();
                        } else {
                            showName = item.real_name;
                        }
                        mSendObjDialog.setMessage(showName);
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

//                                ticketSendObjPresenter.sendTicket(IOUtils.getToken(getContext()), ticketQueryAct.mTicketId, item.mobile, item.userName, dialogNums, item.userType,helper.getPosition());
                                mSendObjDialog.dismiss();

                            }
                        });
                        mSendObjDialog.show();

                    }
                });
                helper.getView(R.id.sendorgobj_reduceticket).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String showName;
                        if (!TextUtil.isEmpty(item.tel)) {
                            StringBuilder nameSb = new StringBuilder(item.real_name).append("(").append(item.tel).append(")");
                            showName = nameSb.toString();
                        } else {
                            showName = item.real_name;
                        }
                        mSendObjReduceDialog.setTitle("请输入收回数量");
                        mSendObjReduceDialog.setMessage(showName);
                        mSendObjReduceDialog.numsEd.setText("");
                        mSendObjReduceDialog.setNoOnclickListener("取消", new SendObjDialog.onNoOnclickListener() {
                            @Override
                            public void onNoClick() {
                                mSendObjReduceDialog.dismiss();
                            }
                        });
                        mSendObjReduceDialog.setYesOnclickListener("确认", new SendObjDialog.onYesOnclickListener() {
                            @Override
                            public void onYesClick() {
                                dialogNums = mSendObjDialog.getNums();

//                                ticketSendObjPresenter.sendTicket(IOUtils.getToken(getContext()), ticketQueryAct.mTicketId, item.mobile, item.userName, dialogNums, item.userType,helper.getPosition());
                                mSendObjReduceDialog.dismiss();

                            }
                        });
                        mSendObjReduceDialog.show();
                    }
                });

            }
        };

        mSendObjLv.setAdapter(mSendObjAdapter);
    }
}
