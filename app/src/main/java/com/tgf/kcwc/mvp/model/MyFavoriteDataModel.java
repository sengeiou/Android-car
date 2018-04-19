package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author:Jenny
 * Date:2017/5/22
 * E-mail:fishloveqin@gmail.com
 * 我的收藏数据模型
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyFavoriteDataModel {

    /**
     * pagination : {"page":1,"count":3}
     * list : [{"img_path":"xxxsseexx11","source_id":4,"add_time":"2017-02-15 09:00:12","type":"moto","model":"car","attach":"这个是附加内容~这个是附加内容~这个是附加内容~11","id":9,"type":"标题~标题~11"},{"img_path":"asdfasdf","source_id":5,"add_time":"2017-02-15 09:04:45","type":"moto","model":"car","attach":"asfafdsaf","id":10,"type":"5555555555555"},{"img_path":"asdfasdf~~~~","source_id":6,"add_time":"2017-02-15 09:05:05","type":"moto","model":"car","attach":"","id":11,"type":"5555555555555~~~~"}]
     */

    public Pagination pagination;
    public List<FavoriteItem> list;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FavoriteItem {
        /**
         * img_path : xxxsseexx11
         * source_id : 4
         * add_time : 2017-02-15 09:00:12
         * type : moto
         * model : car
         * attach : 这个是附加内容~这个是附加内容~这个是附加内容~11
         * id : 9
         * type : 标题~标题~11
         */

        @JsonProperty("img_path")
        public String imgPath;
        @JsonProperty("source_id")
        public int    sourceId;
        @JsonProperty("add_time")
        public String addTime;
        public String type;
        public String model;
        public String attach;
        public int    id;
        public String title;
    }
}
