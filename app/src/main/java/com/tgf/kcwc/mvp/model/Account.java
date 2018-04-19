package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tgf.kcwc.util.URLUtil;

import java.io.Serializable;

/**
 * Author：Jenny
 * Date:2016/12/8 20:38
 * E-mail：fishloveqin@gmail.com
 * 用户个人资料实体类
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account implements Serializable, Parcelable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("user_info")
    public UserInfo userInfo;
    @JsonProperty("is_open_page")
    public int isOpenPage;
    @JsonProperty("openid")
    public String openid;
    @JsonProperty("text")
    public String text;

    public Org org;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Org implements Serializable, Parcelable{
        public int id;

        public String name;

        public String longitude;

        public String latitude;

        public String logo;
        @JsonProperty("full_name")
        public String fullName;

        public String banner;
        @JsonProperty("name_en")
        public String nameEn;

        public String pinyin;

        public String letter;

        public String tel;
        @JsonProperty("service_score")
        public int serviceScore;
        @JsonProperty("env_score")
        public int envScore;

        public int star;
        @JsonProperty("s_address")
        public String sAddress;

        public String address;

        public String text;
        @JsonProperty("org_type_id")
        public int orgTypeId;
        @JsonProperty("merchant_type")
        public int merchantType;
        @JsonProperty("management_type_ids")
        public String managementTypeIds;
        @JsonProperty("province_id")
        public int provinceId;
        @JsonProperty("area_id")
        public int areaId;
        @JsonProperty("is_voucher")
        public int isVoucher;
        @JsonProperty("is_xc")
        public int isXc;
        @JsonProperty("is_exhibition")
        public int isExhibition;
        @JsonProperty("brand_ids")
        public String brandIds;
        @JsonProperty("start_time")
        public String startTime;
        @JsonProperty("end_time")
        public String endTime;
        @JsonProperty("data_img_ids")
        public String dataImgIds;
        @JsonProperty("contract_num")
        public String contractNum;

        public int status;
        @JsonProperty("create_by")
        public int createBy;
        @JsonProperty("create_time")
        public String createTime;
        @JsonProperty("update_by")
        public int updateBy;
        @JsonProperty("update_time")
        public String updateTime;
        @JsonProperty("is_delete")
        public int isDelete;

        public int points;

        public int exp;
        @JsonProperty("service_area_id")
        public int serviceAreaId;
        @JsonProperty("non_car_pid")
        public int nonCarPid;

        public int level;
        @JsonProperty("freeze_points")
        public int freezePoints;


        public Org() {
        }

        protected Org(Parcel in) {
            id = in.readInt();
            name = in.readString();
            longitude = in.readString();
            latitude = in.readString();
            logo = in.readString();
            fullName = in.readString();
            banner = in.readString();
            nameEn = in.readString();
            pinyin = in.readString();
            letter = in.readString();
            tel = in.readString();
            serviceScore = in.readInt();
            envScore = in.readInt();
            star = in.readInt();
            sAddress = in.readString();
            address = in.readString();
            text = in.readString();
            orgTypeId = in.readInt();
            merchantType = in.readInt();
            managementTypeIds = in.readString();
            provinceId = in.readInt();
            areaId = in.readInt();
            isVoucher = in.readInt();
            isXc = in.readInt();
            isExhibition = in.readInt();
            brandIds = in.readString();
            startTime = in.readString();
            endTime = in.readString();
            dataImgIds = in.readString();
            contractNum = in.readString();
            status = in.readInt();
            createBy = in.readInt();
            createTime = in.readString();
            updateBy = in.readInt();
            updateTime = in.readString();
            isDelete = in.readInt();
            points = in.readInt();
            exp = in.readInt();
            serviceAreaId = in.readInt();
            nonCarPid = in.readInt();
            level = in.readInt();
            freezePoints = in.readInt();
        }


        public static final Creator<Org> CREATOR = new Creator<Org>() {
            @Override
            public Org createFromParcel(Parcel in) {
                return new Org(in);
            }

            @Override
            public Org[] newArray(int size) {
                return new Org[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(name);
            dest.writeString(longitude);
            dest.writeString(latitude);
            dest.writeString(logo);
            dest.writeString(fullName);
            dest.writeString(banner);
            dest.writeString(nameEn);
            dest.writeString(pinyin);
            dest.writeString(letter);
            dest.writeString(tel);
            dest.writeInt(serviceScore);
            dest.writeInt(envScore);
            dest.writeInt(star);
            dest.writeString(sAddress);
            dest.writeString(address);
            dest.writeString(text);
            dest.writeInt(orgTypeId);
            dest.writeInt(merchantType);
            dest.writeString(managementTypeIds);
            dest.writeInt(provinceId);
            dest.writeInt(areaId);
            dest.writeInt(isVoucher);
            dest.writeInt(isXc);
            dest.writeInt(isExhibition);
            dest.writeString(brandIds);
            dest.writeString(startTime);
            dest.writeString(endTime);
            dest.writeString(dataImgIds);
            dest.writeString(contractNum);
            dest.writeInt(status);
            dest.writeInt(createBy);
            dest.writeString(createTime);
            dest.writeInt(updateBy);
            dest.writeString(updateTime);
            dest.writeInt(isDelete);
            dest.writeInt(points);
            dest.writeInt(exp);
            dest.writeInt(serviceAreaId);
            dest.writeInt(nonCarPid);
            dest.writeInt(level);
            dest.writeInt(freezePoints);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserInfo implements Serializable, Parcelable {


        public String imId = "";
        @JsonProperty("msg_total")
        public int msgTotal;
        @JsonProperty("nickname")
        public String nick;
        private static final long serialVersionUID = 2L;
        @JsonProperty("real_name")
        public String realName;
        @JsonProperty("avatar")
        public String avatar;
        @JsonProperty("fans_num")
        public int fans_num;
        @JsonProperty("follow_num")
        public int follow_num;

        @JsonProperty("is_bind_wechat")
        public int is_bind_wechat;
        @JsonProperty("is_comment")
        public int is_comment;
        @JsonProperty("is_master")
        public int is_master;
        @JsonProperty("is_model")
        public int is_model;
        @JsonProperty("is_register")
        public int is_register;
        @JsonProperty("level")
        public int level;
        @JsonProperty("nick_name")
        public String nickName;
        @JsonProperty("sign")
        public String sign;
        @JsonProperty("star")
        public String star;
        @JsonProperty("tel")
        public String tel;
        @JsonProperty("user_id")
        public int id;
        @JsonProperty("user_name")
        public String username;
        @JsonProperty("token")
        public String token;
        @JsonProperty("attached")
        public String attached;
        //        0未设置1设置
        @JsonProperty("is_pwd")
        public int isPWD;

        @JsonProperty("login_num")
        public String loginNum;
        @JsonProperty("id")
        public int userId;
        @JsonProperty("sex")
        public int sex;
        @JsonProperty("gender")
        public int gender;

        @JsonProperty("is_daren")
        public int isDaren;

        @JsonProperty("is_doyen")
        public int isExpert;
        @JsonProperty("brand_logo")
        public String brandLogo;

        @JsonProperty("master_brand")
        public String masterLogo;
        public int age;

        @JsonProperty("is_follow")
        public int isFlw;
        @JsonProperty("sign_text")
        public String desc;
        public int count;
        @JsonProperty("is_vip")
        public int isVip;
        @JsonProperty("play_auth")
        public int playAuth;
        public boolean isSelected;
        public String mobile;

        public String getAvatar() {
            return URLUtil.builderImgUrl(avatar, 144, 144);
        }

        public UserInfo() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.imId);
            dest.writeInt(this.msgTotal);
            dest.writeString(this.nick);
            dest.writeString(this.realName);
            dest.writeString(this.avatar);
            dest.writeInt(this.fans_num);
            dest.writeInt(this.follow_num);
            dest.writeInt(this.is_bind_wechat);
            dest.writeInt(this.is_comment);
            dest.writeInt(this.is_master);
            dest.writeInt(this.is_model);
            dest.writeInt(this.is_register);
            dest.writeInt(this.level);
            dest.writeString(this.nickName);
            dest.writeString(this.sign);
            dest.writeString(this.star);
            dest.writeString(this.tel);
            dest.writeInt(this.id);
            dest.writeString(this.username);
            dest.writeString(this.token);
            dest.writeString(this.attached);
            dest.writeInt(this.isPWD);
            dest.writeString(this.loginNum);
            dest.writeInt(this.userId);
            dest.writeInt(this.sex);
            dest.writeInt(this.gender);
            dest.writeInt(this.isDaren);
            dest.writeInt(this.isExpert);
            dest.writeString(this.brandLogo);
            dest.writeString(this.masterLogo);
            dest.writeInt(this.age);
            dest.writeInt(this.isFlw);
            dest.writeString(this.desc);
            dest.writeInt(this.count);
            dest.writeInt(this.isVip);
            dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
            dest.writeString(this.mobile);
        }

        protected UserInfo(Parcel in) {
            this.imId = in.readString();
            this.msgTotal = in.readInt();
            this.nick = in.readString();
            this.realName = in.readString();
            this.avatar = in.readString();
            this.fans_num = in.readInt();
            this.follow_num = in.readInt();
            this.is_bind_wechat = in.readInt();
            this.is_comment = in.readInt();
            this.is_master = in.readInt();
            this.is_model = in.readInt();
            this.is_register = in.readInt();
            this.level = in.readInt();
            this.nickName = in.readString();
            this.sign = in.readString();
            this.star = in.readString();
            this.tel = in.readString();
            this.id = in.readInt();
            this.username = in.readString();
            this.token = in.readString();
            this.attached = in.readString();
            this.isPWD = in.readInt();
            this.loginNum = in.readString();
            this.userId = in.readInt();
            this.sex = in.readInt();
            this.gender = in.readInt();
            this.isDaren = in.readInt();
            this.isExpert = in.readInt();
            this.brandLogo = in.readString();
            this.masterLogo = in.readString();
            this.age = in.readInt();
            this.isFlw = in.readInt();
            this.desc = in.readString();
            this.count = in.readInt();
            this.isVip = in.readInt();
            this.isSelected = in.readByte() != 0;
            this.mobile = in.readString();
        }

        public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
            @Override
            public UserInfo createFromParcel(Parcel source) {
                return new UserInfo(source);
            }

            @Override
            public UserInfo[] newArray(int size) {
                return new UserInfo[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.userInfo);
        dest.writeInt(this.isOpenPage);
        dest.writeString(this.text);
    }

    public Account() {
    }

    protected Account(Parcel in) {
        this.userInfo = (UserInfo) in.readSerializable();
        this.isOpenPage = in.readInt();
        this.text = in.readString();
    }

    public static final Parcelable.Creator<Account> CREATOR = new Parcelable.Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel source) {
            return new Account(source);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };
}
