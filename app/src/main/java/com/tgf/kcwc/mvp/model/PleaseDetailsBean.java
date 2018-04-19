package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Administrator on 2017/4/28.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PleaseDetailsBean {

    public int    code;

    public String msg;

    public Data   data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {

        public String                          type;

        public String                          rendezvous;
        @JsonProperty("rend_city")
        public String                          rendCity;
        @JsonProperty("rend_longitude")
        public String                          rendLongitude;
        @JsonProperty("rend_latitude")
        public String                          rendLatitude;
        @JsonProperty("car_id")
        public String                          carId;

        public Org                             org;
        @JsonProperty("car_list")
        public List<CarList>                   carList;

        public int                             id;
        @JsonProperty("is_digest")
        public int                             isDigest;

        public int                             status;
        @JsonProperty("create_by")
        public CreateBy                        createBy;

        public String                          cover;

        public String                          title;
        @JsonProperty("platform_type")
        public String                          platformType;
        @JsonProperty("scene_type")
        public int                             sceneType;

        public String                          start;
        @JsonProperty("start_city")
        public String                          startCity;
        @JsonProperty("start_longitude")
        public String                          startLongitude;
        @JsonProperty("start_latitude")
        public String                          startLatitude;

        public String                          destination;
        @JsonProperty("dest_city")
        public String                          destCity;
        @JsonProperty("dest_longitude")
        public String                          destLongitude;
        @JsonProperty("dest_latitude")
        public String                          destLatitude;
        @JsonProperty("roadbook_id")
        public int                             roadbookId;
        @JsonProperty("create_time")
        public String                          createTime;
        @JsonProperty("begin_time")
        public String                          beginTime;
        @JsonProperty("end_time")
        public String                          endTime;

        public String                          budget;
        @JsonProperty("limit_min")
        public int                             limitMin;
        @JsonProperty("limit_max")
        public int                             limitMax;
        @JsonProperty("deadline_time")
        public String                          deadlineTime;
        @JsonProperty("only_owner")
        public int                             onlyOwner;
        @JsonProperty("need_review")
        public int                             needReview;
        @JsonProperty("other_condition")
        public String                          otherCondition;

        public Intro                           intro;

        public Group                           group;
        @JsonProperty("topic_list")
        public List<DrivDetailsBean.TopicList> topicList;
        @JsonProperty("is_praise")
        public int                             isPraise;

        public int                             num;
        @JsonProperty("pass_num")
        public int                             passNum;
        @JsonProperty("activity_status")
        public String                          activityStatus;
        @JsonProperty("share_list")
        public List<DrivDetailsBean.ShareList> shareList;

        public List<DrivDetailsBean.Honour>    honour;
        @JsonProperty("apply_list")
        public List<DrivDetailsBean.ApplyList> applyList;
        @JsonProperty("apply_count")
        public int                             applyCount;
        @JsonProperty("is_apply")
        public int             isApply;
        @JsonProperty("reply_count")
        public int    replyCount;
    }

    public static class Intro {
        public List<DrivDetailsBean.MEditorDatas> mEditorDatas;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CarList {
        public int    id;

        public String cover;
        @JsonProperty("car_name")
        public String carName;
        @JsonProperty("factory_name")
        public String factoryName;
        @JsonProperty("factory_logo")
        public String factoryLogo;
        @JsonProperty("series_name")
        public String seriesName;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Org {
        public int    id;
        public String name;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Group {
        public String id;
        public String name;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateBy {
        public int    id;

        public String avatar;

        public String username;
        @JsonProperty("create_count")
        public int    createCount;

        public int    sex;
        @JsonProperty("is_doyen")
        public int    isDoyen;
        @JsonProperty("is_model")
        public int    isModel;
        @JsonProperty("is_master")
        public int    isMaster;

        public String logo;
    }

}
