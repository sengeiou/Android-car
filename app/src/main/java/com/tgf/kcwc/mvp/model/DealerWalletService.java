package com.tgf.kcwc.mvp.model;

import java.util.List;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Author:Jenny
 * Date:2017/10/18
 * E-mail:fishloveqin@gmail.com
 * 机构钱包
 */

public interface DealerWalletService {


    /**
     * 获取我添加的银行卡
     *
     * @param token
     * @return
     */
    @GET("org_purse/bank_card/lists")
    Observable<ResponseMessage<List<BankCardModel>>> getBankCards(@Query("token") String token);


    /**
     * 获取账户余额
     *
     * @param token
     * @return
     */
    @GET("org_purse/account/remainder")
    Observable<ResponseMessage<AccountBalanceModel>> getAccountBalances(@Query("token") String token);

    /**
     * #添加银行卡
     * /org_purse/bank_card/add  [POST]
     * <p>
     * 参数：
     * token：
     * card_code: 卡号
     * name: 持卡人姓名
     * pay_password: 支付密码
     */
    @FormUrlEncoded
    @POST("org_purse/bank_card/add")
    Observable<ResponseMessage<Object>> bindBankCard(@FieldMap Map<String, String> params);


    /**
     * 获取银行卡信息
     *
     * @param token
     * @param code
     * @return
     */
    @GET("org_purse/bank_card/get_card_bank")
    Observable<ResponseMessage<BankCardModel>> getBankCardInfo(@Query("token") String token, @Query("card_code") String code);


    /**
     * #设置支付密码
     * /org_purse/account/change_password  [POST]
     * <p>
     * 参数：
     * token
     * old_password:老支付密码  和验证码传其中一个
     * ver_code:验证码  和老支付密码传其中一个
     * new_password:新密码
     */

    @FormUrlEncoded
    @POST("org_purse/account/change_password")
    Observable<ResponseMessage<Object>> setPayPwd(@FieldMap Map<String, String> params);


    /**
     * #用户流水记录
     *
     * @param params
     * @return
     */
    @GET("org_purse/account/record")
    Observable<ResponseMessage<BalanceStatementModel>> getTradeList(@QueryMap Map<String, String> params);


    /**
     * /org_purse/withdraw/apply   [POST]
     * <p>
     * 参数：
     * token
     * bank_id:选中的银行卡的id
     * money:提现金额
     * pay_password:支付密码
     */


    @FormUrlEncoded
    @POST("org_purse/withdraw/apply")
    Observable<ResponseMessage<Object>> applyWithdraw(@FieldMap Map<String, String> params);


    /**
     * #校验验证码
     * token
     * tel:手机号
     * ver_code:验证码
     *
     * @return
     */
    @GET("org_purse/account/check_ver_code")
    Observable<ResponseMessage<Object>> validateCheckCode(@Query("token") String token, @Query("tel") String tel, @Query("ver_code") String verCode);


    /**
     * 获取我添加的银行卡
     *
     * @param token
     * @return
     */
    @GET("org_purse/account/record_detail")
    Observable<ResponseMessage<BalanceDetailModel>> getBalanceDetail(@Query("token") String token, @Query("id") String id);


    /**
     * 验证支付密码
     *
     * @param token
     * @return
     */
    @GET("org_purse/account/verify_password")
    Observable<ResponseMessage<Object>> validatePayPwd(@Query("token") String token, @Query("pay_password") String pwd);


    /**
     * 验证支付密码
     *
     * @param params
     * @return
     */
    @GET("org_purse/account/verify_password")
    Observable<ResponseMessage<Object>> validatePayPwd(@QueryMap Map<String, String> params);


    /**
     * 设置银行卡选中状态
     *
     * @param params
     * @return
     */
    // @FormUrlEncoded
    @GET("org_purse/bank_card/set_selected")
    Observable<ResponseMessage<Object>> setBankCardSelected(@QueryMap Map<String, String> params);


    /**
     * 获取默认选中的银行卡
     *
     * @param token
     * @return
     */
    @GET("org_purse/bank_card/get_selected")
    Observable<ResponseMessage<BankCardModel>> getSelectedBankCard(@Query("token") String token);


    /**
     * 充值
     * <p>
     * token
     * bank_id:银行卡id
     * money:金额
     * pay_password:支付密码
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("org_purse/recharge/bankcard")
    Observable<ResponseMessage<Object>> setPrePaid(@FieldMap Map<String, String> params);


    /**
     * 获取钱包账单
     *
     * @param token
     * @return
     */
    @GET("org_purse/account/bill")
    Observable<ResponseMessage<AccountBillModel>> getAccountBill(@Query("token") String token, @Query("date") String date);


    /**
     * #设置小额免密支付
     * /org_purse/account/set_free_pay
     * 参数：
     * token
     * quota:免密额度
     * ver_code：验证码
     * status:1 开启 0 关闭
     */

    @FormUrlEncoded
    @POST("org_purse/account/set_free_pay")
    Observable<ResponseMessage<Object>> setNonPwdPayment(@FieldMap Map<String, String> params);
}
