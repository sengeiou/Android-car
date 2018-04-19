package com.tgf.kcwc.common;

import android.os.Environment;

import com.tgf.kcwc.mvp.model.Account;

import java.io.File;

import static com.tgf.kcwc.common.Constants.BaseApi.PROTOCOL_HTTP;
import static com.tgf.kcwc.common.Constants.BaseApi.PROTOCOL_HTTPS;

/**
 * Author:Jenny
 * Date:16/4/23 22:16
 * E-mail:fishloveqin@gmail.com
 * 参数常量
 */
public class Constants {
    public static final int HTTP_LOGIN_ERROR = 10001;
    public static final int HTTP_LOGIN_OUT = 20077;
    public static final String KEY_ACCOUNT_FILE = "account";
    //发布宝贝文件存储
    public static final String KEY_RELEASE_SALE_FILE = "release_sale_file";
    //参展申请——展位时段文件存储
    public static final String KEY_EXHIBITION_INTERNAL_FILE = "release_sale_file";
    public static final String KEY_IMGS = "key_imgs";
    public static final String KEY_IMG = "key_img";
    public static final String NETUTIL = "https://www.baidu.com/";
    public static final File PHOTO_DIR = new File(
            Environment.getExternalStorageDirectory() + "/ride_kcwc/Camera");

    /**
     * 驾途缓存路径
     **/
    public static final String RIDE_LINE_CACHE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/kcwc/ridebook_line/";
    //全局我的资料信息
    public static Account.UserInfo myInfo;
    public static final int PWD_MAX_LENGTH = 6;

    public static interface SPParams {
        String SP_NAME = "kcwc_prefs";

        String RIDE_ID = "ride_id";
        String RIDE_STATE = "ride_state";
        String TOTAL_TIME = "total_time";
        String SPEED_MAX = "speed_max";
    }

    public static interface BaseApi {

        String PROTOCOL_HTTP = "http://";
        String PROTOCOL_HTTPS = "https://";
        String IMG_BASE_URL = (IS_TEST ? PROTOCOL_HTTP : PROTOCOL_HTTPS) + imgHost;

        String STATIC_RES_HOST = staticResHost;
        //        String WEP_BASE_HOST   = "m.51kcwc.com";

        String API_BASE_URL = (IS_TEST ? PROTOCOL_HTTP : PROTOCOL_HTTPS) + apiHost + "/";
//        String API_BASE_URL = (IS_TEST ? PROTOCOL_HTTP : PROTOCOL_HTTPS) + "car.majie.i.cacf.cn" + "/";

    }

    public static interface H5 {

        String WAP_LINK = (IS_TEST ? PROTOCOL_HTTP : PROTOCOL_HTTPS) + webHost;
    }

    public static interface ImageUrls {

        String IMG_DEF_URL = (IS_TEST ? PROTOCOL_HTTP : PROTOCOL_HTTPS) + staticResHost
                + "/car/share/default.png";
    }

    public static final boolean IS_TEST = true;
    private static String apiHost = "car.i.cacf.cn";
    private static String webHost = "car.i.cacf.cn";
    private static String imgHost = "img.i.cacf.cn";
    private static String staticResHost = "static.i.cacf.cn";

    static {
        if (IS_TEST) {
            apiHost = "car.i.cacf.cn";
            imgHost = "img.i.cacf.cn";
            staticResHost = "static.i.cacf.cn";
            webHost = "car.i.cacf.cn";
        } else {
            apiHost = "i.api.51kcwc.com"; //car.i.cacf.cn    api.i.cacf.cn
            imgHost = "img.51kcwc.com";
            staticResHost = "static.51kcwc.com";
            webHost = "m.51kcwc.com";
        }

    }

    public static interface Navigation {

        String HOME_TAB = "home";
        String SEE_TAB = "see";
        String POSTED_TAB = "posted";
        String PLAY_TAB_TAB = "play";
        String ME_TAB = "me";

        int HOME_INDEX = 0;
        int SEE_INDEX = 1;
        int PLAY_INDEX = 3;
        int ME_INDEX = 4;
    }

    public static interface Topiccode {

        String RECOMMEND_TAB = "recommend";
        String HOT_TAB = "hot";
        String ME_TAB = "me";
    }

    public static interface SeriesStatus {

        public int ON = 1;  //在售IS
        public int PRE = 2; //预售
        public int STOP = 3;//停售

    }

    public static interface InteractionCode {

        int REQUEST_CODE = 1;
        int REQUEST_CODE_2 = 2;

        int REQUEST_CODE_HOME = 100;

        int QQ_SHARE_REQUEST_CODE = 10103;
        int QQ_ZONE_SHARE_REQUEST_CODE = 10104;

        int WEB_BO_TYPE = 2;
        int QQ_OR_ZONE_TYPE = 1;

        int MAIN_PLAY_DRIVING_START = 10105;

        int MAIN_PLAY_DRIVING_END = 10106;
    }

    /**
     * 分享结果
     */
    public static interface Share {
        String SHARE_SUCCEED = "succeed";
        String SHARE_CANCEL = "cance";
        String SHARE_REPULSE = "repulse";
        String SHARE_RETURN = "return";

    }

    /**
     * 路线返回
     */
    public static interface PathBack {
        String DRIVINGHOME_SUCCEED = "DrivingHomeActivity";//ride
        String SPONSORDRIVING_SUCCEED = "SponsorDrivingActivity";//发起请你玩
        String COMPILEDRIVING_SUCCEED = "CompileDrivingActivity";//编辑请你玩
        String SPONSORIS = "SPONSORIS"; //是否返回
        String SPONSOR = "SPONSOR";  //返回的ID
        String ROADBOOKTITLE = "ROADBOOKTITLE";  //返回的标题
        String ROADBOOKTYPE = "roadbooktype";  //返回的类型
    }

    /**
     * 车型返回
     */
    public static interface CarSeriesBack {
        String COMPILESTOREBELOW_SUCCEED = "CompileStoreBelowActivity";//编辑拍店内展车
        String SPONSORSTOREBELOW_SUCCEED = "SponsorStoreBelowActivity";//发起拍店内展车
        String SPONSORSTOREEXHIBITION_SUCCEED = "SponsorStoreExhibitionActivity";//发起拍展会展车
        String COMPILESTOREEXHIBITION_SUCCEED = "CompileStoreExhibitionActivity";//编辑拍展会展车
        String SPONSORPLEASEPLAY_SUCCEED = "SponsorPleasePlayActivity";//发起请你玩
        String COMPILEPLEASEPLAY_SUCCEED = "CompilePleasePlayActivity";//编辑请你玩


        String MANUFACTURERNAME = " manufacturername"; //厂商名字
        String SERIESNAME = "seriesname"; //车系名字
        String NAME = "name"; //名字
        String ID = "id"; //车型ID
        String CARID = "carid"; //车系ID
        String MFIRMID = "mFirmId"; //品牌ID
        String HALLID = "hallId";
        String BOOTHID = "boothId";
    }

    /**
     * 玩车
     */
    public static interface PlayTabSkip {
        String DRIVINGFRAGMENT = "DrivingFragment";//开车去
        String PLEASEPLAYFRAGMENT = "PleasePlayFragment";//请你玩
        String TABPLAYFRAGMENT = "TabPlayFragment";//玩车
        String HAVEFUNFRAGMENT = "HaveFunFragment";//玩得爽
        String TRIPBOOKELIST = "TripBookeListFragment";//路书

    }

    public static interface IntentParams {
        String INTENT_TYPE = "type";
        int SINGLE_BRAND_TYPE = 1;                  //品牌列表单选
        int MUlTI_BRAND_TYPE = 2;                  //品牌列表多选
        String FACTORY_NAME = "factory_name";
        String SOURCE_ID = "source_id";
        String THREAD_ID = "thread_id";
        String ID = "id";
        String ID2 = "id2";
        String ID3 = "id3";
        String ID4 = "id4";
        String TITLE = "title";
        String ISSEE = "issee";
        String DATA = "data";
        String DATA2 = "data2";
        String DATA3 = "data3";
        String DATA4 = "data4";
        String DATA5 = "data5";
        String CODE="code";
        String WD_CASH_DATA="wd_cash_data";
        String PRE_PAID_DATA="pre_paid_data";
        String PRE_PAID_DATA_CLEAR="pre_paid_data_clear";
        String PRE_PAID_DATA_CLEAR_VALUE="clear";
        String NAME = "name";
        String IS_JUMP = "is_jump";
        String IS_SHOWLIST = "isshowlist";
        String TEL = "tel";              //电话号码
        String LNG = "lng";              //经度
        String LAT = "lat";
        String LONGITUDE = "longitude";              //经度
        String LATITUDE = "latitude";
        String IDS = "ids";
        String EVENT_ID = "event_id";
        String FROM_ID = "from_id";
        String FROM_TYPE = "from_type";
        String NEW_CAR_VALUE = "newCar";           //activity newCar    showCar  model topic
        String SHOW_CAR_VALUE = "showCar";
        String ACTIVITY_VALUE = "activity";
        String EVENT_VALUE = "event";
        String MODEL_VALUE = "model";
        String TOPIC_VALUE = "topic";
        String RIDE_ID = "ride_id";
        String RIDE_ADDRESS = "ride_address";
        String IS_END = "is_end";
        String IS_NO_SPEED_STATE = "is_no_speed_state";
        String USER_ID = "user_id";
        //纬度
        int REQUEST_CODE = 1;
        int REQUEST_CODE_2 = 2;
        int REQUEST_CODE_3 = 3;
        String INDEX = "index";
        String COUNT = "count";
        String SERIES_ID = "series_id";
        String EXHIBIT_ID = "exhibit_id";
        String DISCOUNTS_TYPE = "discounts_type"; //优惠类型
        String DISCOUNTS_TIME_LIMIT  = "discounts_time_limit"; //限时优惠
        String DISCOUNTS_EXHIBITION = "discounts_exhibition"; //展期优惠
        String SPONSORPLEASE = "SponsorPlease";
        String CAR_ID = "car_id";
        String EDIT_TOPIC = "edit_topic";
        String TAG_BRAND_ID = "tag_brand_id";
        String IS_SEPBRAND = "is_sep_brand";     //是否为特定品牌
        String IS_OPEN_ADD = "is_open_add";

        String RIDE_BOOK_MODULE = "ride_module";      //发布路书模块
        String DRIVER_CAR_MODULE = "driver_car_module";//开车去
        String PLAY_CAR_MODULE = "play_car_module";  //请你玩

        String MODULE_TYPE = "module_type";//模块类型，车主自售、PK,我要看等
        String SERIES_NAME = "series_name";
        String BG_RIDE_TYPE = "bg_ride_type";
        String SHOW_LOGINOUT = "loginout";
        //没有未完成的路线，开始新路线1
        int ALL_NEWLINE = 1;
        //有未完成的路线，开始新路线2
        int CONTINUE_NEWLINE = 2;
        //有未完成的路线，继续路线3
        int CONTINUE_CONTINUELINE = 3;
        String IS_GO = "is_go";
        String IS_NORMAL_STATE = "is_normal_state";
        String IS_BASE_STATE = "is_base_state";
        String ALL_RIDE_REP = "1";                //起点到结束点的报告
        String PART_RIDE_REP = "2";                //两个节点间的报告
        String PATH_BACK = "type";                //路线选择返回

        String PLAYINDEX = "playindex";

        String TAG_TYPE = "tag_type";//发帖标签类型，新车、展车、模特

        String SELECT_EXHIBITION = "select_exhibition";//选择展会
        String SELECT_DRIVING = "select_driving";//选择开车去
        String MESSAGEREFRESH = "messagerefresh";//消息刷新
        String PHONETYPE = "phonetype";//是拍照还是选图片
        String ISUNLIMITED = "isUnlimited";//是否拥有不限车型

        int SALE_INDEX = 3;//车主自售
        int SALE_DISCOUNTS = 4;//找优惠
        int SEE_EXHIBIT = 1;//展会看

        String PAY_STATUS = "pay_status";

    }

    public static interface KeyParams {

        String CAR_MODEL_KEY = "car_model_key";
        String ACCOUNT_KEY = "account";
        String RICH_E_DATA = "rich_e_data";
        String PK_DATAS = "pk_datas";

        String IS_CONTRAST = "is_contrast";

        String WX_PAY_DATA = "wx_pay_data";

        String PRE_REG_SELECT_MODEL = "pre_reg_select_model";//意向品牌/车系/车型Key

        String PRE_REG_SELECT_MODEL_VALUE = "pre_reg_select_model_value";//意向品牌/车系/车型Value

        String SYS_TAG_MODEL_VALUE = "sys_tag_model_value";//保存展车、新车、模特标签Value

        String KEY_DATA = "key_data";


        String TAGS_DATA = "tags_data";

        String SET_PWD_DATA = "set_pwd_data";

        String CHECK_CODE = "check_code";
        String PAY_OLD_PWD = "pay_old_pwd";
    }

    public static interface Types {

        int LOAD_MORE_STYLE_1 = 1;
        int LOAD_MORE_STYLE_2 = 2;

        int APPLY_TICKET = 9;
        int APPLY_CERT = 10;
        int APPLY_TICKET_SUCCESS = 11;
        int APPLY_CERT_SUCCESS = 12;
    }

    public static interface PayStatus {

        int SUCCESS = 1;
        int FAILURE = 0;
        int UN_NEED = 0;
        int NEED_PAY = 1;

        int APAY_SUCCESS = 9000;
    }

    public static interface PayTypes {

        int WX = 1;
        int ALIPAY = 2;
        int KCWC = 3;
    }

    public static interface NetworkStatusCode {
        int SUCCESS = 0;
        int STATUS_CODE1 = 10022;
        int STATUS_CODE2 = 120006;
        int STATUS_CODE3 = 20005;
        int STATUS_CODE4 = 20006;
        int CERT_APPLY_SUCCESS = 20008;
        int FAILURE = 9999;
    }


    public static interface GoodsStatus {

        int SHELVE = 1;

    }

    public static interface GoodsType {

        int VEHICLE_TYPE = 1;
    }

    public static interface ChatMsgType {

        int RECEIVER = 0;
        int SENDER = 1;
    }

    public static interface RideTypes {

        String RIDE_BOOK = "roadbook";//路书
        String CYCLE = "cycle";//开车去
        String PLAY = "play";//请你玩

    }

    //wx0499a379fa2913c5
    public static final String WEIXIN_APP_ID = "wxf91e04313b38b17e";
    public static final String PACKAGE_VALUE = "Sign=WXPay";
    public static final String WX_CASH_STATE = "wx_cash_state";
    public static final String WX_LOGIN_STATE = "wx_login_state";

    public static interface CheckInFormKey {

        /*company=&qd_id=&area=&car=&brand_sub=&age=&visit_date=&address=&dept=&price=&city=&location=&position=&brand=&sex=&cid=&name=&industry=&province= */
        String NAME = "name";
        String AGE = "age";
        String CAR = "car";
        String BRAND = "brand";
        String SERIES = "series";
        String LOCATION = "location";
        String SEX = "sex";
        String COMPANY = "company";
        String QD_ID = "qd_id";
        String AREA = "area";
        String BRAND_SUB = "brand_sub";
        String VISIT_DATE = "visit_date";
        String ADDRESS = "address";
        String DEPT = "dept";
        String PRICE = "price";
        String CITY = "city";
        String POSITION = "position";  //职务
        String INDUSTRY = "industry";
        String PROVINCE = "province";
        String CID = "cid";
        int IS_REQUIRE = 1;
        String TOKEN = "token";
        String TF_ID = "tf_id";
    }

    public static interface QRValues {

        String SIGN_IN_OUT = "sign_in_out";
        String CERT_REG = "cert_reg";
        String ADD_FRIEND_ID = "http://car.i.cacf.cn/#/member/home/master/";
        String RIDECHECKID = "rideCheckId="; //签到
        String COUPONID = Constants.H5.WAP_LINK + "/#/scan/coupon?"; //代金券
        String TICKETM_SEND = Constants.H5.WAP_LINK + "/#/my/ticket/handout/success/?"; //代金券
    }

    public static final int PRICE_MAX = 200;
    public static final int PRICE_MIN = 0;

    public static final int PK_MAX = 10;
    public static final int TICKETS_MAX = 9;

    public static interface SinaShare {

        //2045436852
        /**
         * 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY
         */
        public static final String APP_KEY = "4044063761";

        /**
         * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
         * <p>
         * <p>
         * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
         * 但是没有定义将无法使用 SDK 认证登录。
         * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
         * </p>
         */
        public static final String REDIRECT_URL = "https://*.51kcwc.com";

        /**
         * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
         * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
         * 选择赋予应用的功能。
         * <p>
         * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
         * 使用权限，高级权限需要进行申请。
         * <p>
         * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
         * <p>
         * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
         * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
         */
        public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
                + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                + "follow_app_official_microblog,"
                + "invitation_write";
    }

    public static interface TencentShare {

        String APP_KEY = "1106169374";
    }

    public static interface UserParams {

        int IS_OWN = 1;//表示当前用户是本人
    }

    public static interface TopicParams {

        String VEHICLE_TYPE = "car";

        String MODULE1 = "thread";

        String SHOW_CAR = "showCar";//展车
        String NEW_CAR = "newCar";//新车
        String MODEL = "model";//模特

        String EXHIBITION_PARAM = "exhibition_param";

        int CAN_BE_REPORT = 0;

    }

    /**
     * 'evaluate'     => '评测',
     * 'thread'       => '帖子',
     * 'activity'     => '活动',
     * 'article'      => '资讯',
     * 'event'        => '展会',
     * 'organization' => '经销商',
     * 'car'          => '车辆详情',
     * 'series'       => '车系',
     * 'coupon'       => '代金券',
     * 'roadbook'     => '路书',
     * 'store_car'    => '店内车辆',
     * 'words'        => '普通帖子',
     * 'goods'        => '车主自售',
     * 'play'         => '请你玩',
     * 'cycle'        => '开车去'
     */
    public static interface FavoriteTypes {


        /* 'thread','series','activity','article','event',
        'organization','evaluate','store_car','coupon',
        'car','words','goods','play','cycle','roadbook'*/

        String SERIES = "series";
        String STORE_CAR = "store_car";
        String COUPON = "coupon";
        String CAR = "car";
        String THREAD = "thread";
        String WORDS = "words";
        String EVALUATE = "evaluate";
        String ACTIVITY = "activity";
        String EVENT = "event";
        String ARTICLE = "article";
        String ORGANIZATION = "organization";
        String ROADBOOK = "roadbook";
        String GOODS = "goods";
        String PLAY = "play";
        String CYCLE = "cycle";
    }

    //    public static final String SALE_TOKEN = "aWXdC35IzKJMyFubndyS0gwHLqMztGJX";

    //    public static final String SALE_TOKEN = "aWXdC35IzKJMyFubndyS0gwHLqMztGJX";

    //    public static final String SALE_TOKEN = "aWXdC35IzKJMyFubndyS0gwHLqMztGJX";

    //    public static final String SALE_TOKEN = "aWXdC35IzKJMyFubndyS0gwHLqMztGJX";

    //    public static final String SALE_TOKEN = "aWXdC35IzKJMyFubndyS0gwHLqMztGJX";
    public static final String SALE_TOKEN = "OSAxLAV4tNYoD6WohyNzqgsOc6joatcB";
    public static final String SALE_USER_ID = "110";


    public interface TicketStatus {

        /* *
         0.未使用 1.已使用 2已失效 3.已过期 4已取消 5.退款中 6.已退款 7退款失败 */

        public int UN_USED = 0;

        public int USED = 1;

        public int EXP = 2;//
        public int EXPIRED = 3;
        public int CANCEL = 4;
        public int REFUNDING = 5;
        public int REFUNDED = 6;
        public int REFUND_FAILURE = 7;

        public int FORWARDED = 8;

        public int DISABLED = 9;
    }

    /**
     * 模块类型
     */
    public interface ModuleTypes {

        public String RELEASE_SALE = "release_sale";//发布车主自售

        public String CONTRAST_PK = "contrast_pk";//车型对比

        public String OWNER_SEE = "see";//我要看


        public String PRE_REG_TICKET = "pre_reg_ticket";//门票申领

        public String PRE_REG_CERT = "pre_reg_cert";//证件申领

        String SPONSORDRIVING_SUCCEED = "SponsorDrivingActivity";//发起请你玩

        String COMPILEDRIVING_SUCCEED = "CompileDrivingActivity";//编辑请你玩

        String TAB_HOME_SEE = "home_see";//首页我要看
    }

    public static interface DeviceName {

        String HW_CHE = "Che2-TL00M";
    }

    public static interface BalanceTypes {

        int IN = 1;
        int OUT = 2;
    }
}
