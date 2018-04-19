package com.tgf.kcwc.me.message;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.autonavi.rtbt.IFrameForRTBT;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.app.MainActivity;
import com.tgf.kcwc.base.BaseActivity;
import com.tgf.kcwc.comment.CommentEditorActivity;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.driving.driv.DrivingDetailsActivity;
import com.tgf.kcwc.driving.driv.ListviewHint;
import com.tgf.kcwc.driving.please.PleasePlayDetailsActivity;
import com.tgf.kcwc.me.UserPageActivity;
import com.tgf.kcwc.mvp.model.BaseArryBean;
import com.tgf.kcwc.mvp.model.BaseBean;
import com.tgf.kcwc.mvp.model.MessagePrivateListBean;
import com.tgf.kcwc.mvp.model.MessageSystemListBean;
import com.tgf.kcwc.mvp.model.PlayListBean;
import com.tgf.kcwc.mvp.presenter.MessageSystemPresenter;
import com.tgf.kcwc.mvp.view.MessageSystemListView;
import com.tgf.kcwc.posting.TopicDetailActivity;
import com.tgf.kcwc.see.sale.SaleDetailActivity;
import com.tgf.kcwc.tripbook.TripbookDetailActivity;
import com.tgf.kcwc.util.CommonUtils;
import com.tgf.kcwc.util.DateFormatUtil;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.TextUtil;
import com.tgf.kcwc.util.URLUtil;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.FunctionView;
import com.tgf.kcwc.view.MyListView;
import com.tgf.kcwc.view.link.Link;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by Administrator on 2017/5/18.
 */

public class SystemMessagesActivity extends BaseActivity implements MessageSystemListView {
    MessageSystemPresenter mMessageSystemPresenter;
    ListView mListView;
    LinearLayout mBottomDelete;
    private TextView mDelete; //删除
    private TextView mEmpty; //清空
    int page = 1;
    boolean isRefresh = true;

    private WrapAdapter<MessageSystemListBean.DataList> mAdapter;

    private WrapAdapter<MessageSystemListBean.Keylist> mKeyListAdapter;

    private WrapAdapter<MessageSystemListBean.TipsText> mListAdapter;

    List<MessageSystemListBean.DataList> Datalist = new ArrayList<>(); //数据源
    private ListviewHint mListviewHint;                         //尾部

    String Name = "";
    String Type = "";
    int eventId = 0;

    @Override
    protected void setUpViews() {

    }

    @Override
    protected void titleBarCallback(ImageButton back, FunctionView function, TextView text) {
        backEvent(back);
        text.setText(Name);
        function.setImageResource(R.drawable.btn_deletewhite);
        function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (MessageSystemListBean.DataList dataList : Datalist) {
                    dataList.isShow = !dataList.isShow;
                }
                if (Datalist.get(0).isShow) {
                    mBottomDelete.setVisibility(View.VISIBLE);
                } else {
                    mBottomDelete.setVisibility(View.GONE);
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Name = getIntent().getStringExtra("name");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_systemmessages);
        initRefreshLayout(mBGDelegate);
        Type = getIntent().getStringExtra("type");
        eventId = getIntent().getIntExtra("event_id", 0);
        mMessageSystemPresenter = new MessageSystemPresenter();
        mMessageSystemPresenter.attachView(this);
        mListView = (ListView) findViewById(R.id.list);
        mBottomDelete = (LinearLayout) findViewById(R.id.bottomdelete);
        mDelete = (TextView) findViewById(R.id.delete);
        mEmpty = (TextView) findViewById(R.id.empty);


        mAdapter = new WrapAdapter<MessageSystemListBean.DataList>(mContext, R.layout.message_system_generalitem, Datalist) {
            @Override
            public void convert(ViewHolder helper, final MessageSystemListBean.DataList item) {
                SimpleDraweeView mExternalHead = helper.getView(R.id.externalhead); //外部头像
                TextView mExternalTime = helper.getView(R.id.externaltime); //外部时间
                LinearLayout mLayout = helper.getView(R.id.layout); //外部LinearLayout

                LinearLayout mHeadPortraitLayout = helper.getView(R.id.headportraitlayout); //头像带回复
                TextView mHeadPortraitName = helper.getView(R.id.headportraitname); //带回复的名字
                TextView mHeadportraittime = helper.getView(R.id.headportraittime); //带回复的时间
                TextView mHeadportraitReply = helper.getView(R.id.headportraitreply); //带回复
                SimpleDraweeView mHeadportraitavatar = helper.getView(R.id.headportraitavatar); //带回复的头像

                LinearLayout mAnnouncement = helper.getView(R.id.announcement); //系统提示
                TextView mAnnouncementName = helper.getView(R.id.announcementname); //系统提示名字
                TextView mAnnouncementTime = helper.getView(R.id.announcementtime); //系统提示时间
                TextView mAnnouncementTitle = helper.getView(R.id.announcementtitle); //提示title

                TextView mWebpage = helper.getView(R.id.webpage); //单链接 网页
                TextView mSkip = helper.getView(R.id.skip); //单链接 跳转
                MyListView mKeyListView = helper.getView(R.id.keylistview);//keylist

                MyListView mListview = helper.getView(R.id.listview);//显示一些文字list  （不知道是什么）

                LinearLayout mDoubleLayout = helper.getView(R.id.doublelayout);//双连接
                TextView mDoubleLayoutLeft = helper.getView(R.id.doublelayoutleft);//双连接左边
                TextView mDoubleLayoutRight = helper.getView(R.id.doublelayoutright);//双连接右边

                LinearLayout mInvitationLayout = helper.getView(R.id.invitationlayout);//帖子
                SimpleDraweeView cover = helper.getView(R.id.invitationcover);//帖子图片
                TextView mInvitationTitle = helper.getView(R.id.invitationtitle);//帖子名字
                TextView mInvitationIntro = helper.getView(R.id.invitationintro);//帖子信息

                final ImageView delete = helper.getView(R.id.delete);//删除

                mExternalHead.setVisibility(View.GONE);
                mHeadPortraitLayout.setVisibility(View.GONE);
                mHeadPortraitName.setVisibility(View.GONE);
                mHeadportraitReply.setVisibility(View.GONE);
                mHeadportraitavatar.setVisibility(View.GONE);
                mAnnouncement.setVisibility(View.GONE);
                mAnnouncementName.setVisibility(View.GONE);
                mAnnouncementTime.setVisibility(View.GONE);
                mAnnouncementTitle.setVisibility(View.GONE);
                mWebpage.setVisibility(View.GONE);
                mSkip.setVisibility(View.GONE);
                mKeyListView.setVisibility(View.GONE);
                mDoubleLayout.setVisibility(View.GONE);
                mInvitationLayout.setVisibility(View.GONE);
                mListview.setVisibility(View.GONE);


                mExternalTime.setVisibility(View.VISIBLE);
                mAnnouncementTitle.setVisibility(View.VISIBLE);
                mExternalTime.setText(item.createtime);
                mAnnouncementTitle.setText(item.appContent.patternContent);
                List<MessageSystemListBean.Content> content = item.appContent.content;
                if (content != null && content.size() != 0) {
                    for (int i = 0; i < content.size(); i++) {
                        if (!TextUtil.isEmpty(content.get(i).color)) {
                            boolean isLink = false;
                            if (content.get(i).urlAlias > 0) {
                                isLink = true;
                            } else {
                                isLink = false;
                            }
                            final int finalI = i;
                            ViewUtil.link(content.get(i).text, mAnnouncementTitle, new Link.OnClickListener() {
                                @Override
                                public void onClick(Object o, String clickedText) {

                                }
                            }, Color.parseColor(content.get(i).color), isLink);

                        }
                    }
                }
                if (item.isShow)

                {
                    delete.setVisibility(View.VISIBLE);
                } else

                {
                    delete.setVisibility(View.GONE);
                }
                delete.setImageResource(R.drawable.icon_deletagray);
                delete.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick(View v) {
                        if (item.isDelete) {
                            delete.setImageResource(R.drawable.icon_deletagray);
                        } else {
                            delete.setImageResource(R.drawable.btn_deletey);
                        }
                        item.isDelete = !item.isDelete;
                    }
                });


                if (item.type == 1)

                {
                    mLayout.setVisibility(View.VISIBLE);
                    mAnnouncementName.setVisibility(View.VISIBLE);
                    mAnnouncement.setVisibility(View.VISIBLE);
                    mAnnouncementTime.setVisibility(View.VISIBLE);
                    mLayout.setBackground(mRes.getDrawable(R.drawable.message_layout_bg));
                    mAnnouncementName.setText(item.title);
                    mAnnouncementTime.setText(DateFormatUtil.timeLogic(item.createtime));
                } else if (item.type == 2)

                {
                    mAnnouncementTitle.setVisibility(View.VISIBLE);
                    mHeadPortraitLayout.setVisibility(View.VISIBLE);
                    mInvitationLayout.setVisibility(View.VISIBLE);
                    mHeadportraitavatar.setVisibility(View.VISIBLE);
                    mHeadPortraitName.setVisibility(View.VISIBLE);
                    mHeadportraittime.setVisibility(View.VISIBLE);
                    cover.setVisibility(View.VISIBLE);
                    mInvitationTitle.setVisibility(View.VISIBLE);
                    mInvitationIntro.setVisibility(View.VISIBLE);
                    mLayout.setVisibility(View.VISIBLE);
                    mLayout.setBackground(mRes.getDrawable(R.drawable.message_layout_bg));
                    mHeadportraitavatar.setImageURI(URLUtil.builderImgUrl(item.logo.get(0).linkUrl, 144, 144));
                    mHeadPortraitName.setText(item.logo.get(0).linkTag);
                    cover.setImageURI(URLUtil.builderImgUrl(item.postinglogo, 144, 144));
                    mInvitationTitle.setText(item.postingtitle);
                    mInvitationIntro.setText(item.postingoutlining);

                    if (item.keylist != null && item.keylist.size() != 0) {
                        mKeyListView.setVisibility(View.VISIBLE);
                        List<MessageSystemListBean.Keylist> stringslist = new ArrayList<>(); //数据源
                        stringslist.addAll(item.keylist);
                        mKeyListAdapter = new WrapAdapter<MessageSystemListBean.Keylist>(mContext, R.layout.system_generaitem_item, stringslist) {
                            @Override
                            public void convert(ViewHolder helper, MessageSystemListBean.Keylist item) {
                                TextView mKey = helper.getView(R.id.key);
                                TextView mVal = helper.getView(R.id.val);
                                mKey.setText(item.key);
                                mVal.setText(item.val);
                            }
                        };
                        mKeyListView.setAdapter(mKeyListAdapter);
                    } else {
                        mKeyListView.setVisibility(View.GONE);
                    }

                    mHeadportraitavatar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map<String, Serializable> args = new HashMap<String, Serializable>();
                            args.put(Constants.IntentParams.ID, item.reply.receiverUid);
                            CommonUtils.startNewActivity(mContext, args, UserPageActivity.class);
                        }
                    });
                    mHeadPortraitName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map<String, Serializable> args = new HashMap<String, Serializable>();
                            args.put(Constants.IntentParams.ID, item.senderUid);
                            CommonUtils.startNewActivity(mContext, args, UserPageActivity.class);
                        }
                    });

                    if (Type.equals("6")) {
                        mHeadportraittime.setText(DateFormatUtil.timeLogic(item.createtime) + "赞了这篇帖子");
                        mHeadportraitReply.setVisibility(View.GONE);
                    } else {
                        mHeadportraittime.setText(DateFormatUtil.timeLogic(item.createtime) + "回复了这篇帖子");
                        mHeadportraitReply.setVisibility(View.VISIBLE);
                    }

                    mHeadportraitReply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MessageSystemListBean.Reply reply = item.reply;
                            Map<String, Serializable> args = new HashMap<String, Serializable>();
                            args.put(Constants.IntentParams.INTENT_TYPE, reply.module);
                            args.put(Constants.IntentParams.ID, reply.resourceId);
                            args.put(Constants.IntentParams.ID2, reply.pid);
                            args.put(Constants.IntentParams.ID3, reply.receiverUid);
                            args.put(Constants.IntentParams.ID4, reply.vehicleType);
                            CommonUtils.startNewActivity(mContext, args, CommentEditorActivity.class);
                        }
                    });
                    mInvitationLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            select(item);
                        }
                    });

                    mHeadportraitavatar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map<String, Serializable> args = new HashMap<String, Serializable>();
                            args.put(Constants.IntentParams.ID, item.senderUid);
                            CommonUtils.startNewActivity(mContext, args, UserPageActivity.class);
                        }
                    });

                } else if (item.type == 3 || item.type == 4 || item.type == 9)

                {
                    mAnnouncement.setVisibility(View.VISIBLE);
                    mAnnouncementTime.setVisibility(View.VISIBLE);
                    mAnnouncementName.setVisibility(View.VISIBLE);

                    mAnnouncementTime.setText(DateFormatUtil.timeLogic(item.createtime));
                    mAnnouncementName.setText(item.title);
                    mLayout.setBackground(mRes.getDrawable(R.drawable.message_layout_bg));

                    if (item.keylist != null && item.keylist.size() != 0) {
                        mKeyListView.setVisibility(View.VISIBLE);
                        List<MessageSystemListBean.Keylist> stringslist = new ArrayList<>(); //数据源
                        stringslist.addAll(item.keylist);
                        mKeyListAdapter = new WrapAdapter<MessageSystemListBean.Keylist>(mContext, R.layout.system_generaitem_item, stringslist) {
                            @Override
                            public void convert(ViewHolder helper, MessageSystemListBean.Keylist item) {
                                TextView mKey = helper.getView(R.id.key);
                                TextView mVal = helper.getView(R.id.val);
                                mKey.setText(item.key);
                                mVal.setText(item.val);
                            }
                        };
                        mKeyListView.setAdapter(mKeyListAdapter);
                    } else {
                        mKeyListView.setVisibility(View.GONE);
                    }
                    if (item.jumplist != null && item.jumplist.size() != 0) {
                        if (item.jumplist.size() == 1) {
                            mSkip.setVisibility(View.VISIBLE); // 单双连接暂时屏蔽
                            mDoubleLayout.setVisibility(View.GONE);
                            mSkip.setText(item.jumplist.get(0).urlName);
                            mSkip.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int urlAlias = item.jumplist.get(0).urlAlias;
                                    selectSkip(urlAlias,item.label);
                                    // CommonUtils.showToast(mContext, item.jumplist.get(0).urlName);
                                }
                            });
                        } else if (item.jumplist.size() == 2) {
                            mSkip.setVisibility(View.GONE);
                            mDoubleLayout.setVisibility(View.VISIBLE); // 单双连接暂时屏蔽
                            mDoubleLayoutLeft.setText(item.jumplist.get(0).urlName);
                            mDoubleLayoutRight.setText(item.jumplist.get(1).urlName);
                            mDoubleLayoutLeft.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int urlAlias = item.jumplist.get(0).urlAlias;
                                    selectSkip(urlAlias,item.label);
                                    //CommonUtils.showToast(mContext, item.jumplist.get(0).urlName);
                                }
                            });
                            mDoubleLayoutRight.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int urlAlias = item.jumplist.get(1).urlAlias;
                                    selectSkip(urlAlias,item.label);
                                    //CommonUtils.showToast(mContext, item.jumplist.get(1).urlName);
                                }
                            });
                        }
                    } else {
                        mSkip.setVisibility(View.GONE);
                        mDoubleLayout.setVisibility(View.GONE);
                    }
                    if (item.tipsText != null && item.tipsText.size() != 0) {
                        mListview.setVisibility(View.VISIBLE);
                        mListAdapter = new WrapAdapter<MessageSystemListBean.TipsText>(mContext, R.layout.system_generaitemlist_item, item.tipsText) {
                            @Override
                            public void convert(ViewHolder helper, MessageSystemListBean.TipsText item) {
                                TextView name = helper.getView(R.id.name);
                                name.setText(item.label);
                            }
                        };
                        mListview.setAdapter(mListAdapter);
                    } else {
                        mListview.setVisibility(View.GONE);
                    }


                } else if (item.type == 5 || item.type == 6)

                {
                    mExternalHead.setVisibility(View.VISIBLE);
                    mAnnouncementTitle.setVisibility(View.VISIBLE);
                    mLayout.setVisibility(View.VISIBLE);
                    mLayout.setBackground(mRes.getDrawable(R.drawable.messagewhite));
                    if (item.logo.size() != 0) {
                        mExternalHead.setImageURI(URLUtil.builderImgUrl(item.logo.get(0).linkUrl, 144, 144));
                    }

                    if (item.keylist != null && item.keylist.size() != 0) {
                        mKeyListView.setVisibility(View.VISIBLE);
                        List<MessageSystemListBean.Keylist> stringslist = new ArrayList<>(); //数据源
                        stringslist.addAll(item.keylist);
                        mKeyListAdapter = new WrapAdapter<MessageSystemListBean.Keylist>(mContext, R.layout.system_generaitem_item, stringslist) {
                            @Override
                            public void convert(ViewHolder helper, MessageSystemListBean.Keylist item) {
                                TextView mKey = helper.getView(R.id.key);
                                TextView mVal = helper.getView(R.id.val);
                                mKey.setText(item.key);
                                mVal.setText(item.val);
                            }
                        };
                        mKeyListView.setAdapter(mKeyListAdapter);
                    } else {
                        mKeyListView.setVisibility(View.GONE);
                    }
                    if (item.jumplist != null && item.jumplist.size() != 0) {
                        if (item.jumplist.size() == 1) {
                            mSkip.setVisibility(View.VISIBLE); // 单双连接暂时屏蔽
                            mDoubleLayout.setVisibility(View.GONE);
                            mSkip.setText(item.jumplist.get(0).urlName);
                            mSkip.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int urlAlias = item.jumplist.get(0).urlAlias;
                                    selectSkip(urlAlias,item.label);
                                    // CommonUtils.showToast(mContext, item.jumplist.get(0).urlName);
                                }
                            });
                        } else if (item.jumplist.size() == 2) {
                            mSkip.setVisibility(View.GONE);
                            mDoubleLayout.setVisibility(View.VISIBLE); // 单双连接暂时屏蔽
                            mDoubleLayoutLeft.setText(item.jumplist.get(0).urlName);
                            mDoubleLayoutRight.setText(item.jumplist.get(1).urlName);
                            mDoubleLayoutLeft.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int urlAlias = item.jumplist.get(0).urlAlias;
                                    selectSkip(urlAlias,item.label);
                                    //CommonUtils.showToast(mContext, item.jumplist.get(0).urlName);
                                }
                            });
                            mDoubleLayoutRight.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int urlAlias = item.jumplist.get(1).urlAlias;
                                    selectSkip(urlAlias,item.label);
                                    //CommonUtils.showToast(mContext, item.jumplist.get(1).urlName);
                                }
                            });
                        }
                    } else {
                        mSkip.setVisibility(View.GONE);
                        mDoubleLayout.setVisibility(View.GONE);
                    }
                } else if (item.type == 7)

                {
                    mLayout.setVisibility(View.VISIBLE);
                    mExternalHead.setVisibility(View.VISIBLE);
                    mAnnouncementTitle.setVisibility(View.VISIBLE);
                    mExternalHead.setImageURI(URLUtil.builderImgUrl(item.logo.get(0).linkUrl, 144, 144));
                    mLayout.setBackground(mRes.getDrawable(R.drawable.messagewhite));
                }

            }

            /**
             * 单双连接
             * @param urlAlias
             */

            public void selectSkip(int urlAlias,String model) {
                MessageSkipManagement.skipModule(mContext,urlAlias, model);
            }

            public void select(final MessageSystemListBean.DataList item) {
                Map<String, Serializable> args = null;
                if (item.model.equals("goods")) { //车主自售
                    args = new HashMap<String, Serializable>();
                    args.put(Constants.IntentParams.ID, item.postingid);
                    CommonUtils.startNewActivity(mContext, args, SaleDetailActivity.class);
                } else if (item.model.equals("play")) { //请你玩
                    args = new HashMap<String, Serializable>();
                    args.put("id", item.postingid + "");
                    CommonUtils.startNewActivity(mContext, args, PleasePlayDetailsActivity.class);
                } else if (item.model.equals("cycle")) { //开车去
                    args = new HashMap<String, Serializable>();
                    args.put("id", item.postingid + "");
                    CommonUtils.startNewActivity(mContext, args, DrivingDetailsActivity.class);
                } else if (item.model.equals("words")) {//普通帖子
                    args = new HashMap<String, Serializable>();
                    args.put(Constants.IntentParams.ID, item.postingid + "");
                    CommonUtils.startNewActivity(mContext, args, TopicDetailActivity.class);
                } else if (item.model.equals("evaluate")) { //达人评测
                    args.put(Constants.IntentParams.ID, item.postingid + "");
                    CommonUtils.startNewActivity(mContext, args, TopicDetailActivity.class);
                } else if (item.model.equals("roadbook")) {//路书
                    args = new HashMap<String, Serializable>();
                    args.put(Constants.IntentParams.ID, item.postingid);
                    CommonUtils.startNewActivity(mContext, args, TripbookDetailActivity.class);
                } else {
                    CommonUtils.showToast(mContext, "正在开发中");
                }
            }

        };
        mListView.setAdapter(mAdapter);
        mListviewHint = new ListviewHint(mContext);
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> stringList = new ArrayList<String>();
                for (MessageSystemListBean.DataList dataList : Datalist) {
                    if (dataList.isDelete) {
                        stringList.add(dataList.id + "");
                    }
                }
                if (stringList.size() != 0) {
                    Gson gson = new Gson();
                    String letteruser = gson.toJson(stringList);
                    mMessageSystemPresenter.getDelmessage(IOUtils.getToken(mContext), letteruser);
                } else {
                    CommonUtils.showToast(mContext, "请选择删除对象");
                }
            }
        });
        mEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> stringList = new ArrayList<String>();
                for (MessageSystemListBean.DataList dataList : Datalist) {
                    stringList.add(dataList.id + "");
                }
                if (stringList.size() != 0) {
                    Gson gson = new Gson();
                    String letteruser = gson.toJson(stringList);
                    mMessageSystemPresenter.getDelmessage(IOUtils.getToken(mContext), letteruser);
                } else {
                    CommonUtils.showToast(mContext, "请选择删除对象");
                }
            }
        });

        mMessageSystemPresenter.getAppStatistics(IOUtils.getToken(mContext), Type, page, eventId);
    }


    BGARefreshLayout.BGARefreshLayoutDelegate mBGDelegate = new BGARefreshLayout.BGARefreshLayoutDelegate() {

        @Override
        public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            // 在这里加载最新数据

            //            if (HttpUtils.isConnectNetwork(getApplicationContext())) {
            //                // 如果网络可用，则加载网络数据
            //
            //                loadMore();
            //            } else {
            //                // 网络不可用，结束下拉刷新
            //                HttpUtils.registerNWReceiver(getApplicationContext());
            //                mRefreshLayout.endRefreshing();
            //            }
            // mPageIndex = 1;
            page = 1;
            isRefresh = true;
            mListView.removeFooterView(mListviewHint);
            loadMore();
        }

        @Override
        public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
            // 在这里加载更多数据，或者更具产品需求实现上拉刷新也可以

            //            if (HttpUtils.isConnectNetwork(getApplicationContext())) {
            //                loadMore();
            //                return true;
            //            } else {
            //                // 网络不可用，返回false，不显示正在加载更多
            //                HttpUtils.registerNWReceiver(getApplicationContext());
            //                mRefreshLayout.endRefreshing();
            //                return false;
            //            }
            //loadMore();
            if (isRefresh) {
                page++;
                loadMore();
            }
            return false;
        }
    };

    public void loadMore() {
        mMessageSystemPresenter.getAppStatistics(IOUtils.getToken(mContext), Type, page, eventId);
    }

    @Override
    public void StatisticsListSucceed(MessageSystemListBean messageListBean) {
        stopRefreshAll();
        if (page == 1) {
            Datalist.clear();
        }
        if (messageListBean.data.lists == null || messageListBean.data.lists.size() == 0) {
            isRefresh = false;
            if (page != 1) {
                mListView.addFooterView(mListviewHint);
            }
        }
        Datalist.addAll(messageListBean.data.lists);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void PrivateListSucceed(MessagePrivateListBean messageListBean) {

    }

    @Override
    public void DeleteSucceed(BaseArryBean baseBean) {
        mBottomDelete.setVisibility(View.GONE);
        mMessageSystemPresenter.getAppStatistics(IOUtils.getToken(mContext), Type, page, eventId);
    }

    @Override
    public void dataListDefeated(String msg) {
        CommonUtils.showToast(mContext, msg);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public Context getContext() {
        return mContext;
    }

}
