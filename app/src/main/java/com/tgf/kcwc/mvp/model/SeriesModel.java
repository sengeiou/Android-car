package com.tgf.kcwc.mvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Author：Jenny
 * Date:2017/3/28 16:24
 * E-mail：fishloveqin@gmail.com
 * 车系
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SeriesModel {

    /**
     * list : [{"factory_name":"邓丹汽车2厂","factory_logo":"/car/1701/10/654089c1aec299812f53f777b7236140.jpg","series_name":"超级跑车系列","cover":"/1482290038.png","price":"120.10-125.22","match_num":2,"series_id":4},{"factory_name":"邓丹汽车2厂","factory_logo":"/car/1701/10/654089c1aec299812f53f777b7236140.jpg","series_name":"邓丹的货车系列","cover":"/car/1701/10/7b97377c66346b701fb42797230f87d0.jpg","price":"1.00-120.00","match_num":2,"series_id":5},{"factory_name":"一汽奥迪","factory_logo":"http://kcwc2pic.51kcwc.com/brand/aodi@3x.png","series_name":"奥迪A6L","cover":"http://img5.imgtn.bdimg.com/it/u=2572716819,1277922842&fm=23&gp=0.jpg","price":"10.00-55.00","match_num":4,"series_id":15},{"factory_name":"上汽大众","factory_logo":"/car/1701/20/ffeb12bca370f63433826e5e66f916c6.jpg","series_name":"辉昂","cover":"/car/1701/12/ba884d41e8d44853dfe3c16733a7b88f.jpg","price":"148.80-148.80","match_num":1,"series_id":16},{"factory_name":"东风标致","factory_logo":"/car/1701/20/ffeb12bca370f63433826e5e66f916c6.jpg","series_name":"标致4008","cover":"/car/1701/19/e2d227cff25444b1c0822eeab9ea7806.jpg","price":"30.00-100.00","match_num":3,"series_id":17}]
     * carTotal : 22
     * seriesTotal : 14
     */

    public int           carTotal;
    public int           seriesTotal;
    public List<CarBean> list;

    public CustomMade    customMade;

    public static class CustomMade {

        @JsonProperty("brand")
        public List<Brand> brands;
        @JsonProperty("seat_num")
        public String      seatNum;
        @JsonProperty("guide_price_key")
        public String      guidePriceKey;
        @JsonProperty("price_range_max")
        public String      priceMax;
        @JsonProperty("price_range_min")
        public String      priceMin;
    }

}
