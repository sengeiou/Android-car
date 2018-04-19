package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Jenny on 2017/8/7.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SeriesByBrandModel {


    /**
     * list_in : [{"factory":{"id":8,"name":"一汽奥迪","logo":"/car/1705/31/861be7739f28d2c2b8adaa86159f3fd5.png"},"series":[{"id":22,"name":"A4L","logo":"/car/1701/19/e2d227cff25444b1c0822eeab9ea7806.jpg","price":"10.00万-200.00万"},{"id":15,"name":"奥迪A6L","logo":"/car/1704/25/5576941b48dfd41d318f51e089cb39c5.png","price":"0.00万-109.00万"}]}]
     * list_pre : [{"factory":{"id":8,"name":"一汽奥迪","logo":"/car/1705/31/861be7739f28d2c2b8adaa86159f3fd5.png"},"series":[{"id":22,"name":"A4L","logo":"/car/1701/19/e2d227cff25444b1c0822eeab9ea7806.jpg","price":"10.00万-200.00万"},{"id":15,"name":"奥迪A6L","logo":"/car/1704/25/5576941b48dfd41d318f51e089cb39c5.png","price":"0.00万-109.00万"}]}]
     * list_stop : [{"factory":{"id":8,"name":"一汽奥迪","logo":"/car/1705/31/861be7739f28d2c2b8adaa86159f3fd5.png"},"series":[{"id":15,"name":"奥迪A6L","logo":"/car/1704/25/5576941b48dfd41d318f51e089cb39c5.png","price":"0.00万-109.00万"},{"id":23,"name":"A8L","logo":"/car/1701/19/e2d227cff25444b1c0822eeab9ea7806.jpg","price":"50.00万-210.00万"}]}]
     * brand : {"id":33,"name":"奥迪","logo":"http://kcwc2pic.51kcwc.com/brand/aodi@3x.png"}
     * sales_status : {"in":6,"pre":3,"stop":3}
     */

    public BrandBean brand;
    @JsonProperty("sales_status")
    public SalesStatusBean salesStatus;
    @JsonProperty("list_in")
    public List<SeriesData> listIn;
    @JsonProperty("list_pre")
    public List<SeriesData> listPre;
    @JsonProperty("list_stop")
    public List<SeriesData> listStop;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BrandBean {
        /**
         * id : 33
         * name : 奥迪
         * logo : http://kcwc2pic.51kcwc.com/brand/aodi@3x.png
         */

        public int id;
        public String name;
        public String logo;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SalesStatusBean {
        /**
         * in : 6
         * pre : 3
         * stop : 3
         */

        public int in;
        public int pre;
        public int stop;
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SeriesData {
        /**
         * factory : {"id":8,"name":"一汽奥迪","logo":"/car/1705/31/861be7739f28d2c2b8adaa86159f3fd5.png"}
         * series : [{"id":15,"name":"奥迪A6L","logo":"/car/1704/25/5576941b48dfd41d318f51e089cb39c5.png","price":"0.00万-109.00万"},{"id":23,"name":"A8L","logo":"/car/1701/19/e2d227cff25444b1c0822eeab9ea7806.jpg","price":"50.00万-210.00万"}]
         */

        public FactoryBean factory;
        public List<SeriesBean> series;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class FactoryBean {
            /**
             * id : 8
             * name : 一汽奥迪
             * logo : /car/1705/31/861be7739f28d2c2b8adaa86159f3fd5.png
             */

            public int id;
            public String name;
            public String logo;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class SeriesBean {
            /**
             * id : 15
             * name : 奥迪A6L
             * logo : /car/1704/25/5576941b48dfd41d318f51e089cb39c5.png
             * price : 0.00万-109.00万
             */

            public int id;
            public String name;
            public String logo;
            public String price;
        }
    }


}
