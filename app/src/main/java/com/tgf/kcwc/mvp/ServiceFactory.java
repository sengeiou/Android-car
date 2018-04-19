package com.tgf.kcwc.mvp;

import com.tgf.kcwc.mvp.model.AboutService;
import com.tgf.kcwc.mvp.model.ActionRecordService;
import com.tgf.kcwc.mvp.model.AddCustomerService;
import com.tgf.kcwc.mvp.model.AddGroupService;
import com.tgf.kcwc.mvp.model.AppListService;
import com.tgf.kcwc.mvp.model.ApplyHintService;
import com.tgf.kcwc.mvp.model.BaseInfoService;
import com.tgf.kcwc.mvp.model.BusinessAttentionService;
import com.tgf.kcwc.mvp.model.BusinessRecordDetailService;
import com.tgf.kcwc.mvp.model.BusinessRecordService;
import com.tgf.kcwc.mvp.model.BuyMotoService;
import com.tgf.kcwc.mvp.model.CancelApplyService;
import com.tgf.kcwc.mvp.model.CarCertDetailService;
import com.tgf.kcwc.mvp.model.CertifcateService;
import com.tgf.kcwc.mvp.model.ChangeExhibitionService;
import com.tgf.kcwc.mvp.model.CityService;
import com.tgf.kcwc.mvp.model.CommentService;
import com.tgf.kcwc.mvp.model.CommunityService;
import com.tgf.kcwc.mvp.model.CouponService;
import com.tgf.kcwc.mvp.model.CustomizedCollectService;
import com.tgf.kcwc.mvp.model.DealerWalletService;
import com.tgf.kcwc.mvp.model.DeleteGroupService;
import com.tgf.kcwc.mvp.model.DrivingService;
import com.tgf.kcwc.mvp.model.EditCustomerService;
import com.tgf.kcwc.mvp.model.ExhibitionApplyService;
import com.tgf.kcwc.mvp.model.ExhibitionDepositService;
import com.tgf.kcwc.mvp.model.ExhibitionIntervalService;
import com.tgf.kcwc.mvp.model.ExhibitionPosQrService;
import com.tgf.kcwc.mvp.model.ExhibitionService;
import com.tgf.kcwc.mvp.model.FindDiscountService;
import com.tgf.kcwc.mvp.model.GeneralizationService;
import com.tgf.kcwc.mvp.model.GetApplyService;
import com.tgf.kcwc.mvp.model.GetRemainService;
import com.tgf.kcwc.mvp.model.GroupMoveService;
import com.tgf.kcwc.mvp.model.GroupingService;
import com.tgf.kcwc.mvp.model.HelpCenterDetailService;
import com.tgf.kcwc.mvp.model.HelpCenterService;
import com.tgf.kcwc.mvp.model.IntegralService;
import com.tgf.kcwc.mvp.model.LineDataService;
import com.tgf.kcwc.mvp.model.ManageCouponService;
import com.tgf.kcwc.mvp.model.MessageListService;
import com.tgf.kcwc.mvp.model.MotoBuyService;
import com.tgf.kcwc.mvp.model.MotoDeatailService;
import com.tgf.kcwc.mvp.model.MotoParamService;
import com.tgf.kcwc.mvp.model.MyExhibitionInfoService;
import com.tgf.kcwc.mvp.model.MyWalletService;
import com.tgf.kcwc.mvp.model.OrderPayService;
import com.tgf.kcwc.mvp.model.OwnerSaleService;
import com.tgf.kcwc.mvp.model.PayParamService;
import com.tgf.kcwc.mvp.model.PlayHomeService;
import com.tgf.kcwc.mvp.model.PleaseListService;
import com.tgf.kcwc.mvp.model.PleaseService;
import com.tgf.kcwc.mvp.model.PotentialCustomerTrackService;
import com.tgf.kcwc.mvp.model.RideDataService;
import com.tgf.kcwc.mvp.model.RobOrderDecryptService;
import com.tgf.kcwc.mvp.model.RobOrderService;
import com.tgf.kcwc.mvp.model.SearchService;
import com.tgf.kcwc.mvp.model.SeecarService;
import com.tgf.kcwc.mvp.model.SeekService;
import com.tgf.kcwc.mvp.model.SelectExhibitionPosService;
import com.tgf.kcwc.mvp.model.SelectExhibitionService;
import com.tgf.kcwc.mvp.model.SetttingService;
import com.tgf.kcwc.mvp.model.SignInService;
import com.tgf.kcwc.mvp.model.StoreBelowService;
import com.tgf.kcwc.mvp.model.StoreExhibitionService;
import com.tgf.kcwc.mvp.model.ThirdPartyService;
import com.tgf.kcwc.mvp.model.TicketManageService;
import com.tgf.kcwc.mvp.model.TicketService;
import com.tgf.kcwc.mvp.model.TopicListService;
import com.tgf.kcwc.mvp.model.TransmitWinningHomeService;
import com.tgf.kcwc.mvp.model.TripBookRecordService;
import com.tgf.kcwc.mvp.model.TripBookService;
import com.tgf.kcwc.mvp.model.UploadAvatarService;
import com.tgf.kcwc.mvp.model.UserManagerService;
import com.tgf.kcwc.mvp.model.VehicleService;
import com.tgf.kcwc.mvp.model.WxStatusService;
import com.tgf.kcwc.mvp.presenter.AlipayService;
import com.tgf.kcwc.util.RXUtil;

/**
 * Author：Jenny
 * Date:2016/8/26 14:19
 * E-mail：fishloveqin@gmail.com
 * 网络接口服务工厂
 */
public final class ServiceFactory {

    private static TripBookService mTripBookService;
    private static BuyMotoService buyMotoService;

    private ServiceFactory() {

    }


    private static StoreExhibitionService mStoreExhibitionService;
    private static TransmitWinningHomeService mTransmitWinningHomeService;
    private static UserManagerService mUMService;
    private static MotoDeatailService mOrganizationService;
    private static UploadAvatarService mUploadService;
    private static VehicleService mVehicleService;
    private static MotoParamService mMotoParmService;
    private static CouponService mCouponService;
    private static ExhibitionService mExhibitionService;
    private static DrivingService mDrivingService;
    private static PleaseService mPleaseService;
    private static AppListService mAppListService;
    private static FindDiscountService mFindDiscountService;
    private static RideDataService mRideDataService;
    private static PleaseListService mPleaseListService;
    private static PlayHomeService mPlayHomeService;
    private static TopicListService mTopicListService;
    private static MessageListService mMessageListService;
    private static SeekService mSeekService;
    private static SignInService mSignInService;
    private static CityService mCityService;
    private static IntegralService mIntegralService;
    private static StoreBelowService mStoreBelowService;
    private static PotentialCustomerTrackService mTrackService;
    private static RobOrderDecryptService mOrderService;
    private static EditCustomerService mCustomerDetailService;
    private static ActionRecordService mActionService;
    private static BusinessAttentionService mAttentionService;
    private static AddCustomerService mAddCustomerService;
    private static AddGroupService mAddGroupService;
    private static GroupMoveService mGroupMoveService;
    private static DeleteGroupService mDeleteGroupService;
    private static GroupingService mGroupingService;
    private static RobOrderService mRobOrderService;
    private static ManageCouponService mManageCouponService;
    private static BusinessRecordDetailService mBusinessRecordDetailService;
    private static BusinessRecordService mBusinessRecordService;
    private static BaseInfoService mBaseInfoService;
    private static CommunityService mCommunityService;
    private static CustomizedCollectService mCustomizedCollectService;
    private static SearchService mSearchService;
    private static HelpCenterService mHelpCenterService;
    private static HelpCenterDetailService mHelpCenterDetailService;
    private static TicketManageService mTicketManageService;
    private static SelectExhibitionService mSelectExhibitionService;
    private static ExhibitionDepositService mExhibitionDepositService;
    private static ExhibitionApplyService mExhibitionApplyService;
    private static ExhibitionIntervalService mExhibitionIntervalService;
    private static SelectExhibitionPosService mSelectExhibitionPosService;
    private static OrderPayService mOrderPayService;
    private static PayParamService mPayParamService;
    private static TripBookRecordService mTripBookRecordService;
    private static AboutService mAboutService;
    private static MyExhibitionInfoService mMyExhibitionInfoService;
    private static ExhibitionPosQrService mExhibitionPosQrService;
    private static LineDataService mLineDataService;
    private static MyWalletService mWalletService;
    private static DealerWalletService mDealerWalletService;
    private static CarCertDetailService mCarCertDetailService;
    private static GetApplyService mGetApplyService;
    private static ApplyHintService mApplyHintService;
    private static GetRemainService mGetRemainService;
    private static AlipayService mAlipayService;
    private static WxStatusService mWxStatusService;
    private static CancelApplyService mCancelApplyService;
    private static ChangeExhibitionService mChangeExhibitionService;
    /***
     * 获取当前展位
     * @return
     */
    public static synchronized ChangeExhibitionService getChangeExhibitionService() {
        if (mChangeExhibitionService == null) {
            mChangeExhibitionService = RXUtil.Factory.create(ChangeExhibitionService.class);
        }
        return mChangeExhibitionService;
    }
    /***
     * 取消参展
     * @return
     */
    public static synchronized CancelApplyService getCancelApplyService() {
        if (mCancelApplyService == null) {
            mCancelApplyService = RXUtil.Factory.create(CancelApplyService.class);
        }
        return mCancelApplyService;
    }
    private static ThirdPartyService mThirdService;

    public static synchronized ThirdPartyService getThirdPartyService() {
        if (mThirdService == null) {
            mThirdService = RXUtil.Factory.create(ThirdPartyService.class);
        }
        return mThirdService;
    }

    /***
     * 微信状态
     * @return
     */
    public static synchronized WxStatusService getWxStatusService() {
        if (mWxStatusService == null) {
            mWxStatusService = RXUtil.Factory.create(WxStatusService.class);
        }
        return mWxStatusService;
    }

    /***
     * 支付宝
     * @return
     */
    public static synchronized AlipayService getAlipayService() {
        if (mAlipayService == null) {
            mAlipayService = RXUtil.Factory.create(AlipayService.class);
        }
        return mAlipayService;
    }

    /***
     * 查看展会剩余展位数
     * @return
     */
    public static synchronized GetRemainService getGetRemainService() {
        if (mGetRemainService == null) {
            mGetRemainService = RXUtil.Factory.create(GetRemainService.class);
        }
        return mGetRemainService;
    }

    /***
     * 二手车参展申请须知
     * @return
     */
    public static synchronized ApplyHintService getApplyHintService() {
        if (mApplyHintService == null) {
            mApplyHintService = RXUtil.Factory.create(ApplyHintService.class);
        }
        return mApplyHintService;
    }

    /***
     * 二手车参展申请
     * @return
     */
    public static synchronized GetApplyService getGetApplyService() {
        if (mGetApplyService == null) {
            mGetApplyService = RXUtil.Factory.create(GetApplyService.class);
        }
        return mGetApplyService;
    }

    /***
     * 新增证件详情
     * @return
     */
    public static synchronized CarCertDetailService getCarCertDetailService() {
        if (mCarCertDetailService == null) {
            mCarCertDetailService = RXUtil.Factory.create(CarCertDetailService.class);
        }
        return mCarCertDetailService;
    }

    /***
     * 线路数据
     * @return
     */
    public static synchronized LineDataService getLineDataService() {
        if (mLineDataService == null) {
            mLineDataService = RXUtil.Factory.create(LineDataService.class);
        }
        return mLineDataService;
    }

    /***
     * 展位签到及打卡
     * @return
     */
    public static synchronized ExhibitionPosQrService getExhibitionPosQrService() {
        if (mExhibitionPosQrService == null) {
            mExhibitionPosQrService = RXUtil.Factory.create(ExhibitionPosQrService.class);
        }
        return mExhibitionPosQrService;
    }

    /***
     * 获取我的参展所有信息
     * @return
     */
    public static synchronized MyExhibitionInfoService getMyExhibitionInfoService() {
        if (mMyExhibitionInfoService == null) {
            mMyExhibitionInfoService = RXUtil.Factory.create(MyExhibitionInfoService.class);
        }
        return mMyExhibitionInfoService;
    }

    /***
     * 获取路书——我的记录
     * @return
     */
    public static synchronized AboutService getAboutService() {
        if (mAboutService == null) {
            mAboutService = RXUtil.Factory.create(AboutService.class);
        }
        return mAboutService;
    }

    /***
     * 获取路书——我的记录
     * @return
     */
    public static synchronized TripBookRecordService getTripBookRecordService() {
        if (mTripBookRecordService == null) {
            mTripBookRecordService = RXUtil.Factory.create(TripBookRecordService.class);
        }
        return mTripBookRecordService;
    }

    /***
     * 获取订单详情
     * @return
     */
    public static synchronized PayParamService getPayParamService() {
        if (mPayParamService == null) {
            mPayParamService = RXUtil.Factory.create(PayParamService.class);
        }
        return mPayParamService;
    }

    /***
     * 获取订单详情
     * @return
     */
    public static synchronized OrderPayService getOrderPayService() {
        if (mOrderPayService == null) {
            mOrderPayService = RXUtil.Factory.create(OrderPayService.class);
        }
        return mOrderPayService;
    }

    /***
     * 展位相关
     * @return
     */
    public static synchronized SelectExhibitionPosService getSelectExhibitionPosService() {
        if (mSelectExhibitionPosService == null) {
            mSelectExhibitionPosService = RXUtil.Factory.create(SelectExhibitionPosService.class);
        }
        return mSelectExhibitionPosService;
    }

    /***
     * 展位时段
     * @return
     */
    public static synchronized ExhibitionIntervalService getExhibitionIntervalService() {
        if (mExhibitionIntervalService == null) {
            mExhibitionIntervalService = RXUtil.Factory.create(ExhibitionIntervalService.class);
        }
        return mExhibitionIntervalService;
    }

    /***
     * 提交申请
     * @return
     */
    public static synchronized ExhibitionApplyService getExhibitionApplyService() {
        if (mExhibitionApplyService == null) {
            mExhibitionApplyService = RXUtil.Factory.create(ExhibitionApplyService.class);
        }
        return mExhibitionApplyService;
    }

    /***
     * 展位投诉
     * @return
     */
    public static synchronized ExhibitionDepositService getExhibitionDepositService() {
        if (mExhibitionDepositService == null) {
            mExhibitionDepositService = RXUtil.Factory.create(ExhibitionDepositService.class);
        }
        return mExhibitionDepositService;
    }

    /***
     * 获取展会列表
     * @return
     */
    public static synchronized SelectExhibitionService getSelectExhibitionService() {
        if (mSelectExhibitionService == null) {
            mSelectExhibitionService = RXUtil.Factory.create(SelectExhibitionService.class);
        }
        return mSelectExhibitionService;
    }

    /***
     * 用户管理服务接口对象
     * @return
     */
    public static synchronized UserManagerService getUMService() {
        if (mUMService == null) {
            mUMService = RXUtil.Factory.create(UserManagerService.class);
        }
        return mUMService;
    }


    /***
     * 上传头像图片
     * @return
     */
    public static synchronized UploadAvatarService getUploadService() {
        if (mUploadService == null) {
            mUploadService = RXUtil.Factory.create(UploadAvatarService.class);
        }
        return mUploadService;
    }

    /**
     * 整车模块接口服务对象实例
     *
     * @return
     */
    public static synchronized VehicleService getVehicleService() {
        if (mVehicleService == null) {
            mVehicleService = RXUtil.Factory.create(VehicleService.class);
        }
        return mVehicleService;
    }

    /***
     * 经销商列表获取
     * @return
     */
    public static synchronized MotoDeatailService getMotoDetailService() {
        if (mOrganizationService == null) {
            mOrganizationService = RXUtil.Factory.create(MotoDeatailService.class);
        }
        return mOrganizationService;
    }

    /**
     * 摩托车参数
     *
     * @return
     */
    public static synchronized MotoParamService getMotoParamService() {
        if (mMotoParmService == null) {
            mMotoParmService = RXUtil.Factory.create(MotoParamService.class);
        }
        return mMotoParmService;
    }

    private static TicketService mTicketService;

    public static synchronized TicketService getTicketService() {
        if (mTicketService == null) {
            mTicketService = RXUtil.Factory.create(TicketService.class);
        }
        return mTicketService;
    }

    /***
     * 代金券接口对象
     * @return
     */
    public static synchronized CouponService getCouponService() {
        if (mCouponService == null) {
            mCouponService = RXUtil.Factory.create(CouponService.class);

        }
        return mCouponService;
    }

    /***
     * 展会接口对象
     * @return
     */
    public static synchronized ExhibitionService getExhibitionService() {
        if (mExhibitionService == null) {
            mExhibitionService = RXUtil.Factory.create(ExhibitionService.class);

        }
        return mExhibitionService;
    }

    /***
     * 开车去接口对象
     * @return
     */
    public static synchronized DrivingService getDrivingService() {
        if (mDrivingService == null) {
            mDrivingService = RXUtil.Factory.create(DrivingService.class);

        }
        return mDrivingService;
    }

    /***
     * 请你玩接口对象
     * @return
     */
    public static synchronized PleaseService getPleaseService() {
        if (mPleaseService == null) {
            mPleaseService = RXUtil.Factory.create(PleaseService.class);

        }
        return mPleaseService;
    }

    /***
     * 会员中心 开车去
     * @return
     */
    public static synchronized AppListService getAppListService() {
        if (mAppListService == null) {
            mAppListService = RXUtil.Factory.create(AppListService.class);

        }
        return mAppListService;
    }

    /***
     * 会员中心 请你玩
     * @return
     */
    public static synchronized PleaseListService getPleaseListService() {
        if (mPleaseListService == null) {
            mPleaseListService = RXUtil.Factory.create(PleaseListService.class);

        }
        return mPleaseListService;
    }

    /***
     * 玩车首页
     * @return
     */
    public static synchronized PlayHomeService getPlayHomeService() {
        if (mPlayHomeService == null) {
            mPlayHomeService = RXUtil.Factory.create(PlayHomeService.class);

        }
        return mPlayHomeService;
    }

    /***
     * 玩车 话题
     * @return
     */
    public static synchronized TopicListService getTopicListService() {
        if (mTopicListService == null) {
            mTopicListService = RXUtil.Factory.create(TopicListService.class);

        }
        return mTopicListService;
    }

    /***
     * 消息中心
     * @return
     */
    public static synchronized MessageListService getMessageListService() {
        if (mMessageListService == null) {
            mMessageListService = RXUtil.Factory.create(MessageListService.class);

        }
        return mMessageListService;
    }

    /***
     * 搜索
     * @return
     */
    public static synchronized SeekService getSeekService() {
        if (mSeekService == null) {
            mSeekService = RXUtil.Factory.create(SeekService.class);

        }
        return mSeekService;
    }

    /***
     *  签到
     * @return
     */
    public static synchronized SignInService getSignInService() {
        if (mSignInService == null) {
            mSignInService = RXUtil.Factory.create(SignInService.class);

        }
        return mSignInService;
    }

    /***
     *  城市列表
     * @return
     */
    public static synchronized CityService getCityService() {
        if (mCityService == null) {
            mCityService = RXUtil.Factory.create(CityService.class);

        }
        return mCityService;
    }

    /***
     *  积分
     * @return
     */
    public static synchronized IntegralService getIntegralService() {
        if (mIntegralService == null) {
            mIntegralService = RXUtil.Factory.create(IntegralService.class);
        }
        return mIntegralService;
    }

    /***
     *  拍店内展车
     * @return
     */
    public static synchronized StoreBelowService getStoreBelowService() {
        if (mStoreBelowService == null) {
            mStoreBelowService = RXUtil.Factory.create(StoreBelowService.class);
        }
        return mStoreBelowService;
    }

    /***
     *  拍展会展车
     * @return
     */
    public static synchronized StoreExhibitionService getStoreExhibitionService() {
        if (mStoreExhibitionService == null) {
            mStoreExhibitionService = RXUtil.Factory.create(StoreExhibitionService.class);
        }
        return mStoreExhibitionService;
    }

    /***
     *  转发有奖
     * @return
     */
    public static synchronized TransmitWinningHomeService getTransmitWinningHomeService() {
        if (mTransmitWinningHomeService == null) {
            mTransmitWinningHomeService = RXUtil.Factory.create(TransmitWinningHomeService.class);
        }
        return mTransmitWinningHomeService;
    }

    private static CommentService mCommentService;

    public static synchronized CommentService getCommentService() {
        if (mCommentService == null) {
            mCommentService = RXUtil.Factory.create(CommentService.class);
        }

        return mCommentService;
    }

    private static CertifcateService mCertService;

    public static synchronized CertifcateService getCertService() {
        if (mCertService == null) {
            mCertService = RXUtil.Factory.create(CertifcateService.class);
        }
        return mCertService;
    }

    private static OwnerSaleService mSaleService;

    public static synchronized OwnerSaleService getSaleService() {

        if (mSaleService == null) {
            mSaleService = RXUtil.Factory.create(OwnerSaleService.class);
        }

        return mSaleService;
    }

    private static GeneralizationService mGService;

    public static synchronized GeneralizationService getGeneralizationService() {

        if (mGService == null) {
            mGService = RXUtil.Factory.create(GeneralizationService.class);
        }
        return mGService;
    }

    private static MotoBuyService mMotoBuyService;

    public static synchronized MotoBuyService getMotoBuyService() {

        if (mMotoBuyService == null) {
            mMotoBuyService = RXUtil.Factory.create(MotoBuyService.class);
        }
        return mMotoBuyService;
    }

    public static synchronized FindDiscountService getFindDiscountService() {
        if (mFindDiscountService == null) {
            mFindDiscountService = RXUtil.Factory.create(FindDiscountService.class);
        }
        return mFindDiscountService;
    }

    /**
     * 路书接口
     *
     * @return
     */
    public static synchronized TripBookService getTripBookService() {
        if (mTripBookService == null) {
            mTripBookService = RXUtil.Factory.create(TripBookService.class);
        }
        return mTripBookService;
    }

    public static synchronized RideDataService getRideService() {

        if (mRideDataService == null) {
            mRideDataService = RXUtil.Factory.create(RideDataService.class);
        }

        return mRideDataService;
    }

    public static synchronized BuyMotoService getBuyMotoService() {

        if (buyMotoService == null) {
            buyMotoService = RXUtil.Factory.create(BuyMotoService.class);
        }

        return buyMotoService;
    }

    private static SeecarService seecarService;

    public static synchronized SeecarService getSeecarService() {
        if (seecarService == null) {
            seecarService = RXUtil.Factory.create(SeecarService.class);
        }

        return seecarService;
    }

    private static SetttingService mSetttingService;

    public static synchronized SetttingService getSetttingService() {
        if (mSetttingService == null) {
            mSetttingService = RXUtil.Factory.create(SetttingService.class);
        }

        return mSetttingService;
    }

    /**
     * 潜客跟踪
     *
     * @return
     */
    public static synchronized PotentialCustomerTrackService getTrackService() {

        if (mTrackService == null) {
            mTrackService = RXUtil.Factory.create(PotentialCustomerTrackService.class);
        }
        return mTrackService;
    }

    /**
     * 抢单解密——抢单状态
     *
     * @return
     */
    public static synchronized RobOrderDecryptService getOrderProcessService() {

        if (mOrderService == null) {
            mOrderService = RXUtil.Factory.create(RobOrderDecryptService.class);
        }
        return mOrderService;
    }

    /**
     * 保存客户资料
     *
     * @return
     */
    public static synchronized EditCustomerService saveCustomerDetailService() {

        if (mCustomerDetailService == null) {
            mCustomerDetailService = RXUtil.Factory.create(EditCustomerService.class);
        }
        return mCustomerDetailService;
    }

    /**
     * 客户行为记录
     *
     * @return
     */
    public static synchronized ActionRecordService getActionRecordService() {

        if (mActionService == null) {
            mActionService = RXUtil.Factory.create(ActionRecordService.class);
        }
        return mActionService;
    }

    /**
     * 好友关注
     *
     * @return
     */
    public static synchronized BusinessAttentionService getAttentionService() {

        if (mAttentionService == null) {
            mAttentionService = RXUtil.Factory.create(BusinessAttentionService.class);
        }
        return mAttentionService;
    }

    /**
     * 添加客户
     *
     * @return
     */
    public static synchronized AddCustomerService getAddCustomerService() {

        if (mAddCustomerService == null) {
            mAddCustomerService = RXUtil.Factory.create(AddCustomerService.class);
        }
        return mAddCustomerService;
    }

    /**
     * 添加分组
     *
     * @return
     */
    public static synchronized AddGroupService getAddGroupService() {

        if (mAddGroupService == null) {
            mAddGroupService = RXUtil.Factory.create(AddGroupService.class);
        }
        return mAddGroupService;
    }

    /**
     * 组间移动
     *
     * @return
     */
    public static synchronized GroupMoveService getGroupMoveService() {

        if (mGroupMoveService == null) {
            mGroupMoveService = RXUtil.Factory.create(GroupMoveService.class);
        }
        return mGroupMoveService;
    }

    /**
     * 删除分组
     *
     * @return
     */
    public static synchronized DeleteGroupService getDeleteGroupService() {

        if (mDeleteGroupService == null) {
            mDeleteGroupService = RXUtil.Factory.create(DeleteGroupService.class);
        }
        return mDeleteGroupService;
    }

    /**
     * 好友分组
     *
     * @return
     */
    public static synchronized GroupingService getGroupingService() {

        if (mGroupingService == null) {
            mGroupingService = RXUtil.Factory.create(GroupingService.class);
        }
        return mGroupingService;
    }

    /**
     * 潜客抢单
     *
     * @return
     */
    public static synchronized RobOrderService getRobOrderService() {

        if (mRobOrderService == null) {
            mRobOrderService = RXUtil.Factory.create(RobOrderService.class);
        }
        return mRobOrderService;
    }

    /**
     * 代金券管理
     *
     * @return
     */
    public static synchronized ManageCouponService getManageCouponService() {

        if (mManageCouponService == null) {
            mManageCouponService = RXUtil.Factory.create(ManageCouponService.class);
        }
        return mManageCouponService;
    }


    /**
     * 商务记录详情
     *
     * @return
     */
    public static synchronized BusinessRecordDetailService getBusinessRecordDetailService() {

        if (mBusinessRecordDetailService == null) {
            mBusinessRecordDetailService = RXUtil.Factory.create(BusinessRecordDetailService.class);
        }
        return mBusinessRecordDetailService;
    }

    /**
     * 商务记录
     *
     * @return
     */
    public static synchronized BusinessRecordService getBusinessRecordService() {

        if (mBusinessRecordService == null) {
            mBusinessRecordService = RXUtil.Factory.create(BusinessRecordService.class);
        }
        return mBusinessRecordService;
    }

    /**
     * 客户详情—基本信息
     *
     * @return
     */
    public static synchronized BaseInfoService getBaseInfoService() {

        if (mBaseInfoService == null) {
            mBaseInfoService = RXUtil.Factory.create(BaseInfoService.class);
        }
        return mBaseInfoService;
    }

    /**
     * 行为分析—社区
     *
     * @return
     */
    public static CommunityService getCommunityService() {

        if (mCommunityService == null) {
            mCommunityService = RXUtil.Factory.create(CommunityService.class);
        }
        return mCommunityService;
    }

    /**
     * 行为分析—定制及收藏
     *
     * @return
     */
    public static CustomizedCollectService getCustomizedCollectService() {

        if (mCustomizedCollectService == null) {
            mCustomizedCollectService = RXUtil.Factory.create(CustomizedCollectService.class);
        }
        return mCustomizedCollectService;
    }

    /**
     * 行为分析—检索
     *
     * @return
     */
    public static SearchService getSearchService() {

        if (mSearchService == null) {
            mSearchService = RXUtil.Factory.create(SearchService.class);
        }
        return mSearchService;
    }

    /**
     * 设置——帮助中心列表
     *
     * @return
     */
    public static HelpCenterService getHelpCenterService() {

        if (mHelpCenterService == null) {
            mHelpCenterService = RXUtil.Factory.create(HelpCenterService.class);
        }
        return mHelpCenterService;
    }

    /**
     * 设置——帮助中心详情
     *
     * @return
     */
    public static HelpCenterDetailService getHelpCenterDetailService() {

        if (mHelpCenterDetailService == null) {
            mHelpCenterDetailService = RXUtil.Factory.create(HelpCenterDetailService.class);
        }
        return mHelpCenterDetailService;
    }

    /***
     * 票务管理服务接口对象
     * @return
     */
    public static synchronized TicketManageService getTicketManageService() {
        if (mTicketManageService == null) {
            mTicketManageService = RXUtil.Factory.create(TicketManageService.class);
        }
        return mTicketManageService;
    }


    /***
     * 钱包【我的】相关业务接口
     * @return
     */
    public static synchronized MyWalletService getMyWalletService() {
        if (mWalletService == null) {
            mWalletService = RXUtil.Factory.create(MyWalletService.class);
        }
        return mWalletService;
    }


    /***
     * 钱包【机构】相关业务接口
     * @return
     */
    public static synchronized DealerWalletService getDealerWalletService() {
        if (mDealerWalletService == null) {
            mDealerWalletService = RXUtil.Factory.create(DealerWalletService.class);
        }
        return mDealerWalletService;
    }
}
