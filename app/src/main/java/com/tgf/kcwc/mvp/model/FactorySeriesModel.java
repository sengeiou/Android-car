package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;

import java.util.List;

/**
 * Author:Jenny
 * Date:2017/8/31
 * E-mail:fishloveqin@gmail.com
 * 厂商车系Model
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class FactorySeriesModel {


    /**
     * list : [{"id":8,"name":"一汽奥迪","logo":"/car/1705/31/861be7739f28d2c2b8adaa86159f3fd5.png","series":[{"id":15,"name":"奥迪A6L","logo":"/car/1704/25/5576941b48dfd41d318f51e089cb39c5.png","price":"9.99万-109.00万"},{"id":22,"name":"A4L","logo":"/car/1701/19/e2d227cff25444b1c0822eeab9ea7806.jpg","price":"10.00万-200.00万"},{"id":23,"name":"A8L","logo":"/car/1701/19/e2d227cff25444b1c0822eeab9ea7806.jpg","price":"210.00万"},{"id":69,"name":"奥迪测试车系","logo":"/car/1708/22/436e629cdc109bbea09448f35b1b99ee.png","price":"100000.00万"}]}]
     * brand : {"id":33,"name":"奥迪","logo":"http://kcwc2pic.51kcwc.com/brand/aodi@3x.png"}
     */

    public Brand brand;
    public List<Factory> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Brand {
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
    public static class Factory {
        /**
         * id : 8
         * name : 一汽奥迪
         * logo : /car/1705/31/861be7739f28d2c2b8adaa86159f3fd5.png
         * series : [{"id":15,"name":"奥迪A6L","logo":"/car/1704/25/5576941b48dfd41d318f51e089cb39c5.png","price":"9.99万-109.00万"},{"id":22,"name":"A4L","logo":"/car/1701/19/e2d227cff25444b1c0822eeab9ea7806.jpg","price":"10.00万-200.00万"},{"id":23,"name":"A8L","logo":"/car/1701/19/e2d227cff25444b1c0822eeab9ea7806.jpg","price":"210.00万"},{"id":69,"name":"奥迪测试车系","logo":"/car/1708/22/436e629cdc109bbea09448f35b1b99ee.png","price":"100000.00万"}]
         */

        public int id;
        public String name;
        public String logo;
        public List<Series> series;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Series extends BaseIndexPinyinBean {
            /**
             * id : 15
             * name : 奥迪A6L
             * logo : /car/1704/25/5576941b48dfd41d318f51e089cb39c5.png
             * price : 9.99万-109.00万
             */

            public int id;
            public String name;
            public String logo;
            public String price;
            public boolean isSelected;
            public String title;
            @Override
            public String getTarget() {
                return title;
            }

            @Override
            public String getSuspensionTag() {
                return title;
            }

            @Override
            public boolean isNeedToPinyin() {
                return false;
            }
        }
    }
}
