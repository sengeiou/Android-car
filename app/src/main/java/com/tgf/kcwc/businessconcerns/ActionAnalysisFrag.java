package com.tgf.kcwc.businessconcerns;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tgf.kcwc.R;
import com.tgf.kcwc.adapter.WrapAdapter;
import com.tgf.kcwc.base.BaseFragment;
import com.tgf.kcwc.common.Constants;
import com.tgf.kcwc.mvp.model.CommunityModel;
import com.tgf.kcwc.mvp.model.CommunityView;
import com.tgf.kcwc.mvp.model.CustomizedCollectModel;
import com.tgf.kcwc.mvp.model.MorePopupwindowBean;
import com.tgf.kcwc.mvp.model.SearchModel;
import com.tgf.kcwc.mvp.presenter.CommunityPresenter;
import com.tgf.kcwc.mvp.presenter.CustomizedCollectPresenter;
import com.tgf.kcwc.mvp.presenter.SearchPresenter;
import com.tgf.kcwc.mvp.view.CustomizedCollectView;
import com.tgf.kcwc.mvp.view.SearchView;
import com.tgf.kcwc.util.IOUtils;
import com.tgf.kcwc.util.ViewUtil;
import com.tgf.kcwc.view.FlexBoxLayout;
import com.tgf.kcwc.view.HistogramView;
import com.tgf.kcwc.view.LinePieView;
import com.tgf.kcwc.view.MorePopupWindow;
import com.tgf.kcwc.view.MyGridView;
import com.tgf.kcwc.view.MyListView;
import com.tgf.kcwc.view.StraightChartView;

import java.util.ArrayList;
import java.util.List;

/**
 * @anthor noti
 * @time 2017/8/3 8:53
 * 行为分析
 */

public class ActionAnalysisFrag extends BaseFragment implements View.OnClickListener, CommunityView, CustomizedCollectView, SearchView {
    private ScrollView scrollView;
    //定制数据分析 behaviour
    private LinearLayout customizedAll;
    private TextView customizedTitle;
    private TextView customizedTime;

    private MyListView customizedListView;
    private ArrayList<CustomizedCollectModel.CustomMadeItem.ListItem> customizedList = new ArrayList<>();
    private WrapAdapter customizedAdapter;

    //检索行为分析
    private LinearLayout brandPriceLl;
    private LinearLayout searchAll;
    private LinearLayout searchPriceLl;
    private LinearLayout searchBrandLl;
    private TextView searchTitle;
    private TextView searchTime;
    private TextView searchBrand;
    private LinePieView searchLine;
    private TextView searchPrice;
    private StraightChartView searchStraight;
    //浏览行为分析
    private TextView browsedTitle;
    private TextView browsedTime;
    private HistogramView browsedHistogram;
    private MyListView browsedListView;
    //收藏行为分析 behaviour
    private LinearLayout collectBrandLl;
    private LinearLayout collectModel;
    private LinearLayout collectAll;
    private TextView collectTitle;
    private TextView collectTime;
    private TextView collectBrand;
    private LinePieView collectLine;
    private TextView collectPriceTitle;
    private TextView collectPriceTime;
    private HistogramView collectHistogram;
    //观展行为分析
    private TextView exhibitionTitle;
    private TextView exhibitionTime;
    private TextView exhibitionBrandTitle;
    private TextView exhibitionBrandTime;
    //社区行为分析
    private LinearLayout communityAll;
    private TextView communityTitle;
    private TextView communityTime;
    private LinearLayout keywordHobbyLl;
    private LinearLayout communityHobbyLl;
    private TextView communityHobbyTag;
    private MyGridView communityHobbyGrid;
    private ArrayList<CommunityModel.HobbyItem> hobbyItems = new ArrayList<>();
    private WrapAdapter hobbyAdapter;

    ArrayList<String> keywordsItems = new ArrayList<>();
    private LinearLayout communityKeywordLl;
    private TextView communityKeyword;
    private FlexBoxLayout communityKeywordFlex;
    private TextView communityBrowsedNote;
    private TextView communityGroup;
    //社区
    CommunityPresenter communityPresenter;
    //定制和收藏
    CustomizedCollectPresenter customizedCollectPresenter;
    //检索
    SearchPresenter searchPresenter;
    //popwindow集合数据
    List<MorePopupwindowBean> popList = new ArrayList<>();
    //好友id
    private int friendId;
    //社区行为分析时间间隔（1,2,3,4,5）
    private int communityTimeInterval = 1;
    //检索
    private int searchTimeInterval = 1;
    private String[] popValues;

    public ActionAnalysisFrag(int friendId) {
        super();
        this.friendId = friendId;
    }

    @Override
    protected void updateData() {
//        //定制及收藏
//        customizedCollectPresenter = new CustomizedCollectPresenter();
//        customizedCollectPresenter.attachView(this);
//        customizedCollectPresenter.getCustomizedCollect(Constants.SALE_TOKEN, friendId);
//        //检索
//        searchPresenter = new SearchPresenter();
//        searchPresenter.attachView(this);
//        searchPresenter.getSearch(Constants.SALE_TOKEN, friendId, searchTimeInterval);
//        //社区
//        communityPresenter = new CommunityPresenter();
//        communityPresenter.attachView(this);
//        communityPresenter.getCommunity(Constants.SALE_TOKEN, friendId, communityTimeInterval);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_action_analysis;
    }

    @Override
    protected void initView() {
        setUserVisibleHint(true);
        scrollView = findView(R.id.analysis_sv);

        customizedAll = findView(R.id.customized_all);
        customizedTitle = findView(R.id.customized_time);
        brandPriceLl = findView(R.id.brandPriceLl);
        searchAll = findView(R.id.search_all);
        searchPriceLl = findView(R.id.search_price_ll);
        searchBrandLl = findView(R.id.search_brand_ll);
        searchTitle = findView(R.id.search_time);
        customizedTime = findView(R.id.customized_select_time);
        searchTime = findView(R.id.search_select_time);
        searchBrand = findView(R.id.search_brand);
        customizedListView = findView(R.id.customized_lv);
        searchLine = findView(R.id.search_line_pie_view);
        searchPrice = findView(R.id.search_price);
        searchStraight = findView(R.id.search_straight_chart);
        browsedTitle = findView(R.id.browsed_time);
        browsedTime = findView(R.id.browsed_select_time);
        browsedHistogram = findView(R.id.browsed_histogram_view);
        browsedListView = findView(R.id.browsed_time_lv);
        collectModel = findView(R.id.collect_model);
        collectBrandLl = findView(R.id.collect_brand_ll);
        collectBrand = findView(R.id.collect_brand);
        collectAll = findView(R.id.collect_all);
        collectTitle = findView(R.id.collect_time);
        collectTime = findView(R.id.collect_select_time);
        collectLine = findView(R.id.collect_line_pie_view);
        collectPriceTitle = findView(R.id.collect_price_title);
        collectPriceTime = findView(R.id.collect_price);
        collectHistogram = findView(R.id.collect_histogram);
        exhibitionTitle = findView(R.id.exhibition_time);
        exhibitionTime = findView(R.id.exhibition_select_time);
        exhibitionBrandTitle = findView(R.id.exhibition_brand_title);
        exhibitionBrandTime = findView(R.id.exhibition_brand);
        communityAll = findView(R.id.community_all);
        communityTitle = findView(R.id.community_time);
        communityTime = findView(R.id.community_select_time);
        keywordHobbyLl = findView(R.id.keywordHobbyLl);
        communityHobbyLl = findView(R.id.community_hobby_ll);
        communityHobbyTag = findView(R.id.community_hobby_tag);
        communityHobbyGrid = findView(R.id.community_grid);
        communityKeywordLl = findView(R.id.community_keyword_ll);
        communityKeyword = findView(R.id.community_keyword);
        communityKeywordFlex = findView(R.id.community_flex);
        communityBrowsedNote = findView(R.id.community_browsed_note);
        communityGroup = findView(R.id.community_group);

        communityTime.setOnClickListener(this);
        exhibitionTime.setOnClickListener(this);
        collectTime.setOnClickListener(this);
        searchTime.setOnClickListener(this);
        browsedTime.setOnClickListener(this);
        customizedTime.setOnClickListener(this);

        initPop();
//        String[] model = new String[]{"长安","宝马","奔驰"};
//        String[] progress = new String[]{"120","80","2"};
//        collectHistogram.setData(progress,model);
    }

    @Override
    protected void initData() {
//        super.initData();
        //定制及收藏
        customizedCollectPresenter = new CustomizedCollectPresenter();
        customizedCollectPresenter.attachView(this);
        customizedCollectPresenter.getCustomizedCollect(IOUtils.getToken(getContext()), friendId);
        //检索
        searchPresenter = new SearchPresenter();
        searchPresenter.attachView(this);
        searchPresenter.getSearch(IOUtils.getToken(getContext()), friendId, searchTimeInterval);
        //社区
        communityPresenter = new CommunityPresenter();
        communityPresenter.attachView(this);
        communityPresenter.getCommunity(IOUtils.getToken(getContext()), friendId, communityTimeInterval);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.customized_select_time:
                break;
            case R.id.browsed_select_time:
                break;
            case R.id.search_select_time:
                MorePopupWindow morePopupWindow = new MorePopupWindow(getActivity(),
                        popList, new MorePopupWindow.MoreOnClickListener() {
                    @Override
                    public void moreOnClickListener(int position, MorePopupwindowBean item) {
                        switch (position) {
                            case 0://一周内
                                searchTimeInterval = 1;
                                searchPresenter.getSearch(IOUtils.getToken(getContext()), friendId, searchTimeInterval);
                                searchTitle.setText("检索行为分析(一周内)");
                                break;
                            case 1://一个月内
                                searchTimeInterval = 2;
                                searchPresenter.getSearch(IOUtils.getToken(getContext()), friendId, searchTimeInterval);
                                searchTitle.setText("检索行为分析(一个月内)");
                                break;
                            case 2://3个月内
                                searchTimeInterval = 3;
                                searchPresenter.getSearch(IOUtils.getToken(getContext()), friendId, searchTimeInterval);
                                searchTitle.setText("检索行为分析(3个月内)");
                                break;
                            case 3://6个月内
                                searchTimeInterval = 4;
                                searchPresenter.getSearch(IOUtils.getToken(getContext()), friendId, searchTimeInterval);
                                searchTitle.setText("检索行为分析(6个月内)");
                                break;
                            case 4://12个月内
                                searchTimeInterval = 5;
                                searchPresenter.getSearch(IOUtils.getToken(getContext()), friendId, searchTimeInterval);
                                searchTitle.setText("检索行为分析(12个月内)");
                                break;
                        }
                    }
                });
                morePopupWindow.showPopupWindow(searchTime);
                break;
            case R.id.collect_select_time:
                break;
            case R.id.exhibition_select_time:
                break;
            case R.id.community_select_time:
                MorePopupWindow morePopupWindow1 = new MorePopupWindow(getActivity(),
                        popList, new MorePopupWindow.MoreOnClickListener() {
                    @Override
                    public void moreOnClickListener(int position, MorePopupwindowBean item) {
                        switch (position) {
                            case 0://一周内
                                communityTimeInterval = 1;
                                communityPresenter.getCommunity(IOUtils.getToken(getContext()), friendId, communityTimeInterval);
                                communityTitle.setText("社区行为分析(一周内)");
                                break;
                            case 1://一个月内
                                communityTimeInterval = 2;
                                communityPresenter.getCommunity(IOUtils.getToken(getContext()), friendId, communityTimeInterval);
                                communityTitle.setText("社区行为分析(一个月内)");
                                break;
                            case 2://3个月内
                                communityTimeInterval = 3;
                                communityPresenter.getCommunity(IOUtils.getToken(getContext()), friendId, communityTimeInterval);
                                communityTitle.setText("社区行为分析(3个月内)");
                                break;
                            case 3://6个月内
                                communityTimeInterval = 4;
                                communityPresenter.getCommunity(IOUtils.getToken(getContext()), friendId, communityTimeInterval);
                                communityTitle.setText("社区行为分析(6个月内)");
                                break;
                            case 4://12个月内
                                communityTimeInterval = 5;
                                communityPresenter.getCommunity(IOUtils.getToken(getContext()), friendId, communityTimeInterval);
                                communityTitle.setText("社区行为分析(12个月内)");
                                break;
                        }
                    }
                });
                morePopupWindow1.showPopupWindow(communityTime);
                break;
        }
    }

    /**
     * 初始化popwindow选项
     */
    public void initPop() {
        popList.clear();
        popValues = mRes.getStringArray(R.array.business_analysis_time);
        for (String value : popValues) {
            MorePopupwindowBean morePopupwindowBean = null;
            morePopupwindowBean = new MorePopupwindowBean();
            morePopupwindowBean.title = value;
            popList.add(morePopupwindowBean);
        }
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        showLoadingIndicator(active);
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void showCustomized(ArrayList<CustomizedCollectModel.CustomMadeItem.ListItem> listItem) {
        if (listItem == null || listItem.size() == 0) {
            customizedAll.setVisibility(View.GONE);
            customizedListView.setVisibility(View.GONE);
        } else {
            customizedAll.setVisibility(View.VISIBLE);
            customizedListView.setVisibility(View.VISIBLE);
        }
        customizedList.clear();
        customizedList.addAll(listItem);
        if (null != customizedAdapter) {
            customizedListView.setAdapter(customizedAdapter);
            customizedAdapter.notifyDataSetChanged();
        } else {
            initCustomizedAdapter();
            customizedListView.setAdapter(customizedAdapter);
            ViewUtil.setListViewHeightBasedOnChildren(customizedListView);
        }
    }

    @Override
    public void showCollect(CustomizedCollectModel.CollectItem collectItem) {
        if (collectItem.factoryItems != null && collectItem.factoryItems.size() != 0 && collectItem.seriesItems != null && collectItem.seriesItems.size() != 0) {
            collectAll.setVisibility(View.VISIBLE);
        } else {
            collectAll.setVisibility(View.GONE);
        }
        ArrayList<CustomizedCollectModel.CollectItem.FactoryItem> factoryItem = new ArrayList<>();
        ArrayList<CustomizedCollectModel.CollectItem.SeriesItem> seriesItem = new ArrayList<>();
        //品牌
        double[] num = null;
        String[] name = null;
        //设置收藏——倾向品牌
        if (collectItem.factoryItems != null && collectItem.factoryItems.size() != 0) {
            factoryItem.addAll(collectItem.factoryItems);
            collectBrandLl.setVisibility(View.VISIBLE);
            //取第一个
            collectBrand.setText(factoryItem.get(0).factoryName);
            num = new double[factoryItem.size()];
            name = new String[factoryItem.size()];
            for (int i = 0; i < factoryItem.size(); i++) {
                num[i] = factoryItem.get(i).percent;
                name[i] = factoryItem.get(i).factoryName;
            }
            collectLine.setData(num, name);
        } else {
            collectBrandLl.setVisibility(View.GONE);
        }
        //车系
        String[] seriesNum = null;
        String[] seriesName = null;
        //设置收藏——倾向车系
        if (collectItem.seriesItems != null && collectItem.seriesItems.size() != 0) {
            seriesItem.addAll(collectItem.seriesItems);
            collectModel.setVisibility(View.VISIBLE);
            //取第一个
            collectPriceTime.setText(seriesItem.get(0).seriesName);
            seriesNum = new String[seriesItem.size()];
            seriesName = new String[seriesItem.size()];
            for (int i = 0; i < seriesItem.size(); i++) {
                seriesNum[i] = String.valueOf(seriesItem.get(i).number);
                seriesName[i] = seriesItem.get(i).seriesName;
            }
            if (seriesName.length != 0 && seriesName != null && seriesNum.length != 0 && seriesNum != null){
                collectHistogram.setData(seriesNum, seriesName);
            }
        } else {
            collectModel.setVisibility(View.GONE);
        }
    }

    public void initCustomizedAdapter() {
        customizedAdapter = new WrapAdapter<CustomizedCollectModel.CustomMadeItem.ListItem>(mContext, customizedList, R.layout.customized_data_item) {
            @Override
            public void convert(ViewHolder helper, CustomizedCollectModel.CustomMadeItem.ListItem item) {
                helper.setText(R.id.customized_time, item.createTime);
//                item.countries+item.level+item.factoryNames+item.level
                helper.setText(R.id.customized_brand, item.factoryNames);
                helper.setText(R.id.customized_price, item.price);
                helper.setText(R.id.customized_seat, item.seatNumRange);
            }
        };
    }

    @Override
    public void showSearch(SearchModel searchModels) {
        // TODO: 2017/10/24
        if (searchModels == null) {
            brandPriceLl.setVisibility(View.GONE);
        } else {
            brandPriceLl.setVisibility(View.VISIBLE);
        }
        ArrayList<SearchModel.FactoryItem> factoryItem = new ArrayList<>();
        String[] names = null;

        ArrayList<SearchModel.PriceItem> priceItem = new ArrayList<>();
        double[] nums = null;
        if (null != searchModels.factoryItems && searchModels.factoryItems.size() != 0) {
            factoryItem.clear();
            factoryItem.addAll(searchModels.factoryItems);
            searchBrandLl.setVisibility(View.VISIBLE);
            searchBrand.setText(factoryItem.get(0).factoryName);
            nums = new double[factoryItem.size()];
            names = new String[factoryItem.size()];
            for (int i = 0; i < factoryItem.size(); i++) {
                nums[i] = factoryItem.get(i).percent;
                names[i] = factoryItem.get(i).factoryName;
            }
            searchLine.setData(nums, names);
        } else {
            searchBrandLl.setVisibility(View.GONE);
        }
        //价格
        if (null != searchModels.priceItems && searchModels.priceItems.size() != 0) {
            priceItem.clear();
            priceItem.addAll(searchModels.priceItems);
            searchPriceLl.setVisibility(View.VISIBLE);
            String[] price = new String[priceItem.size()];
            double[] percent= new double[priceItem.size()];
            double maxDou = -1;
            String priceRange = "";
            for (int i = 0; i < priceItem.size(); i++) {
                price[i] = priceItem.get(i).priceRange;
                percent[i] = priceItem.get(i).percent;
                String[] priceArr = price[i].split("-");
                priceRange = priceItem.get(i).priceRange;
                maxDou = Math.max(maxDou,Math.max(Double.parseDouble(priceArr[0]),Double.parseDouble(priceArr[1])));
            }
            searchPrice.setText(priceRange + "万");
            searchStraight.setData(price,percent,maxDou);
        } else {
            searchPriceLl.setVisibility(View.GONE);
        }
    }

    @Override
    public void showCommunity(CommunityModel communityModel) {
        if (communityModel != null) {
            keywordHobbyLl.setVisibility(View.VISIBLE);
        } else {
            keywordHobbyLl.setVisibility(View.GONE);
        }
        //兴趣标签
        if (communityModel.hobbyItem != null && communityModel.hobbyItem.size() != 0) {
            hobbyItems.clear();
            hobbyItems.addAll(communityModel.hobbyItem);
            communityHobbyLl.setVisibility(View.VISIBLE);
            hobbyAdapter = new WrapAdapter<CommunityModel.HobbyItem>(mContext, hobbyItems, R.layout.community_hobby_item) {
                @Override
                public void convert(ViewHolder helper, CommunityModel.HobbyItem item) {
                    helper.setText(R.id.hobby_text, item.name);
                }
            };
            communityHobbyGrid.setAdapter(hobbyAdapter);
            hobbyAdapter.notifyDataSetChanged();
        } else {
            communityHobbyLl.setVisibility(View.GONE);
        }
        //关键字
        if (communityModel.keywordsItem != null && communityModel.keywordsItem.size() != 0) {
            keywordsItems.clear();
            keywordsItems.addAll(communityModel.keywordsItem);
            communityKeywordLl.setVisibility(View.VISIBLE);
            // TODO: 2017/10/24
//            communityKeywordFlex.removeAllViewsInLayout();
            for (int i = 0; i < keywordsItems.size(); i++) {
                TextView keyword = new TextView(mContext);
                keyword.setText(Html.fromHtml("<font color=\"#E9240A\">" + "#" + "</font>" + keywordsItems.get(i)));
                keyword.setTextColor(getResources().getColor(R.color.text_color9));
                keyword.setTextSize(12);
                communityKeywordFlex.setHorizontalSpace(30);
                communityKeywordFlex.setVerticalSpace(12);
                communityKeywordFlex.addView(keyword);
            }
        } else {
            communityKeywordLl.setVisibility(View.GONE);
        }
    }
}
